package org.smartup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.smartup.exception.BookmarkErrorCode;
import org.smartup.exception.BookmarkException;
import org.smartup.service.BookmarkService;
import yandex.cloud.sdk.functions.Context;

import java.util.Map;

import static org.mockito.Mockito.when;


public class BookmarkHandlerTest {
    BookmarkService bookmarkService;
    BookmarkHandler bookmarkHandler;
    RequestData requestData;
    Context context;
    @BeforeEach
    void setUp (){
        bookmarkHandler = new BookmarkHandler();
        bookmarkService = Mockito.mock(BookmarkService.class);
        bookmarkHandler.setBookmarkService(bookmarkService);
        requestData = new RequestData();
        context =  Mockito.mock(Context.class);
    }
    @Test
    void getAllBookmarksOk () throws BookmarkException {
        requestData.setHttpMethod("GET");
        ResponseData responseData = bookmarkHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Mockito.verify(bookmarkService).getBookmarks(context);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(200, responseData.getStatusCode());
    }

    @Test
    void getBookmarkOk () throws BookmarkException{
        requestData.setHttpMethod("GET");
        requestData.setPathParams(Map.of("id", "1"));
        ResponseData responseData = bookmarkHandler.handle(requestData, context);
        Mockito.verify(bookmarkService).getBookmark(context, "1");
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(200, responseData.getStatusCode());
    }
    @Test
    void deleteBookmarkOk () throws BookmarkException{
        requestData.setHttpMethod("DELETE");
        requestData.setPathParams(Map.of("id", "1"));
        ResponseData responseData = bookmarkHandler.handle(requestData, context);
        Mockito.verify(bookmarkService).deleteBookmark(context, "1");
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(200, responseData.getStatusCode());
    }
    @Test
    void deleteAllBookmarksOk () throws BookmarkException{
        requestData.setHttpMethod("DELETE");
        ResponseData responseData = bookmarkHandler.handle(requestData, context);
        Mockito.verify(bookmarkService).deleteAllBookmarks(context);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(200, responseData.getStatusCode());
    }

    @Test
    void postBookmarkOk () throws BookmarkException{
        requestData.setHttpMethod("POST");
        ResponseData responseData = bookmarkHandler.handle(requestData, context);
        Mockito.verify(bookmarkService).addNewBookmark(context, requestData);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(200, responseData.getStatusCode());
    }

    @Test
    void getBookmarkNotFound () throws BookmarkException{
        requestData.setHttpMethod("GET");
        requestData.setPathParams(Map.of("id", "0"));
        when(bookmarkService.getBookmark(context, "0")).thenThrow(new BookmarkException(BookmarkErrorCode.BOOKMARK_ID_NOT_FOUND));
        ResponseData responseData = bookmarkHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(404, responseData.getStatusCode());
    }
    @Test
    void deleteBookmarkWrongId () throws BookmarkException{
        requestData.setHttpMethod("DELETE");
        requestData.setPathParams(Map.of("id", "not number"));
        when(bookmarkService.deleteBookmark(context, "not number")).thenThrow(new BookmarkException(BookmarkErrorCode.BOOKMARK_ID_INCORRECT));
        ResponseData responseData = bookmarkHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(400, responseData.getStatusCode());
    }

    @Test
    void postBookmarkWrongParentId () throws BookmarkException{
        requestData.setHttpMethod("POST");
        when(bookmarkService.addNewBookmark(context, requestData)).thenThrow(new BookmarkException(BookmarkErrorCode.FOLDER_ID_NOT_FOUND));
        ResponseData responseData = bookmarkHandler.handle(requestData, context);
        Mockito.verify(bookmarkService).addNewBookmark(context, requestData);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(404, responseData.getStatusCode());
    }
    @Test
    void getAllBookmarksDatabaseError () throws BookmarkException{
        requestData.setHttpMethod("GET");
        when(bookmarkService.getBookmarks(context)).thenThrow(new BookmarkException(BookmarkErrorCode.FAIL_DATABASE_CONNECTION));
        ResponseData responseData = bookmarkHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(503, responseData.getStatusCode());
    }


}

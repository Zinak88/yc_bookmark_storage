package org.smartup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.smartup.exception.ApiException;
import org.smartup.service.BookmarkService;
import yandex.cloud.sdk.functions.Context;

import java.util.Map;


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
    void getAllBookmarksOk () throws ApiException{
        requestData.setHttpMethod("GET");
        ResponseData responseData = bookmarkHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Mockito.verify(bookmarkService).getBookmarks(context);
    }

    @Test
    void getBookmarkOk () throws ApiException{
        requestData.setHttpMethod("GET");
        requestData.setPathParams(Map.of("id", "1"));
        ResponseData responseData = bookmarkHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Mockito.verify(bookmarkService).getBookmark(context, "1");
    }
    @Test
    void deleteBookmarkOk () throws ApiException{
        requestData.setHttpMethod("DELETE");
        requestData.setPathParams(Map.of("id", "1"));
        bookmarkHandler.handle(requestData, context);
        Mockito.verify(bookmarkService).deleteBookmark(context, "1");
    }
    @Test
    void deleteAllBookmarksOk () throws ApiException{
        requestData.setHttpMethod("DELETE");
        bookmarkHandler.handle(requestData, context);
        Mockito.verify(bookmarkService).deleteAllBookmarks(context);
    }

    @Test
    void postBookmarkOk () throws ApiException{
        requestData.setHttpMethod("POST");
        bookmarkHandler.handle(requestData, context);
        Mockito.verify(bookmarkService).addNewBookmark(context, requestData);
    }
}

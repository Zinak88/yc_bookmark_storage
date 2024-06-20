package org.smartup.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.smartup.RequestData;
import org.smartup.dao.BookmarkDao;
import org.smartup.exception.BookmarkErrorCode;
import org.smartup.exception.BookmarkException;
import org.smartup.model.Bookmark;
import yandex.cloud.sdk.functions.Context;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookmarkServiceTest {
    BookmarkService bookmarkService;
    BookmarkDao bookmarkDao;
    RequestData requestData;
    Context context;
    Connection connection;

    @BeforeEach
    void setUp (){
        bookmarkService = new BookmarkService();
        bookmarkDao = Mockito.mock(BookmarkDao.class);
        connection = Mockito.mock(Connection.class);
        bookmarkService.setBookmarkDao(bookmarkDao);
        bookmarkService.setConnection(connection);
    }
    @Test
    void addNewBookmark () throws BookmarkException {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1L);
        bookmark.setAddedDate(LocalDate.ofYearDay(2000, 1));
        when(bookmarkDao.addNewBookmark(any(Connection.class), any(Bookmark.class))).thenReturn(bookmark);
        requestData = new RequestData();
        requestData.setHeaders(Map.of("Content-Type","application/json"));
        requestData.setBody("{\"url\": \"text\"}");
        bookmarkService.addNewBookmark(context, requestData);

        Mockito.verify(bookmarkDao).addNewBookmark(any(), any());
    }
    @Test
    void addNewBookmarkWithoutUrl () {
        requestData = new RequestData();
        requestData.setHeaders(Map.of("Content-Type","application/json"));
        requestData.setBody("{ }");
        BookmarkException exc = Assertions.assertThrows(BookmarkException.class, () -> bookmarkService.addNewBookmark(context, requestData));
        Assertions.assertEquals(BookmarkErrorCode.URL_IS_BLANK, exc.getErrorCode());
    }

    @Test
    void addNewBookmarkWrongBody () {
        requestData = new RequestData();
        BookmarkException exc = Assertions.assertThrows(BookmarkException.class, () -> bookmarkService.addNewBookmark(context, requestData));
        Assertions.assertEquals(BookmarkErrorCode.INCORRECT_POST_BODY, exc.getErrorCode());
    }

    @Test
    void getAllBookmarks () throws BookmarkException {
        bookmarkService.getBookmarks(context);
        Mockito.verify(bookmarkDao).getAllBookmarks(any());
    }

    @Test
    void getBookmark () throws BookmarkException {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1L);
        bookmark.setAddedDate(LocalDate.ofYearDay(2000, 1));
        when(bookmarkDao.getBookmark(connection, 1L)).thenReturn(bookmark);
        bookmarkService.getBookmark(context, "1");
        Mockito.verify(bookmarkDao).getBookmark(connection, 1L);
    }
    @Test
    void getBookmarkWrongId (){
        BookmarkException exc = Assertions.assertThrows(BookmarkException.class, () -> bookmarkService.getBookmark(context, "text"));
        Assertions.assertEquals(BookmarkErrorCode.BOOKMARK_ID_INCORRECT, exc.getErrorCode());
    }

    @Test
    void deleteBookmark () throws BookmarkException {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1L);
        bookmark.setAddedDate(LocalDate.ofYearDay(2000, 1));
        when(bookmarkDao.deleteBookmark(connection, 1L)).thenReturn(bookmark);
        bookmarkService.deleteBookmark(context, "1");
        Mockito.verify(bookmarkDao).deleteBookmark(connection, 1L);
    }
    @Test
    void deleteBookmarkWrongId (){
        BookmarkException exc = Assertions.assertThrows(BookmarkException.class, () -> bookmarkService.deleteBookmark(context, "text"));
        Assertions.assertEquals(BookmarkErrorCode.BOOKMARK_ID_INCORRECT, exc.getErrorCode());
    }


    @Test
    void deleteAllBookmarks () throws BookmarkException {
        bookmarkService.deleteAllBookmarks(context);
        Mockito.verify(bookmarkDao).deleteAllBookmarks(connection);
    }
}

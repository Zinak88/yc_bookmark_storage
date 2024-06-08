package org.smartup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.smartup.RequestData;
import org.smartup.dao.BookmarkDao;
import org.smartup.exception.ApiException;
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
    void addNewBookmark () throws ApiException {
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
    void getAllBookmarks () throws ApiException {
        bookmarkService.getBookmarks(context);
        Mockito.verify(bookmarkDao).getAllBookmarks(any());
    }

    @Test
    void getBookmark () throws ApiException {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1L);
        bookmark.setAddedDate(LocalDate.ofYearDay(2000, 1));
        when(bookmarkDao.getBookmark(connection, 1L)).thenReturn(bookmark);
        bookmarkService.getBookmark(context, "1");
        Mockito.verify(bookmarkDao).getBookmark(connection, 1L);
    }

    @Test
    void deleteBookmark () throws ApiException {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1L);
        bookmark.setAddedDate(LocalDate.ofYearDay(2000, 1));
        when(bookmarkDao.deleteBookmark(connection, 1L)).thenReturn(bookmark);
        bookmarkService.deleteBookmark(context, "1");
        Mockito.verify(bookmarkDao).deleteBookmark(connection, 1L);
    }

    @Test
    void deleteAllBookmarks () throws ApiException {
        bookmarkService.deleteAllBookmarks(context);
        Mockito.verify(bookmarkDao).deleteAllBookmarks(connection);
    }
}

package org.smartup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.smartup.RequestData;
import org.smartup.dao.BookmarkDao;
import org.smartup.exception.ApiException;
import org.smartup.mapper.Mapper;
import org.smartup.model.Bookmark;
import yandex.cloud.sdk.functions.Context;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookmarkServiceTest {
    BookmarkService bookmarkService;
    BookmarkDao bookmarkDao;
    RequestData requestData;
    Context context;
    Mapper mapper;
    @BeforeEach
    void setUp (){
        bookmarkService = new BookmarkService();
        bookmarkDao = Mockito.mock(BookmarkDao.class);
        bookmarkService.setBookmarkDao(bookmarkDao);
        mapper = Mockito.mock(Mapper.class);
        bookmarkService.setMapper(mapper);
        context = Mockito.mock(Context.class);

    }
    @Test
    void addNewBookmark () throws ApiException {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1L);
        when(mapper.bookmarkRequestDtoToBookmark(any())).thenReturn(bookmark);
        when(bookmarkDao.addNewBookmark(context, bookmark)).thenReturn(bookmark);

        requestData = new RequestData();
        requestData.setHeaders(Map.of("Content-Type","application/json"));
        requestData.setBody("{\"url\": \"text\"}");
        bookmarkService.addNewBookmark(context, requestData);

        Mockito.verify(bookmarkDao).addNewBookmark(context, bookmark);
    }

    @Test
    void getAllBookmarks () throws ApiException {
        bookmarkService.getBookmarks(context);
        Mockito.verify(bookmarkDao).getAllBookmarks(context);
    }

    @Test
    void getBookmark () throws ApiException {
        when(bookmarkDao.getBookmark(context, 1L)).thenReturn(new Bookmark());
        bookmarkService.getBookmark(context, "1");
        Mockito.verify(bookmarkDao).getBookmark(context, 1L);
    }

    @Test
    void deleteBookmark () throws ApiException {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1L);
        when(bookmarkDao.deleteBookmark(context, 1L)).thenReturn(bookmark);
        bookmarkService.deleteBookmark(context, "1");
        Mockito.verify(bookmarkDao).deleteBookmark(context, 1L);
    }

    @Test
    void deleteAllBookmarks () throws ApiException {
        bookmarkService.deleteAllBookmarks(context);
        Mockito.verify(bookmarkDao).deleteAllBookmarks(context);
    }
}

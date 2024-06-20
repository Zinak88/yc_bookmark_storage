package org.smartup.service;

import com.google.gson.Gson;
import lombok.Setter;
import org.smartup.RequestData;
import org.smartup.dao.BookmarkDao;
import org.smartup.dbc.DatabaseConnector;
import org.smartup.dto.request.BookmarkRequestDto;
import org.smartup.dto.response.BookmarkResponseDto;
import org.smartup.exception.*;
import org.smartup.mapper.BookmarkMapper;
import org.smartup.model.Bookmark;
import yandex.cloud.sdk.functions.Context;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class BookmarkService {

    private final BookmarkMapper bookmarkMapper = new BookmarkMapper();
    @Setter
    private BookmarkDao bookmarkDao = new BookmarkDao();
    @Setter
    private Connection connection;
    private static final Gson gson = new Gson();

    public BookmarkResponseDto addNewBookmark(Context context, RequestData request) throws BookmarkException {

        if ((request.getBody()==null) || (!request.getHeaders().get("Content-Type").equals("application/json"))) {
            throw new BookmarkException(BookmarkErrorCode.INCORRECT_POST_BODY);
        }
        BookmarkRequestDto dto = gson.fromJson(request.getBody(), BookmarkRequestDto.class);
        if (dto.getUrl()==null || dto.getUrl().isBlank()) {
            throw new BookmarkException(BookmarkErrorCode.URL_IS_BLANK);
        }
        Bookmark bookmark = bookmarkMapper.bookmarkRequestDtoToBookmark(dto);
        initConnection(context);
        bookmark = bookmarkDao.addNewBookmark(connection, bookmark);
        return bookmarkMapper.bookmarkToBookmarkResponseDto(bookmark);
    }
    public List<BookmarkResponseDto> getBookmarks(Context context) throws BookmarkException{
        initConnection(context);
        List<Bookmark> bookmarks = bookmarkDao.getAllBookmarks(connection);
        List<BookmarkResponseDto> responseDto = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            responseDto.add(bookmarkMapper.bookmarkToBookmarkResponseDto(bookmark));
        }
        return responseDto;
    }
    public BookmarkResponseDto getBookmark(Context context, String requestId) throws BookmarkException{
        long id = parseBookmarkRequestId(requestId);
        initConnection(context);
        Bookmark bookmark = bookmarkDao.getBookmark(connection, id);
        if (bookmark==null) {
            throw new BookmarkException(BookmarkErrorCode.BOOKMARK_ID_NOT_FOUND);
        }
        return bookmarkMapper.bookmarkToBookmarkResponseDto(bookmark);
    }

    public BookmarkResponseDto deleteBookmark(Context context, String requestId) throws BookmarkException{
        long id = parseBookmarkRequestId(requestId);
        initConnection(context);
        Bookmark bookmark = bookmarkDao.deleteBookmark(connection, id);
        if (bookmark==null) {
            throw new BookmarkException(BookmarkErrorCode.BOOKMARK_ID_NOT_FOUND);
        } else {
            return bookmarkMapper.bookmarkToBookmarkResponseDto(bookmark);
        }
    }

    public void deleteAllBookmarks(Context context) throws BookmarkException{
        initConnection(context);
        bookmarkDao.deleteAllBookmarks(connection);
    }



    private void initConnection (Context context) throws BookmarkException {
        try {
            if (connection == null) {
                connection = new DatabaseConnector().get(context);
            }
        } catch (DatabaseException exc) {
            throw new BookmarkException(BookmarkErrorCode.FAIL_DATABASE_CONNECTION);
        }
    }
    private long parseBookmarkRequestId(String requestId) throws BookmarkException{
        try {
            return Long.parseLong(requestId);
        } catch (NumberFormatException exc) {
            throw new BookmarkException(BookmarkErrorCode.BOOKMARK_ID_INCORRECT);
        }
    }
}

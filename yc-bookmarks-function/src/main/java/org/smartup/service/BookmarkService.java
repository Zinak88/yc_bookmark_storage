package org.smartup.service;

import com.google.gson.Gson;
import lombok.Setter;
import org.smartup.RequestData;
import org.smartup.dao.BookmarkDao;
import org.smartup.dbc.DatabaseConnector;
import org.smartup.dto.request.BookmarkRequestDto;
import org.smartup.dto.response.BookmarkResponseDto;
import org.smartup.exception.ApiException;
import org.smartup.exception.BookmarkException;
import org.smartup.exception.DatabaseException;
import org.smartup.exception.ServerErrorCode;
import org.smartup.mapper.Mapper;
import org.smartup.model.Bookmark;
import yandex.cloud.sdk.functions.Context;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class BookmarkService {

    private final Mapper mapper = new Mapper();
    @Setter
    private BookmarkDao bookmarkDao = new BookmarkDao();
    @Setter
    private Connection connection;
    private static final Gson gson = new Gson();

    public BookmarkResponseDto addNewBookmark(Context context, RequestData request) throws ApiException {

        if ((request.getBody()==null) || (!request.getHeaders().get("Content-Type").equals("application/json"))) {
            throw new BookmarkException(ServerErrorCode.INCORRECT_POST_BODY);
        }
        BookmarkRequestDto dto = gson.fromJson(request.getBody(), BookmarkRequestDto.class);
        if (dto.getUrl()==null || dto.getUrl().isBlank()) {
            throw new BookmarkException(ServerErrorCode.URL_IS_BLANK);
        }
        Bookmark bookmark = mapper.bookmarkRequestDtoToBookmark(dto);
        initConnection(context);
        bookmark = bookmarkDao.addNewBookmark(connection, bookmark);
        return mapper.bookmarkToBookmarkResponseDto(bookmark);
    }
    public List<BookmarkResponseDto> getBookmarks(Context context) throws ApiException{
        initConnection(context);
        List<Bookmark> bookmarks = bookmarkDao.getAllBookmarks(connection);
        List<BookmarkResponseDto> responseDto = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            responseDto.add(mapper.bookmarkToBookmarkResponseDto(bookmark));
        }
        return responseDto;
    }
    public BookmarkResponseDto getBookmark(Context context, String requestId) throws ApiException{
        long id = parseRequestId(requestId);
        initConnection(context);
        Bookmark bookmark = bookmarkDao.getBookmark(connection, id);
        if (bookmark==null) {
            throw new BookmarkException(ServerErrorCode.BOOKMARK_ID_NOT_FOUND);
        }
        return mapper.bookmarkToBookmarkResponseDto(bookmark);
    }

    public BookmarkResponseDto deleteBookmark(Context context, String requestId) throws ApiException{
        long id = parseRequestId(requestId);
        initConnection(context);
        Bookmark bookmark = bookmarkDao.deleteBookmark(connection, id);
        if (bookmark==null) {
            throw new BookmarkException(ServerErrorCode.BOOKMARK_ID_NOT_FOUND);
        } else {
            return mapper.bookmarkToBookmarkResponseDto(bookmark);
        }
    }

    public void deleteAllBookmarks(Context context) throws ApiException{
        initConnection(context);
        bookmarkDao.deleteAllBookmarks(connection);
    }



    private void initConnection (Context context) throws DatabaseException {
        if (connection==null){
            connection = new DatabaseConnector().get(context);
        }
    }
    private long parseRequestId (String requestId) throws BookmarkException{
        try {
            return Long.parseLong(requestId);
        } catch (NumberFormatException exc) {
            throw new BookmarkException(ServerErrorCode.BOOKMARK_ID_INCORRECT);
        }
    }
}

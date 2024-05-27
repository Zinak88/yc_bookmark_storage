package org.smartup.service;

import com.jsoniter.JsonIterator;
import lombok.Setter;
import org.smartup.RequestData;
import org.smartup.dao.BookmarkDao;
import org.smartup.dto.request.BookmarkRequestDto;
import org.smartup.dto.response.BookmarkResponseDto;
import org.smartup.exception.ApiException;
import org.smartup.exception.BookmarkException;
import org.smartup.exception.ServerErrorCode;
import org.smartup.mapper.Mapper;
import org.smartup.model.Bookmark;
import yandex.cloud.sdk.functions.Context;

import java.util.ArrayList;
import java.util.List;


public class BookmarkService {
    @Setter
    private Mapper mapper = new Mapper();
    @Setter
    private BookmarkDao bookmarkDao = new BookmarkDao();

    public BookmarkResponseDto addNewBookmark(Context context, RequestData request) throws ApiException {

        if ((request.getBody()==null) || (!request.getHeaders().get("Content-Type").equals("application/json"))) {
            throw new BookmarkException(ServerErrorCode.INCORRECT_POST_BODY);
        }
        BookmarkRequestDto dto = JsonIterator.deserialize(request.getBody(), BookmarkRequestDto.class);
        if (dto.getUrl()==null || dto.getUrl().isBlank()) {
            throw new BookmarkException(ServerErrorCode.URL_IS_BLANK);
        }
        Bookmark bookmark = mapper.bookmarkRequestDtoToBookmark(dto);
        bookmark = bookmarkDao.addNewBookmark(context, bookmark);
        return mapper.bookmarkToBookmarkResponseDto(bookmark);
    }
    public List<BookmarkResponseDto> getBookmarks(Context context) throws ApiException{
        List<Bookmark> bookmarks = bookmarkDao.getAllBookmarks(context);
        List<BookmarkResponseDto> responseDto = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            responseDto.add(mapper.bookmarkToBookmarkResponseDto(bookmark));
        }
        return responseDto;
    }
    public BookmarkResponseDto getBookmark(Context context, String requestId) throws ApiException{
        long id;
        try {
            id = Long.parseLong(requestId);
        } catch (NumberFormatException exc) {
            throw new BookmarkException(ServerErrorCode.BOOKMARK_ID_INCORRECT);
        }
        Bookmark bookmark = bookmarkDao.getBookmark(context, id);
        if (bookmark==null) {
            throw new BookmarkException(ServerErrorCode.BOOKMARK_ID_NOT_FOUND);
        }
        return mapper.bookmarkToBookmarkResponseDto(bookmark);
    }

    public BookmarkResponseDto deleteBookmark(Context context, String requestId) throws ApiException{
        long id;
        try {
            id = Long.parseLong(requestId);
        } catch (NumberFormatException exc) {
            throw new BookmarkException(ServerErrorCode.BOOKMARK_ID_INCORRECT);
        }
        Bookmark bookmark = bookmarkDao.deleteBookmark(context, id);
        if (bookmark==null) {
            throw new BookmarkException(ServerErrorCode.BOOKMARK_ID_NOT_FOUND);
        } else {
            return mapper.bookmarkToBookmarkResponseDto(bookmark);
        }
    }

    public void deleteAllBookmarks(Context context) throws ApiException{
        bookmarkDao.deleteAllBookmarks(context);
    }
}

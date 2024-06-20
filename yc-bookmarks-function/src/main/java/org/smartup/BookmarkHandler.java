package org.smartup;
import com.google.gson.Gson;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartup.dto.response.BookmarkResponseDto;
import org.smartup.exception.BookmarkException;
import org.smartup.service.BookmarkService;
import yandex.cloud.sdk.functions.Context;
import yandex.cloud.sdk.functions.YcFunction;

import java.util.List;

public class BookmarkHandler implements YcFunction<RequestData, ResponseData> {

    @Setter
    private BookmarkService bookmarkService = new BookmarkService();
    private static final Gson gson = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(BookmarkHandler.class);

    @Override
    public ResponseData handle(RequestData request, Context context) {
        String method = request.getHttpMethod();
        LOGGER.info(" # " + method);
        String id = request.getPathParams()==null ? null : request.getPathParams().get("id");
        if (!(id==null)) {
            LOGGER.info(" # request id: " + id);
        }
        switch (method) {
            case ("POST") -> {
                try {
                    BookmarkResponseDto responseDto = bookmarkService.addNewBookmark(context, request);
                    return new ResponseData(gson.toJson(responseDto));
                } catch (BookmarkException exc) {
                    return getResponseFromBookmarkException(exc);
                }
            }
            case ("GET") -> {
                try {
                    if (id == null) {
                        List<BookmarkResponseDto> responseDto = bookmarkService.getBookmarks(context);
                        return new ResponseData(gson.toJson(responseDto));
                    } else {
                        BookmarkResponseDto responseDto = bookmarkService.getBookmark(context, id);
                        return new ResponseData(gson.toJson(responseDto));
                    }
                } catch (BookmarkException exc) {
                    return getResponseFromBookmarkException(exc);
                }
            }
            case ("DELETE") -> {
                try {
                    if (id == null) {
                        bookmarkService.deleteAllBookmarks(context);
                        return new ResponseData("Ok");
                    } else {
                        BookmarkResponseDto responseDto = bookmarkService.deleteBookmark(context, id);
                        return new ResponseData(gson.toJson(responseDto));
                    }
                } catch (BookmarkException exc) {
                    return getResponseFromBookmarkException(exc);
                }
            }
            case ("OPTIONS") -> {
                ResponseData response = new ResponseData (204, "No content", "text/plain");
                response.getHeaders().put("Allow", "OPTIONS, GET, DELETE, POST");
                return response;
            }

        }
        return new ResponseData (405, "Method not supported", "text/plain" );
    }

    private ResponseData getResponseFromBookmarkException(BookmarkException exc) {
        final int statusCode;
        switch (exc.getErrorCode()) {
            case FOLDER_ID_NOT_FOUND, BOOKMARK_ID_NOT_FOUND -> statusCode = 404;
            case FAIL_DATABASE_CONNECTION -> statusCode = 503;
            case FAIL_EXECUTE_QUERY -> statusCode = 500;
            default  -> statusCode = 400;
        }
        return new ResponseData(statusCode, exc.getMessage(), "text/plain");
    }


}
package org.smartup.exception;

public class BookmarkException extends ApiException {
    public BookmarkException(ServerErrorCode code) {
        super(code);
    }
}

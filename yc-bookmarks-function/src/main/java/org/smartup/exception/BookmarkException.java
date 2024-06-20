package org.smartup.exception;

public class BookmarkException extends Exception {
    private final BookmarkErrorCode errorCode;

    public BookmarkException(BookmarkErrorCode code) {
        super(code.getMessage());
        errorCode = code;
    }

    public BookmarkErrorCode getErrorCode() {
        return errorCode;
    }
}

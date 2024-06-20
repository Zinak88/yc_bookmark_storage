package org.smartup.exception;

public class FolderException extends Exception {
    private final FolderErrorCode errorCode;

    public FolderException(FolderErrorCode code) {
        super(code.getMessage());
        errorCode = code;
    }

    public FolderErrorCode getErrorCode() {
        return errorCode;
    }
}

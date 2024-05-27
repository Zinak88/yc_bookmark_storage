package org.smartup.exception;

public class ApiException extends Exception{
    private final ServerErrorCode serverErrorCode;

    public ApiException(ServerErrorCode code) {
        super(code.massage);
        serverErrorCode = code;
    }

    public ServerErrorCode getErrorCode() {
        return serverErrorCode;
    }
}

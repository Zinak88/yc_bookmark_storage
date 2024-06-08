package org.smartup.exception;

import lombok.Getter;

public class DatabaseException extends ApiException{

    @Getter
    private int statusCode;
    public DatabaseException(ServerErrorCode code) {
        super(code);
    }
    public DatabaseException(int statusCode, ServerErrorCode code) {
        super(code);
        this.statusCode = statusCode;
    }
}

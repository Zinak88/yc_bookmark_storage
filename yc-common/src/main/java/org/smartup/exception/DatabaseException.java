package org.smartup.exception;

public class DatabaseException extends ApiException{

    public DatabaseException(ServerErrorCode code) {
        super(code);
    }
}

package org.smartup.exception;

public enum ServerErrorCode {
    FAIL_DATABASE_CONNECTION("Database not available");
    final String massage;
    ServerErrorCode(String massage) {
        this.massage = massage;
    }
}

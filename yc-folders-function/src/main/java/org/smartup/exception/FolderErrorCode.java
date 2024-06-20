package org.smartup.exception;

public enum FolderErrorCode {

    FOLDER_ID_NOT_FOUND ("Folder ID not found"),
    FOLDER_ID_INCORRECT("Folder ID incorrect. Required positive integer"),
    TITLE_IS_BLANK("Title is blank"),
    FAIL_EXECUTE_QUERY("Can't execute query"),
    FAIL_DATABASE_CONNECTION("Database not available"),
    INCORRECT_POST_BODY("Method POST required json body");
    final String message;
    FolderErrorCode(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}

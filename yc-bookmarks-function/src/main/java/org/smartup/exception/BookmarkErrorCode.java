package org.smartup.exception;

public enum BookmarkErrorCode {

    FOLDER_ID_NOT_FOUND ("Folder ID not found"),
    FAIL_EXECUTE_QUERY("Can't execute query"),
    BOOKMARK_ID_NOT_FOUND("Bookmark ID not found"),
    BOOKMARK_ID_INCORRECT("Bookmark ID incorrect. Required positive integer"),
    FAIL_DATABASE_CONNECTION("Database not available"),
    INCORRECT_POST_BODY("Method POST required json body"),
    URL_IS_BLANK ("Url is blank");

    final String message;
    BookmarkErrorCode(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}

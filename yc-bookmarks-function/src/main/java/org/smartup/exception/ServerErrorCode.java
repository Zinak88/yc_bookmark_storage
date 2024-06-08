package org.smartup.exception;

public enum ServerErrorCode {

    FOLDER_ID_NOT_FOUND ("Folder ID not found"),
    FAIL_EXECUTE_QUERY("Can't execute query"),
    BOOKMARK_ID_NOT_FOUND("Bookmark ID not found"),
    BOOKMARK_ID_INCORRECT("Bookmark ID incorrect. Required positive integer"),
    INCORRECT_POST_BODY("Method POST required json body"),
    URL_IS_BLANK ("Url is blank");

    final String massage;
    ServerErrorCode(String massage) {
        this.massage = massage;
    }

}

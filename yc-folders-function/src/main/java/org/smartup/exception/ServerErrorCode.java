package org.smartup.exception;

public enum ServerErrorCode {

    FOLDER_ID_NOT_FOUND ("Folder ID not found"),
    FOLDER_ID_INCORRECT("Folder ID incorrect. Required positive integer"),
    TITLE_IS_BLANK("Title is blank"),
    FAIL_EXECUTE_QUERY("Can't execute query"),
    INCORRECT_POST_BODY("Method POST required json body");

    final String massage;
    ServerErrorCode(String massage) {
        this.massage = massage;
    }

}

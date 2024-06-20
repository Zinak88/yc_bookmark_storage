package org.smartup.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.smartup.RequestData;
import org.smartup.dao.FolderDao;
import org.smartup.exception.FolderErrorCode;
import org.smartup.exception.FolderException;
import org.smartup.model.Folder;
import yandex.cloud.sdk.functions.Context;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FolderServiceTest {
    FolderService folderService;
    FolderDao folderDao;
    RequestData requestData;
    Context context;
    Connection connection;
    @BeforeEach
    void setUp (){
        folderService = new FolderService();
        folderDao = Mockito.mock(FolderDao.class);
        connection = Mockito.mock(Connection.class);
        folderService.setFolderDao(folderDao);
        folderService.setConnection(connection);

    }
    @Test
    void addNewFolder () throws FolderException {
        Folder folder = new Folder();
        folder.setId(1L);
        folder.setAddedDate(LocalDate.ofYearDay(2000, 1));
        when(folderDao.addNewFolder(any(Connection.class), any(Folder.class))).thenReturn(folder);

        requestData = new RequestData();
        requestData.setHeaders(Map.of("Content-Type","application/json"));
        requestData.setBody("{\"title\": \"text\"}");
        folderService.addNewFolder(context, requestData);

        Mockito.verify(folderDao).addNewFolder(any(), any());
    }
    @Test
    void addNewFolderWithoutTitle () {
        requestData = new RequestData();
        requestData.setHeaders(Map.of("Content-Type","application/json"));
        requestData.setBody("{ }");
        FolderException exc = Assertions.assertThrows(FolderException.class, () -> folderService.addNewFolder(context, requestData));
        Assertions.assertEquals(FolderErrorCode.TITLE_IS_BLANK, exc.getErrorCode());
    }

    @Test
    void addNewFolderWrongBody () {
        requestData = new RequestData();
        FolderException exc = Assertions.assertThrows(FolderException.class, () -> folderService.addNewFolder(context, requestData));
        Assertions.assertEquals(FolderErrorCode.INCORRECT_POST_BODY, exc.getErrorCode());
    }

    @Test
    void getAllFolders () throws FolderException {
        folderService.getAllFolders(context);
        Mockito.verify(folderDao).getAllFolders(connection);
    }

    @Test
    void getFolder () throws FolderException {
        Folder folder = new Folder();
        folder.setId(1L);
        folder.setAddedDate(LocalDate.ofYearDay(2000, 1));
        when(folderDao.getFolder(connection, 1L)).thenReturn(folder);

        folderService.getFolder(context, "1");
        Mockito.verify(folderDao).getFolder(connection, 1L);
    }
    @Test
    void getFolderWrongId (){
        FolderException exc = Assertions.assertThrows(FolderException.class, () -> folderService.getFolder(context, "text"));
        Assertions.assertEquals(FolderErrorCode.FOLDER_ID_INCORRECT, exc.getErrorCode());
    }

    @Test
    void deleteFolder () throws FolderException {
        Folder folder = new Folder();
        folder.setId(1L);
        folder.setAddedDate(LocalDate.ofYearDay(2000, 1));
        when(folderDao.deleteFolder(connection, 1L)).thenReturn(folder);

        folderService.deleteFolder(context, "1");
        Mockito.verify(folderDao).deleteFolder(connection, 1L);
    }
    @Test
    void deleteFolderWrongId (){
        FolderException exc = Assertions.assertThrows(FolderException.class, () -> folderService.deleteFolder(context, "text"));
        Assertions.assertEquals(FolderErrorCode.FOLDER_ID_INCORRECT, exc.getErrorCode());
    }

    @Test
    void deleteAllFolders () throws FolderException {
        folderService.deleteAllFolders(context);
        Mockito.verify(folderDao).deleteAllFolders(connection);
    }

}

package org.smartup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.smartup.RequestData;
import org.smartup.dao.FolderDao;
import org.smartup.exception.ApiException;
import org.smartup.mapper.Mapper;
import org.smartup.model.Bookmark;
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
    void addNewFolder () throws ApiException {
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
    void getAllFolders () throws ApiException {
        folderService.getAllFolders(context);
        Mockito.verify(folderDao).getAllFolders(connection);
    }

    @Test
    void getFolder () throws ApiException {
        Folder folder = new Folder();
        folder.setId(1L);
        folder.setAddedDate(LocalDate.ofYearDay(2000, 1));
        when(folderDao.getFolder(connection, 1L)).thenReturn(folder);

        folderService.getFolder(context, "1");
        Mockito.verify(folderDao).getFolder(connection, 1L);
    }

    @Test
    void deleteFolder () throws ApiException {
        Folder folder = new Folder();
        folder.setId(1L);
        folder.setAddedDate(LocalDate.ofYearDay(2000, 1));
        when(folderDao.deleteFolder(connection, 1L)).thenReturn(folder);

        folderService.deleteFolder(context, "1");
        Mockito.verify(folderDao).deleteFolder(connection, 1L);
    }

    @Test
    void deleteAllFolders () throws ApiException {
        folderService.deleteAllFolders(context);
        Mockito.verify(folderDao).deleteAllFolders(connection);
    }

}

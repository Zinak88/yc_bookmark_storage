package org.smartup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.smartup.RequestData;
import org.smartup.dao.FolderDao;
import org.smartup.exception.ApiException;
import org.smartup.mapper.Mapper;
import org.smartup.model.Folder;
import yandex.cloud.sdk.functions.Context;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FolderServiceTest {
    FolderService folderService;
    FolderDao folderDao;
    RequestData requestData;
    Context context;
    Mapper mapper;
    @BeforeEach
    void setUp (){
        folderService = new FolderService();
        folderDao = Mockito.mock(FolderDao.class);
        folderService.setFolderDao(folderDao);
        mapper = Mockito.mock(Mapper.class);
        folderService.setMapper(mapper);
        context = Mockito.mock(Context.class);

    }
    @Test
    void addNewFolder () throws ApiException {
        Folder folder = new Folder();
        folder.setId(1L);
        when(mapper.folderRequestDtoToFolder(any())).thenReturn(folder);
        when(folderDao.addNewFolder(context, folder)).thenReturn(folder);

        requestData = new RequestData();
        requestData.setHeaders(Map.of("Content-Type","application/json"));
        requestData.setBody("{\"title\": \"text\"}");
        folderService.addNewFolder(context, requestData);

        Mockito.verify(folderDao).addNewFolder(context, folder);
    }

    @Test
    void getAllFolders () throws ApiException {
        folderService.getAllFolders(context);
        Mockito.verify(folderDao).getAllFolders(context);
    }

    @Test
    void getFolder () throws ApiException {
        when(folderDao.getFolder(context, 1L)).thenReturn(new Folder());
        folderService.getFolder(context, "1");
        Mockito.verify(folderDao).getFolder(context, 1L);
    }

    @Test
    void deleteFolder () throws ApiException {
        Folder folder = new Folder();
        folder.setId(1L);
        when(folderDao.deleteFolder(context, 1L)).thenReturn(folder);
        folderService.deleteFolder(context, "1");
        Mockito.verify(folderDao).deleteFolder(context, 1L);
    }

    @Test
    void deleteAllFolders () throws ApiException {
        folderService.deleteAllFolders(context);
        Mockito.verify(folderDao).deleteAllFolders(context);
    }

}

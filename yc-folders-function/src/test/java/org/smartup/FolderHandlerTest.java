package org.smartup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.smartup.exception.ApiException;
import org.smartup.service.FolderService;
import yandex.cloud.sdk.functions.Context;

import java.util.Map;


public class FolderHandlerTest {
    FolderService folderService;
    FolderHandler folderHandler;
    RequestData requestData;
    Context context;
    @BeforeEach
    void setUp (){
        folderHandler = new FolderHandler();
        folderService = Mockito.mock(FolderService.class);
        folderHandler.setFolderService(folderService);
        requestData = new RequestData();
        context =  Mockito.mock(Context.class);
    }
    @Test
    void getAllFoldersOk () throws ApiException {
        requestData.setHttpMethod("GET");
        ResponseData responseData = folderHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Mockito.verify(folderService).getAllFolders(context);
    }

    @Test
    void getFolderOk () throws ApiException{
        requestData.setHttpMethod("GET");
        requestData.setPathParams(Map.of("id", "1"));
        ResponseData responseData = folderHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Mockito.verify(folderService).getFolder(context, "1");
    }
    @Test
    void deleteFolderOk () throws ApiException{
        requestData.setHttpMethod("DELETE");
        requestData.setPathParams(Map.of("id", "1"));
        folderHandler.handle(requestData, context);
        Mockito.verify(folderService).deleteFolder(context, "1");
    }
    @Test
    void deleteAllFoldersOk () throws ApiException{
        requestData.setHttpMethod("DELETE");
        folderHandler.handle(requestData, context);
        Mockito.verify(folderService).deleteAllFolders(context);
    }

    @Test
    void postFolderOk () throws ApiException{
        requestData.setHttpMethod("POST");
        folderHandler.handle(requestData, context);
        Mockito.verify(folderService).addNewFolder(context, requestData);
    }

}

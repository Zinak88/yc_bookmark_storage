package org.smartup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.smartup.exception.FolderErrorCode;
import org.smartup.exception.FolderException;
import org.smartup.service.FolderService;
import yandex.cloud.sdk.functions.Context;

import java.util.Map;

import static org.mockito.Mockito.when;


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
    void getAllFoldersOk () throws FolderException {
        requestData.setHttpMethod("GET");
        ResponseData responseData = folderHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Mockito.verify(folderService).getAllFolders(context);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(200, responseData.getStatusCode());
    }

    @Test
    void getFolderOk () throws FolderException{
        requestData.setHttpMethod("GET");
        requestData.setPathParams(Map.of("id", "1"));
        ResponseData responseData = folderHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Mockito.verify(folderService).getFolder(context, "1");
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(200, responseData.getStatusCode());
    }
    @Test
    void deleteFolderOk () throws FolderException{
        requestData.setHttpMethod("DELETE");
        requestData.setPathParams(Map.of("id", "1"));
        ResponseData responseData = folderHandler.handle(requestData, context);
        Mockito.verify(folderService).deleteFolder(context, "1");
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(200, responseData.getStatusCode());
    }
    @Test
    void deleteAllFoldersOk () throws FolderException{
        requestData.setHttpMethod("DELETE");
        ResponseData responseData = folderHandler.handle(requestData, context);
        Mockito.verify(folderService).deleteAllFolders(context);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(200, responseData.getStatusCode());
    }

    @Test
    void postFolderOk () throws FolderException{
        requestData.setHttpMethod("POST");
        ResponseData responseData = folderHandler.handle(requestData, context);
        Mockito.verify(folderService).addNewFolder(context, requestData);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(200, responseData.getStatusCode());
    }

    @Test
    void getFolderNotFound () throws FolderException{
        requestData.setHttpMethod("GET");
        requestData.setPathParams(Map.of("id", "0"));
        when(folderService.getFolder(context, "0")).thenThrow(new FolderException(FolderErrorCode.FOLDER_ID_NOT_FOUND));
        ResponseData responseData = folderHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(404, responseData.getStatusCode());
    }
    @Test
    void deleteFolderWrongId () throws FolderException{
        requestData.setHttpMethod("DELETE");
        requestData.setPathParams(Map.of("id", "not number"));
        when(folderService.deleteFolder(context, "not number")).thenThrow(new FolderException(FolderErrorCode.FOLDER_ID_INCORRECT));
        ResponseData responseData = folderHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(400, responseData.getStatusCode());
    }

    @Test
    void postFolderWrongParentId () throws FolderException{
        requestData.setHttpMethod("POST");
        when(folderService.addNewFolder(context, requestData)).thenThrow(new FolderException(FolderErrorCode.FOLDER_ID_NOT_FOUND));
        ResponseData responseData = folderHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(404, responseData.getStatusCode());
    }
    @Test
    void getAllFoldersDatabaseError () throws FolderException{
        requestData.setHttpMethod("GET");
        when(folderService.getAllFolders(context)).thenThrow(new FolderException(FolderErrorCode.FAIL_DATABASE_CONNECTION));
        ResponseData responseData = folderHandler.handle(requestData, context);
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(503, responseData.getStatusCode());
    }

}

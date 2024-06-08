package org.smartup;

import com.google.gson.Gson;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartup.dto.response.FolderResponseDto;
import org.smartup.exception.ApiException;
import org.smartup.exception.DatabaseException;
import org.smartup.service.FolderService;
import yandex.cloud.sdk.functions.Context;
import yandex.cloud.sdk.functions.YcFunction;

import java.util.List;

public class FolderHandler implements YcFunction<RequestData, ResponseData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderHandler.class);
    private static final Gson gson = new Gson();
    @Setter
    private FolderService folderService = new FolderService();

    @Override
    public ResponseData handle(RequestData request, Context context) {
        String method = request.getHttpMethod();
        LOGGER.info(" # " + method);

        String id = request.getPathParams()==null ? null : request.getPathParams().get("id");
        if (!(id==null)) {
            LOGGER.info(" # request id: " + id);
        }

        switch (method) {
            case ("POST") -> {
                try {
                    FolderResponseDto responseDto = folderService.addNewFolder(context, request);
                    LOGGER.info(gson.toJson(responseDto));
                    return new ResponseData(gson.toJson(responseDto));
                } catch (DatabaseException exc) {
                    return new ResponseData(exc.getStatusCode(), exc.getMessage(), "text/plain");
                } catch (ApiException exc) {
                    return new ResponseData(400, exc.getMessage(), "text/plain");
                }
            }
            case ("DELETE") -> {
                try {
                    if (id == null) {
                        folderService.deleteAllFolders(context);
                        return new ResponseData("Ok");
                    } else {
                        FolderResponseDto responseDto = folderService.deleteFolder(context, id);
                        return new ResponseData(gson.toJson(responseDto));
                    }
                } catch (DatabaseException exc) {
                    return new ResponseData(exc.getStatusCode(), exc.getMessage(), "text/plain");
                } catch (ApiException exc) {
                    return new ResponseData(400, exc.getMessage(), "text/plain");
                }
            }
            case ("GET") -> {
                try {
                    if (id == null) {
                        List<FolderResponseDto> responseDto = folderService.getAllFolders(context);
                        return new ResponseData(gson.toJson(responseDto));
                    } else {
                        FolderResponseDto responseDto = folderService.getFolder(context, id);
                        return new ResponseData(gson.toJson(responseDto));
                    }
                } catch (DatabaseException exc) {
                    return new ResponseData(exc.getStatusCode(), exc.getMessage(), "text/plain");
                } catch (ApiException exc) {
                    return new ResponseData(400, exc.getMessage(), "text/plain");
                }
            }
            case ("OPTIONS") -> {
                ResponseData response = new ResponseData (204, "No content", "text/plain");
                response.getHeaders().put("Allow", "OPTIONS, GET, DELETE, POST");
                return response;
            }
        }
       return new ResponseData (405, "Method not supported", "text/plain");
    }
}
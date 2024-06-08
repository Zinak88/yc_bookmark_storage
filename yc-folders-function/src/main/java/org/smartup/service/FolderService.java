package org.smartup.service;

import com.google.gson.Gson;
import lombok.Setter;
import org.smartup.RequestData;
import org.smartup.dao.FolderDao;
import org.smartup.dbc.DatabaseConnector;
import org.smartup.dto.request.FolderRequestDto;
import org.smartup.dto.response.FolderResponseDto;
import org.smartup.exception.ApiException;
import org.smartup.exception.BookmarkException;
import org.smartup.exception.DatabaseException;
import org.smartup.exception.ServerErrorCode;
import org.smartup.mapper.Mapper;

import org.smartup.model.Folder;
import yandex.cloud.sdk.functions.Context;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class FolderService {
    @Setter
    private Mapper mapper = new Mapper();
    @Setter
    private FolderDao folderDao = new FolderDao();
    @Setter
    private Connection connection;
    private static final Gson gson = new Gson();

    public FolderResponseDto addNewFolder(Context context, RequestData request) throws ApiException {

        if ((request.getBody()==null) || (!request.getHeaders().get("Content-Type").equals("application/json"))) {
            throw new BookmarkException(ServerErrorCode.INCORRECT_POST_BODY);
        }
        FolderRequestDto dto = gson.fromJson(request.getBody(), FolderRequestDto.class);
        if (dto.getTitle()==null || dto.getTitle().isBlank()) {
            throw new BookmarkException(ServerErrorCode.TITLE_IS_BLANK);
        }
        Folder folder = mapper.folderRequestDtoToFolder(dto);
        initConnection(context);
        folder = folderDao.addNewFolder(connection, folder);
        return mapper.folderToFolderResponseDto(folder);
    }
    public List<FolderResponseDto> getAllFolders(Context context) throws ApiException{
        initConnection(context);
        List<Folder> folders = folderDao.getAllFolders(connection);
        List<FolderResponseDto> responseDto = new ArrayList<>();
        for (Folder folder : folders) {
            responseDto.add(mapper.folderToFolderResponseDto(folder));
        }
        return responseDto;
    }
    public FolderResponseDto getFolder(Context context, String requestId) throws ApiException{
        long id = parseRequestId(requestId);
        initConnection(context);
        Folder folder = folderDao.getFolder(connection, id);
        if (folder==null) {
            throw new BookmarkException(ServerErrorCode.FOLDER_ID_NOT_FOUND);
        }
        return mapper.folderToFolderResponseDto(folder);
    }

    public FolderResponseDto deleteFolder(Context context, String requestId) throws ApiException{
        long id = parseRequestId(requestId);
        initConnection(context);
        Folder folder = folderDao.deleteFolder(connection, id);
        if (folder==null) {
            throw new BookmarkException(ServerErrorCode.FOLDER_ID_NOT_FOUND);
        } else {
            return mapper.folderToFolderResponseDto(folder);
        }
    }

    public void deleteAllFolders(Context context) throws ApiException{
        initConnection(context);
        folderDao.deleteAllFolders(connection);
    }

    private void initConnection (Context context) throws DatabaseException {
        if (connection==null){
            connection = new DatabaseConnector().get(context);
        }
    }

    private long parseRequestId (String requestId) throws BookmarkException{
        try {
            return Long.parseLong(requestId);
        } catch (NumberFormatException exc) {
            throw new BookmarkException(ServerErrorCode.FOLDER_ID_INCORRECT);
        }
    }
}

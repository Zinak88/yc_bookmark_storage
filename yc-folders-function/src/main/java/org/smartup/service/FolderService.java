package org.smartup.service;

import com.google.gson.Gson;
import lombok.Setter;
import org.smartup.RequestData;
import org.smartup.dao.FolderDao;
import org.smartup.dbc.DatabaseConnector;
import org.smartup.dto.request.FolderRequestDto;
import org.smartup.dto.response.FolderResponseDto;
import org.smartup.exception.FolderErrorCode;
import org.smartup.exception.FolderException;
import org.smartup.exception.DatabaseException;
import org.smartup.mapper.FolderMapper;

import org.smartup.model.Folder;
import yandex.cloud.sdk.functions.Context;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class FolderService {
    @Setter
    private FolderMapper folderMapper = new FolderMapper();
    @Setter
    private FolderDao folderDao = new FolderDao();
    @Setter
    private Connection connection;
    private static final Gson gson = new Gson();

    public FolderResponseDto addNewFolder(Context context, RequestData request) throws FolderException {

        if ((request.getBody()==null) || (!request.getHeaders().get("Content-Type").equals("application/json"))) {
            throw new FolderException(FolderErrorCode.INCORRECT_POST_BODY);
        }
        FolderRequestDto dto = gson.fromJson(request.getBody(), FolderRequestDto.class);
        if (dto.getTitle()==null || dto.getTitle().isBlank()) {
            throw new FolderException(FolderErrorCode.TITLE_IS_BLANK);
        }
        Folder folder = folderMapper.folderRequestDtoToFolder(dto);
        initConnection(context);
        folder = folderDao.addNewFolder(connection, folder);
        return folderMapper.folderToFolderResponseDto(folder);
    }
    public List<FolderResponseDto> getAllFolders(Context context) throws FolderException{
        initConnection(context);
        List<Folder> folders = folderDao.getAllFolders(connection);
        List<FolderResponseDto> responseDto = new ArrayList<>();
        for (Folder folder : folders) {
            responseDto.add(folderMapper.folderToFolderResponseDto(folder));
        }
        return responseDto;
    }
    public FolderResponseDto getFolder(Context context, String requestId) throws FolderException{
        long id = parseFolderRequestId(requestId);
        initConnection(context);
        Folder folder = folderDao.getFolder(connection, id);
        if (folder==null) {
            throw new FolderException(FolderErrorCode.FOLDER_ID_NOT_FOUND);
        }
        return folderMapper.folderToFolderResponseDto(folder);
    }

    public FolderResponseDto deleteFolder(Context context, String requestId) throws FolderException{
        long id = parseFolderRequestId(requestId);
        initConnection(context);
        Folder folder = folderDao.deleteFolder(connection, id);
        if (folder==null) {
            throw new FolderException(FolderErrorCode.FOLDER_ID_NOT_FOUND);
        } else {
            return folderMapper.folderToFolderResponseDto(folder);
        }
    }

    public void deleteAllFolders(Context context) throws FolderException{
        initConnection(context);
        folderDao.deleteAllFolders(connection);
    }

    private void initConnection (Context context) throws FolderException {
        try {
            if (connection == null) {
                connection = new DatabaseConnector().get(context);
            }
        } catch (DatabaseException exc) {
        throw new FolderException(FolderErrorCode.FAIL_DATABASE_CONNECTION);
    }
    }

    private long parseFolderRequestId(String requestId) throws FolderException {
        try {
            return Long.parseLong(requestId);
        } catch (NumberFormatException exc) {
            throw new FolderException(FolderErrorCode.FOLDER_ID_INCORRECT);
        }
    }
}

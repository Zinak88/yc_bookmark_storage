package org.smartup.service;

import com.jsoniter.JsonIterator;
import lombok.Setter;
import org.smartup.RequestData;
import org.smartup.dao.FolderDao;
import org.smartup.dto.request.FolderRequestDto;
import org.smartup.dto.response.FolderResponseDto;
import org.smartup.exception.ApiException;
import org.smartup.exception.BookmarkException;
import org.smartup.exception.ServerErrorCode;
import org.smartup.mapper.Mapper;
import org.smartup.model.Folder;
import yandex.cloud.sdk.functions.Context;

import java.util.ArrayList;
import java.util.List;

public class FolderService {
    @Setter
    private Mapper mapper = new Mapper();
    @Setter
    private FolderDao folderDao = new FolderDao();

    public FolderResponseDto addNewFolder(Context context, RequestData request) throws ApiException {

        if ((request.getBody()==null) || (!request.getHeaders().get("Content-Type").equals("application/json"))) {
            throw new BookmarkException(ServerErrorCode.INCORRECT_POST_BODY);
        }
        FolderRequestDto dto = JsonIterator.deserialize(request.getBody(), FolderRequestDto.class);
        if (dto.getTitle()==null || dto.getTitle().isBlank()) {
            throw new BookmarkException(ServerErrorCode.TITLE_IS_BLANK);
        }
        Folder folder = mapper.folderRequestDtoToFolder(dto);
        folder = folderDao.addNewFolder(context, folder);
        return mapper.folderToFolderResponseDto(folder);
    }
    public List<FolderResponseDto> getAllFolders(Context context) throws ApiException{
        List<Folder> folders = folderDao.getAllFolders(context);
        List<FolderResponseDto> responseDto = new ArrayList<>();
        for (Folder folder : folders) {
            responseDto.add(mapper.folderToFolderResponseDto(folder));
        }
        return responseDto;
    }
    public FolderResponseDto getFolder(Context context, String requestId) throws ApiException{
        long id;
        try {
            id = Long.parseLong(requestId);
        } catch (NumberFormatException exc) {
            throw new BookmarkException(ServerErrorCode.FOLDER_ID_INCORRECT);
        }
        Folder folder = folderDao.getFolder(context, id);
        if (folder==null) {
            throw new BookmarkException(ServerErrorCode.FOLDER_ID_NOT_FOUND);
        }
        return mapper.folderToFolderResponseDto(folder);
    }

    public FolderResponseDto deleteFolder(Context context, String requestId) throws ApiException{
        long id;
        try {
            id = Long.parseLong(requestId);
        } catch (NumberFormatException exc) {
            throw new BookmarkException(ServerErrorCode.FOLDER_ID_INCORRECT);
        }
        Folder folder = folderDao.deleteFolder(context, id);
        if (folder==null) {
            throw new BookmarkException(ServerErrorCode.FOLDER_ID_NOT_FOUND);
        } else {
            return mapper.folderToFolderResponseDto(folder);
        }
    }

    public void deleteAllFolders(Context context) throws ApiException{
        folderDao.deleteAllFolders(context);
    }
}

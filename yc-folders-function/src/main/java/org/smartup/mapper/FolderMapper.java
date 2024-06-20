package org.smartup.mapper;

import org.smartup.dto.request.FolderRequestDto;
import org.smartup.dto.response.FolderResponseDto;
import org.smartup.model.Folder;

public class FolderMapper {

    public Folder folderRequestDtoToFolder (FolderRequestDto dto) {
        Folder f = new Folder();
        f.setTitle(dto.getTitle());
        f.setDescription(dto.getDescription());
        f.setParentFolderId(dto.getParentFolderId());
        return  f;
    }

    public FolderResponseDto folderToFolderResponseDto (Folder f) {
        String addedDate = f.getAddedDate() == null ? null : f.getAddedDate().toString();
        return new FolderResponseDto(f.getId(), f.getTitle(), f.getDescription(), f.getParentFolderId(), addedDate);
    }
}

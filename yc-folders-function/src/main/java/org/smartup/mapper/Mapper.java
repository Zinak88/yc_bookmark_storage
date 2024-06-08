package org.smartup.mapper;

import org.smartup.dto.request.FolderRequestDto;
import org.smartup.dto.response.FolderResponseDto;
import org.smartup.model.Folder;

public class Mapper {

    public Folder folderRequestDtoToFolder (FolderRequestDto dto) {
        Folder f = new Folder();
        f.setTitle(dto.getTitle());
        String description = dto.getDescription();
        if (!(description==null) && !description.isBlank()) {
            f.setDescription(description);
        }
        Long parentFolderId = dto.getParentFolderId();
        if(!(parentFolderId==null)) {
            f.setParentFolderId(parentFolderId);
        }
        return  f;
    }

    public FolderResponseDto folderToFolderResponseDto (Folder f) {
        return new FolderResponseDto(f.getId(), f.getTitle(), f.getDescription(), f.getParentFolderId(), f.getAddedDate().toString());
    }
}

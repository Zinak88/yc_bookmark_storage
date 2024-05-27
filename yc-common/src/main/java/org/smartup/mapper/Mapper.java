package org.smartup.mapper;

import org.smartup.dto.request.BookmarkRequestDto;
import org.smartup.dto.response.BookmarkResponseDto;
import org.smartup.dto.request.FolderRequestDto;
import org.smartup.dto.response.FolderResponseDto;
import org.smartup.model.Bookmark;
import org.smartup.model.Folder;

public class Mapper {
    public Bookmark bookmarkRequestDtoToBookmark (BookmarkRequestDto dto) {
        Bookmark bm = new Bookmark();
        bm.setUrl(dto.getUrl());
        String description = dto.getDescription();
        if (!(description==null) && !description.isBlank()) {
            bm.setDescription(description);
        }
        Long parentFolderId = dto.getParentFolderId();
        if(!(parentFolderId==null)) {
            bm.setParentFolderId(parentFolderId);
        }
        return bm;
    }

    public BookmarkResponseDto bookmarkToBookmarkResponseDto (Bookmark bm) {
        return new BookmarkResponseDto(bm.getId(), bm.getUrl(), bm.getDescription(), bm.getParentFolderId(), bm.getAddedDate().toString());
    }

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

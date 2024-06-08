package org.smartup.mapper;

import org.smartup.dto.request.BookmarkRequestDto;
import org.smartup.dto.response.BookmarkResponseDto;
import org.smartup.model.Bookmark;

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

}

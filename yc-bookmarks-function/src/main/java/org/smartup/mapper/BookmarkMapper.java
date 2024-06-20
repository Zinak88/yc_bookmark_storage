package org.smartup.mapper;

import org.smartup.dto.request.BookmarkRequestDto;
import org.smartup.dto.response.BookmarkResponseDto;
import org.smartup.model.Bookmark;

public class BookmarkMapper {
    public Bookmark bookmarkRequestDtoToBookmark (BookmarkRequestDto dto) {
        Bookmark bm = new Bookmark();
        bm.setUrl(dto.getUrl());
        bm.setDescription(dto.getDescription());
        bm.setParentFolderId(dto.getParentFolderId());

        return bm;
    }

    public BookmarkResponseDto bookmarkToBookmarkResponseDto (Bookmark bm) {
        String addedDate = bm.getAddedDate() == null ? null : bm.getAddedDate().toString();
        return new BookmarkResponseDto(bm.getId(), bm.getUrl(), bm.getDescription(), bm.getParentFolderId(), addedDate);
    }

}

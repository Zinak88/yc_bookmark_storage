package org.smartup.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.smartup.dto.request.BookmarkRequestDto;
import org.smartup.dto.response.BookmarkResponseDto;
import org.smartup.model.Bookmark;

import java.time.LocalDate;
import java.time.Month;

public class BookmarkMapperTest {
    private BookmarkMapper bookmarkMapper;
    @BeforeEach
    void setUp () {
        bookmarkMapper = new BookmarkMapper();
    }
    @Test
    @DisplayName("Bookmark: dto -> model")
    void bookmarkRequestDtoToBookmark(){
        BookmarkRequestDto dto = new BookmarkRequestDto();

        Bookmark mappedBookmark = bookmarkMapper.bookmarkRequestDtoToBookmark(dto);
        Assertions.assertNotNull(mappedBookmark);
        Assertions.assertNull(mappedBookmark.getUrl());
        Assertions.assertNull(mappedBookmark.getDescription());
        Assertions.assertNull(mappedBookmark.getParentFolderId());
    }
    @Test
    @DisplayName("Bookmark: dto (all params) -> model")
    void bookmarkRequestDtoToBookmarkWithAllParams(){
        BookmarkRequestDto dto = new BookmarkRequestDto();
        dto.setUrl("url");
        dto.setDescription("desc");
        dto.setParentFolderId(1L);

        Bookmark mappedBookmark = bookmarkMapper.bookmarkRequestDtoToBookmark(dto);
        Assertions.assertNotNull(mappedBookmark);
        Assertions.assertEquals(dto.getUrl(), mappedBookmark.getUrl());
        Assertions.assertEquals(dto.getDescription(), mappedBookmark.getDescription());
        Assertions.assertEquals(dto.getParentFolderId(), mappedBookmark.getParentFolderId());
    }

    @Test
    @DisplayName("Bookmark: model -> dto")
    void bookmarkToBookmarkResponseDto(){
        Bookmark bookmark = new Bookmark();

        BookmarkResponseDto mappedDto = bookmarkMapper.bookmarkToBookmarkResponseDto(bookmark);
        Assertions.assertNotNull(mappedDto);
        Assertions.assertNull(mappedDto.getUrl());
        Assertions.assertNull(mappedDto.getId());
        Assertions.assertNull(mappedDto.getDescription());
        Assertions.assertNull(mappedDto.getAddedDate());
        Assertions.assertNull(mappedDto.getParentFolderId());
    }

    @Test
    @DisplayName("Bookmark: model (all params) -> dto")
    void bookmarkToBookmarkResponseDtoWithAllParams(){
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl("url");
        bookmark.setId(1L);
        bookmark.setAddedDate(LocalDate.of(2024, Month.APRIL, 1));
        bookmark.setParentFolderId(2L);
        bookmark.setDescription("desc");

        BookmarkResponseDto mappedDto = bookmarkMapper.bookmarkToBookmarkResponseDto(bookmark);
        Assertions.assertNotNull(mappedDto);
        Assertions.assertEquals(bookmark.getUrl(), mappedDto.getUrl());
        Assertions.assertEquals(bookmark.getId(), mappedDto.getId());
        Assertions.assertEquals(bookmark.getParentFolderId(), mappedDto.getParentFolderId());
        Assertions.assertEquals(bookmark.getDescription(), mappedDto.getDescription());
        Assertions.assertEquals(bookmark.getAddedDate().toString(), mappedDto.getAddedDate());
    }
}

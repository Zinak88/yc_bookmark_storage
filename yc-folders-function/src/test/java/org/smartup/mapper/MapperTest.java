package org.smartup.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.smartup.dto.request.BookmarkRequestDto;
import org.smartup.dto.request.FolderRequestDto;
import org.smartup.dto.response.BookmarkResponseDto;
import org.smartup.dto.response.FolderResponseDto;
import org.smartup.model.Bookmark;
import org.smartup.model.Folder;

import java.time.LocalDate;
import java.time.Month;

public class MapperTest {
    private Mapper mapper;
    @BeforeEach
    void setUp () {
        mapper = new Mapper();
    }

    @Test
    @DisplayName("Folder: dto -> model")
    void folderRequestDtoToFolder(){
        FolderRequestDto dto = new FolderRequestDto();
        dto.setTitle("title");

        Folder mappedFolder = mapper.folderRequestDtoToFolder(dto);
        Assertions.assertNotNull(mappedFolder);
        Assertions.assertEquals(dto.getTitle(), mappedFolder.getTitle());
    }
    @Test
    @DisplayName("Folder: model -> dto")
    void folderToFolderResponseDto(){
        Folder folder = new Folder();
        folder.setTitle("title");
        folder.setId(1L);
        folder.setAddedDate(LocalDate.of(2024, Month.APRIL, 1));

        FolderResponseDto mappedDto = mapper.folderToFolderResponseDto(folder);
        Assertions.assertNotNull(mappedDto);
        Assertions.assertEquals(folder.getTitle(), mappedDto.getTitle());
        Assertions.assertEquals(folder.getId(), mappedDto.getId());
    }
}

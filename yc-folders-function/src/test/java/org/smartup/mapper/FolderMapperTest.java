package org.smartup.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.smartup.dto.request.FolderRequestDto;
import org.smartup.dto.response.FolderResponseDto;
import org.smartup.model.Folder;

import java.time.LocalDate;
import java.time.Month;

public class FolderMapperTest {
    private FolderMapper folderMapper;
    @BeforeEach
    void setUp () {
        folderMapper = new FolderMapper();
    }

    @Test
    @DisplayName("Folder: dto -> model")
    void folderRequestDtoToFolder(){
        FolderRequestDto dto = new FolderRequestDto();

        Folder mappedFolder = folderMapper.folderRequestDtoToFolder(dto);
        Assertions.assertNotNull(mappedFolder);
        Assertions.assertNull(mappedFolder.getTitle());
        Assertions.assertNull(mappedFolder.getDescription());
        Assertions.assertNull(mappedFolder.getParentFolderId());
    }

    @Test
    @DisplayName("Folder: dto (full params)-> model")
    void folderRequestDtoToFolderFullParams(){
        FolderRequestDto dto = new FolderRequestDto();
        dto.setTitle("title");
        dto.setDescription("description");
        dto.setParentFolderId(1L);

        Folder mappedFolder = folderMapper.folderRequestDtoToFolder(dto);
        Assertions.assertNotNull(mappedFolder);
        Assertions.assertEquals(dto.getTitle(), mappedFolder.getTitle());
        Assertions.assertEquals(dto.getDescription(), mappedFolder.getDescription());
        Assertions.assertEquals(dto.getParentFolderId(), mappedFolder.getParentFolderId());
    }

    @Test
    @DisplayName("Folder: model -> dto")
    void folderToFolderResponseDto(){
        Folder folder = new Folder();

        FolderResponseDto mappedDto = folderMapper.folderToFolderResponseDto(folder);
        Assertions.assertNotNull(mappedDto);
        Assertions.assertNull(mappedDto.getAddedDate());
        Assertions.assertNull(mappedDto.getTitle());
        Assertions.assertNull(mappedDto.getParentFolderId());
        Assertions.assertNull(mappedDto.getDescription());
        Assertions.assertNull(mappedDto.getId());
    }

    @Test
    @DisplayName("Folder: model (full params)-> dto")
    void folderToFolderResponseDtoFullParams(){
        Folder folder = new Folder();
        folder.setTitle("title");
        folder.setId(1L);
        folder.setAddedDate(LocalDate.of(2024, Month.APRIL, 1));
        folder.setParentFolderId(2L);
        folder.setDescription("description");

        FolderResponseDto mappedDto = folderMapper.folderToFolderResponseDto(folder);
        Assertions.assertNotNull(mappedDto);
        Assertions.assertEquals(folder.getTitle(), mappedDto.getTitle());
        Assertions.assertEquals(folder.getId(), mappedDto.getId());
        Assertions.assertEquals(folder.getAddedDate().toString(), mappedDto.getAddedDate());
        Assertions.assertEquals(folder.getParentFolderId(), mappedDto.getParentFolderId());
        Assertions.assertEquals(folder.getDescription(), mappedDto.getDescription());
    }
}

package org.smartup.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class FolderResponseDto {
    private Long id;
    private String title;
    private String description;
    private Long parentFolderId;
    private String addedDate;
    private Map<Integer,String> folders;
    private Map<Integer,String> bookmarks;
    public FolderResponseDto (Long id,String title,String description,Long parentFolderId,String addedDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.parentFolderId = parentFolderId;
        this.addedDate = addedDate;
    }


}

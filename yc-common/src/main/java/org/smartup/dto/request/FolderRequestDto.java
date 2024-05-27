package org.smartup.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderRequestDto {
    private String description;
    private String title;
    private Long parentFolderId;
}

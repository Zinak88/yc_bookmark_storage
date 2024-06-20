package org.smartup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponseDto {
    private Long id;
    private String url;
    private String description;
    private Long parentFolderId;
    private String addedDate;

}

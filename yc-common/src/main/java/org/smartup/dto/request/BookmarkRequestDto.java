package org.smartup.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkRequestDto {

        private String url;
        private String description;
        private Long parentFolderId;


}

package org.smartup.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class Bookmark {

    private Long id;

    private String url;

    private LocalDate addedDate;

    private String description;

    private Long parentFolderId;

}

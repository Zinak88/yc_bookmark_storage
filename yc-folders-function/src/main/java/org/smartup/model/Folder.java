package org.smartup.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Folder {

    private Long id;

    private String title;

    private String description;

    private LocalDate addedDate;

   // private List<Bookmark> bookmarkList;

  //  private List<Folder> folderList;

    private Long parentFolderId;

}

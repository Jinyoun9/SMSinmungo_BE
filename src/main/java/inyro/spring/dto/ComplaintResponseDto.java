package inyro.spring.dto;

import inyro.spring.entity.Complaint;
import inyro.spring.enums.ComplaintStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ComplaintResponseDto {
    private Long id;
    private String author;
    private String title;
    private String contents;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int view;
    private int good;
    private ComplaintStatus status;

    //private String department;

    public ComplaintResponseDto(Complaint complaint){
        this.id = complaint.getId();
        this.author = complaint.getAuthor();
        this.title = complaint.getTitle();
        this.contents = complaint.getContents();
        this.category = complaint.getCategory();
        this.createdAt = complaint.getCreatedAt();
        this.modifiedAt = complaint.getModifiedAt();
        this.view = complaint.getView();
        this.good = complaint.getGood();
        this.status = complaint.getStatus();

        //this.department = complaint.getDepartment();
    }

}

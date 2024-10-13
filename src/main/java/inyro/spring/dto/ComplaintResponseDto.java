package inyro.spring.dto;

import inyro.spring.entity.Complaint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ComplaintResponseDto {
    private Long id;
    private String author;
    private String password;
    private String title;
    private String contents;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int view;
    private int good;
    private String status;

    public ComplaintResponseDto(Complaint entity){
        this.id = entity.getId();
        this.author = entity.getAuthor();
        this.title = entity.getTitle();
        this.contents = entity.getContents();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
        this.view = entity.getView();
        this.good = entity.getGood();
        this.status = entity.getStatus();
    }
}

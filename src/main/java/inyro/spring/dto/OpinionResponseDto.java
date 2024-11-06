package inyro.spring.dto;

import inyro.spring.entity.Opinion;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OpinionResponseDto {
    private Long id;
    private String author;
    private String category;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int view;
    private int good;


    public OpinionResponseDto(Opinion opinion){
        this.id = opinion.getId();
        this.author = opinion.getAuthor();
        this.category = "대화";
        this.contents = opinion.getContents();
        this.createdAt = opinion.getCreatedAt();
        this.modifiedAt = opinion.getModifiedAt();
        this.good = opinion.getGood();
        this.view = opinion.getView();
    }
}

package com.smsinmungo.model;

import com.smsinmungo.dto.UnivPostDto;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UnivPost extends Timestamp{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int view;

    @Column(nullable = false)
    private int good;

    @Column(nullable = false)
    private String status;

    public void like() {
        this.good++;
    }

    public UnivPost(UnivPostDto univPostDto) {
        this.author = univPostDto.getAuthor();
        this.password = univPostDto.getPassword();
        this.title = univPostDto.getTitle();
        this.contents = univPostDto.getContents();
        this.category = univPostDto.getCategory();
        this.view = 0;
        this.good = 0;
        this.status = "NEW";
    }
}

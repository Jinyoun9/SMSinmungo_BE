package inyro.spring.entity;

import inyro.spring.dto.ComplaintRequestsDto;
import inyro.spring.dto.OpinionRequestsDto;
import inyro.spring.enums.Category;
import inyro.spring.enums.ComplaintStatus;
import inyro.spring.enums.Department;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends Timestamped {

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

    @Enumerated(EnumType.STRING)
    private Department department; // 민원에만 필요

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int good;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status;

    public Post(ComplaintRequestsDto requestsDto) {
        this.author = requestsDto.getAuthor();
        this.password = requestsDto.getPassword();
        this.title = requestsDto.getTitle();
        this.contents = requestsDto.getContents();
        this.department = requestsDto.getDepartment();
        this.category = Category.민원;
        this.status = ComplaintStatus.NEW; // 기본값 설정
    }

    public Post(OpinionRequestsDto requestsDto) {
        this.title = requestsDto.getTitle();
        this.contents = requestsDto.getContents();
        this.author = requestsDto.getAuthor();
        this.password = requestsDto.getPassword();
        this.category = Category.의견;
    }

    public void update(ComplaintRequestsDto dto) {
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.department = dto.getDepartment();
        this.category = dto.getCategory();
        this.status = dto.getStatus(); //관리자가 수정할 때
    }

    public void update(OpinionRequestsDto requestsDto) {
        this.title = requestsDto.getTitle();
        this.contents = requestsDto.getContents();
    }
}
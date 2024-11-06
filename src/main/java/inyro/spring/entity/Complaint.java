package inyro.spring.entity;

import inyro.spring.dto.ComplaintRequestsDto;
import inyro.spring.enums.Category;
import inyro.spring.enums.ComplaintStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Complaint extends Timestamped {

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
    private String department;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    @Column(columnDefinition = "integer default 0",nullable = false)
    private int good;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status;

    public Complaint(ComplaintRequestsDto requestsDto) {
        this.author = requestsDto.getAuthor();
        this.password = requestsDto.getPassword();
        this.title = requestsDto.getTitle();
        this.contents = requestsDto.getContents();
        this.department = requestsDto.getDepartment();
        this.category = Category.민원;
        this.status = ComplaintStatus.NEW; // 기본값 설정
        //this.department = requestDto.getDepartment();
    }

    public void update(ComplaintRequestsDto dto) {
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.department = dto.getDepartment();
        this.category = dto.getCategory();
        this.status = dto.getStatus(); //관리자가 수정할 때
    }
}

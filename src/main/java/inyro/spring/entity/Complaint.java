package inyro.spring.entity;

import inyro.spring.dto.ComplaintRequestsDto;
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
    private String category;

    @Column(nullable = false)
    private int view;

    @Column(nullable = false)
    private int good;

    @Column(nullable = false)
    private String status;

    public Complaint(ComplaintRequestsDto requestsDto) {
        this.author = requestsDto.getAuthor();
        this.password = requestsDto.getPassword();
        this.title = requestsDto.getTitle();
        this.contents = requestsDto.getContents();
        this.category = requestsDto.getCategory();
        this.view = 0;
        this.good = 0;
        this.status = "NEW";
    }
}

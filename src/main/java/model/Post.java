package model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long post_id;
    @Column(nullable = false)
    private Long staff_id;
    @Column(nullable = false)
    private Long student_id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(name = "writedatetime", nullable = false)
    private LocalDateTime write_datetime;
    private int good;
    @Column(nullable = false, length = 10)

    private String department;
    @Column(nullable = false, length = 5)

    private String category;
    private String file;
    private Long view;
    @Column(nullable = false, length = 10)
    private String status;

    public void setStatus(String status){
        this.status = status;
    }

}

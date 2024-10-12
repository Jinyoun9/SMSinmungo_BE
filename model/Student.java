package inyro.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
public class Student {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String major;
}

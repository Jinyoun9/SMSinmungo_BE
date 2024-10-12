package inyro.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
public class Staff {

    @Id
    @GeneratedValue
    @Column(name = "staff_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String department;
}

package inyro.demo.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "staff")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String staffId;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 15)
    private String department;

    @Column(nullable = false, length = 30)
    private String email;

}

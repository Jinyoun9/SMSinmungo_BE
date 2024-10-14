package model;

import jakarta.persistence.*;
@Entity
@Table(name = "member")
public class Member { //role 에 대한 validation 필요
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private String name;
    private String department;
    private String major;



}

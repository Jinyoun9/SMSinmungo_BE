package com.smsinmungo.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;


  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;


//  @Column(nullable = false)
  private String name;

//  @Column(nullable = false)
  private String major;

  private String department;

  private String role;

  private boolean enabled;

  protected Member() {
  }

  public Member(String email, String password, String department, String major,String role) {
    this.email = email;
    this.password = password;
    this.department = department;
    this.major = major;
    this.role = role;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}

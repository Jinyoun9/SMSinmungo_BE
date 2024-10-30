package inyro.SMSinmungBE.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Member {

  @Id
  @GeneratedValue
  @Column(name = "user_id")
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

  protected Member() {
  }

  public Member(String email, String password, String department, String major,String role) {
    this.email = email;
    this.password = password;
    this.department = department;
    this.major = major;
    this.role = role;
  }
}

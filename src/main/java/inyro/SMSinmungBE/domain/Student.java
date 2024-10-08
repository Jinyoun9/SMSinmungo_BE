package inyro.SMSinmungBE.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

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

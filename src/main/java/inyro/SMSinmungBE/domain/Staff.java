package inyro.SMSinmungBE.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

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

package com.smsinmungo.config.security.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
public class RefreshEntity {

  @Id
  @GeneratedValue
  private Long id;

  private String email;
  private String refresh;
  private String expiration;

  protected RefreshEntity() {}

  public RefreshEntity(String email, String refresh, String expiration) {
    this.email = email;
    this.refresh = refresh;
    this.expiration = expiration;
  }
}

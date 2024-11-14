package com.smsinmungo.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;
  private LocalDateTime expiryDate;

  @OneToOne
  @JoinColumn(name = "member_id") // Member의 기본 키인 'member_id' 컬럼을 참조
  private Member member;


  public VerificationToken(String token, LocalDateTime expiryDate, Member member) {
    this.token = token;
    this.expiryDate = expiryDate;
    this.member = member;
  }
}

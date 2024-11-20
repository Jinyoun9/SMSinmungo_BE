package com.smsinmungo.service;

import com.smsinmungo.dto.LogInDto;
import com.smsinmungo.dto.SignUpDto;
import com.smsinmungo.repository.MemberRepository;
import com.smsinmungo.domain.Member;
import com.smsinmungo.domain.VerificationToken;
import com.smsinmungo.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

  private final MemberRepository memberRepository;
  private final VerificationTokenRepository verificationTokenRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final EmailService emailService;

  public void signUp(SignUpDto signUpDto) throws MessagingException {
    String email = signUpDto.getEmail();
    String password = signUpDto.getPassword();
    String department = signUpDto.getDepartment();
    String major = signUpDto.getMajor();

    // 이메일 중복 체크 제거
    Boolean isExist = memberRepository.existsByEmail(email);
    if (isExist) {
         throw new IllegalArgumentException("Email already exists");
    }

    // 비밀번호 암호화 후 Member 저장 (활성화된 상태로 저장)
    Member member = new Member(email, bCryptPasswordEncoder.encode(password), department, major, "ROLE_USER");
    member.setEnabled(true); // 이메일 인증 없이 바로 활성화 상태로 저장
    memberRepository.save(member);

    // 인증 토큰 생성 및 저장 제거
    String token = UUID.randomUUID().toString();
    VerificationToken verificationToken = new VerificationToken(token, LocalDateTime.now().plusHours(24), member);
    verificationTokenRepository.save(verificationToken);

    // 이메일로 인증 코드 전송 제거
    String title = "회원가입 이메일 인증";
    String content = "<html><body><h1>인증 코드: " + token + "</h1><p>해당 코드를 홈페이지에 입력하세요.</p></body></html>";
    emailService.sendEmail(email, title, content);
  }


  public void verifyEmail(String token) {
    VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                                                                     .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

    if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Token expired");
    }

    Member member = verificationToken.getMember();
    member.setEnabled(true);
    memberRepository.save(member);

    verificationTokenRepository.delete(verificationToken);
  }



}

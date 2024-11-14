package com.smsinmungo.controller;

import com.smsinmungo.dto.SignUpDto;
import com.smsinmungo.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JoinController {

  private final AuthService authService;

  @PostMapping("/join")
  public ResponseEntity<String> joinProcess(@RequestBody SignUpDto signUpDto) {
    log.info("회원가입 요청 - email: {}, department: {}", signUpDto.getEmail(), signUpDto.getDepartment());

    try {
      authService.signUp(signUpDto);
      return ResponseEntity.ok("회원가입 요청이 완료되었습니다. 이메일을 확인하여 인증을 완료하세요.");
    } catch (IllegalArgumentException e) {
      log.error("회원가입 실패: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (MessagingException e) {
      log.error("이메일 전송 실패: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송에 실패했습니다. 다시 시도해주세요.");
    }
  }

  @GetMapping("/verify-email")
  public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
    try {
      authService.verifyEmail(token);
      return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    } catch (IllegalArgumentException e) {
      log.error("이메일 인증 실패: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

}

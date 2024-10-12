package inyro.SMSinmungBE.controller;

import inyro.SMSinmungBE.dto.SignUpDto;
import inyro.SMSinmungBE.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JoinController {

  private final AuthService authService;

  @PostMapping("/join")
  public String joinProcess(@RequestBody SignUpDto signUpDto) {

    log.info("email: {}, password: {}", signUpDto.getEmail(), signUpDto.getPassword());
    authService.signUp(signUpDto);
    return "ok";
  }

}

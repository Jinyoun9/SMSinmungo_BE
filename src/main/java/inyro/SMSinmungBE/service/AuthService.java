package inyro.SMSinmungBE.service;

import inyro.SMSinmungBE.domain.Member;
import inyro.SMSinmungBE.dto.LogInDto;
import inyro.SMSinmungBE.dto.SignUpDto;
import inyro.SMSinmungBE.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

  private final MemberRepository memberRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public void signUp(SignUpDto signUpDto) {
    String email = signUpDto.getEmail();
    String password = signUpDto.getPassword();

    Boolean isExist = memberRepository.existsByEmail(email);

    if (isExist) {
      return ;
    }

    Member member = new Member(email, bCryptPasswordEncoder.encode(password), "ROLE_ADMIN");
    memberRepository.save(member);
  }


}

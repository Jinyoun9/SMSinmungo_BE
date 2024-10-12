package inyro.SMSinmungBE.config.security.custom;

import inyro.SMSinmungBE.domain.Member;
import inyro.SMSinmungBE.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member userData = memberRepository.findByEmail(email);

    if (userData != null) {
      return new CustomUserDetails(userData);
    }

    return null;
  }
}

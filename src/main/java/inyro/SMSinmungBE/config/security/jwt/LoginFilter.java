package inyro.SMSinmungBE.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import inyro.SMSinmungBE.dto.LogInDto;
import inyro.SMSinmungBE.repository.RefreshRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private static final int COOKIE_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

  private final AuthenticationManager authenticationManager;
  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response) throws AuthenticationException {

    LogInDto logInDto = new LogInDto();
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
      logInDto = objectMapper.readValue(messageBody, LogInDto.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    //클라이언트 요청에서 username, password 추출
    String email = logInDto.getEmail();
    String password = logInDto.getPassword();


    /**
     * 스프링 시큐리티에서 email, password 검증하기 위해서는 token 에 담아야 함
     * UsernamePasswordAuthenticationToken 이라는 dto 에 담아서 AuthenticationManager 로 전달
     */
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

    return authenticationManager.authenticate(authToken);
  }


  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain chain, Authentication authentication)
      throws IOException, ServletException {


    //username 가져오기
    String username = authentication.getName();

    //role 가져오기
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    String role = auth.getAuthority();

    String access = jwtUtil.createJwt("access", username, role, ACCESS_TOKEN_EXPIRE_TIME);
    String refresh = jwtUtil.createJwt("refresh", username, role, REFRESH_TOKEN_EXPIRE_TIME);

    //db 에 refresh token 저장
    addRefreshEntity(username, refresh, REFRESH_TOKEN_EXPIRE_TIME);

    //응답 생성
    response.addHeader("access", access);
    response.addCookie(createCookie("refresh", refresh));
    response.setStatus(HttpStatus.OK.value());
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException failed)
      throws IOException, ServletException {

    response.setStatus(401);
  }

  private void addRefreshEntity(String email, String refresh, Long expiredMs) {

    //만료일자
    Date date = new Date(System.currentTimeMillis() + expiredMs);

    RefreshEntity refreshEntity = new RefreshEntity(email, refresh, date.toString());

    refreshRepository.save(refreshEntity);
  }

  private Cookie createCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(COOKIE_EXPIRE_TIME); //생명주기
    cookie.setHttpOnly(true); //JS로 해당 쿠키가 접근되지 못하게 막음

    return cookie;
  }
}

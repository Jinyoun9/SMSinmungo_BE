package inyro.SMSinmungBE.config.security;

import inyro.SMSinmungBE.config.security.jwt.JWTFilter;
import inyro.SMSinmungBE.config.security.jwt.JWTUtil;
import inyro.SMSinmungBE.config.security.jwt.LoginFilter;
import inyro.SMSinmungBE.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthenticationConfiguration authenticationConfiguration;
  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;


  //Password 암호화
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {

    return new BCryptPasswordEncoder();
  }

  //AuthenticationManager Bean 등록
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

    return configuration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //CSRF 비활성화
    http.csrf(csrf -> csrf.disable());
    //WebMvcConfig 설정에 따름
    http.cors(Customizer.withDefaults());

    //FormLogin, BasicHttp 비활성화
    http.formLogin((auth) -> auth.disable());
    http.httpBasic((auth) -> auth.disable());

    //경로별 인가작업
    http.authorizeHttpRequests((auth) ->
        auth.requestMatchers("/login", "/", "/join", "error", "/verify-email").permitAll() //login, root, join 경로에는 모든 권한을 부여
            .requestMatchers("/admin").hasRole("ADMIN") //admin 경로는 ADMIN 권한을 가진 자만이 허용
            .requestMatchers("/reissue").permitAll()
            .anyRequest().authenticated()); //다른 모든 경로는 로그인 한 사용자만 접근 가능

    http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

    http
        .addFilterAt(
            new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository),
            UsernamePasswordAuthenticationFilter.class
        );

    //세션 관리 상태 없음 으로 설정, 서버가 클라이언트의 세션 상태를 유지하지 않음
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


    return http.build();
  }

}

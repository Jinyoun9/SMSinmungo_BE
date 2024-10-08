package inyro.SMSinmungBE.config.security.jwt;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

  //객체 키 생성
  private SecretKey secretKey;

  public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
    //SecretKeySpec 객체를 생성하여 secretKey 필드에 할당 -> SecretKeySpec 객체는 SecretKey 인터페이스를 구현하는 객체
    //secret 문자열을 UTF-8 형식의 바이트 배열로 변환
    //HMAC-SHA256 알고리즘을 설정하여 JWT 서명에 사용될 알고리즘을 지정 -> JWT 의 기본 알고리즘
    this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  //검증 메서드
  public String getEmail(String token) {
    /**
     * Jwts.parser() -> Jwts parser 생성
     * .verifyWith(secretKey) -> 토큰 검증에 사용할 비밀 키 설정 토큰 위 변조 검증이 이루어짐
     * .build() 파서 구성을 완료하고, 검증을 위한 파서 객체를 생성
     * .parseSignedClaims(token) JWT의 서명된 클레임(payload)을 추출하는 메서드
     * .getPayload() 파싱된 클레임 객체에서 payload 부분을 가져옴
     * .get("email", String.class)는 JWT의 페이로드에서 "email"이라는 이름의 클레임을 String 타입으로 가져엄
    */
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
               .get("email", String.class);
  }

  public String getName(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
               .get("name", String.class);
  }

  public String getMajor(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
               .get("major", String.class);
  }

  //토큰 유효기간 만료 확인
  public Boolean isExpired(String token) {
    /**
     * .getExpiration()은 JWT의 만료 시간을 Date 객체로 반환합니다.
     * .before(new Date())는 getExpiration()으로 가져온 만료 시간이 현재 시각(new Date()) 이전인지 확인
     */
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
               .getExpiration().before(new Date());
  }

  //JWTToken 생성
  public String createJwt(String email, String username, String major, Long expiredMs) {
    /**
     * issuedAt 메서드로 토큰 발행 시각을 설정
     * expiration 메서드로 토큰의 만료 시각 설정 현재시각 + expiredMs
     * signWith(secretKey) -> secretKey를 사용에 토큰에 서명
     * compact 메서드는 설정된 내용을 바탕으로 최종 JWT 토큰을 생성하고, 이를 String 형태로 반환
     */
    return Jwts.builder()
               .claim("email", email)
               .claim("username", username)
               .claim("major", major)
               .issuedAt(new Date(System.currentTimeMillis()))
               .expiration(new Date(System.currentTimeMillis() + expiredMs))
               .signWith(secretKey)
               .compact();
  }
}

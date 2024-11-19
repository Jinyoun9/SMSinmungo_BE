package com.smsinmungo.controller;

import com.smsinmungo.config.security.jwt.JWTUtil;
import com.smsinmungo.config.security.jwt.RefreshEntity;
import com.smsinmungo.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;

  @PostMapping("/reissue")
  public ResponseEntity<?> issue(HttpServletRequest request, HttpServletResponse response) {
    //refresh token, request 에서 받아옴
    String refresh = null;

    //cookie를 순회를 돌아 refresh 토큰을 찾아와 저장
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refresh")) {
        refresh = cookie.getValue();
      }
    }

    //refresh "쿠키"가 없으면
    if (refresh == null) {
      //response status code
      return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
    }

    //expired check, refresh 쿠키가 만료 되었는가?
    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {

      //response status code
      return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
    }

    //"토큰"이 refresh인지 확인 (발급시 페이로드에 명시)
    String category = jwtUtil.getCategory(refresh);
    if (!category.equals("refresh")) {
      //response status code
      return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
    }

    //DB에 refresh 토큰이 저장되어 있는지 확인
    Boolean isExist = refreshRepository.existsByRefresh(refresh);
    if (!isExist) {

      //response body
      return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
    }

    //토큰에서 email role을 뽑아서 새로운 access 토큰 생성
    String email = jwtUtil.getEmail(refresh);
    String role = jwtUtil.getRole(refresh);
    String department = jwtUtil.getDepartment(refresh);
    String major = jwtUtil.getmajor(refresh);

    //make new JWT
    String newAccess = jwtUtil.createJwt("access", email, department, major,  role, 600000L);
    String newRefresh = jwtUtil.createJwt("refresh", email, department, major, role, 86400000L);

    //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
    refreshRepository.deleteByRefresh(refresh);
    addRefreshEntity(email, newRefresh, 86400000L);

    //response
    response.setHeader("access", newAccess);
    response.addCookie(createCookie("refresh", newRefresh));

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24*60*60);
    cookie.setHttpOnly(true);

    return cookie;
  }

  private void addRefreshEntity(String email, String refresh, Long expiredMs) {

    Date date = new Date(System.currentTimeMillis() + expiredMs);

    RefreshEntity refreshEntity = new RefreshEntity(email, refresh, date.toString());
    refreshRepository.save(refreshEntity);
  }
}

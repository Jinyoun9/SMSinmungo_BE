package com.smsinmungo.controller;

import com.smsinmungo.config.security.custom.CustomUserDetailsService;
import com.smsinmungo.config.security.jwt.JWTUtil;
import com.smsinmungo.domain.Member;
import com.smsinmungo.dto.LogInDto;
import com.smsinmungo.dto.TokenResponseDto;
import com.smsinmungo.repository.MemberRepository;
import com.smsinmungo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LogInDto loginRequest, HttpServletResponse response) {
//        Authentication authentication = authService.authenticate(loginRequest);
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        TokenResponseDto tokenResponse = authService.generateToken(userDetails);

        Member member = memberRepository.findByEmail(loginRequest.getEmail());
        String accessToken = jwtUtil.createJwt("access",
                member.getEmail(),
                member.getDepartment(),
                member.getMajor(),
                member.getRole(),
                1000000L);
        HttpHeaders headers = authService.setResponseHeaderWithToken(accessToken);
        //response.addCookie(tokenResponse.getRefreshCookie());

        return ResponseEntity.ok()
                .headers(headers)
                .body(accessToken);
    }
}

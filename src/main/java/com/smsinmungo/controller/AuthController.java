package com.smsinmungo.controller;

import com.smsinmungo.config.security.custom.CustomUserDetailsService;
import com.smsinmungo.config.security.jwt.JWTUtil;
import com.smsinmungo.domain.Member;
import com.smsinmungo.dto.LogInDto;
import com.smsinmungo.repository.MemberRepository;
import com.smsinmungo.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok().build();
    }
}

package com.smsinmungo.service;

import com.smsinmungo.config.security.jwt.JWTUtil;
import com.smsinmungo.domain.Member;
import com.smsinmungo.dto.CommentDto;
import com.smsinmungo.exception.CommentNotFoundException;
import com.smsinmungo.model.Comment;

import com.smsinmungo.repository.CommentRepository;


import com.smsinmungo.repository.MemberRepository;
import com.smsinmungo.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    private final CommentRepository commentRepository;
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository,  MemberRepository memberRepository, PostRepository postRepository,
                          JWTUtil jwtUtil) {
        this.commentRepository = commentRepository;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public Comment saveComment(CommentDto commentDto, String token) {
        Comment comment; //Comment 객체 선언
        String email = jwtUtil.getEmail(token);
        Member member = memberRepository.findByEmail(email);
        LocalDateTime localDateTime = LocalDateTime.now();

        comment = Comment.builder() //comment 는 role 구분X, comment가 달린 post 정보 추가
                .member(member)
                .content(commentDto.getContent())
                .write_date(localDateTime)
                .good(0)
                .bad(0)
                .post(commentDto.getPost())
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public void likeComment(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("토큰이 비어있습니다.");
        }

        String email = jwtUtil.getEmail(token);
        Member member = memberRepository.findByEmail(email);

        Comment comment = commentRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("이 아이디의 comment 가 존재하지 않습니다"));

        comment.like();
        commentRepository.save(comment); //like++ 후 저장
    }

    @Transactional
    public void dislikeComment(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("토큰이 비어있습니다.");
        }

        String email = jwtUtil.getEmail(token);
        Member member = memberRepository.findByEmail(email);

        Comment comment = commentRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("이 아이디의 comment 가 존재하지 않습니다"));

        comment.dislike();
        commentRepository.save(comment); //dislike++ 후 저장
    }

    @Transactional
    public void deleteComment(String token) { //comment 삭제
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("토큰이 비어있습니다.");
        }

        String email = jwtUtil.getEmail(token);
        Member member = memberRepository.findByEmail(email);

        Comment comment = commentRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 comment 조회 실패"));
        commentRepository.delete(comment);
    }
}

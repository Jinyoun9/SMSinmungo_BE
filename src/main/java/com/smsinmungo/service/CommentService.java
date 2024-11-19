package com.smsinmungo.service;

import com.smsinmungo.config.security.jwt.JWTUtil;
import com.smsinmungo.dto.CommentDto;
import com.smsinmungo.exception.CommentNotFoundException;
import com.smsinmungo.model.Comment;

import com.smsinmungo.repository.CommentRepository;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    private final CommentRepository commentRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public CommentService(CommentRepository commentRepository, JWTUtil jwtUtil) {
        this.commentRepository = commentRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public Comment saveComment(CommentDto commentDto, String token) {
        Comment comment; //Comment 객체 선언
        String role = jwtUtil.getRole(token); //token 에서 role 추출

        if (role.equals("ADMIN")) { //회원이 admin 이면 구분
            comment = Comment.builder()
                    .member(commentDto.getMember()) 
                    .content(commentDto.getContent())
                    .write_date(LocalDateTime.now())
                    .good(0)
                    .bad(0)
                    .build();

        } else { //회원이 student
            comment = Comment.builder()
                    .member(commentDto.getMember()) 
                    .content(commentDto.getContent())
                    .write_date(LocalDateTime.now())
                    .good(0)
                    .bad(0)
                    .build();
        }

        return commentRepository.save(comment);
    }

    @Transactional
    public void likeComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(()-> new CommentNotFoundException("이 아이디의 comment 가 존재하지 않습니다: " + id));
        comment.like();
        commentRepository.save(comment); //like++ 후 저장
    }

    @Transactional
    public void dislikeComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("이 아이디의 comment 가 존재하지 않습니다: " + id));
        comment.dislike();
        commentRepository.save(comment); //dislike++ 후 저장
    }

    @Transactional
    public void deleteComment(Long id) { //comment 삭제
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 comment 조회 실패"));
        commentRepository.delete(comment);
    }
}

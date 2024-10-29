package inyro.demo.service;

import inyro.demo.dto.CommentDto;
import inyro.demo.exception.CommentNotFoundException;
import inyro.demo.model.Comment;
import inyro.demo.repository.CommentRepository;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment saveComment(CommentDto commentDto) {
        Comment comment; //Comment 객체 선언

        if (commentDto.getMember().getRole().equals("staff")) { //회원이 staff 이면 구분
            comment = Comment.builder()
                    .member(commentDto.getMember()) //staff
                    .content(commentDto.getContent())
                    .write_date(LocalDateTime.now())
                    .good(0)
                    .bad(0)
                    .build();

        } else { //회원이 student
            comment = Comment.builder()
                    .member(commentDto.getMember()) //student
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

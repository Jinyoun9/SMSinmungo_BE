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

        if (commentDto.getStaff() != null) { //회원이 staff 이면 구분?
            comment = Comment.builder()
                    .staff(commentDto.getStaff()) //staff
                    .content(commentDto.getContent())
                    .write_date(LocalDateTime.now())
                    .good(0)
                    .bad(0)
                    .build();

        } else { //회원이 student
            comment = Comment.builder()
                    .student(commentDto.getStudent()) //student
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
        commentRepository.deleteById(id); //기존에 repository 에 존재하던 comment 객체를 제거후 저장 OR 그냥 저장??
        commentRepository.save(comment);
    }

    @Transactional
    public void dislikeComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("이 아이디의 comment 가 존재하지 않습니다: " + id));
        comment.dislike();
        commentRepository.deleteById(id); //기존에 repository 에 존재하던 comment 객체를 제거후 저장 OR 그냥 저장??
        commentRepository.save(comment);
    }
}

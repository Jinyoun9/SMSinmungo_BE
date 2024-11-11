package inyro.demo.controller;

import inyro.demo.dto.CommentDto;
import inyro.demo.exception.CommentNotFoundException;
import inyro.demo.model.Comment;
import inyro.demo.service.CommentService;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/write/{token}")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody CommentDto commentDto, BindingResult bindingResult, String token) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            Comment comment = commentService.saveComment(commentDto, token);
            return ResponseEntity.ok(comment);
        }
    }

    @PatchMapping("/like/{id}")
    public ResponseEntity<Void> likeComment(@PathVariable("id") Long id) {
        try {
            commentService.likeComment(id);
            return ResponseEntity.ok().build();
        } catch (CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/dislike/{id}")
    public ResponseEntity<Void> dislikeComment(@PathVariable("id") Long id) {
        try {
            commentService.dislikeComment(id);
            return ResponseEntity.ok().build();
        } catch (CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable("id") Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

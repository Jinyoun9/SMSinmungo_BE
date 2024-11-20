package com.smsinmungo.controller;

import com.smsinmungo.docs.UnivPostDocs;
import com.smsinmungo.dto.UnivPostDto;
import com.smsinmungo.model.UnivPost;
import com.smsinmungo.service.UnivPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/univ")
public class UnivPostController implements UnivPostDocs {

    private final UnivPostService univPostService;

    @Autowired
    public UnivPostController(UnivPostService univPostService) {
        this.univPostService = univPostService;
    }

    @PostMapping("/post/{token}")
    public ResponseEntity<UnivPost> createPost(@RequestBody UnivPostDto univPostDto, BindingResult bindingResult, String token) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            univPostService.savePost(univPostDto, token);
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UnivPost> deletePost(@PathVariable("id") Long id) {
        try {
            univPostService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PatchMapping("/like/{id}")
    public ResponseEntity<Void> likePost(@PathVariable("id") Long id) {
        try {
            univPostService.likePost(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping("/test")
    public void test(@RequestHeader("Authorization") String token, @RequestBody UnivPostDto univPostDto){
        univPostService.postTest(univPostDto, token);
    }
    @GetMapping("/test")
    public List<UnivPost> test(@RequestHeader("Authorization") String token){
        return univPostService.getTest(token);
    }

}

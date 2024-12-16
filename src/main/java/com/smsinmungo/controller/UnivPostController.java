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
    public ResponseEntity<UnivPost> createPost(@RequestHeader("Authorization") String token, @RequestBody UnivPostDto univPostDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            univPostService.savePost(univPostDto, token);
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UnivPost> deletePost(@RequestHeader("Authorization") String token) {
        try {
            univPostService.deletePost(token);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PatchMapping("/like/{token}")
    public ResponseEntity<Void> likePost(@RequestHeader("Authorization") String token) {
        try {
            univPostService.likePost(token);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    @GetMapping("/postList/{token}")
    public List<UnivPost> getPosts(@RequestHeader("Authorization") String token) {
        return univPostService.getUnivPosts(token);
    }

    /*
    @PostMapping("/test")
    public void test(@RequestHeader("Authorization") String token, @RequestBody UnivPostDto univPostDto){
        univPostService.postTest(univPostDto, token);
    }
    @GetMapping("/test")
    public List<UnivPost> test(@RequestHeader("Authorization") String token){
        return univPostService.getTest(token);
    }*/
}

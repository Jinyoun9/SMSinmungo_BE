package inyro.demo.controller;

import inyro.demo.dto.UnivPostDto;
import inyro.demo.model.UnivPost;
import inyro.demo.service.UnivPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnivPostController {

    private final UnivPostService univPostService;

    @Autowired
    public UnivPostController(UnivPostService univPostService) {
        this.univPostService = univPostService;
    }

    @RequestMapping("/univ/post/{token}")
    public ResponseEntity<UnivPost> createPost(@RequestBody UnivPostDto univPostDto, BindingResult bindingResult, String token) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            univPostService.savePost(univPostDto, token);
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping("/univ/delete/{id}")
    public ResponseEntity<UnivPost> deletePost(@PathVariable("id") Long id) {
        try {
            univPostService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @RequestMapping("/univ/like/{id}")
    public ResponseEntity<Void> likePost(@PathVariable("id") Long id) {
        try {
            univPostService.likePost(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}

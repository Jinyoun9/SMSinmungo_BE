/*

package inyro.spring.controller;

import inyro.spring.Service.OpinionService;
import inyro.spring.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OpinionController {
    private final OpinionService opinionService;

    @GetMapping("/opinions")
    public List<OpinionResponseDto> getOpinion() {
        return opinionService.getPosts();  // 메서드 이름 수정
    }

    @PostMapping("/opinions/post")
    public OpinionResponseDto createOpinion(@RequestBody OpinionRequestsDto requestsDto) {
        return opinionService.createPost(requestsDto);  // 메서드 이름 수정
    }

    @PutMapping("/opinions/modify/{id}")
    public OpinionResponseDto updateOpinion(@PathVariable Long id, @RequestBody OpinionRequestsDto requestsDto) throws Exception {
        return opinionService.updatePost(id, requestsDto);  // 메서드 이름 수정
    }

    @DeleteMapping("/opinions/delete/{id}")
    public SuccessResponseDto deleteOpinion(@PathVariable Long id, @RequestBody OpinionRequestsDto requestsDto) throws Exception {
        return opinionService.deletePost(id, requestsDto);  // 메서드 이름 수정
    }
}
*/
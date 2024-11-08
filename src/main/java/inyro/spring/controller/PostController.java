package inyro.spring.controller;

import inyro.spring.Service.PostService;
import inyro.spring.dto.*;
import inyro.spring.enums.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts") // 통합 API 경로
public class PostController {
    private final PostService postService;

    // 민원 목록 조회
    @GetMapping("/complaints")
    public List<ComplaintResponseDto> getComplaints() {
        return postService.getComplaints();
    }

    // 의견 목록 조회
    @GetMapping("/opinions")
    public List<OpinionResponseDto> getOpinions() {
        return postService.getOpinions();
    }

    // 민원 작성
    @PostMapping("/complaints")
    public ComplaintResponseDto createComplaint(@RequestBody ComplaintRequestsDto requestsDto) {
        return postService.createComplaint(requestsDto);
    }

    // 의견 작성
    @PostMapping("/opinions")
    public OpinionResponseDto createOpinion(@RequestBody OpinionRequestsDto requestsDto) {
        return postService.createOpinion(requestsDto);
    }

    // 민원 조회
    @GetMapping("/complaints/{id}")
    public ComplaintResponseDto getComplaint(@PathVariable Long id) {
        return postService.getComplaint(id);
    }

    // 부서별 민원 글 조회
    @GetMapping("/complaints/department/{department}")
    public List<ComplaintResponseDto> getPostsByDepartment(@PathVariable Department department) {
        return postService.getPostsByDepartment(department);
    }

    // 의견 조회
    @GetMapping("/opinions/{id}")
    public OpinionResponseDto getOpinion(@PathVariable Long id) {
        return postService.getOpinion(id);
    }

    // 민원 수정
    @PutMapping("/complaints/{id}")
    public ComplaintResponseDto updateComplaint(@PathVariable Long id, @RequestBody ComplaintRequestsDto requestsDto) throws Exception {
        return postService.updateComplaint(id, requestsDto);
    }
/*
    // 의견 수정
    @PutMapping("/opinions/{id}")
    public OpinionResponseDto updateOpinion(@PathVariable Long id, @RequestBody OpinionRequestsDto requestsDto) throws Exception {
        return postService.updateOpinion(id, requestsDto);
    }
*/
    // 민원 삭제
    @DeleteMapping("/complaints/{id}")
    public SuccessResponseDto deleteComplaint(@PathVariable Long id, @RequestBody ComplaintRequestsDto requestsDto) throws Exception {
        return postService.deleteComplaint(id, requestsDto);
    }

    // 의견 삭제
    @DeleteMapping("/opinions/{id}")
    public SuccessResponseDto deleteOpinion(@PathVariable Long id, @RequestBody OpinionRequestsDto requestsDto) throws Exception {
        return postService.deleteOpinion(id, requestsDto);
    }
}

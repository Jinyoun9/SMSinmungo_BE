package inyro.spring.controller;

import inyro.spring.Service.PostService;
import inyro.spring.dto.*;
import inyro.spring.enums.Department;
import io.swagger.v3.oas.annotations.Operation;
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
    public CursorResult<ComplaintResponseDto> getComplaints(
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "6") int size
    ) {
        return postService.getComplaintsWithCursor(cursor, size);
    }

    // 의견 목록 조회 
    @GetMapping("/opinions")
    public CursorResult<OpinionResponseDto> getOpinions(
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "6") int size
    ){
        return postService.getOpinionsWithCursor(cursor,size);
    }

    // 민원 작성 - STUDENT만 가능 
    @Operation(
        summary = "민원 작성",
        description = "학생(STUDENT) 권한을 가진 사용자만 민원을 작성할 수 있습니다."
    )
    @PostMapping("/complaints")
    public ComplaintResponseDto createComplaint(
        @RequestBody ComplaintRequestsDto requestsDto,
        @RequestHeader("access") String token 
    ) {
        return postService.createComplaint(requestsDto, token);
    }

    // 의견 작성 - STUDENT만 가능 
    @Operation(
        summary = "의견 작성",
        description = "학생(STUDENT) 권한을 가진 사용자만 의견을 작성할 수 있습니다."
    )
    @PostMapping("/opinions")
    public OpinionResponseDto createOpinion(
        @RequestBody OpinionRequestsDto requestsDto,
        @RequestHeader("access") String token
    ) {
        return postService.createOpinion(requestsDto, token);
    }


    // 선택 민원 조회 (조회수 증가)
    @GetMapping("/complaints/{id}")
    public ComplaintResponseDto getComplaint(@PathVariable Long id,@RequestParam(required = false) String userId) {
        String viewerId = (userId != null) ? userId : "anonymous_" + System.currentTimeMillis();
        return postService.getComplaint(id, viewerId);
    }

    // 선택 의견 조회 (조회수 증가)
    @GetMapping("/opinions/{id}")
    public OpinionResponseDto getOpinion(@PathVariable Long id, @RequestParam(required = false) String userId) {
        String viewerId = (userId != null) ? userId : "anonymous_" + System.currentTimeMillis();
        return postService.getOpinion(id, viewerId);
    }

    // 부서별 민원 글 조회
    @GetMapping("/complaints/department/{department}")
    public List<ComplaintResponseDto> getPostsByDepartment(@PathVariable String department) {
        Department dept = Department.valueOf(department.toUpperCase());
        return postService.getPostsByDepartment(dept);
    }


    // 민원 수정
    @PutMapping("/complaints/{id}")
    public ComplaintResponseDto updateComplaint(@PathVariable Long id, @RequestBody ComplaintRequestsDto requestsDto) throws Exception {
        return postService.updateComplaint(id, requestsDto);
    }

    // 좋아요 추가
    @PostMapping("/{id}/like")
    public LikeResponseDto addLike(@PathVariable Long id, @RequestParam String userId) {
        return postService.addLike(id, userId);
    }

    // 좋아요 취소
    @DeleteMapping("/{id}/like")
    public LikeResponseDto removeLike(@PathVariable Long id, @RequestParam String userId) {
        return postService.removeLike(id, userId);
    }

}

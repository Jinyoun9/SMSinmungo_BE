package inyro.spring.controller;

import inyro.spring.Service.PostService;
import inyro.spring.docs.PostDocs;
import inyro.spring.dto.*;
import inyro.spring.enums.Department;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts") // 통합 API 경로
@Tag(name = "Post", description = "게시물(민원/의견) 관련 API")
public class PostController implements PostDocs {
    private final PostService postService;

    // 민원 목록 조회
    @Operation(summary = "민원 목록 조회", description = "커서 기반 페이지네이션으로 민원 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "민원 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/complaints")
    public CursorResult<ComplaintResponseDto> getComplaints(
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "6") int size
    ) {
        return postService.getComplaintsWithCursor(cursor, size);
    }

    // 의견 목록 조회 
    @Operation(summary = "의견 목록 조회", description = "커서 기반 페이지네이션으로 의견 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "의견 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/opinions")
    public CursorResult<OpinionResponseDto> getOpinions(
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "6") int size
    ){
        return postService.getOpinionsWithCursor(cursor,size);
    }

    // 민원 작성 - STUDENT만 가능 
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "민원 작성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "403", description = "권한 없음")
    })
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
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "민원 작성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "403", description = "권한 없음")
    })
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
    @Operation(summary = "민원 상세 조회", description = "특정 민원의 상세 정보를 조회합니다. 조회 시 조회수가 증가합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "민원 조회 성공"),
        @ApiResponse(responseCode = "404", description = "민원을 찾을 수 없음")
    })
    @GetMapping("/complaints/{id}")
    public ComplaintResponseDto getComplaint(@PathVariable Long id,@RequestParam(required = false) String userId) {
        String viewerId = (userId != null) ? userId : "anonymous_" + System.currentTimeMillis();
        return postService.getComplaint(id, viewerId);
    }

    // 선택 의견 조회 (조회수 증가)
    @Operation(summary = "의견 상세 조회", description = "특정 의견의 상세 정보를 조회합니다. 조회 시 조회수가 증가합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "의견 조회 성공"),
        @ApiResponse(responseCode = "404", description = "의견을 찾을 수 없음")
    })
    @GetMapping("/opinions/{id}")
    public OpinionResponseDto getOpinion(@PathVariable Long id, @RequestParam(required = false) String userId) {
        String viewerId = (userId != null) ? userId : "anonymous_" + System.currentTimeMillis();
        return postService.getOpinion(id, viewerId);
    }

    // 부서별 민원 글 조회
    @Operation(summary = "부서별 민원 조회", description = "특정 부서에 해당하는 민원 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "부서별 민원 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 부서 정보")
    })
    @GetMapping("/complaints/department/{department}")
    public List<ComplaintResponseDto> getPostsByDepartment(@PathVariable String department) {
        Department dept = Department.valueOf(department.toUpperCase());
        return postService.getPostsByDepartment(dept);
    }

    // 좋아요 추가
    @Operation(summary = "좋아요 추가", description = "게시물에 좋아요를 추가합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "좋아요 추가 성공"),
        @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음")
    })
    @PostMapping("/{id}/like")
    public LikeResponseDto addLike(@PathVariable Long id, @RequestParam String userId) {
        return postService.addLike(id, userId);
    }

    // 좋아요 취소
    @Operation(summary = "좋아요 취소", description = "게시물의 좋아요를 취소합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
        @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음")
    })
    @DeleteMapping("/{id}/like")
    public LikeResponseDto removeLike(@PathVariable Long id, @RequestParam String userId) {
        return postService.removeLike(id, userId);
    }

}

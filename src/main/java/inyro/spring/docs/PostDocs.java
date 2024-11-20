package inyro.spring.docs;

import inyro.spring.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Post", description = "게시물(민원/의견) 관련 API")
public interface PostDocs {

    @Operation(summary = "민원 목록 조회", description = "커서 기반 페이지네이션으로 민원 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "민원 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    CursorResult<ComplaintResponseDto> getComplaints(
        @Parameter(description = "마지막으로 받은 게시물의 ID") Long cursor,
        @Parameter(description = "한 페이지당 게시물 수 (기본값: 6)") int size
    );

    @Operation(summary = "의견 목록 조회", description = "커서 기반 페이지네이션으로 의견 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "의견 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    CursorResult<OpinionResponseDto> getOpinions(Long cursor, int size);

    @Operation(summary = "민원 작성", description = "학생(STUDENT) 권한을 가진 사용자만 민원을 작성할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "민원 작성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    ComplaintResponseDto createComplaint(
        @Parameter(description = "민원 작성 정보") ComplaintRequestsDto requestsDto,
        @Parameter(description = "인증 토큰") String token
    );

    @Operation(summary = "의견 작성", description = "학생(STUDENT) 권한을 가진 사용자만 의견을 작성할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "의견 작성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    OpinionResponseDto createOpinion(OpinionRequestsDto requestsDto, String token);

    @Operation(summary = "민원 상세 조회", description = "특정 민원의 상세 정보를 조회합니다. 조회 시 조회수가 증가합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "민원 조회 성공"),
        @ApiResponse(responseCode = "404", description = "민원을 찾을 수 없음")
    })
    ComplaintResponseDto getComplaint(
        @Parameter(description = "민원 ID") Long id,
        @Parameter(description = "조회자 ID (없을 경우 익명으로 처리)") String userId
    );

    @Operation(summary = "의견 상세 조회", description = "특정 의견의 상세 정보를 조회합니다. 조회 시 조회수가 증가합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "의견 조회 성공"),
        @ApiResponse(responseCode = "404", description = "의견을 찾을 수 없음")
    })
    OpinionResponseDto getOpinion(Long id, String userId);

    @Operation(summary = "부서별 민원 조회", description = "특정 부서에 해당하는 민원 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "부서별 민원 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 부서 정보")
    })
    List<ComplaintResponseDto> getPostsByDepartment(
        @Parameter(description = "부서명 (시설, 행정, 보건, 교육)") String department
    );

    @Operation(summary = "민원 수정", description = "기존 민원의 내용을 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "민원 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "민원을 찾을 수 없음")
    })
    ComplaintResponseDto updateComplaint(Long id, ComplaintRequestsDto requestsDto) throws Exception;

    @Operation(summary = "좋아요 추가", description = "게시물에 좋아요를 추가합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "좋아요 추가 성공"),
        @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음")
    })
    LikeResponseDto addLike(Long id, String userId);

    @Operation(summary = "좋아요 취소", description = "게시물의 좋아요를 취소합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
        @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음")
    })
    LikeResponseDto removeLike(Long id, String userId);
}
package com.smsinmungo.docs;

import com.smsinmungo.dto.UnivPostDto;
import com.smsinmungo.model.UnivPost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "UnivPost", description = "정책 전 의견수렴 관련 API 입니다.")
public interface UnivPostDocs {
    @Operation(summary = "좋아요", description = "해당 게시물 작성자의 id를 매개변수로 받아 해당 게시물의 좋아요 +1.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "게시물 좋아요 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "게시물 좋아요 실패") })
    public ResponseEntity<Void> likePost(@PathVariable("id") Long id);

    @Operation(summary = "게시물생성", description = "token을 string으로 받아 role을 추출하여 ADMIN 일때만 게시물 생성.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "게시물 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "게시물 생성 실패(invalid role)") })
    public ResponseEntity<UnivPost> createPost(@RequestHeader("Authorization") String token, @RequestBody UnivPostDto univPostDto,
                                               BindingResult bindingResult);

    @Operation(summary = "삭제", description = "작성자의 id를 매개변수로 받아 해당 게시물을 삭제.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "게시물 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "게시물 삭제 실패") })
    public ResponseEntity<UnivPost> deletePost(@PathVariable("id") Long id);

}

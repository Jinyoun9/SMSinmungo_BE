package com.smsinmungo.docs;


import com.smsinmungo.model.Comment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


// 보통은 컨트롤러 메서드에 @Operation 을 열거하는데 가독성이 떨어진다고 생각해서 Docs 인터페이스 하나 만들고, 사용할 컨트롤러에서 implements 하는 식으로 사용합니다.
@Tag(name = "Comment", description = "댓글 관련 API입니다.")
public interface CommentDocs {
    @Operation(summary = "좋아요", description = "댓글의 id를 매개변수로 받아 해당 댓글의 좋아요 +1.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "유저 정보 저장 실패(유저 중복)") })
    public ResponseEntity<Void> likeComment(@PathVariable("id") Long id);

    @Operation(summary = "싫어요", description = "댓글의 id를 매개변수로 받아 해당 댓글의 싫어요 +1.")
        @ApiResponses(value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "유저 정보 저장 실패(유저 중복)") })
        public ResponseEntity<Void> dislikeComment(@PathVariable("id") Long id);

    @Operation(summary = "삭제", description = "댓글의 id를 매개변수로 받아 해당 댓글을 삭제.")
        @ApiResponses(value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "유저 정보 저장 실패(유저 중복)") })
        public ResponseEntity<Comment> deleteComment(@PathVariable("id") Long id);

}

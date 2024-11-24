package com.smsinmungo.dto;

import com.smsinmungo.domain.Member;

import com.smsinmungo.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Member member;
    private Post post; //댓글이 달린 게시물 정보 추가
    private String content;
}

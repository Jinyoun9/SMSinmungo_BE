package inyro.demo.dto;

import inyro.demo.model.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Member member;
    private String content;
}

package com.smsinmungo.dto;
import com.smsinmungo.domain.Member;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UnivPostDto {

    private String author;
    private String password;
    private String title;
    private String contents;
    private String category;
    private int good;
    private int view;
    private Member member;
}

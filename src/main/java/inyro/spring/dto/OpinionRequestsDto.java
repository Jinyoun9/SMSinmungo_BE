package inyro.spring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OpinionRequestsDto {
    private String author;
    private String password;
    private String title;
    private String contents;
}

package inyro.spring.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComplaintRequestsDto {
    private String author;
    private String password;
    private String title;
    private String contents;
    private String category;
}

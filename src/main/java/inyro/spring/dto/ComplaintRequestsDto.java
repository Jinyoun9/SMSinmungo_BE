package inyro.spring.dto;

import inyro.spring.enums.Category;
import inyro.spring.enums.ComplaintStatus;
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
    private String department;
    private Category category;
    private ComplaintStatus status;
}

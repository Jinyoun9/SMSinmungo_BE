package inyro.spring.dto;

import inyro.spring.enums.Category;
import inyro.spring.enums.ComplaintStatus;
import inyro.spring.enums.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ComplaintRequestsDto {
    //private String author;
    private String password;
    private String title;
    private String contents;
    private Department department;
    private Category category;
    private ComplaintStatus status;
}

package inyro.spring.dto;

import inyro.spring.enums.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ComplaintRequestsDto {
    private String password;
    private String title;
    private String contents;
    private Department department;
}

package inyro.demo.dto;

import inyro.demo.model.Staff;
import inyro.demo.model.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Staff staff;
    private Student student;
    private String content;
}

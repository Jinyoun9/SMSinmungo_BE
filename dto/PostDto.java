package com.smsinmungo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class PostDto {
    private Long post_id;
    private String title;
    private String content;
    private LocalDateTime write_datetime;
    private int good;
    private String department;
    private String file; //상세 조회때만 필요
    private Long view;
    private String status;
    private String category;

    public PostDto(Long post_id, String title, String content,
                   LocalDateTime write_datetime,
                   int good, String department, String file,
                   Long view, String status, String category){
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.write_datetime = write_datetime;
        this.good = good;
        this.department = department;
        this.file = file;
        this.view = view;
        this.status = status;
        this.category = category;
    }


}

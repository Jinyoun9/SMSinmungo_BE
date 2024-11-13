package com.smsinmungo.dto;

import lombok.Builder;

@Builder
public class AlarmDto {
    private Long alarm_id;
    private Long member_id;
    private String title;

    public AlarmDto(Long alarm_id, Long member_id, String title){
        this.alarm_id = alarm_id;
        this.member_id = member_id;
        this.title = title;
    }
}

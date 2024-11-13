package com.smsinmungo.service;

import com.smsinmungo.dto.AlarmDto;

import java.util.List;

public interface AlarmService {
    List<AlarmDto> getAlarms(String department); //알람 리스트 조회

}

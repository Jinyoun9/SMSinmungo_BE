package com.smsinmungo.service;

import com.smsinmungo.repository.AlarmRepository;
import com.smsinmungo.dto.AlarmDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import com.smsinmungo.model.Alarm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AlarmServiceImpl implements AlarmService{
    private AlarmRepository alarmRepository;


    @Override
    @Transactional
    public List<AlarmDto> getAlarms(String department){
        List<Alarm> alarms = alarmRepository.findAllByDepartment(department); //[요청] 관리자의 department 에 해당하는 알람만 불러오기
        if (alarms == null){
            throw new EntityNotFoundException("There is no alarm");
        }

        return alarms.stream()
                .map(alarm -> AlarmDto.builder()
                        .alarm_id(alarm.getAlarm_id())
                        .title(alarm.getTitle())
                        .build())
                .collect(Collectors.toList());
    }

}

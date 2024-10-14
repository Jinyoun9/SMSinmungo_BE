package service;

import Repository.AlarmRepository;
import jakarta.transaction.Transactional;
import model.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmServiceImpl implements AlarmService{
    private AlarmRepository alarmRepository;

    @Autowired
    public AlarmServiceImpl(AlarmRepository alarmRepository){
        this.alarmRepository = alarmRepository;
    }

    @Override
    @Transactional
    public List<Alarm> getAlarms(){
        return alarmRepository.findAll();
    }

}

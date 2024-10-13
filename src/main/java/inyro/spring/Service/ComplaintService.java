package inyro.spring.Service;

import inyro.spring.dto.ComplaintRequestsDto;
import inyro.spring.dto.ComplaintResponseDto;
import inyro.spring.entity.Complaint;
import inyro.spring.repository.ComplaintRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository ComplaintRepository;

    @Transactional
    public ComplaintResponseDto createPost(ComplaintRequestsDto requestsDto){
        Complaint complaint = new Complaint(requestsDto);
        ComplaintRepository.save(complaint);
        return new ComplaintResponseDto(complaint);
    }
}

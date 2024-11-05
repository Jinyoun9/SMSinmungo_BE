package inyro.spring.Service;

import inyro.spring.dto.ComplaintRequestsDto;
import inyro.spring.dto.ComplaintResponseDto;
import inyro.spring.dto.SuccessResponseDto;
import inyro.spring.entity.Complaint;
import inyro.spring.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository ComplaintRepository;

    @Transactional(readOnly = true)
    public List<ComplaintResponseDto> getPosts() {
        return ComplaintRepository.findAllByOrderByModifiedAtDesc().stream().map(ComplaintResponseDto::new).toList();
    }

    @Transactional
    public ComplaintResponseDto createPost(ComplaintRequestsDto requestsDto){
        Complaint complaint = new Complaint(requestsDto);
        ComplaintRepository.save(complaint);
        return new ComplaintResponseDto(complaint);
    }

    @Transactional
    public ComplaintResponseDto getPost(Long id) {
        return ComplaintRepository.findById(id).map(ComplaintResponseDto::new).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다.")
        );
    }

    @Transactional
    public ComplaintResponseDto updatePost(Long id, ComplaintRequestsDto requestsDto) throws Exception {
        Complaint complaint = ComplaintRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다.")
        );
        if (!requestsDto.getPassword().equals(complaint.getPassword()))
            throw new Exception("비밀번호가 일치하지 않습니다.");

        complaint.update(requestsDto);
        return new ComplaintResponseDto(complaint);
    }

    @Transactional
    public SuccessResponseDto deletePost(Long id, ComplaintRequestsDto requestsDto) throws Exception {
        Complaint complaint = ComplaintRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다.")
        );

        if (!requestsDto.getPassword().equals(complaint.getPassword()))
            throw new Exception("비밀번호가 일치하지 않습니다.");

        ComplaintRepository.deleteById(id);
        return new SuccessResponseDto(true);
    }
}

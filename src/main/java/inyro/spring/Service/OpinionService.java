
/*
package inyro.spring.Service;

import inyro.spring.dto.OpinionRequestsDto;
import inyro.spring.dto.OpinionResponseDto;
import inyro.spring.dto.SuccessResponseDto;
import inyro.spring.entity.Opinion;
import inyro.spring.repository.OpinionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpinionService {
    private final OpinionRepository opinionRepository;

    @Transactional(readOnly = true)
    public List<OpinionResponseDto> getPosts() {
        return opinionRepository.findAllByOrderByCreatedAtDesc().stream().map(OpinionResponseDto::new).toList();
    }

    @Transactional
    public OpinionResponseDto createPost(OpinionRequestsDto requestsDto){
        Opinion opinion = new Opinion(requestsDto);
        opinionRepository.save(opinion);
        return new OpinionResponseDto(opinion);
    }

    @Transactional
    public OpinionResponseDto getPost(Long id) {
        return opinionRepository.findById(id).map(OpinionResponseDto::new).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다.")
        );
    }

    @Transactional
    public OpinionResponseDto updatePost(Long id, OpinionRequestsDto requestsDto) throws Exception {
        Opinion opinion = opinionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다.")
        );
        if (!requestsDto.getPassword().equals(opinion.getPassword()))
            throw new Exception("비밀번호가 일치하지 않습니다.");

        opinion.update(requestsDto);
        return new OpinionResponseDto(opinion);
    }

    @Transactional
    public SuccessResponseDto deletePost(Long id, OpinionRequestsDto requestsDto) throws Exception {
        Opinion opinion = opinionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다.")
        );

        if (!requestsDto.getPassword().equals(opinion.getPassword()))
            throw new Exception("비밀번호가 일치하지 않습니다.");

        opinionRepository.deleteById(id);
        return new SuccessResponseDto(true);
    }
}
*/
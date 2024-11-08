package inyro.spring.Service;

import inyro.spring.dto.*;
import inyro.spring.entity.Post;
import inyro.spring.enums.Category;
import inyro.spring.enums.Department;
import inyro.spring.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // 민원 목록 조회
    @Transactional(readOnly = true)
    public List<ComplaintResponseDto> getComplaints() {
        return postRepository.findByCategoryOrderByModifiedAtDesc(Category.민원)
                .stream().map(ComplaintResponseDto::new).toList();
    }

    // 의견 목록 조회
    @Transactional(readOnly = true)
    public List<OpinionResponseDto> getOpinions() {
        return postRepository.findByCategoryOrderByCreatedAtDesc(Category.의견)
                .stream().map(OpinionResponseDto::new).toList();
    }

    // 민원 작성
    @Transactional
    public ComplaintResponseDto createComplaint(ComplaintRequestsDto requestsDto) {
        Post post = new Post(requestsDto);
        postRepository.save(post);
        return new ComplaintResponseDto(post);
    }

    // 의견 작성
    @Transactional
    public OpinionResponseDto createOpinion(OpinionRequestsDto requestsDto) {
        Post post = new Post(requestsDto);
        postRepository.save(post);
        return new OpinionResponseDto(post);
    }

    // 민원 조회
    @Transactional(readOnly = true)
    public ComplaintResponseDto getComplaint(Long id) {
        Post post = postRepository.findByIdAndCategory(id,Category.민원)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디이거나 민원이 아닙니다."));
        return new ComplaintResponseDto(post);
    }

    // 의견 조회
    @Transactional(readOnly = true)
    public OpinionResponseDto getOpinion(Long id) {
        Post post = postRepository.findByIdAndCategory(id, Category.의견)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디이거나 의견이 아닙니다."));
        return new OpinionResponseDto(post);
    }

    // 민원 수정
    @Transactional
    public ComplaintResponseDto updateComplaint(Long id, ComplaintRequestsDto requestsDto) throws Exception {
        Post post = getPostByIdAndCategory(id, Category.민원);
        verifyPassword(post, requestsDto.getPassword());
        post.update(requestsDto);
        return new ComplaintResponseDto(post);
    }
/*
    // 의견 수정
    @Transactional
    public OpinionResponseDto updateOpinion(Long id, OpinionRequestsDto requestsDto) throws Exception {
        Post post = getPostByIdAndCategory(id, Category.의견);
        verifyPassword(post, requestsDto.getPassword());
        post.update(requestsDto);
        return new OpinionResponseDto(post);
    }
*/
    // 민원 삭제
    @Transactional
    public SuccessResponseDto deleteComplaint(Long id, ComplaintRequestsDto requestsDto) throws Exception {
        Post post = getPostByIdAndCategory(id, Category.민원);
        verifyPassword(post, requestsDto.getPassword());
        postRepository.delete(post);
        return new SuccessResponseDto(true);
    }

    // 의견 삭제
    @Transactional
    public SuccessResponseDto deleteOpinion(Long id, OpinionRequestsDto requestsDto) throws Exception {
        Post post = getPostByIdAndCategory(id, Category.의견);
        verifyPassword(post, requestsDto.getPassword());
        postRepository.delete(post);
        return new SuccessResponseDto(true);
    }

    // 카테고리, id 확인
    private Post getPostByIdAndCategory(Long id, Category category) {
        return postRepository.findById(id)
                .filter(post -> post.getCategory() == category)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 " + category + " 아이디입니다."));
    }

    // 비밀번호 검증
    private void verifyPassword(Post post, String password) throws Exception {
        if (!post.getPassword().equals(password)) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }
    }

    //부서별 조회
    @Transactional(readOnly = true)
    public List<ComplaintResponseDto> getPostsByDepartment(Department department) {
        return postRepository.findByDepartment(department)
                .stream().map(ComplaintResponseDto::new).toList();
    }
}

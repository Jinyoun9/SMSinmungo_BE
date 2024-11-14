package com.smsinmungo.service;


import com.smsinmungo.model.JWTUtil;
import com.smsinmungo.model.UnivPost;
import com.smsinmungo.repository.UnivPostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UnivPostService {

    private final UnivPostRepository univPostRepository;
    private final JWTUtil jwtUtil;

    public UnivPostService(UnivPostRepository univPostRepository, JWTUtil jwtUtil) {
        this.univPostRepository = univPostRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void savePost(com.smsinmungo.dto.UnivPostDto univPostDto, String token) {
        UnivPost univPost;

        String role = jwtUtil.getRole(token); //token 에서 role 추출
        if (role.equals("ADMIN")) { // role 이 admin 일때만 post 가능
            univPost = UnivPost.builder()
                    .title(univPostDto.getTitle())
                    .contents(univPostDto.getContents())
                    .author(univPostDto.getAuthor())
                    .good(univPostDto.getGood())
                    .category(univPostDto.getCategory())
                    .view(univPostDto.getView())
                    .build();
            univPostRepository.save(univPost);
        }
    }

    @Transactional
    public void deletePost(Long id) {
        UnivPost univPost = univPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾지 못했습니다"));
        univPostRepository.delete(univPost);
    }

    @Transactional
    public void likePost(Long id) {
        UnivPost univPost = univPostRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("이 아이디의 Post 가 존재하지 않습니다: " + id));
        univPost.like();
        univPostRepository.save(univPost); //like++ 후 저장
    }

}

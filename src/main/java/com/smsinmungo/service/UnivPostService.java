package com.smsinmungo.service;

import com.smsinmungo.config.security.jwt.JWTUtil;
import com.smsinmungo.domain.Member;
import com.smsinmungo.dto.UnivPostDto;
import com.smsinmungo.model.UnivPost;
import com.smsinmungo.repository.MemberRepository;
import com.smsinmungo.repository.UnivPostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class UnivPostService {

    private final UnivPostRepository univPostRepository;
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    public UnivPostService(UnivPostRepository univPostRepository, MemberRepository memberRepository, JWTUtil jwtUtil) {
        this.univPostRepository = univPostRepository;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void savePost(UnivPostDto univPostDto, String token) {
        UnivPost univPost;
        LocalDateTime localDateTime = LocalDateTime.now();

        if (token == null || token.trim().isEmpty()) { //token 이 비어있는지 확인
            throw new IllegalArgumentException("토큰이 비어있습니다.");
        }

        String role = jwtUtil.getRole(token); //token 에서 role 추출
        String email = jwtUtil.getEmail(token);
        Member member = memberRepository.findByEmail(email);

        if (role.equals("ROLE_ADMIN")) { // role 이 admin 일때만 post 가능
            univPost = UnivPost.builder()
                    .title(univPostDto.getTitle())
                    .contents(univPostDto.getContents())
                    .author(univPostDto.getAuthor())
                    .good(univPostDto.getGood())
                    .category(univPostDto.getCategory())
                    .view(univPostDto.getView())
                    .good(0)
                    .member(member)
                    .createdAt(localDateTime)
                    .modifiedAt(localDateTime)
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

    @Transactional
    public List<UnivPost> getUnivPosts(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("토큰이 비어있습니다.");
        }
        String email = jwtUtil.getEmail(token);
        Member member = memberRepository.findByEmail(email);

        List<UnivPost> univPostList = univPostRepository.findAllByMemberId(member.getId());

        return univPostList;
    }


    /*
    public void postTest(UnivPostDto univPostDto, String token) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("토큰이 비어있습니다.");
        }
        String email = jwtUtil.getEmail(token);
        Member member = memberRepository.findByEmail(email);
        System.out.println(jwtUtil.getEmail(token));

        UnivPost univPost = UnivPost.builder()
                .author(univPostDto.getAuthor())
                .title(univPostDto.getTitle())
                .contents(univPostDto.getContents())
                .category(univPostDto.getCategory())
                .member(member)
                .createdAt(localDateTime)
                .modifiedAt(localDateTime)
                .build();

        univPostRepository.save(univPost);

    }
     */

    /*
    public List<UnivPost> getTest(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("토큰이 비어있습니다.");
        }
        String email = jwtUtil.getEmail(token);
        Member member = memberRepository.findByEmail(email);
        System.out.println(jwtUtil.getEmail(token));

        List<UnivPost> univPostList = univPostRepository.findAllByMemberId(member.getId());

        return univPostList;
    }

     */
}

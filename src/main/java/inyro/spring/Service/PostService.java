package inyro.spring.Service;

import inyro.spring.dto.*;
import inyro.spring.entity.Post;
import inyro.spring.enums.*;
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
    public CursorResult<ComplaintResponseDto> getComplaintsWithCursor(Long cursor, int size) {
        Long startCursor = (cursor == null) ? postRepository.findFirstByCategoryOrderByIdDesc(Category.민원).map(Post::getId).orElse(Long.MAX_VALUE) : cursor;

        List<Post> posts = postRepository.findByCategoryWithCursor(Category.민원, startCursor, size+1);

        boolean hasNext = posts.size() > size;
        List<Post> content = hasNext ? posts.subList(0, size) : posts;

        Long nextCursor = content.isEmpty() ? null : content.get(content.size() -1).getId();

        return new CursorResult<>(content.stream().map(ComplaintResponseDto::new).toList(), nextCursor, hasNext);
    }   

    // 의견 목록 조회
    @Transactional(readOnly = true)
    public CursorResult<OpinionResponseDto> getOpinionsWithCursor(Long cursor, int size) {
        Long startCursor = (cursor == null) ? postRepository.findFirstByCategoryOrderByIdDesc(Category.의견).map(Post::getId).orElse(Long.MAX_VALUE) : cursor;
        List<Post> posts = postRepository.findByCategoryWithCursor(Category.의견, startCursor, size+1);

        boolean hasNext = posts.size() > size;
        List<Post> content = hasNext ? posts.subList(0, size) : posts;

        Long nextCursor = content.isEmpty() ? null : content.get(content.size() -1).getId();

        return new CursorResult<>(content.stream().map(OpinionResponseDto::new).toList(), nextCursor, hasNext);
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

    //민원 조회수 증가 
    @Transactional
    public ComplaintResponseDto getComplaint(Long id, String userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        
        post.incrementView(userId); 
        return new ComplaintResponseDto(post);
    }

    //의견 조회수 증가
    @Transactional
    public OpinionResponseDto getOpinion(Long id, String userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        
        post.incrementView(userId);
        return new OpinionResponseDto(post);
    }

    //좋아요 추가
    @Transactional
    public LikeResponseDto addLike(Long id, String userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        
        if (!post.incrementGood(userId)) {
            throw new IllegalStateException("이미 좋아요를 누른 게시물입니다.");
        }
        
        return new LikeResponseDto(post);
    }

    //좋아요 취소 
    @Transactional
    public LikeResponseDto removeLike(Long id, String userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        if (!post.decrementGood(userId)) {
            throw new IllegalStateException("좋아요를 누르지 않은 게시물입니다.");
        }

        return new LikeResponseDto(post);
    }
}

package inyro.spring.Service;

import inyro.spring.dto.*;
import inyro.spring.entity.Post;
import inyro.spring.enums.*;
import inyro.spring.repository.PostRepository;
import inyro.spring.util.TempJwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private static final String ROLE_STUDENT = "ROLE_STUDENT";
    private static final String STUDENT_ONLY_MESSAGE = "학생만 %s을(를) 작성할 수 있습니다.";

    private final PostRepository postRepository;
    private final TempJwtUtil jwtUtil;
    private final EmailService emailService;

    // 임시 관리자 이메일 맵 - Department enum과 일치
    private final Map<String, String> adminEmails = Map.of(
        "시설", "facility.admin@example.com",
        "행정", "admin.admin@example.com",
        "보건", "health.admin@example.com",
        "교육", "education.admin@example.com"
    );


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
    public ComplaintResponseDto createComplaint(ComplaintRequestsDto requestsDto, String token) {
        String role = jwtUtil.getRole(token);
        String author = jwtUtil.getName(token);

         if (!ROLE_STUDENT.equals(role)) {
            throw new IllegalStateException(String.format(STUDENT_ONLY_MESSAGE, "민원"));
        }

        Post post = new Post(requestsDto);
        post.setAuthor(author);
        Post savedPost = postRepository.save(post);

        // NEW 상태이고 부서가 지정된 경우에만 이메일 발송
        if (savedPost.getStatus() == ComplaintStatus.NEW && savedPost.getDepartment() != null) {
            String adminEmail = adminEmails.get(savedPost.getDepartment().toString());
            if (adminEmail != null) {
                sendComplaintEmail(savedPost, adminEmail);
            }
        }

        return new ComplaintResponseDto(savedPost);
    }

    private void sendComplaintEmail(Post post, String adminEmail) {
        String emailSubject = "[새로운 민원] " + post.getTitle();
        String emailContent = String.format(
            "새로운 민원이 등록되었습니다.\n\n" +
            "제목: %s\n" +
            "작성자: %s\n" +
            "부서: %s\n" +
            "내용: %s",
            post.getTitle(),
            post.getAuthor(),
            post.getDepartment(),
            post.getContents()
        );

        try {
            emailService.sendNotification(adminEmail, emailSubject, emailContent);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }


    // 의견 작성
    @Transactional
    public OpinionResponseDto createOpinion(OpinionRequestsDto requestsDto, String token) {
        String role = jwtUtil.getRole(token);
        String author = jwtUtil.getName(token);

        if (!ROLE_STUDENT.equals(role)) {
            throw new IllegalStateException(String.format(STUDENT_ONLY_MESSAGE, "의견"));
        }

        Post post = new Post(requestsDto);
        post.setAuthor(author);

        Post savedPost = postRepository.save(post);
        return new OpinionResponseDto(savedPost);
    }

    // 민원 조회
    @Transactional
    public ComplaintResponseDto getComplaint(Long id, String userId) {
        Post post = postRepository.findByIdAndCategory(id, Category.민원)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디이거나 민원이 아닙니다."));
        
        if (userId != null) {
            post.incrementView(userId);
        }
        return new ComplaintResponseDto(post);
    }

    // 의견 조회
    @Transactional
    public OpinionResponseDto getOpinion(Long id, String userId) {
        Post post = postRepository.findByIdAndCategory(id, Category.의견)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디이거나 의견이 아닙니다."));

        if (userId != null) {
            post.incrementView(userId);
        }
        return new OpinionResponseDto(post);
    }

    //부서별 조회
    @Transactional(readOnly = true)
    public List<ComplaintResponseDto> getPostsByDepartment(Department department) {
        return postRepository.findByDepartment(department)
                .stream().map(ComplaintResponseDto::new).toList();
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

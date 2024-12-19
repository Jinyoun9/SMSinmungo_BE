package inyro.spring.entity;

import inyro.spring.dto.ComplaintRequestsDto;
import inyro.spring.dto.OpinionRequestsDto;
import inyro.spring.enums.Category;
import inyro.spring.enums.ComplaintStatus;
import inyro.spring.enums.Department;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Enumerated(EnumType.STRING)
    private Department department; // 민원에만 필요

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    @ElementCollection
    @CollectionTable(name = "post_views", joinColumns = @JoinColumn(name = "post_id"))
    private Set<ViewInfo> viewedUsers = new HashSet<>();

    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ViewInfo {
        private String userId;
        private LocalDate viewedAt;

        public ViewInfo(String userId) {
            this.userId = userId;
            this.viewedAt = LocalDate.now();
        }
    }

    public void incrementView(String userId) {
        boolean hasViewedToday = viewedUsers.stream()
                .anyMatch(view -> view.getUserId().equals(userId)
                        && view.getViewedAt().equals(LocalDate.now()));

        if (!hasViewedToday) {
            viewedUsers.add(new ViewInfo(userId));
            this.view++;
        }
    }

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int good;

    @ElementCollection
    @CollectionTable(name = "post_likes", joinColumns = @JoinColumn(name = "post_id"))
    private Set<LikeInfo> likedUsers = new HashSet<>();

    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LikeInfo {
        private String userId;
        private LocalDateTime likedAt;
        private boolean isDeleted = false;

        public LikeInfo(String userId) {
            this.userId = userId;
            this.likedAt = LocalDateTime.now();
        }

        public void markAsDeleted() {
            this.isDeleted = true;
        }
    }

    public boolean incrementGood(String userId) {
        if (hasActiveLike(userId)) {
            return false;
        }

        likedUsers.removeIf(like -> like.getUserId().equals(userId));

        likedUsers.add(new LikeInfo(userId));
        this.good++;
        return true;
    }

    public boolean decrementGood(String userId) {
        Optional<LikeInfo> likeInfo = likedUsers.stream()
                .filter(like -> like.getUserId().equals(userId) && !like.isDeleted)
                .findFirst();

        if (likeInfo.isPresent()) {
            likeInfo.get().markAsDeleted();
            if (this.good > 0) {
                this.good--;
            }
            return true;
        }
        return false;
    }

    public boolean hasActiveLike(String userId) {
        return likedUsers.stream()
                .anyMatch(like -> like.getUserId().equals(userId) && !like.isDeleted);
    }

    public int getActiveGoodCount() {
        return (int) likedUsers.stream()
                .filter(like -> !like.isDeleted)
                .count();
    }

    public void markUserLikesAsDeleted(String userId) {
        likedUsers.stream()
                .filter(like -> like.getUserId().equals(userId))
                .forEach(LikeInfo::markAsDeleted);
        recalculateGoodCount();
    }

    private void recalculateGoodCount() {
        this.good = getActiveGoodCount();
    }

    public Post(ComplaintRequestsDto requestsDto) {
        this.title = requestsDto.getTitle();
        this.contents = requestsDto.getContents();
        this.department = requestsDto.getDepartment();
        this.category = Category.민원;
        this.status = ComplaintStatus.NEW; // 기본값 설정
    }

    public Post(OpinionRequestsDto requestsDto) {
        this.title = requestsDto.getTitle();
        this.contents = requestsDto.getContents();
        this.category = Category.의견;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
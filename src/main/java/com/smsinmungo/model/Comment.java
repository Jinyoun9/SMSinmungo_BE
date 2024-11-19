package com.smsinmungo.model;

import com.smsinmungo.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime write_date;
    private int good;
    private int bad;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private Member member;

    public void like() {
        this.good++;
    }

    public void dislike() {
        this.bad++;
    }
}

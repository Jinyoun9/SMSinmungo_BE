package com.smsinmungo.repository;

import com.smsinmungo.model.UnivPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnivPostRepository extends JpaRepository<UnivPost, Long> {
    List<UnivPost> findAllByMemberId(long memberId);
}

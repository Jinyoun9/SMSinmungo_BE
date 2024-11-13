package com.smsinmungo.repository;

import com.smsinmungo.model.UnivPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnivPostRepository extends JpaRepository<UnivPost, Long> {
}

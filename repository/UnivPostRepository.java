package inyro.demo.repository;

import inyro.demo.model.UnivPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnivPostRepository extends JpaRepository<UnivPost, Long> {
}

package inyro.spring.repository;

import inyro.spring.entity.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {
    List<Opinion> findAllByOrderByCreatedAtDesc();
}

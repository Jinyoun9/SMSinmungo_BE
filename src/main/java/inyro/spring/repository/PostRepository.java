package inyro.spring.repository;

import inyro.spring.entity.Post;
import inyro.spring.enums.Category;
import inyro.spring.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryOrderByModifiedAtDesc(Category category);
    List<Post> findByCategoryOrderByCreatedAtDesc(Category category);

    Optional<Post> findByIdAndCategory(Long id, Category category);

    List<Post> findByDepartment(Department department);

    @Query(value = "SELECT * FROM post p WHERE p.category = :#{#category.name()} AND p.id < :cursor ORDER BY p.id DESC LIMIT :size", 
    nativeQuery = true)
    List<Post> findByCategoryWithCursor(
        @Param("category") Category category,
        @Param("cursor") Long cursor,
        @Param("size") int size
    );
    Optional<Post> findFirstByCategoryOrderByIdDesc(Category category);
}


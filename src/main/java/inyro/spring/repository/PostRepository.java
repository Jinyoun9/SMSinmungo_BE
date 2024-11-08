package inyro.spring.repository;

import inyro.spring.entity.Post;
import inyro.spring.enums.Category;
import inyro.spring.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryOrderByModifiedAtDesc(Category category);
    List<Post> findByCategoryOrderByCreatedAtDesc(Category category);

    Optional<Post> findByIdAndCategory(Long id, Category category);

    List<Post> findByDepartment(Department department);
}

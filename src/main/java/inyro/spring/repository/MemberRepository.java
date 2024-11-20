package inyro.spring.repository;

import inyro.spring.entity.TempMember;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<TempMember, Long> {
    List<TempMember> findByRoleAndDepartment(String role, String department);
}

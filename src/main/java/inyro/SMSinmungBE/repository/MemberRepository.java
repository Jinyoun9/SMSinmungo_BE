package inyro.SMSinmungBE.repository;

import inyro.SMSinmungBE.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Boolean existsByEmail(String email);

  Member findByEmail(String email);
}

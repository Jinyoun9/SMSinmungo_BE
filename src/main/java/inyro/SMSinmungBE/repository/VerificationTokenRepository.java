package inyro.SMSinmungBE.repository;

import inyro.SMSinmungBE.domain.VerificationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
  Optional<VerificationToken> findByToken(String token);
}

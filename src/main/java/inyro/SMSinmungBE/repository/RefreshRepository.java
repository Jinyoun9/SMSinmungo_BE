package inyro.SMSinmungBE.repository;

import inyro.SMSinmungBE.config.security.jwt.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

}

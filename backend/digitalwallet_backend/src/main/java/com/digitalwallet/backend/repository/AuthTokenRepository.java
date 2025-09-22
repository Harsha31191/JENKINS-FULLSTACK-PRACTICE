package com.digitalwallet.backend.repository;

import com.digitalwallet.backend.model.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AuthTokenRepository extends JpaRepository<AuthToken, UUID> {
  Optional<AuthToken> findByToken(String token);
  void deleteByToken(String token);
}

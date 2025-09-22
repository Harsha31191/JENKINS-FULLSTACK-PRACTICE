package com.digitalwallet.backend.repository;

import com.digitalwallet.backend.model.Wallet;
import com.digitalwallet.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
  Optional<Wallet> findByUser(User user);
}

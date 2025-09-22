package com.digitalwallet.backend.repository;

import com.digitalwallet.backend.model.Transaction;
import com.digitalwallet.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserOrderByCreatedAtDesc(User user);
}

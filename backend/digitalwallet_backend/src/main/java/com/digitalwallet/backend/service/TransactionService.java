package com.digitalwallet.backend.service;

import com.digitalwallet.backend.model.Transaction;
import com.digitalwallet.backend.model.User;
import com.digitalwallet.backend.model.Wallet;
import com.digitalwallet.backend.repository.TransactionRepository;
import com.digitalwallet.backend.repository.UserRepository;
import com.digitalwallet.backend.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              WalletRepository walletRepository,
                              UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Transaction sendMoney(UUID userId, String recipient, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("Wallet not found"));

        Transaction tx = new Transaction();
        tx.setId(UUID.randomUUID());
        tx.setUser(user);
        tx.setType("SEND");
        tx.setAmount(amount);
        tx.setCurrency(wallet.getCurrency());
        tx.setCreatedAt(Instant.now());
        tx.setMetadata("to:" + recipient);
        tx.setStatus("PENDING");
        transactionRepository.save(tx);

        if (wallet.getBalance().compareTo(amount) < 0) {
            tx.setStatus("FAILED");
            transactionRepository.save(tx);
            throw new IllegalArgumentException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        tx.setStatus("SUCCESS");
        return transactionRepository.save(tx);
    }

    @Transactional
    public Transaction payBill(UUID userId, String biller, String account, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("Wallet not found"));

        Transaction tx = new Transaction();
        tx.setId(UUID.randomUUID());
        tx.setUser(user);
        tx.setType("BILL");
        tx.setAmount(amount);
        tx.setCurrency(wallet.getCurrency());
        tx.setCreatedAt(Instant.now());
        tx.setMetadata("biller:" + biller + ";ref:" + account);
        tx.setStatus("PENDING");
        transactionRepository.save(tx);

        if (wallet.getBalance().compareTo(amount) < 0) {
            tx.setStatus("FAILED");
            transactionRepository.save(tx);
            throw new IllegalArgumentException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        tx.setStatus("SUCCESS");
        return transactionRepository.save(tx);
    }

    @Transactional
    public Transaction payQr(UUID userId, String code, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("Wallet not found"));

        Transaction tx = new Transaction();
        tx.setId(UUID.randomUUID());
        tx.setUser(user);
        tx.setType("QR");
        tx.setAmount(amount);
        tx.setCurrency(wallet.getCurrency());
        tx.setCreatedAt(Instant.now());
        tx.setMetadata("qr:" + code);
        tx.setStatus("PENDING");
        transactionRepository.save(tx);

        if (wallet.getBalance().compareTo(amount) < 0) {
            tx.setStatus("FAILED");
            transactionRepository.save(tx);
            throw new IllegalArgumentException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        tx.setStatus("SUCCESS");
        return transactionRepository.save(tx);
    }

    public List<Transaction> listForUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return transactionRepository.findByUserOrderByCreatedAtDesc(user);
    }
}

package com.digitalwallet.backend.controller;

import com.digitalwallet.backend.model.User;
import com.digitalwallet.backend.model.Wallet;
import com.digitalwallet.backend.repository.UserRepository;
import com.digitalwallet.backend.repository.WalletRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {
  private final UserRepository userRepository;
  private final WalletRepository walletRepository;

  public WalletController(UserRepository userRepository, WalletRepository walletRepository) {
    this.userRepository = userRepository;
    this.walletRepository = walletRepository;
  }

  @GetMapping("/wallet")
  public Map<String, Object> getWallet(Authentication authentication) {
    String userIdStr = authentication.getName();
    UUID userId = UUID.fromString(userIdStr);
    User u = userRepository.findById(userId).orElseThrow();
    Wallet w = walletRepository.findByUser(u).orElseThrow();
    return Map.of(
      "balance", w.getBalance(),
      "currency", w.getCurrency(),
      "accountNumber", w.getId()
    );
  }
}

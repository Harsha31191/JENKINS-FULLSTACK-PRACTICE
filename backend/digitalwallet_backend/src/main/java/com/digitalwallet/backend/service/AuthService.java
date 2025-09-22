package com.digitalwallet.backend.service;

import com.digitalwallet.backend.model.User;
import com.digitalwallet.backend.model.Wallet;
import com.digitalwallet.backend.model.AuthToken;
import com.digitalwallet.backend.repository.UserRepository;
import com.digitalwallet.backend.repository.WalletRepository;
import com.digitalwallet.backend.repository.AuthTokenRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final WalletRepository walletRepository;
  private final AuthTokenRepository tokenRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public AuthService(UserRepository userRepository, WalletRepository walletRepository, AuthTokenRepository tokenRepository) {
    this.userRepository = userRepository;
    this.walletRepository = walletRepository;
    this.tokenRepository = tokenRepository;
  }

  @Transactional
  public void register(String fullName, String email, String password) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Email already exists");
    }
    User u = new User();
    u.setFullName(fullName);
    u.setEmail(email);
    u.setPasswordHash(passwordEncoder.encode(password));
    u.setCreatedAt(Instant.now());
    userRepository.save(u);

    Wallet w = new Wallet();
    w.setUser(u);
    w.setCurrency("INR");
    w.setBalance(BigDecimal.ZERO);
    walletRepository.save(w);
  }

  public String loginAndCreateToken(String email, String password) {
    Optional<User> ou = userRepository.findByEmail(email);
    if (ou.isEmpty()) throw new IllegalArgumentException("Invalid credentials");
    User u = ou.get();
    if (!passwordEncoder.matches(password, u.getPasswordHash())) throw new IllegalArgumentException("Invalid credentials");

    // create token
    String token = UUID.randomUUID().toString();
    AuthToken at = new AuthToken();
    at.setToken(token);
    at.setUser(u);
    at.setCreatedAt(Instant.now());
    tokenRepository.save(at);
    return token;
  }

  public Optional<User> findUserByToken(String token) {
    return tokenRepository.findByToken(token).map(AuthToken::getUser);
  }

  public void revokeToken(String token) {
    tokenRepository.deleteByToken(token);
  }
}

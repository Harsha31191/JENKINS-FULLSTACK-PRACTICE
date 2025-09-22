package com.digitalwallet.backend.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auth_tokens", indexes = {
  @Index(columnList = "token", name = "idx_token", unique = true)
})
public class AuthToken {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false, unique = true, length = 128)
  private String token;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private Instant createdAt = Instant.now();

  public AuthToken() {}

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }

  public String getToken() { return token; }
  public void setToken(String token) { this.token = token; }

  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

package com.digitalwallet.backend.security;

import com.digitalwallet.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {
  private final AuthService authService;

  public SecurityConfig(AuthService authService) {
    this.authService = authService;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
                                         @Value("${cors.allowed-origins:*}") String allowedOrigin) throws Exception {
    TokenAuthenticationFilter tokenFilter = new TokenAuthenticationFilter(authService);

    // --- CORS setup ---
    http.cors(cors -> {
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowedOriginPatterns(List.of("*")); // allow all during dev
      config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
      config.setAllowedHeaders(List.of("*"));
      config.setAllowCredentials(true);
      cors.configurationSource(request -> config);
    });

    // --- CSRF + Session ---
    http.csrf(csrf -> csrf.disable());
    http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // --- Authorization rules ---
    http.authorizeHttpRequests(auth -> auth
      // allow CORS pre-flight
      .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
      // allow auth endpoints
      .requestMatchers("/api/v1/auth/**").permitAll()
      // allow root, docs, health, static resources
      .requestMatchers("/", "/index.html", "/swagger-ui/**",
                       "/v3/api-docs/**", "/actuator/**", "/static/**").permitAll()
      // everything else requires authentication
      .anyRequest().authenticated()
    );

    // --- Token filter ---
    http.addFilterBefore(tokenFilter,
        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder() {
    return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
  }
}

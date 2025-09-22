package com.digitalwallet.backend.security;

import com.digitalwallet.backend.service.AuthService;
import com.digitalwallet.backend.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
  private final AuthService authService;

  public TokenAuthenticationFilter(AuthService authService) {
    this.authService = authService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
    try {
      String header = request.getHeader("Authorization");
      if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
      }
      String token = header.substring(7);
      Optional<User> ou = authService.findUserByToken(token);
      if (ou.isPresent()) {
        User user = ou.get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getId().toString(), null, java.util.Collections.emptyList());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception ex) {
      // ignore and continue unauthenticated
    }
    filterChain.doFilter(request, response);
  }
}

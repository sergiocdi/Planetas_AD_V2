package org.proyecto.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.proyecto.service.AuthService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final AuthService authService;

    public JwtAuthFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        boolean skip = path != null && path.startsWith("/api/auth");
        if (skip) {
            log.debug("Skipping JwtAuthFilter for path: {}", path);
        }
        return skip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            log.debug("Processing JWT token for path: {}", request.getServletPath());
            if (authService.validateToken(token)) {
                Long userId = authService.getUserIdFromToken(token);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, java.util.List.of());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.debug("Invalid JWT token on path: {}", request.getServletPath());
            }
        }
        filterChain.doFilter(request, response);
    }
}
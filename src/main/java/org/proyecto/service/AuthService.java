package org.proyecto.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.proyecto.domain.Usuario;
import org.proyecto.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("uVv1oZQe3lq3Jw9uXlZkY2h0b3Rlc3QxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTA="));

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String login(String nickname, String rawPassword) {
        Optional<Usuario> opt = authenticate(nickname, rawPassword);
        if (opt.isEmpty()) return null;
        return generateToken(opt.get());
    }

    public Optional<Usuario> authenticate(String nickname, String rawPassword) {
        Optional<Usuario> opt = usuarioRepository.findByNickname(nickname);
        if (opt.isEmpty()) return Optional.empty();
        Usuario u = opt.get();
        if (!passwordEncoder.matches(rawPassword, u.getPassword())) return Optional.empty();
        return Optional.of(u);
    }

    public String generateToken(Usuario u) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(u.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(3600)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String encode(String raw) { return passwordEncoder.encode(raw); }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            return Long.valueOf(Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody().getSubject());
        } catch (Exception e) {
            return null;
        }
    }
}
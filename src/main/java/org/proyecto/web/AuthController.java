package org.proyecto.web;

import org.proyecto.service.AuthService;
import org.proyecto.service.PlanetaService;
import org.proyecto.domain.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final PlanetaService planetaService;

    public AuthController(AuthService authService, PlanetaService planetaService) {
        this.authService = authService;
        this.planetaService = planetaService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        log.debug("Login request received for nickname: {}", req.get("nickname"));
        Optional<Usuario> userOpt = authService.authenticate(req.get("nickname"), req.get("password"));
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
        
        Usuario user = userOpt.get();
        String token = authService.generateToken(user);

        return ResponseEntity.ok(Map.of(
            "token", token,
            "id", user.getId(),
            "nickname", user.getNickname(),
            "monedas", user.getMonedas(),
            "planetas", planetaService.listByUsuarioId(user.getId())
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() { return ResponseEntity.ok(Map.of("message", "logged_out")); }
}
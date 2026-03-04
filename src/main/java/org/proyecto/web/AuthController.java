package org.proyecto.web;

import org.proyecto.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login exclusivo para el middleware (usuario admin)
     * Solo el usuario administrador puede obtener un token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String nickname = req.get("nickname");
        String password = req.get("password");
        
        log.debug("Login request received for nickname: {}", nickname);
        
        // Solo permitir login del usuario admin
        String token = authService.loginAdmin(nickname, password);
        if (token == null) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials_or_not_admin"));
        }

        return ResponseEntity.ok(Map.of(
            "token", token,
            "message", "Middleware authenticated successfully"
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() { 
        return ResponseEntity.ok(Map.of("message", "logged_out")); 
    }
}
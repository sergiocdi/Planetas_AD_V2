package org.proyecto.web;

import org.proyecto.domain.Usuario;
import org.proyecto.domain.Planeta;
import org.proyecto.service.UsuarioService;
import org.proyecto.service.PlanetaService;
import org.proyecto.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final PlanetaService planetaService;
    private final AuthService authService;

    public UsuarioController(UsuarioService usuarioService, PlanetaService planetaService, AuthService authService) {
        this.usuarioService = usuarioService;
        this.planetaService = planetaService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Usuario u) {
        Usuario savedUser = usuarioService.create(u);
        String token = authService.generateToken(savedUser);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", savedUser.getId(),
                "nickname", savedUser.getNickname(),
                "monedas", savedUser.getMonedas(),
                "planetas", Collections.emptyList()
        ));
    }

    @GetMapping
    public List<Usuario> list() { return usuarioService.list(); }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> get(@PathVariable Long id) {
        return usuarioService.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Nuevo endpoint: lista de planetas del usuario por id (requiere token)
    @GetMapping("/{id}/planetas")
    public ResponseEntity<List<Planeta>> getPlanetasDelUsuario(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        Long authUserId = (Long) authentication.getPrincipal();
        // Permitir que solo el propio usuario consulte sus planetas (o relajar esta política si no es necesario)
        /*if (!authUserId.equals(id)) {
            return ResponseEntity.status(403).build();
        }*/
        return ResponseEntity.ok(planetaService.listByUsuarioId(id));
    }

    // Comprar un planeta: recibe nombre y tipo del planeta; valida monedas y devuelve lista actualizada o error
    @PostMapping("/{id}/comprar-planeta")
    public ResponseEntity<?> comprarPlaneta(@PathVariable Long id, @RequestBody Map<String, String> req, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        Long authUserId = (Long) authentication.getPrincipal();
        if (!authUserId.equals(id)) {
            return ResponseEntity.status(403).build();
        }
        String nombre = req.get("nombre");
        String tipo = req.get("tipo");
        if (nombre == null || nombre.isBlank() || tipo == null || tipo.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "nombre y tipo son obligatorios"));
        }
        Planeta p = new Planeta();
        p.setNombre(nombre);
        p.setTipo(tipo);
        try {
            planetaService.create(p, id);
            return ResponseEntity.ok(planetaService.listByUsuarioId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public Usuario update(@PathVariable Long id, @RequestBody Usuario u) { return usuarioService.update(id, u); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { usuarioService.delete(id); }
}
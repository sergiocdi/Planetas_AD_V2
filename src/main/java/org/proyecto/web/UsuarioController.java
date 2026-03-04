package org.proyecto.web;

import org.proyecto.domain.Usuario;
import org.proyecto.domain.Planeta;
import org.proyecto.service.UsuarioService;
import org.proyecto.service.PlanetaService;
import org.proyecto.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;
import java.util.Map;

/**
 * Controller para gestión de usuarios.
 * NOTA: Esta versión está diseñada para ser consumida por un middleware.
 * El middleware se autentica con un usuario admin y realiza operaciones
 * en nombre de los usuarios del juego.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final PlanetaService planetaService;

    public UsuarioController(UsuarioService usuarioService, PlanetaService planetaService) {
        this.usuarioService = usuarioService;
        this.planetaService = planetaService;
    }

    /**
     * Crear un nuevo usuario (jugador)
     * El middleware crea usuarios en nombre del sistema de juego
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Usuario u) {
        Usuario savedUser = usuarioService.create(u);
        return ResponseEntity.ok(Map.of(
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

    /**
     * Lista de planetas del usuario por id
     * El middleware consulta los planetas de cualquier usuario
     */
    @GetMapping("/{id}/planetas")
    public ResponseEntity<List<Planeta>> getPlanetasDelUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(planetaService.listByUsuarioId(id));
    }

    /**
     * Comprar un planeta para un usuario específico
     * El middleware indica el usuario que compra el planeta mediante el path {id}
     */
    @PostMapping("/{id}/comprar-planeta")
    public ResponseEntity<?> comprarPlaneta(@PathVariable Long id, @RequestBody Map<String, String> req) {
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
    public Usuario update(@PathVariable Long id, @RequestBody Usuario u) { 
        return usuarioService.update(id, u); 
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { 
        usuarioService.delete(id); 
    }
}
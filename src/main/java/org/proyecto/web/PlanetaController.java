package org.proyecto.web;

import org.proyecto.domain.Planeta;
import org.proyecto.service.PlanetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@RestController
@RequestMapping("/api/planetas")
public class PlanetaController {
    private final PlanetaService planetaService;
    public PlanetaController(PlanetaService planetaService) { this.planetaService = planetaService; }

    @PostMapping
    public ResponseEntity<?> create(Authentication authentication, @RequestBody Planeta p) {
        if (authentication == null || authentication.getPrincipal() == null) return ResponseEntity.status(401).build();
        Long userId = (Long) authentication.getPrincipal();
        try {
            return ResponseEntity.ok(planetaService.create(p, userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<Planeta> list() { return planetaService.list(); }

    @GetMapping("/ranking")
    public List<Map<String, Object>> ranking() {
        return planetaService.ranking().stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("planeta", p.getNombre());
            m.put("usuario", p.getUsuario() != null ? p.getUsuario().getNickname() : null);
            m.put("victorias", p.getNumeroVictorias());
            return m;
        }).collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public Planeta update(@PathVariable Long id, @RequestBody Planeta p) { return planetaService.update(id, p); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { planetaService.delete(id); }
}
package org.proyecto.web;

import org.proyecto.domain.Planeta;
import org.proyecto.service.PlanetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 * Controller para gestión de planetas.
 * NOTA: Esta versión está diseñada para ser consumida por un middleware.
 */
@RestController
@RequestMapping("/api/planetas")
public class PlanetaController {
    private final PlanetaService planetaService;
    
    public PlanetaController(PlanetaService planetaService) { 
        this.planetaService = planetaService; 
    }

    /**
     * Crear un planeta para un usuario específico
     * El middleware debe enviar el usuarioId en el body
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> req) {
        Long usuarioId = null;
        if (req.get("usuarioId") != null) {
            usuarioId = Long.valueOf(req.get("usuarioId").toString());
        }
        if (usuarioId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "usuarioId es obligatorio"));
        }
        
        Planeta p = new Planeta();
        p.setNombre((String) req.get("nombre"));
        p.setTipo((String) req.get("tipo"));
        
        try {
            return ResponseEntity.ok(planetaService.create(p, usuarioId));
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
    public Planeta update(@PathVariable Long id, @RequestBody Planeta p) { 
        return planetaService.update(id, p); 
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { 
        planetaService.delete(id); 
    }
}
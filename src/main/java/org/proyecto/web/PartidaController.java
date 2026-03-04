package org.proyecto.web;

import org.proyecto.domain.Participante;
import org.proyecto.domain.Partida;
import org.proyecto.service.PartidaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller para gestión de partidas.
 * NOTA: Esta versión está diseñada para ser consumida por un middleware.
 */
@RestController
@RequestMapping("/api/partidas")
public class PartidaController {
    private final PartidaService partidaService;
    
    public PartidaController(PartidaService partidaService) { 
        this.partidaService = partidaService; 
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/guardar")
    public ResponseEntity<Partida> guardar(@RequestBody Map<String, Object> req) {
        Map<String, Object> partidaMap = (Map<String, Object>) req.get("partida");
        Partida partida = new Partida();
        if (partidaMap.get("id") != null) partida.setId(Long.valueOf(partidaMap.get("id").toString()));
        partida.setEstado((String) partidaMap.get("estado"));
        partida.setNumeroRonda(Integer.parseInt(partidaMap.get("numeroRonda").toString()));
        
        List<Map<String, Object>> parts = (List<Map<String, Object>>) req.get("participantes");
        List<Participante> participantes = parts.stream().map(m -> {
            Participante p = new Participante();
            if (m.get("id") != null) p.setId(Long.valueOf(m.get("id").toString()));
            Map<String, Object> usuario = (Map<String, Object>) m.get("usuario");
            Map<String, Object> planeta = (Map<String, Object>) m.get("planeta");
            org.proyecto.domain.Usuario u = new org.proyecto.domain.Usuario();
            u.setId(Long.valueOf(usuario.get("id").toString()));
            org.proyecto.domain.Planeta pl = new org.proyecto.domain.Planeta();
            pl.setId(Long.valueOf(planeta.get("id").toString()));
            p.setUsuario(u);
            p.setPlaneta(pl);
            p.setVida(Integer.parseInt(m.get("vida").toString()));
            return p;
        }).toList();
        Partida saved = partidaService.guardarPartida(partida, participantes);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizar(@RequestBody Map<String, Object> req) {
        Long partidaId = Long.valueOf(req.get("idPartida").toString());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> jugadores = (List<Map<String, Object>>) req.get("jugadores");
        Long idGanador = null;
        if (jugadores != null && !jugadores.isEmpty()) {
            Object first = jugadores.get(0).get("idJugador");
            if (first != null) {
                idGanador = Long.valueOf(first.toString());
            }
        }
        partidaService.finalizarPartida(partidaId, idGanador);
        return ResponseEntity.ok(Map.of("status", "finalizada"));
    }

    /**
     * Finalizar partida con posiciones
     * El middleware envía las posiciones de cada jugador
     */
    @PostMapping("/finalizar-con-posiciones")
    public ResponseEntity<?> finalizarConPosiciones(@RequestBody Map<String, Object> req) {
        Long partidaId = Long.valueOf(req.get("idPartida").toString());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> jugadores = (List<Map<String, Object>>) req.get("jugadores");
        if (jugadores == null || jugadores.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "lista de jugadores vacía"));
        }
        partidaService.finalizarPartidaConPosiciones(partidaId, jugadores);
        return ResponseEntity.ok(Map.of("status", "finalizada"));
    }

    @GetMapping
    public List<Partida> listarTodas() {
        return partidaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partida> obtenerPorId(@PathVariable Long id) {
        Partida p = partidaService.obtenerPorId(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }
}
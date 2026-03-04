package org.proyecto.estadisticas.web;

import org.proyecto.estadisticas.document.PartidaDoc;
import org.proyecto.estadisticas.dto.TipoPlanetaEstadisticaDTO;
import org.proyecto.estadisticas.dto.UsuarioEstadisticaDTO;
import org.proyecto.estadisticas.service.EstadisticasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para las estadísticas de partidas (MongoDB)
 */
@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    /**
     * 1. Copia las partidas de MySQL a MongoDB
     * POST /api/estadisticas/copiar
     */
    @PostMapping("/copiar")
    public ResponseEntity<Map<String, Object>> copiarPartidas() {
        int copiadas = estadisticasService.copiarPartidasAMongo();
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Partidas copiadas exitosamente");
        response.put("partidasCopiadas", copiadas);
        return ResponseEntity.ok(response);
    }

    /**
     * 2. Lista todas las partidas guardadas en MongoDB
     * GET /api/estadisticas/partidas
     */
    @GetMapping("/partidas")
    public ResponseEntity<List<PartidaDoc>> listarPartidas() {
        List<PartidaDoc> partidas = estadisticasService.listarTodasLasPartidas();
        return ResponseEntity.ok(partidas);
    }

    /**
     * 3. Obtiene el usuario que más partidas ha ganado
     * GET /api/estadisticas/usuario-top
     */
    @GetMapping("/usuario-top")
    public ResponseEntity<UsuarioEstadisticaDTO> obtenerUsuarioTop() {
        UsuarioEstadisticaDTO usuario = estadisticasService.obtenerUsuarioConMasVictorias();
        if (usuario == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuario);
    }

    /**
     * 4. Obtiene el tipo de planeta que más partidas ha ganado
     * GET /api/estadisticas/tipo-planeta-top
     */
    @GetMapping("/tipo-planeta-top")
    public ResponseEntity<TipoPlanetaEstadisticaDTO> obtenerTipoPlanetaTop() {
        TipoPlanetaEstadisticaDTO tipoPlaneta = estadisticasService.obtenerTipoPlanetaConMasVictorias();
        if (tipoPlaneta == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tipoPlaneta);
    }

    /**
     * Obtiene el ranking completo de usuarios por victorias
     * GET /api/estadisticas/ranking-usuarios
     */
    @GetMapping("/ranking-usuarios")
    public ResponseEntity<List<UsuarioEstadisticaDTO>> obtenerRankingUsuarios() {
        List<UsuarioEstadisticaDTO> ranking = estadisticasService.obtenerRankingUsuarios();
        return ResponseEntity.ok(ranking);
    }

    /**
     * Obtiene el ranking completo de tipos de planeta por victorias
     * GET /api/estadisticas/ranking-tipos-planeta
     */
    @GetMapping("/ranking-tipos-planeta")
    public ResponseEntity<List<TipoPlanetaEstadisticaDTO>> obtenerRankingTiposPlaneta() {
        List<TipoPlanetaEstadisticaDTO> ranking = estadisticasService.obtenerRankingTiposPlaneta();
        return ResponseEntity.ok(ranking);
    }
}

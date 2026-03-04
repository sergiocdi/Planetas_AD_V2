package org.proyecto.estadisticas.service;

import org.proyecto.domain.Participante;
import org.proyecto.domain.Partida;
import org.proyecto.estadisticas.document.ParticipanteDoc;
import org.proyecto.estadisticas.document.PartidaDoc;
import org.proyecto.estadisticas.dto.TipoPlanetaEstadisticaDTO;
import org.proyecto.estadisticas.dto.UsuarioEstadisticaDTO;
import org.proyecto.estadisticas.repository.PartidaDocRepository;
import org.proyecto.repository.ParticipanteRepository;
import org.proyecto.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para manejar las estadísticas de partidas en MongoDB
 */
@Service
public class EstadisticasService {

    private final PartidaRepository partidaRepository;
    private final ParticipanteRepository participanteRepository;
    private final PartidaDocRepository partidaDocRepository;

    public EstadisticasService(PartidaRepository partidaRepository,
                               ParticipanteRepository participanteRepository,
                               PartidaDocRepository partidaDocRepository) {
        this.partidaRepository = partidaRepository;
        this.participanteRepository = participanteRepository;
        this.partidaDocRepository = partidaDocRepository;
    }

    /**
     * 1. Copia todas las partidas de MySQL a MongoDB
     * @return número de partidas copiadas
     */
    public int copiarPartidasAMongo() {
        List<Partida> partidas = partidaRepository.findAll();
        int copiadas = 0;

        for (Partida partida : partidas) {
            // Verificar si ya existe en MongoDB
            if (!partidaDocRepository.existsByPartidaIdMysql(partida.getId())) {
                PartidaDoc partidaDoc = convertirAPartidaDoc(partida);
                partidaDocRepository.save(partidaDoc);
                copiadas++;
            }
        }

        return copiadas;
    }

    /**
     * Convierte una partida de MySQL a documento MongoDB
     */
    private PartidaDoc convertirAPartidaDoc(Partida partida) {
        PartidaDoc doc = new PartidaDoc();
        doc.setPartidaIdMysql(partida.getId());
        doc.setEstado(partida.getEstado());
        doc.setNumeroRonda(partida.getNumeroRonda());
        doc.setFechaCopiado(LocalDateTime.now());

        // Obtener participantes de la partida
        List<Participante> participantes = participanteRepository.findByPartidaId(partida.getId());
        List<ParticipanteDoc> participanteDocs = new ArrayList<>();

        // Identificar al ganador (el participante con vida > 0 en partida finalizada)
        Long ganadorId = null;
        if ("FINALIZADO".equals(partida.getEstado())) {
            for (Participante p : participantes) {
                if (p.getVida() > 0) {
                    ganadorId = p.getId();
                    break;
                }
            }
        }

        for (Participante p : participantes) {
            ParticipanteDoc pDoc = new ParticipanteDoc();
            pDoc.setParticipanteId(p.getId());
            
            if (p.getUsuario() != null) {
                pDoc.setUsuarioId(p.getUsuario().getId());
                pDoc.setUsuarioNickname(p.getUsuario().getNickname());
            }
            
            if (p.getPlaneta() != null) {
                pDoc.setPlanetaId(p.getPlaneta().getId());
                pDoc.setPlanetaNombre(p.getPlaneta().getNombre());
                pDoc.setPlanetaTipo(p.getPlaneta().getTipo());
            }
            
            pDoc.setVida(p.getVida());
            pDoc.setGanador(p.getId().equals(ganadorId));
            
            participanteDocs.add(pDoc);
        }

        doc.setParticipantes(participanteDocs);
        return doc;
    }

    /**
     * 2. Lista todas las partidas guardadas en MongoDB
     */
    public List<PartidaDoc> listarTodasLasPartidas() {
        return partidaDocRepository.findAll();
    }

    /**
     * 3. Obtiene el usuario que más partidas ha ganado
     */
    public UsuarioEstadisticaDTO obtenerUsuarioConMasVictorias() {
        List<PartidaDoc> partidas = partidaDocRepository.findAll();
        
        // Contar victorias por usuario
        Map<Long, Long> victoriasPorUsuario = new HashMap<>();
        Map<Long, String> nicknamesPorUsuario = new HashMap<>();
        
        for (PartidaDoc partida : partidas) {
            if (partida.getParticipantes() != null) {
                for (ParticipanteDoc p : partida.getParticipantes()) {
                    if (p.isGanador() && p.getUsuarioId() != null) {
                        victoriasPorUsuario.merge(p.getUsuarioId(), 1L, Long::sum);
                        nicknamesPorUsuario.putIfAbsent(p.getUsuarioId(), p.getUsuarioNickname());
                    }
                }
            }
        }

        // Encontrar el usuario con más victorias
        return victoriasPorUsuario.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new UsuarioEstadisticaDTO(
                        entry.getKey(),
                        nicknamesPorUsuario.get(entry.getKey()),
                        entry.getValue()))
                .orElse(null);
    }

    /**
     * 4. Obtiene el tipo de planeta que más partidas ha ganado
     */
    public TipoPlanetaEstadisticaDTO obtenerTipoPlanetaConMasVictorias() {
        List<PartidaDoc> partidas = partidaDocRepository.findAll();
        
        // Contar victorias por tipo de planeta
        Map<String, Long> victoriasPorTipo = new HashMap<>();
        
        for (PartidaDoc partida : partidas) {
            if (partida.getParticipantes() != null) {
                for (ParticipanteDoc p : partida.getParticipantes()) {
                    if (p.isGanador() && p.getPlanetaTipo() != null) {
                        victoriasPorTipo.merge(p.getPlanetaTipo(), 1L, Long::sum);
                    }
                }
            }
        }

        // Encontrar el tipo de planeta con más victorias
        return victoriasPorTipo.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new TipoPlanetaEstadisticaDTO(entry.getKey(), entry.getValue()))
                .orElse(null);
    }

    /**
     * Obtiene todas las estadísticas de victorias por usuario
     */
    public List<UsuarioEstadisticaDTO> obtenerRankingUsuarios() {
        List<PartidaDoc> partidas = partidaDocRepository.findAll();
        
        Map<Long, Long> victoriasPorUsuario = new HashMap<>();
        Map<Long, String> nicknamesPorUsuario = new HashMap<>();
        
        for (PartidaDoc partida : partidas) {
            if (partida.getParticipantes() != null) {
                for (ParticipanteDoc p : partida.getParticipantes()) {
                    if (p.isGanador() && p.getUsuarioId() != null) {
                        victoriasPorUsuario.merge(p.getUsuarioId(), 1L, Long::sum);
                        nicknamesPorUsuario.putIfAbsent(p.getUsuarioId(), p.getUsuarioNickname());
                    }
                }
            }
        }

        return victoriasPorUsuario.entrySet().stream()
                .map(entry -> new UsuarioEstadisticaDTO(
                        entry.getKey(),
                        nicknamesPorUsuario.get(entry.getKey()),
                        entry.getValue()))
                .sorted((a, b) -> Long.compare(b.getPartidasGanadas(), a.getPartidasGanadas()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las estadísticas de victorias por tipo de planeta
     */
    public List<TipoPlanetaEstadisticaDTO> obtenerRankingTiposPlaneta() {
        List<PartidaDoc> partidas = partidaDocRepository.findAll();
        
        Map<String, Long> victoriasPorTipo = new HashMap<>();
        
        for (PartidaDoc partida : partidas) {
            if (partida.getParticipantes() != null) {
                for (ParticipanteDoc p : partida.getParticipantes()) {
                    if (p.isGanador() && p.getPlanetaTipo() != null) {
                        victoriasPorTipo.merge(p.getPlanetaTipo(), 1L, Long::sum);
                    }
                }
            }
        }

        return victoriasPorTipo.entrySet().stream()
                .map(entry -> new TipoPlanetaEstadisticaDTO(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> Long.compare(b.getPartidasGanadas(), a.getPartidasGanadas()))
                .collect(Collectors.toList());
    }
}

package org.proyecto.service;

import org.proyecto.domain.*;
import org.proyecto.repository.ParticipanteRepository;
import org.proyecto.repository.PartidaRepository;
import org.proyecto.repository.PlanetaRepository;
import org.proyecto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PartidaService {
    private final PartidaRepository partidaRepository;
    private final ParticipanteRepository participanteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlanetaRepository planetaRepository;

    public PartidaService(PartidaRepository partidaRepository, ParticipanteRepository participanteRepository,
                          UsuarioRepository usuarioRepository, PlanetaRepository planetaRepository) {
        this.partidaRepository = partidaRepository;
        this.participanteRepository = participanteRepository;
        this.usuarioRepository = usuarioRepository;
        this.planetaRepository = planetaRepository;
    }

    @Transactional
    public Partida guardarPartida(Partida partida, List<Participante> participantes) {
        Partida saved = partidaRepository.save(partida);
        for (Participante part : participantes) {
            part.setPartida(saved);
            if (part.getUsuario() != null && part.getUsuario().getId() != null) {
                part.setUsuario(usuarioRepository.findById(part.getUsuario().getId()).orElseThrow());
            }
            if (part.getPlaneta() != null && part.getPlaneta().getId() != null) {
                part.setPlaneta(planetaRepository.findById(part.getPlaneta().getId()).orElseThrow());
            }
            participanteRepository.save(part);
        }
        return saved;
    }

    @Transactional
    public void finalizarPartida(Long partidaId, Long idGanador) {
        Partida partida = partidaRepository.findById(partidaId).orElseThrow();
        partida.setEstado("FINALIZADO");
        partidaRepository.save(partida);
        List<Participante> parts = participanteRepository.findByPartidaId(partidaId);
        Long ganadorUsuarioId = null;
        Long ganadorPlanetaId = null;
        if (idGanador != null) {
            for (Participante p : parts) {
                if (p.getId().equals(idGanador)) {
                    ganadorUsuarioId = p.getUsuario().getId();
                    ganadorPlanetaId = p.getPlaneta().getId();
                    break;
                }
            }
        }
        for (Participante p : parts) {
            Usuario u = usuarioRepository.findById(p.getUsuario().getId()).orElseThrow();
            if (ganadorUsuarioId != null && u.getId().equals(ganadorUsuarioId)) {
                u.setMonedas(u.getMonedas() + 200);
            } else {
                u.setMonedas(u.getMonedas() + 50);
                Planeta planet = planetaRepository.findById(p.getPlaneta().getId()).orElseThrow();
                planet.setVidas(Math.max(0, planet.getVidas() - 1));
                planetaRepository.save(planet);
            }
            usuarioRepository.save(u);
        }
        if (ganadorPlanetaId != null) {
            Planeta ganador = planetaRepository.findById(ganadorPlanetaId).orElseThrow();
            ganador.setNumeroVictorias(ganador.getNumeroVictorias() + 1);
            planetaRepository.save(ganador);
        }
    }

    // Nuevo: finalizar partida usando posiciones de jugadores
    @Transactional
    public void finalizarPartidaConPosiciones(Long partidaId, List<Map<String, Object>> jugadoresPosiciones) {
        Partida partida = partidaRepository.findById(partidaId).orElseThrow();
        partida.setEstado("FINALIZADO");
        partidaRepository.save(partida);

        // Obtener participantes de la partida
        List<Participante> participantes = participanteRepository.findByPartidaId(partidaId);

        // Mapear participanteId -> posición
        java.util.Map<Long, Integer> posiciones = new java.util.HashMap<>();
        for (Map<String, Object> jp : jugadoresPosiciones) {
            Long participanteId = Long.valueOf(jp.get("idJugador").toString());
            Integer posicion = Integer.valueOf(jp.get("posicion").toString());
            posiciones.put(participanteId, posicion);
        }

        // Identificar el participante ganador (posición 1)
        Long ganadorPlanetaId = null;
        for (Participante p : participantes) {
            Integer pos = posiciones.get(p.getId());
            if (pos != null && pos == 1) {
                ganadorPlanetaId = p.getPlaneta().getId();
                break;
            }
        }

        // Actualizar monedas y vidas por participante según posición
        for (Participante p : participantes) {
            Integer pos = posiciones.get(p.getId());
            if (pos == null) continue; // si no vino en la lista, lo ignoramos

            Usuario u = usuarioRepository.findById(p.getUsuario().getId()).orElseThrow();
            Planeta pl = planetaRepository.findById(p.getPlaneta().getId()).orElseThrow();

            // Monedas: 500 para el primero y -50 por cada posición que pierde
            int premio = Math.max(0, 500 - 50 * (pos - 1));
            u.setMonedas(u.getMonedas() + premio);

            // Vidas: restar 1 si no quedó primero
            if (pos != 1) {
                pl.setVidas(Math.max(0, pl.getVidas() - 1));
                planetaRepository.save(pl);
            }

            usuarioRepository.save(u);
        }

        // Incrementar victorias del planeta ganador
        if (ganadorPlanetaId != null) {
            Planeta ganador = planetaRepository.findById(ganadorPlanetaId).orElseThrow();
            ganador.setNumeroVictorias(ganador.getNumeroVictorias() + 1);
            planetaRepository.save(ganador);
        }
    }
}

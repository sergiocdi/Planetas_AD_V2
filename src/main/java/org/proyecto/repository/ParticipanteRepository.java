package org.proyecto.repository;

import org.proyecto.domain.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    List<Participante> findByPartidaId(Long partidaId);
}
package org.proyecto.repository;

import org.proyecto.domain.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
}
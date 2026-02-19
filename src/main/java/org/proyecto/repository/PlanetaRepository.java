package org.proyecto.repository;

import org.proyecto.domain.Planeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanetaRepository extends JpaRepository<Planeta, Long> {
    @Query("select p from Planeta p order by p.numeroVictorias desc")
    List<Planeta> findTopByVictorias();

    // Obtener los planetas de un usuario por su id
    List<Planeta> findByUsuarioId(Long usuarioId);
}
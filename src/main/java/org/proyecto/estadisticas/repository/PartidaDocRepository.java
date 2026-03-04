package org.proyecto.estadisticas.repository;

import org.proyecto.estadisticas.document.PartidaDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repositorio MongoDB para partidas de estadísticas
 */
public interface PartidaDocRepository extends MongoRepository<PartidaDoc, String> {
    Optional<PartidaDoc> findByPartidaIdMysql(Long partidaIdMysql);
    boolean existsByPartidaIdMysql(Long partidaIdMysql);
}

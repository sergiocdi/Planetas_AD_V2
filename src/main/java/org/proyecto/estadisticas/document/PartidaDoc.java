package org.proyecto.estadisticas.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Documento MongoDB que representa una partida para estadísticas
 */
@Document(collection = "partidas")
public class PartidaDoc {
    @Id
    private String id;
    private Long partidaIdMysql;
    private String estado;
    private int numeroRonda;
    private List<ParticipanteDoc> participantes;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaCopiado;

    public PartidaDoc() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getPartidaIdMysql() { return partidaIdMysql; }
    public void setPartidaIdMysql(Long partidaIdMysql) { this.partidaIdMysql = partidaIdMysql; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getNumeroRonda() { return numeroRonda; }
    public void setNumeroRonda(int numeroRonda) { this.numeroRonda = numeroRonda; }
    public List<ParticipanteDoc> getParticipantes() { return participantes; }
    public void setParticipantes(List<ParticipanteDoc> participantes) { this.participantes = participantes; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaCopiado() { return fechaCopiado; }
    public void setFechaCopiado(LocalDateTime fechaCopiado) { this.fechaCopiado = fechaCopiado; }
}

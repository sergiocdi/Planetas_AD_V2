package org.proyecto.estadisticas.document;

/**
 * Documento embebido que representa un participante de una partida
 */
public class ParticipanteDoc {
    private Long participanteId;
    private Long usuarioId;
    private String usuarioNickname;
    private Long planetaId;
    private String planetaNombre;
    private String planetaTipo;
    private int vida;
    private boolean ganador;

    public ParticipanteDoc() {}

    public Long getParticipanteId() { return participanteId; }
    public void setParticipanteId(Long participanteId) { this.participanteId = participanteId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioNickname() { return usuarioNickname; }
    public void setUsuarioNickname(String usuarioNickname) { this.usuarioNickname = usuarioNickname; }
    public Long getPlanetaId() { return planetaId; }
    public void setPlanetaId(Long planetaId) { this.planetaId = planetaId; }
    public String getPlanetaNombre() { return planetaNombre; }
    public void setPlanetaNombre(String planetaNombre) { this.planetaNombre = planetaNombre; }
    public String getPlanetaTipo() { return planetaTipo; }
    public void setPlanetaTipo(String planetaTipo) { this.planetaTipo = planetaTipo; }
    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }
    public boolean isGanador() { return ganador; }
    public void setGanador(boolean ganador) { this.ganador = ganador; }
}

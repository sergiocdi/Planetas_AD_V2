package org.proyecto.estadisticas.dto;

/**
 * DTO para mostrar estadísticas de usuario
 */
public class UsuarioEstadisticaDTO {
    private Long usuarioId;
    private String nickname;
    private long partidasGanadas;

    public UsuarioEstadisticaDTO() {}

    public UsuarioEstadisticaDTO(Long usuarioId, String nickname, long partidasGanadas) {
        this.usuarioId = usuarioId;
        this.nickname = nickname;
        this.partidasGanadas = partidasGanadas;
    }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public long getPartidasGanadas() { return partidasGanadas; }
    public void setPartidasGanadas(long partidasGanadas) { this.partidasGanadas = partidasGanadas; }
}

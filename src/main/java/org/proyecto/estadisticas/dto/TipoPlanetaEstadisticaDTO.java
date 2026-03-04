package org.proyecto.estadisticas.dto;

/**
 * DTO para mostrar estadísticas de tipo de planeta
 */
public class TipoPlanetaEstadisticaDTO {
    private String tipoPlaneta;
    private long partidasGanadas;

    public TipoPlanetaEstadisticaDTO() {}

    public TipoPlanetaEstadisticaDTO(String tipoPlaneta, long partidasGanadas) {
        this.tipoPlaneta = tipoPlaneta;
        this.partidasGanadas = partidasGanadas;
    }

    public String getTipoPlaneta() { return tipoPlaneta; }
    public void setTipoPlaneta(String tipoPlaneta) { this.tipoPlaneta = tipoPlaneta; }
    public long getPartidasGanadas() { return partidasGanadas; }
    public void setPartidasGanadas(long partidasGanadas) { this.partidasGanadas = partidasGanadas; }
}

package org.proyecto.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "partidas")
public class Partida extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String estado;
    @Column(name = "numero_ronda")
    private int numeroRonda;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getNumeroRonda() { return numeroRonda; }
    public void setNumeroRonda(int numeroRonda) { this.numeroRonda = numeroRonda; }
}
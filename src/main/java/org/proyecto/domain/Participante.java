package org.proyecto.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "participantes")
public class Participante extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id")
    private Partida partida;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planeta_id")
    private Planeta planeta;
    private int vida;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Partida getPartida() { return partida; }
    public void setPartida(Partida partida) { this.partida = partida; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Planeta getPlaneta() { return planeta; }
    public void setPlaneta(Planeta planeta) { this.planeta = planeta; }
    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }
}
package org.proyecto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "planetas")
public class Planeta extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String tipo;
    private int vidas = 5;
    @Column(name = "victorias")
    private int numeroVictorias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getVidas() { return vidas; }
    public void setVidas(int vidas) { this.vidas = vidas; }
    public int getNumeroVictorias() { return numeroVictorias; }
    public void setNumeroVictorias(int numeroVictorias) { this.numeroVictorias = numeroVictorias; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
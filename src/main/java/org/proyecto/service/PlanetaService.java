package org.proyecto.service;

import org.proyecto.domain.Planeta;
import org.proyecto.domain.Usuario;
import org.proyecto.repository.PlanetaRepository;
import org.proyecto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PlanetaService {
    private final PlanetaRepository planetaRepository;
    private final UsuarioRepository usuarioRepository;

    private static final Map<String, Integer> COSTES = Map.of(
            "Normal", 200,
            "Fuego", 300,
            "Agua", 300,
            "Planta", 300,
            "Aire", 350,
            "Roca", 500
    );

    public PlanetaService(PlanetaRepository planetaRepository, UsuarioRepository usuarioRepository) {
        this.planetaRepository = planetaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Planeta create(Planeta p, Long usuarioId) {
        Usuario u = usuarioRepository.findById(usuarioId).orElseThrow();
        int coste = COSTES.getOrDefault(p.getTipo(), Integer.MAX_VALUE);
        if (u.getMonedas() < coste) {
            throw new IllegalArgumentException("Monedas insuficientes");
        }
        u.setMonedas(u.getMonedas() - coste);
        p.setUsuario(u);
        p.setVidas(5);
        return planetaRepository.save(p);
    }

    public List<Planeta> list() { return planetaRepository.findAll(); }

    public List<Planeta> ranking() { return planetaRepository.findTopByVictorias(); }

    public List<Planeta> listByUsuarioId(Long usuarioId) {
        return planetaRepository.findByUsuarioId(usuarioId);
    }

    public Planeta update(Long id, Planeta p) {
        p.setId(id);
        return planetaRepository.save(p);
    }

    public void delete(Long id) { planetaRepository.deleteById(id); }
}
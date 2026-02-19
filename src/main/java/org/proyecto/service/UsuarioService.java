package org.proyecto.service;

import org.proyecto.domain.Usuario;
import org.proyecto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;

    public UsuarioService(UsuarioRepository usuarioRepository, AuthService authService) {
        this.usuarioRepository = usuarioRepository;
        this.authService = authService;
    }

    public Usuario create(Usuario u) {
        u.setPassword(authService.encode(u.getPassword()));
        return usuarioRepository.save(u);
    }
    public List<Usuario> list() { return usuarioRepository.findAll(); }
    public Optional<Usuario> get(Long id) { return usuarioRepository.findById(id); }
    public Usuario update(Long id, Usuario u) {
        u.setId(id);
        return usuarioRepository.save(u);
    }
    public void delete(Long id) { usuarioRepository.deleteById(id); }
}
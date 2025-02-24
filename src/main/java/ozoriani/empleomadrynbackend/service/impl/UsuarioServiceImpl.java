package ozoriani.empleomadrynbackend.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ozoriani.empleomadrynbackend.errors.exception.InvalidOperationException;
import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import ozoriani.empleomadrynbackend.model.Usuario;
import ozoriani.empleomadrynbackend.repository.UsuarioRepository;
import ozoriani.empleomadrynbackend.service.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Usuario createUsuario(Usuario usuario) {
        if (usuario == null)
            throw new InvalidOperationException("Usuario cannot be null");
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty())
            throw new InvalidOperationException("Usuario name cannot be null or empty");

        usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario getUsuarioById(UUID id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuario not found"));
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario updateUsuario(UUID id, Usuario usuario) {
        if (usuario == null)
            throw new InvalidOperationException("Usuario cannot be null");

        Usuario usuarioToUpdate = usuarioRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuario not found"));

        updateValues(usuarioToUpdate, usuario);

        return usuarioRepository.save(usuarioToUpdate);
    }

    @Override
    public void deleteUsuario(UUID id) {
        usuarioRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuario not found"));
        usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    private void updateValues(Usuario usuarioToUpdate, Usuario usuario) {
        if (usuario.getNombre() != null)
            usuarioToUpdate.setNombre(usuario.getNombre());
        if (usuario.getEmail() != null)
            usuarioToUpdate.setEmail(usuario.getEmail());
        usuarioToUpdate.setFavoritos(usuario.getFavoritos());
        usuarioToUpdate.setPublicaciones(usuario.getPublicaciones());
    }

}

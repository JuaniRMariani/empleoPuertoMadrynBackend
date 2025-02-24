package ozoriani.empleomadrynbackend.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import ozoriani.empleomadrynbackend.model.Usuario;
import ozoriani.empleomadrynbackend.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();
        usuario = new Usuario(usuarioId, "Juan", "juan@example.com", "password123", new ArrayList<>(), new ArrayList<>());
    }

//    @Test
//    void testCreateUsuario() {
//        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword123");
//        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
//            Usuario savedUser = invocation.getArgument(0);
//            savedUser.setPassword("encryptedPassword123");
//            return savedUser;
//        });
//
//        Usuario creado = usuarioService.createUsuario(usuario);
//
//        assertNotNull(creado);
//        assertEquals("encryptedPassword123", creado.getPassword());
//        verify(passwordEncoder, times(1)).encode("password123");
//    }

    @Test
    void testGetUsuarioById() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        Usuario encontrado = usuarioService.getUsuarioById(usuarioId);
        assertNotNull(encontrado);
        assertEquals(usuario.getEmail(), encontrado.getEmail());
    }

    @Test
    void testGetUsuarioById_NotFound() {
        when(usuarioRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> usuarioService.getUsuarioById(UUID.randomUUID()));
    }

    @Test
    void testGetAllUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario, new Usuario(UUID.randomUUID(), "Maria", "maria@example.com", "password456", new ArrayList<>(), new ArrayList<>()));
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        List<Usuario> resultado = usuarioService.getAllUsuarios();
        assertEquals(2, resultado.size());
    }

    @Test
    void testUpdateUsuario() {
        Usuario usuarioActualizado = new Usuario(usuarioId, "Juan Perez", "juan@example.com", "newpassword123", new ArrayList<>(), new ArrayList<>());
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        Usuario resultado = usuarioService.updateUsuario(usuarioId, usuarioActualizado);
        assertEquals("Juan Perez", resultado.getNombre());
        assertEquals("newpassword123", resultado.getPassword());
    }

    @Test
    void testUpdateUsuario_NotFound() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> usuarioService.updateUsuario(usuarioId, usuario));
    }

    @Test
    void testDeleteUsuario() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(usuarioId);
        assertDoesNotThrow(() -> usuarioService.deleteUsuario(usuarioId));
        verify(usuarioRepository, times(1)).deleteById(usuarioId);
    }

    @Test
    void testDeleteUsuario_NotFound() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> usuarioService.deleteUsuario(usuarioId));
    }

    @Test
    void testFindByEmail() {
        when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(usuario));
        Optional<Usuario> resultado = usuarioService.findByEmail("juan@example.com");
        assertTrue(resultado.isPresent());
        assertEquals(usuario.getEmail(), resultado.get().getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        when(usuarioRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        Optional<Usuario> resultado = usuarioService.findByEmail("notfound@example.com");
        assertFalse(resultado.isPresent());
    }
}

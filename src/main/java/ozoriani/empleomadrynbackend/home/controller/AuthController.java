package ozoriani.empleomadrynbackend.home.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import ozoriani.empleomadrynbackend.config.JwtUtil;
import ozoriani.empleomadrynbackend.errors.exception.ValidationException;
import ozoriani.empleomadrynbackend.home.model.dto.AuthResponse;
import ozoriani.empleomadrynbackend.home.model.dto.GoogleLoginRequest;
import ozoriani.empleomadrynbackend.home.model.entities.Usuario;
import ozoriani.empleomadrynbackend.home.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        try {
            Usuario usuario = authService.authenticateWithGoogle(
                    request.getTokenId(),
                    request.getGoogleId(),
                    request.getEmail(),
                    request.getName());
            String token = authService.generateJwtToken(usuario.getEmail());
            return ResponseEntity.ok(new AuthResponse(usuario.getId().toString(), token));
        } catch (Exception e) {
            throw new ValidationException("Error durante la autenticación con Google: " + e.getMessage());
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ValidationException("El encabezado Authorization debe comenzar con 'Bearer '");
        }

        String token = authHeader.substring(7);
        jwtUtil.validateToken(token);
        return ResponseEntity.ok().build();
    }
}
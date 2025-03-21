package ozoriani.empleomadrynbackend.home.controller;

import ozoriani.empleomadrynbackend.home.model.entities.Favoritos;
import ozoriani.empleomadrynbackend.home.service.FavoritosService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritosController {

    private final FavoritosService favoritosService;

    public FavoritosController(FavoritosService favoritosService) {
        this.favoritosService = favoritosService;
    }

    @PostMapping("/{ofertaId}")
    public ResponseEntity<Favoritos> addFavorite(
            @PathVariable UUID ofertaId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        Favoritos favorito = favoritosService.addFavorite(userEmail, ofertaId);
        return ResponseEntity.ok(favorito);
    }

    @DeleteMapping("/{ofertaId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable UUID ofertaId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        favoritosService.removeFavorite(userEmail, ofertaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Favoritos>> getUserFavorites(Authentication authentication) {
        String userEmail = authentication.getName();
        List<Favoritos> favoritos = favoritosService.getUserFavorites(userEmail);
        return ResponseEntity.ok(favoritos);
    }

    @GetMapping("/{ofertaId}/is-favorite")
    public ResponseEntity<Boolean> isFavorite(
            @PathVariable UUID ofertaId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        boolean isFavorite = favoritosService.isFavorite(userEmail, ofertaId);
        return ResponseEntity.ok(isFavorite);
    }
}
package ozoriani.empleomadrynbackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ozoriani.empleomadrynbackend.service.impl.TokenService;

@RestController
public class AuthController {

    private static final Logger Log = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        Log.debug("Generating basic token for user: '{}'", authentication.getName());
        String token = tokenService.generateToken(authentication);
        Log.debug("Basic token granted: {}", token);
        return token;
    }
}

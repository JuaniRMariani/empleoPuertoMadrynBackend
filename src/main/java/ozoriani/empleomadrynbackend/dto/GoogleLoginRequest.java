package ozoriani.empleomadrynbackend.dto;

import jakarta.validation.constraints.NotBlank;

public class GoogleLoginRequest {
    @NotBlank(message = "El token de Google no puede estar vacío")
    private String tokenId;

    @NotBlank(message = "El Google ID no puede estar vacío")
    private String googleId;

    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    public String getTokenId() { return tokenId; }
    public void setTokenId(String tokenId) { this.tokenId = tokenId; }
    public String getGoogleId() { return googleId; }
    public void setGoogleId(String googleId) { this.googleId = googleId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

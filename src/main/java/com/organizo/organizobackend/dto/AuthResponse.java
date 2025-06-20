package com.organizo.organizobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Resposta com token JWT e informações do usuário.
 */
@Schema(name = "AuthResponse", description = "Resposta de autenticação JWT")
public class AuthResponse {

    @Schema(description = "Token JWT de acesso")
    @NotBlank
    private String token;

    @Schema(description = "Email do usuário autênticado")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Nome do usuário")
    @NotBlank
    private String nome;

    @Schema(description = "Role concedida", example = "CLIENTE")
    @NotBlank
    private String role;

    public AuthResponse() { }

    public AuthResponse(String token, String email, String nome, String role) {
        this.token = token;
        this.email = email;
        this.nome = nome;
        this.role  = role;
    }

    // ===== Getters =====

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getRole()  {
        return role;
    }
}

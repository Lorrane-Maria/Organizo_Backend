package com.organizo.organizobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Dados para autenticação de usuário.
 */
@Schema(name = "LoginRequest", description = "Dados para autenticação de usuário")
public class LoginRequest {

    @Schema(description = "Email cadastrado", example = "user@ex.com", required = true)
    @NotBlank(message = "email é obrigatório")
    @Email(message = "email deve ser válido")
    private String email;

    @Schema(description = "Senha do usuário", required = true)
    @NotBlank(message = "senha é obrigatória")
    private String senha;

    public LoginRequest() { }

    // ===== Getters & Setters =====

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}

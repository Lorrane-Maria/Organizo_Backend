package com.organizo.organizobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Dados para registro de novo usuário.
 */
@Schema(name = "RegistroRequest", description = "Dados para registro de novo usuário")
public class RegistroRequest {

    @Schema(description = "Email para login", required = true, example = "novo@ex.com")
    @NotBlank(message = "email é obrigatório")
    @Email(message = "email deve ser válido")
    private String email;

    @Schema(description = "Senha de acesso (mínimo 6 caracteres)", required = true)
    @NotBlank(message = "senha é obrigatória")
    @Size(min = 6, message = "senha deve ter pelo menos 6 caracteres")
    private String senha;

    @Schema(description = "Nome completo", required = true)
    @NotBlank(message = "nome é obrigatório")
    private String nome;

    @Schema(description = "Role do usuário (CLIENTE, PROFISSIONAL, DONO_SALAO)", required = true)
    @NotNull(message = "role é obrigatório")
    private String role;

    public RegistroRequest() { }

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

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}

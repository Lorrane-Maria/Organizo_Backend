package com.organizo.organizobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de Salão,
 * agora incluindo o campo cnpj.
 */
@Schema(name = "SalaoDTO", description = "Dados de um salão de beleza")
public class SalaoDTO {

    @Schema(description = "ID do salão", example = "1")
    private Long id;

    @Schema(description = "Nome do salão", required = true, example = "Beleza Pura")
    @NotBlank(message = "nome é obrigatório")
    @Size(max = 100, message = "nome não pode exceder 100 caracteres")
    private String nome;

    @Schema(description = "CNPJ do salão", required = true, example = "12345678000199")
    @NotBlank(message = "cnpj é obrigatório")
    @Size(min = 14, max = 18, message = "cnpj deve ter tamanho válido")
    private String cnpj;

    @Schema(description = "Endereço completo", required = true, example = "Rua das Flores, 123")
    @NotBlank(message = "endereco é obrigatório")
    private String endereco;

    @Schema(description = "Telefone de contato", example = "31911112222")
    @Size(max = 20, message = "telefone não pode exceder 20 caracteres")
    private String telefone;

    @Schema(description = "Data de criação do registro")
    private LocalDateTime criadoEm;

    @Schema(description = "Data de última atualização do registro")
    private LocalDateTime atualizadoEm;

    // ========================================================
    // NOVO CAMPO: o ID do usuário (owner) ao qual este salão pertence.
    // ========================================================
    @Schema(description = "ID do usuário que é dono deste salão", example = "5")
    private Long ownerId;

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}

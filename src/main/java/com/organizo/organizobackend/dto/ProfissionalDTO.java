package com.organizo.organizobackend.dto;

import com.organizo.organizobackend.enums.CargoProfissional;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO para Profissional.
 * Transfere dados básicos e identificador do salão.
 */
@Schema(name = "ProfissionalDTO", description = "Dados de um profissional e vínculo com salão")
public class ProfissionalDTO {

    @Schema(description = "ID do profissional", example = "3")
    private Long id;

    @Schema(description = "Nome do profissional", required = true, example = "Ana")
    @NotBlank(message = "nome é obrigatório")
    @Size(max = 50, message = "nome não pode exceder 50 caracteres")
    private String nome;

    @Schema(description = "Sobrenome do profissional", required = true, example = "Silva")
    @NotBlank(message = "sobrenome é obrigatório")
    @Size(max = 50, message = "sobrenome não pode exceder 50 caracteres")
    private String sobrenome;

    @Schema(description = "Email de contato", required = true, example = "ana@ex.com")
    @NotBlank(message = "email é obrigatório")
    @Email(message = "email deve ser válido")
    private String email;

    @Schema(description = "Telefone de contato", example = "31999990000")
    @Size(max = 20, message = "telefone não pode exceder 20 caracteres")
    private String telefone;

    @Schema(description = "ID do salão onde trabalha", required = true, example = "1")
    @NotNull(message = "salaoId é obrigatório")
    private Long salaoId;

    @Schema(description = "Cargo/função (ex.: MANICURE, CABELEREIRO)", required = true, example = "MANICURE")
    @NotNull
    private CargoProfissional cargo;

    /**
     * IDs dos serviços habilitados para este profissional.
     * Opcional na criação (pode vir nulo ou vazio).
     * Se vier preenchido, será ignorado no momento de criar;
     * o vínculo será feito somente no PUT /api/profissionais/{id}/servicos.
     */
    @Schema(description = "IDs dos serviços habilitados para este profissional (opcional na criação)")
    private Set<Long> servicoIds;

    @Schema(description = "Data de criação do registro")
    private LocalDateTime criadoEm;

    @Schema(description = "Data de última atualização do registro")
    private LocalDateTime atualizadoEm;

    // Getters e Setters
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

    public String getSobrenome() {
        return sobrenome;
    }
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Long getSalaoId() {
        return salaoId;
    }

    public void setSalaoId(Long salaoId) {
        this.salaoId = salaoId;
    }

    public CargoProfissional getCargo() {
        return cargo;
    }
    public void setCargo(CargoProfissional cargo) {
        this.cargo = cargo;
    }

    public Set<Long> getServicoIds() {
        return servicoIds;
    }

    public void setServicoIds(Set<Long> servicoIds) {
        this.servicoIds = servicoIds;
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



}

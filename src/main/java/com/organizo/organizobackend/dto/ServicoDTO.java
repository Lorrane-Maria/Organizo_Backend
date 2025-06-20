package com.organizo.organizobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO para transferência de dados de Servico
 * entre client e servidor, sem expor a entidade JPA.
 */
@Schema(name = "ServicoDTO", description = "Dados de um serviço oferecido pelo salão")
public class ServicoDTO {

    @Schema(description = "ID do serviço", example = "2")
    private Long id;

    @Schema(description = "Nome do serviço", required = true, example = "Corte de cabelo")
    @NotBlank(message = "nome é obrigatório")
    private String nome;

    @Schema(description = "Descrição detalhada", required = true, example = "Corte estilizado e secagem")
    @NotBlank(message = "descricao é obrigatório")
    private String descricao;

    @Schema(description = "Duração em minutos", required = true, example = "45")
    @NotNull(message = "duracaoMinutos é obrigatório")
    @Min(value = 1, message = "duracaoMinutos deve ser pelo menos 1 minuto")
    private Integer duracaoMinutos;

    @Schema(description = "Preço do serviço", required = true, example = "80.50")
    @NotNull(message = "preco é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "preco deve ser positivo")
    private BigDecimal preco;

    /** <-- novo campo: o salão ao qual este serviço pertence */
    @Schema(description="ID do salão ao qual o serviço pertence")
    private Long salaoId;

    /** <-- novo campo: IDs dos profissionais que executam este serviço */
    @Schema(description="IDs dos profissionais habilitados para este serviço")
    private Set<Long> profissionalIds;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Long getSalaoId() {
        return salaoId;
    }
    public void setSalaoId(Long salaoId) {
        this.salaoId = salaoId;
    }

    public Set<Long> getProfissionalIds() {
        return profissionalIds;
    }
    public void setProfissionalIds(Set<Long> profissionalIds) {
        this.profissionalIds = profissionalIds;
    }
}

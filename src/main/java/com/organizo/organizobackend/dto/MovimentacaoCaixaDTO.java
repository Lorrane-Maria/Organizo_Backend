package com.organizo.organizobackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "MovimentacaoCaixa", description = "Registro financeiro de um agendamento concluído")
public class MovimentacaoCaixaDTO {

    @Schema(description = "ID da movimentação", example = "7")
    private Long id;

    @Schema(description = "ID do salão associado", example = "2", required = true)
    @NotNull(message = "salaoId é obrigatório")
    private Long salaoId;

    @Schema(description = "ID do profissional associado", example = "3", required = true)
    @NotNull(message = "profissionalId é obrigatório")
    private Long profissionalId;

    @Schema(description = "ID do agendamento relacionado", example = "1", required = true)
    @NotNull(message = "agendamentoId é obrigatório")
    private Long agendamentoId;

    @Schema(description = "Valor total cobrado", example = "150.00", required = true)
    @NotNull(message = "valorTotal é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "valorTotal deve ser positivo")
    private BigDecimal valorTotal;

    @Schema(description = "Porcentagem do profissional", example = "30", required = true)
    @NotNull(message = "porcentagemProfissional é obrigatório")
    @Min(value = 0, message = "porcentagemProfissional deve ser >= 0")
    @Max(value = 100, message = "porcentagemProfissional deve ser <= 100")
    private Double porcentagemProfissional;

    @Schema(description = "Valor destinado ao profissional", example = "45.00", required = true)
    @NotNull(message = "valorProfissional é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "valorProfissional deve ser positivo ou zero")
    private BigDecimal valorProfissional;

    @Schema(description = "Valor destinado ao salão", example = "105.00", required = true)
    @NotNull(message = "valorSalao é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "valorSalao deve ser positivo ou zero")
    private BigDecimal valorSalao;

    @Schema(description = "Data e hora da movimentação")
    private LocalDateTime dataMovimentacao;

    public Long getSalaoId() {
        return salaoId;
    }

    public void setSalaoId(Long salaoId) {
        this.salaoId = salaoId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(Long agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getPorcentagemProfissional() {
        return porcentagemProfissional;
    }

    public void setPorcentagemProfissional(Double porcentagemProfissional) {
        this.porcentagemProfissional = porcentagemProfissional;
    }

    public BigDecimal getValorProfissional() {
        return valorProfissional;
    }

    public void setValorProfissional(BigDecimal valorProfissional) {
        this.valorProfissional = valorProfissional;
    }

    public BigDecimal getValorSalao() {
        return valorSalao;
    }

    public void setValorSalao(BigDecimal valorSalao) {
        this.valorSalao = valorSalao;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
package com.organizo.organizobackend.dto;

import com.organizo.organizobackend.enums.StatusAgendamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de Agendamento.
 * Inclui nomes de cliente, profissional e serviço para conveniência do frontend.
 */
@Schema(name = "Agendamento", description = "Dados de um agendamento de serviço")
public class AgendamentoDTO {

    @Schema(description = "ID do agendamento", example = "10")
    private Long id;

    @Schema(description = "ID do cliente", example = "5", required = true)
    @NotNull(message = "clienteId é obrigatório")
    private Long clienteId;

    @Schema(description = "ID do profissional", example = "3", required = true)
    @NotNull(message = "profissionalId é obrigatório")
    private Long profissionalId;

    @Schema(description = "ID do serviço", example = "2", required = true)
    @NotNull(message = "servicoId é obrigatório")
    private Long servicoId;

    @Schema(description = "Data e hora do agendamento",
            example = "2025-05-01T15:30:00", required = true)
    @NotNull(message = "dataHoraAgendada é obrigatório")
    @Future(message = "dataHoraAgendada deve ser no futuro")
    private LocalDateTime dataHoraAgendada;

    @Schema(description = "Status do agendamento", example = "PENDENTE")
    private StatusAgendamento status;

    // Campos adicionados para facilitar exibição no frontend
    @Schema(description = "Nome do cliente", example = "Maria Silva", accessMode = Schema.AccessMode.READ_ONLY)
    private String nomeCliente;

    @Schema(description = "Nome do profissional", example = "João Pereira", accessMode = Schema.AccessMode.READ_ONLY)
    private String nomeProfissional;

    @Schema(description = "Nome do serviço", example = "Corte de Cabelo", accessMode = Schema.AccessMode.READ_ONLY)
    private String nomeServico;

    // Getters e Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }
    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public Long getServicoId() {
        return servicoId;
    }
    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public LocalDateTime getDataHoraAgendada() {
        return dataHoraAgendada;
    }
    public void setDataHoraAgendada(LocalDateTime dataHoraAgendada) {
        this.dataHoraAgendada = dataHoraAgendada;
    }

    public StatusAgendamento getStatus() {
        return status;
    }
    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getNomeProfissional() {
        return nomeProfissional;
    }
    public void setNomeProfissional(String nomeProfissional) {
        this.nomeProfissional = nomeProfissional;
    }

    public String getNomeServico() {
        return nomeServico;
    }
    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }
}


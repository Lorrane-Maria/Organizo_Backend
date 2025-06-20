package com.organizo.organizobackend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Registra entradas de caixa após conclusão de agendamento.
 */
@Entity
@Table(name = "movimentacao_caixa")
public class MovimentacaoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salao_id", nullable = false)
    private Salao salao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false, unique = true)
    private Agendamento agendamento;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "porcentagem_profissional", nullable = false)
    private Double porcentagemProfissional;

    @Column(name = "valor_profissional", nullable = false)
    private BigDecimal valorProfissional;

    @Column(name = "valor_salao", nullable = false)
    private BigDecimal valorSalao;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;

    public MovimentacaoCaixa() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Salao getSalao() {
        return salao;
    }

    public void setSalao(Salao salao) {
        this.salao = salao;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
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
}

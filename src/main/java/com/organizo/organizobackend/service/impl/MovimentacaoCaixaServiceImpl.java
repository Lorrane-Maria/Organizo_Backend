package com.organizo.organizobackend.service.impl;

import com.organizo.organizobackend.dto.MovimentacaoCaixaDTO;
import com.organizo.organizobackend.model.*;
import com.organizo.organizobackend.repository.*;
import com.organizo.organizobackend.service.MovimentacaoCaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimentacaoCaixaServiceImpl implements MovimentacaoCaixaService {

    @Autowired private MovimentacaoCaixaRepository repo;
    @Autowired private AgendamentoRepository agRepo;

    @Override
    public MovimentacaoCaixaDTO gerarPorAgendamento(Long agendamentoId, Double percProf) {
        Agendamento ag = agRepo.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        // Calcula valores
        BigDecimal total = ag.getServico().getPreco();
        BigDecimal valorProf = total.multiply(BigDecimal.valueOf(percProf / 100));
        BigDecimal valorSalao = total.subtract(valorProf);

        MovimentacaoCaixa mv = new MovimentacaoCaixa();
        mv.setAgendamento(ag);
        mv.setProfissional(ag.getProfissional());
        mv.setSalao(ag.getProfissional().getSalao());
        mv.setValorTotal(total);
        mv.setPorcentagemProfissional(percProf);
        mv.setValorProfissional(valorProf);
        mv.setValorSalao(valorSalao);
        mv.setDataMovimentacao(LocalDateTime.now());

        MovimentacaoCaixa salvo = repo.save(mv);
        return toDTO(salvo);
    }

    @Override
    public List<MovimentacaoCaixaDTO> listarTodos() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private MovimentacaoCaixaDTO toDTO(MovimentacaoCaixa mv) {
        MovimentacaoCaixaDTO dto = new MovimentacaoCaixaDTO();
        dto.setId(mv.getId());
        dto.setAgendamentoId(mv.getAgendamento().getId());
        dto.setProfissionalId(mv.getProfissional().getId());
        dto.setSalaoId(mv.getSalao().getId());
        dto.setValorTotal(mv.getValorTotal());
        dto.setPorcentagemProfissional(mv.getPorcentagemProfissional());
        dto.setValorProfissional(mv.getValorProfissional());
        dto.setValorSalao(mv.getValorSalao());
        dto.setDataMovimentacao(mv.getDataMovimentacao());
        return dto;
    }
}
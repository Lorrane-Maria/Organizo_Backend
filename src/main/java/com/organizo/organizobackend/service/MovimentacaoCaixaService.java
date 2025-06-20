package com.organizo.organizobackend.service;

import com.organizo.organizobackend.dto.MovimentacaoCaixaDTO;
import java.util.List;

public interface MovimentacaoCaixaService {

    MovimentacaoCaixaDTO gerarPorAgendamento(Long agendamentoId, Double porcentagemProfissional);

    List<MovimentacaoCaixaDTO> listarTodos();
}

package com.organizo.organizobackend.service;

import com.organizo.organizobackend.dto.ServicoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Contrato da camada de serviço para Servico.
 * Define operações de listagem e consulta por ID.
 */
public interface ServicoService {

    // lista todos os serviços
    Page<ServicoDTO> listar(Pageable pageable);

    // lista serviços de um salão específico
    Page<ServicoDTO> listarPorSalao(Long salaoId, Pageable pageable);

    ServicoDTO buscarPorId(Long id);

    ServicoDTO criar(Long salaoId, ServicoDTO dto);

    ServicoDTO atualizar(Long id, ServicoDTO dto);

    void deletar(Long id);
}

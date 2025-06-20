package com.organizo.organizobackend.service;

import com.organizo.organizobackend.dto.AgendamentoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contrato de negócio para Agendamento, agora suportando paginação.
 */
public interface AgendamentoService {

    /**
     * Lista todos os agendamentos de forma paginada.
     * @param pageable parâmetros de página, tamanho e ordenação
     * @return página de AgendamentoDTO
     */
    Page<AgendamentoDTO> listar(Pageable pageable);

    /**
     * Lista agendamentos de um cliente de forma paginada.
     * @param clienteId identificador do cliente
     * @param pageable parâmetros de página
     * @return página de AgendamentoDTO do cliente
     */
    Page<AgendamentoDTO> listarPorCliente(Long clienteId, Pageable pageable);

    /**
     * Lista agendamentos de um profissional de forma paginada.
     * @param profissionalId identificador do profissional
     * @param pageable parâmetros de página
     * @return página de AgendamentoDTO do profissional
     */
    Page<AgendamentoDTO> listarPorProfissional(Long profissionalId, Pageable pageable);

    /**
     * Cria um novo agendamento.
     */
    AgendamentoDTO criar(AgendamentoDTO dto);

    /**
     * Confirma um agendamento existente.
     */
    AgendamentoDTO confirmar(Long id);

    /**
     * Cancela um agendamento existente.
     */
    AgendamentoDTO cancelar(Long id);
}

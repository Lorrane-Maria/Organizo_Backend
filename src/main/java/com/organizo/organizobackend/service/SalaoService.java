package com.organizo.organizobackend.service;

import com.organizo.organizobackend.dto.SalaoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Contrato da camada de serviço para Salão,
 * agora com paginação.
 */
public interface SalaoService {

    /**
     * Retorna uma página de serviços cadastrados.
     * @param pageable parâmetros de paginação e ordenação
     */
    Page<SalaoDTO> listar(Pageable pageable);

    /**
     * Busca um salão pelo seu ID.
     * @param id identificador do salão
     * @return DTO do salão encontrado
     * @throws RuntimeException se não encontrar
     */
    SalaoDTO buscarPorId(Long id);

    /**
     * Cria um novo salão no sistema.
     * @param dto dados do salão a criar
     * @return DTO do salão criado (com ID gerado)
     */
    SalaoDTO criar(SalaoDTO dto);

    /**
     * Remove um salão existente.
     * @param id identificador do salão a remover
     */
    void deletar(Long id);

    SalaoDTO atualizar(Long id, SalaoDTO dto);
}

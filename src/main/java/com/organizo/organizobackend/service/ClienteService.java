package com.organizo.organizobackend.service;

import com.organizo.organizobackend.dto.ClienteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Contrato da camada de serviço para Cliente.
 * Define operações de CRUD e listagem.
 */
public interface ClienteService {

    /**
     * Retorna página de clientes.
     */
    Page<ClienteDTO> listar(Pageable pageable);

    /**
     * Busca um cliente pelo ID.
     * @param id identificador do cliente
     */
    ClienteDTO buscarPorId(Long id);

    /**
     * Cria um novo cliente.
     * @param dto dados do cliente
     */
    ClienteDTO criar(ClienteDTO dto);

    /**
     * Remove um cliente existente.
     * @param id identificador do cliente
     */
    void deletar(Long id);
}

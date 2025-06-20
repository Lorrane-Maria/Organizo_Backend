package com.organizo.organizobackend.service.impl;

import com.organizo.organizobackend.dto.ClienteDTO;
import com.organizo.organizobackend.exception.ResourceNotFoundException;
import com.organizo.organizobackend.mapper.ClienteMapper;
import com.organizo.organizobackend.model.Cliente;
import com.organizo.organizobackend.repository.ClienteRepository;
import com.organizo.organizobackend.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação da lógica de negócio para Cliente,
 * realiza conversão entre entidade e DTO e usa o repositório.
 */
@Service
@Transactional
@RequiredArgsConstructor // Substitui o construtor com @Autowired
@Slf4j                 // Adiciona a capacidade de logging com o objeto 'log'
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;
    private final ClienteMapper mapper;

    /**
     * Lista clientes paginados com cache.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClienteDTO> listar(Pageable pageable) {
        log.info("Listando clientes com paginação: {}", pageable);
        return repo.findAll(pageable)
                .map(mapper::toDto); // Mantido 'toDto' para consistência com seu mapper
    }

    /**
     * Busca cliente por ID e armazena em cache.
     */
    @Override
    @Cacheable(value = "clientes", key = "#id")
    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(Long id) {
        log.info("Buscando cliente com ID: {}. Verificando cache primeiro.", id);
        Cliente c = repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente com ID: {} não encontrado no banco de dados.", id);
                    // Usando uma exceção mais específica
                    return new ResourceNotFoundException("Cliente não encontrado: " + id);
                });
        log.info("Cliente com ID: {} encontrado.", id);
        return mapper.toDto(c);
    }

    /**
     * Cria novo cliente e limpa cache de lista.
     */
    @Override
    @CacheEvict(value = "clientes", allEntries = true)
    public ClienteDTO criar(ClienteDTO dto) {
        log.info("Iniciando criação de novo cliente com email: {}", dto.getEmail());
        Cliente c = mapper.toEntity(dto);
        // Aqui você pode adicionar lógicas como encriptar a senha antes de salvar
        Cliente salvo = repo.save(c);
        log.info("Cliente criado com sucesso com ID: {}. Cache 'clientes' invalidado.", salvo.getId());
        return mapper.toDto(salvo);
    }

    /**
     * Deleta cliente por ID e limpa cache.
     */
    @Override
    @CacheEvict(value = "clientes", allEntries = true)
    public void deletar(Long id) {
        log.info("Iniciando exclusão do cliente com ID: {}", id);
        if (!repo.existsById(id)) {
            log.error("Tentativa de deletar cliente inexistente com ID: {}", id);
            throw new ResourceNotFoundException("Cliente não encontrado para exclusão: " + id);
        }
        repo.deleteById(id);
        log.info("Cliente com ID: {} deletado com sucesso. Cache 'clientes' invalidado.", id);
    }
}
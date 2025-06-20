package com.organizo.organizobackend.service.impl;

import com.organizo.organizobackend.dto.SalaoDTO;
import com.organizo.organizobackend.exception.BusinessException;
import com.organizo.organizobackend.exception.ResourceNotFoundException;
import com.organizo.organizobackend.mapper.SalaoMapper;
import com.organizo.organizobackend.model.Salao;
import com.organizo.organizobackend.model.Usuario;
import com.organizo.organizobackend.repository.SalaoRepository;
import com.organizo.organizobackend.repository.UsuarioRepository;
import com.organizo.organizobackend.service.SalaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Implementação da camada de serviço para Salão.
 * Garante que o dono do salão seja o usuário logado na criação.
 * Utiliza exceções customizadas para tratamento de erros.
 */
@Service
public class SalaoServiceImpl implements SalaoService {

    private final SalaoRepository salaoRepo;
    private final SalaoMapper mapper;
    private final UsuarioRepository usuarioRepo;

    @Autowired
    public SalaoServiceImpl(SalaoRepository salaoRepo,
                            SalaoMapper mapper,
                            UsuarioRepository usuarioRepo) {
        this.salaoRepo = salaoRepo;
        this.mapper = mapper;
        this.usuarioRepo = usuarioRepo;
    }

    /**
     * Lista salões de forma paginada.
     * Cache: 'saloes' -> página + tamanho + ordenação.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SalaoDTO> listar(Pageable pageable) {
        return salaoRepo.findAll(pageable)
                .map(mapper::toDto);
    }

    /**
     * Busca um salão pelo ID.
     * Cache: armazena em 'saloes:id'.
     */
    @Override
    @Cacheable(value = "saloes", key = "#id")
    @Transactional(readOnly = true)
    public SalaoDTO buscarPorId(Long id) {
        Salao salao = salaoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salão", "ID", id));
        return mapper.toDto(salao);
    }

    /**
     * Cria novo salão, definindo o usuário logado como proprietário.
     * Limpa cache de listagem.
     */
    @Override
    @CacheEvict(value = "saloes", allEntries = true)
    @Transactional // Garante atomicidade
    public SalaoDTO criar(SalaoDTO dto) {
        // 1. Obter o email do usuário logado
        String ownerEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Buscar o usuário (owner) logado no banco
        Usuario owner = usuarioRepo.findByEmail(ownerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário logado não encontrado no banco de dados: " + ownerEmail));

        // 3. Converter DTO para entidade (MapStruct ignora owner e id)
        Salao salaEntity = mapper.toEntity(dto);

        // 4. Definir o proprietário como o usuário logado (IGNORA o ownerId do DTO)
        salaEntity.setOwner(owner);
        // A linha salaEntity.setId(null); foi REMOVIDA pois era desnecessária e causava erro.

        // 5. Salvar e retornar como DTO
        Salao saved = salaoRepo.save(salaEntity);
        return mapper.toDto(saved);
    }

    /**
     * Atualiza um salão. A verificação de posse é feita via @PreAuthorize no Controller.
     * Limpa cache.
     */
    @Override
    @CacheEvict(value = {"saloes", "saloes::#id"}, allEntries = false)
    @Transactional
    public SalaoDTO atualizar(Long id, SalaoDTO dto) {
        // A checagem se o usuário logado é o dono (ou ADMIN) é feita no Controller com @PreAuthorize
        Salao existente = salaoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salão", "ID", id));

        // Atualiza campos simples (não permite trocar owner aqui)
        existente.setNome(dto.getNome());
        existente.setCnpj(dto.getCnpj());
        existente.setEndereco(dto.getEndereco());
        existente.setTelefone(dto.getTelefone());

        Salao saved = salaoRepo.save(existente);
        return mapper.toDto(saved);
    }

    /**
     * Deleta um salão. A verificação de posse é feita via @PreAuthorize no Controller.
     * Limpa cache.
     */
    @Override
    @CacheEvict(value = {"saloes", "saloes::#id"}, allEntries = false)
    @Transactional
    public void deletar(Long id) {
        // A checagem se o usuário logado é o dono (ou ADMIN) é feita no Controller com @PreAuthorize
        if (!salaoRepo.existsById(id)) {
            throw new ResourceNotFoundException("Salão", "ID", id);
        }
        salaoRepo.deleteById(id);
    }
}


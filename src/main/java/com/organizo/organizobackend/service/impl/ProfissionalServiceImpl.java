package com.organizo.organizobackend.service.impl;

import com.organizo.organizobackend.dto.ProfissionalDTO;
import com.organizo.organizobackend.exception.ResourceNotFoundException;
import com.organizo.organizobackend.mapper.ProfissionalMapper;
import com.organizo.organizobackend.model.Profissional;
import com.organizo.organizobackend.model.Salao;
import com.organizo.organizobackend.model.Servico;
import com.organizo.organizobackend.repository.ProfissionalRepository;
import com.organizo.organizobackend.repository.SalaoRepository;
import com.organizo.organizobackend.repository.ServicoRepository;
import com.organizo.organizobackend.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementação da lógica de negócio para Profissional com paginação e cache.
 * Utiliza exceções customizadas para tratamento de erros.
 */
@Service
public class ProfissionalServiceImpl implements ProfissionalService {

    private final ProfissionalRepository repo;
    private final SalaoRepository salaoRepo;
    private final ServicoRepository servRepo;
    private final ProfissionalMapper mapper;

    @Autowired
    public ProfissionalServiceImpl(
            ProfissionalRepository repo,
            SalaoRepository salaoRepo,
            ServicoRepository servRepo,
            ProfissionalMapper mapper) {
        this.repo = repo;
        this.salaoRepo = salaoRepo;
        this.servRepo = servRepo;
        this.mapper = mapper;
    }

    /**
     * Lista profissionais com paginação.
     */
    @Override
    public Page<ProfissionalDTO> listar(Pageable pageable) {
        return repo.findAll(pageable)
                .map(mapper::toDto);
    }

    /**
     * Lista profissionais de um salão específico com paginação.
     */
    @Override
    public Page<ProfissionalDTO> listarPorSalao(Long salaoId, Pageable pageable) {
        // Verifica se o salão existe antes de listar os profissionais
        if (!salaoRepo.existsById(salaoId)) {
            throw new ResourceNotFoundException("Salão", "ID", salaoId);
        }
        return repo.findBySalaoId(salaoId, pageable)
                .map(mapper::toDto);
    }

    /**
     * Busca profissional por ID.
     */
    @Override
    @Cacheable(value = "profissionais", key = "#id")
    public ProfissionalDTO buscarPorId(Long id) {
        Profissional p = repo.findById(id)
                // Lança exceção específica se não encontrar
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", "ID", id));
        return mapper.toDto(p);
    }


    /**
     * Cria um novo profissional dentro de um salão.
     */
    @Override
    @CacheEvict(value = "profissionais", allEntries = true) // Limpa cache geral ao criar novo
    public ProfissionalDTO criar(Long salaoId, ProfissionalDTO dto) {
        // 1) verificar se o salão existe
        Salao salao = salaoRepo.findById(salaoId)
                // Lança exceção específica se não encontrar
                .orElseThrow(() -> new ResourceNotFoundException("Salão", "ID", salaoId));

        // 2) mapear DTO -> entidade (ignora serviços)
        Profissional prof = mapper.toEntity(dto);
        prof.setSalao(salao);

        // 3) salva sem atribuir ANY servicos ainda
        Profissional salvo = repo.save(prof);
        return mapper.toDto(salvo);
    }

    /**
     * Vincula serviços existentes a um profissional.
     */
    @Override
    @CacheEvict(value = "profissionais", key = "#profissionalId") // Limpa cache do profissional modificado
    public ProfissionalDTO vincularServicos(Long profissionalId, Set<Long> servicoIds) {
        Profissional p = repo.findById(profissionalId)
                // Lança exceção específica se não encontrar
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", "ID", profissionalId));

        // Busca e valida todos os serviços antes de associar
        Set<Servico> servicos = servicoIds.stream()
                .map(sid -> servRepo.findById(sid)
                        // Lança exceção específica se não encontrar
                        .orElseThrow(() -> new ResourceNotFoundException("Serviço", "ID", sid)))
                .collect(Collectors.toSet());
        p.setServicos(servicos);

        Profissional updated = repo.save(p);
        return mapper.toDto(updated);
    }

    /**
     * Atualiza dados básicos de um profissional.
     */
    @Override
    @CacheEvict(value = "profissionais", key = "#id") // Limpa cache do profissional modificado
    public ProfissionalDTO atualizar(Long id, ProfissionalDTO dto) {
        Profissional existente = repo.findById(id)
                // Lança exceção específica se não encontrar
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", "ID", id));

        existente.setNome(dto.getNome());
        existente.setSobrenome(dto.getSobrenome());
        existente.setEmail(dto.getEmail());
        existente.setTelefone(dto.getTelefone());
        existente.setCargo(dto.getCargo());
        // obs.: não tratamos servicos aqui, usar vincularServicos

        Profissional atualizado = repo.save(existente);
        return mapper.toDto(atualizado);
    }

    /**
     * Deleta um profissional.
     */
    @Override
    @CacheEvict(value = {"profissionais", "profissionais::#id"}, allEntries = false) // Limpa cache geral e específico
    public void deletar(Long id) {
        // Verifica se o profissional existe antes de tentar deletar
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Profissional", "ID", id);
        }
        repo.deleteById(id);
    }
}
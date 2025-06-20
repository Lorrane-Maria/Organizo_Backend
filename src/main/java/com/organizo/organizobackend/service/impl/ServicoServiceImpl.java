package com.organizo.organizobackend.service.impl;

import com.organizo.organizobackend.dto.ServicoDTO;
import com.organizo.organizobackend.model.Profissional;
import com.organizo.organizobackend.model.Salao;
import com.organizo.organizobackend.model.Servico;
import com.organizo.organizobackend.mapper.ServicoMapper;
import com.organizo.organizobackend.repository.ProfissionalRepository;
import com.organizo.organizobackend.repository.SalaoRepository;
import com.organizo.organizobackend.repository.ServicoRepository;
import com.organizo.organizobackend.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementação da camada de serviço para Servico,
 * contendo a lógica de conversão e uso do repositório.
 */
@Service
public class ServicoServiceImpl implements ServicoService {

    private final ServicoRepository servicoRepo;
    private final ServicoMapper mapper;
    private final SalaoRepository salaoRepo;
    private final ProfissionalRepository profRepo;

    @Autowired
    public ServicoServiceImpl(ServicoRepository servicoRepo, ServicoMapper mapper, SalaoRepository salaoRepo, ProfissionalRepository profRepo) {
        this.servicoRepo = servicoRepo;
        this.mapper = mapper;
        this.salaoRepo = salaoRepo;
        this.profRepo = profRepo;
    }

    @Override
    public Page<ServicoDTO> listar(Pageable pageable) {
        return servicoRepo.findAll(pageable)
                .map(mapper::toDto);
    }

    /**
     * Lista serviços de forma paginada.
     * Cache "servicos" to reduce load on database.
     */
    @Override
    public Page<ServicoDTO> listarPorSalao(Long salaoId, Pageable pageable) {
        return servicoRepo.findBySalaoId(salaoId, pageable)
                .map(mapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value = "servicos", key = "#id")
    public ServicoDTO buscarPorId(Long id) {
        Servico servico = servicoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado: " + id));
        return mapper.toDto(servico);
    }

    @Override
    public ServicoDTO criar(Long salaoId, ServicoDTO dto) {
        // 1) busca o salão
        Salao salao = salaoRepo.findById(salaoId)
                .orElseThrow(() -> new RuntimeException("Salão não encontrado: " + salaoId));

        // 2) mapeia e vincula
        Servico serv = mapper.toEntity(dto);
        serv.setSalao(salao);

        // 3) associa profissionais *somente se vier lista não-nula*
        if (dto.getProfissionalIds() != null) {
            Set<Profissional> pros = dto.getProfissionalIds().stream()
                    .map(id -> profRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Profissional não encontrado: " + id)))
                    .collect(Collectors.toSet());
            serv.setProfissionais(pros);
        } else {
            // evita NullPointer, inicializa vazio
            serv.setProfissionais(Collections.emptySet());
        }

        // 4) salva e retorna DTO
        Servico saved = servicoRepo.save(serv);
        return mapper.toDto(saved);
    }

    @Override
    public void deletar(Long id) {
        servicoRepo.deleteById(id);
    }

    @Override
    public ServicoDTO atualizar(Long id, ServicoDTO dto) {
        Servico existente = servicoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        existente.setNome(dto.getNome());
        existente.setDescricao(dto.getDescricao());
        existente.setDuracaoMinutos(dto.getDuracaoMinutos());
        existente.setPreco(dto.getPreco());
        Servico salvado = servicoRepo.save(existente);
        return mapper.toDto(salvado);
    }

}

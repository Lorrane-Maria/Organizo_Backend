package com.organizo.organizobackend.service;

import com.organizo.organizobackend.model.*;
import com.organizo.organizobackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Serviço auxiliar para verificações de segurança,
 * especificamente para checar a posse de recursos.
 * Usado em expressões @PreAuthorize.
 * Renomeado implicitamente para "securityCheckService" para refletir escopo maior.
 */
@Service("agendamentoSecurityService") // Nome do bean para @PreAuthorize (mantido por compatibilidade, mas poderia ser renomeado)
public class SalaoSecurityService { // TODO: Renomear classe para SecurityCheckService ou similar

    @Autowired private SalaoRepository salaoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProfissionalRepository profissionalRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private AgendamentoRepository agendamentoRepository;

    // --- Verificações de Posse de Salão --- //

    @Transactional(readOnly = true)
    public boolean isOwner(Long salaoId, String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(username);
        if (usuarioOpt.isEmpty()) return false;
        Usuario usuarioLogado = usuarioOpt.get();

        Optional<Salao> salaoOpt = salaoRepository.findById(salaoId);
        if (salaoOpt.isEmpty()) return false;
        Salao salao = salaoOpt.get();

        return salao.getOwner() != null && salao.getOwner().getId().equals(usuarioLogado.getId());
    }

    @Transactional(readOnly = true)
    public boolean isOwnerOfSalaoContainingProfissional(Long profissionalId, String username) {
        Optional<Profissional> profissionalOpt = profissionalRepository.findById(profissionalId);
        if (profissionalOpt.isEmpty()) return false;
        Profissional profissional = profissionalOpt.get();

        if (profissional.getSalao() == null || profissional.getSalao().getId() == null) return false;

        return isOwner(profissional.getSalao().getId(), username);
    }

    // --- Verificações de Posse de Cliente/Profissional (baseado no email) --- //

    @Transactional(readOnly = true)
    public boolean isClienteOwner(Long clienteId, String username) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        // Compara o email do usuário logado (username) com o email do cliente encontrado
        return clienteOpt.isPresent() && clienteOpt.get().getEmail().equalsIgnoreCase(username);
    }

    @Transactional(readOnly = true)
    public boolean isProfissionalOwner(Long profissionalId, String username) {
        Optional<Profissional> profissionalOpt = profissionalRepository.findById(profissionalId);
        // Compara o email do usuário logado (username) com o email do profissional encontrado
        return profissionalOpt.isPresent() && profissionalOpt.get().getEmail().equalsIgnoreCase(username);
    }

    // --- Verificações de Relação com Agendamento --- //

    @Transactional(readOnly = true)
    public boolean isClienteOfAgendamento(Long agendamentoId, String username) {
        Optional<Agendamento> agendamentoOpt = agendamentoRepository.findById(agendamentoId);
        if (agendamentoOpt.isEmpty() || agendamentoOpt.get().getCliente() == null) return false;
        // Compara o email do usuário logado com o email do cliente do agendamento
        return agendamentoOpt.get().getCliente().getEmail().equalsIgnoreCase(username);
    }

    @Transactional(readOnly = true)
    public boolean isProfissionalOfAgendamento(Long agendamentoId, String username) {
        Optional<Agendamento> agendamentoOpt = agendamentoRepository.findById(agendamentoId);
        if (agendamentoOpt.isEmpty() || agendamentoOpt.get().getProfissional() == null) return false;
        // Compara o email do usuário logado com o email do profissional do agendamento
        return agendamentoOpt.get().getProfissional().getEmail().equalsIgnoreCase(username);
    }

    @Transactional(readOnly = true)
    public boolean isOwnerOfSalaoContainingAgendamento(Long agendamentoId, String username) {
        Optional<Agendamento> agendamentoOpt = agendamentoRepository.findById(agendamentoId);
        if (agendamentoOpt.isEmpty() || agendamentoOpt.get().getProfissional() == null) return false;

        Profissional profissional = agendamentoOpt.get().getProfissional();
        if (profissional.getSalao() == null || profissional.getSalao().getId() == null) return false;

        // Reutiliza a lógica de isOwner para o salão do profissional do agendamento
        return isOwner(profissional.getSalao().getId(), username);
    }
}


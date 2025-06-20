package com.organizo.organizobackend.service.impl;

import com.organizo.organizobackend.dto.AgendamentoDTO;
import com.organizo.organizobackend.enums.StatusAgendamento;
import com.organizo.organizobackend.exception.BusinessException;
import com.organizo.organizobackend.exception.ResourceNotFoundException;
import com.organizo.organizobackend.model.Agendamento;
import com.organizo.organizobackend.model.Cliente;
import com.organizo.organizobackend.model.Profissional;
import com.organizo.organizobackend.model.Servico;
import com.organizo.organizobackend.repository.AgendamentoRepository;
import com.organizo.organizobackend.repository.ClienteRepository;
import com.organizo.organizobackend.repository.ProfissionalRepository;
import com.organizo.organizobackend.repository.ServicoRepository;
import com.organizo.organizobackend.service.AgendamentoService;
import com.organizo.organizobackend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementação da lógica de negócio para Agendamento,
 * incluindo paginação, envio de e‑mail e tratamento de erros customizado.
 */
@Service
public class AgendamentoServiceImpl implements AgendamentoService {

    private static final Logger logger = LoggerFactory.getLogger(AgendamentoServiceImpl.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    @Autowired private AgendamentoRepository agRepo;
    @Autowired private ClienteRepository clienteRepo;
    @Autowired private ProfissionalRepository profRepo;
    @Autowired private ServicoRepository servRepo;
    @Autowired private EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public Page<AgendamentoDTO> listar(Pageable pageable) {
        return agRepo.findAll(pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgendamentoDTO> listarPorCliente(Long clienteId, Pageable pageable) {
        if (!clienteRepo.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente", "ID", clienteId);
        }
        return agRepo.findByClienteId(clienteId, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgendamentoDTO> listarPorProfissional(Long profissionalId, Pageable pageable) {
        if (!profRepo.existsById(profissionalId)) {
            throw new ResourceNotFoundException("Profissional", "ID", profissionalId);
        }
        return agRepo.findByProfissionalId(profissionalId, pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional
    public AgendamentoDTO criar(AgendamentoDTO dto) {
        if (dto.getDataHoraAgendada() == null || dto.getDataHoraAgendada().isBefore(LocalDateTime.now())) {
            throw new BusinessException("A data e hora do agendamento devem ser futuras.");
        }

        Cliente cliente = clienteRepo.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "ID", dto.getClienteId()));
        Profissional prof = profRepo.findById(dto.getProfissionalId())
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", "ID", dto.getProfissionalId()));
        Servico serv = servRepo.findById(dto.getServicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço", "ID", dto.getServicoId()));

        // TODO: Adicionar validação de conflito de horário para o profissional

        Agendamento ag = new Agendamento();
        ag.setCliente(cliente);
        ag.setProfissional(prof);
        ag.setServico(serv);
        ag.setDataHoraAgendada(dto.getDataHoraAgendada());
        ag.setStatus(StatusAgendamento.PENDENTE);

        Agendamento salvo = agRepo.save(ag);

        // Envio de e‑mail ao cliente e profissional
        sendEmail(
                cliente.getEmail(),
                "Organizo - Agendamento Criado (Pendente)",
                String.format("Olá %s,\n\nSeu agendamento para '%s' com %s em %s foi criado com sucesso.\nStatus atual: PENDENTE.\n\nAtenciosamente,\nEquipe Organizo",
                        cliente.getNome(), serv.getNome(), prof.getNome(),
                        salvo.getDataHoraAgendada().format(formatter)),
                salvo.getId()
        );
        sendEmail(
                prof.getEmail(),
                "Organizo - Novo Agendamento Recebido",
                String.format("Olá %s,\n\nVocê recebeu um novo agendamento de %s para o serviço '%s' em %s.\nStatus: PENDENTE.\n\nAtenciosamente,\nEquipe Organizo",
                        prof.getNome(), cliente.getNome(), serv.getNome(),
                        salvo.getDataHoraAgendada().format(formatter)),
                salvo.getId()
        );

        return toDTO(salvo);
    }

    @Override
    @Transactional
    public AgendamentoDTO confirmar(Long id) {
        Agendamento ag = agRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento", "ID", id));

        if (ag.getStatus() != StatusAgendamento.PENDENTE) {
            throw new BusinessException("Agendamento não pode ser confirmado pois não está com status PENDENTE (Status atual: " + ag.getStatus() + ")");
        }

        ag.setStatus(StatusAgendamento.CONFIRMADO);
        Agendamento salvo = agRepo.save(ag);

        // Enviar email de confirmação
        Cliente cliente = salvo.getCliente();
        Profissional prof = salvo.getProfissional();
        Servico serv = salvo.getServico();

        sendEmail(
                cliente.getEmail(),
                "Organizo - Agendamento Confirmado!",
                String.format("Olá %s,\n\nBoas notícias! Seu agendamento para '%s' com %s em %s foi CONFIRMADO.\n\nEsperamos você!\nEquipe Organizo",
                        cliente.getNome(), serv.getNome(), prof.getNome(),
                        salvo.getDataHoraAgendada().format(formatter)),
                salvo.getId()
        );
        sendEmail(
                prof.getEmail(),
                "Organizo - Agendamento Confirmado",
                String.format("Olá %s,\n\nO agendamento de %s para o serviço '%s' em %s foi CONFIRMADO.\n\nAtenciosamente,\nEquipe Organizo",
                        prof.getNome(), cliente.getNome(), serv.getNome(),
                        salvo.getDataHoraAgendada().format(formatter)),
                salvo.getId()
        );

        return toDTO(salvo);
    }

    @Override
    @Transactional
    public AgendamentoDTO cancelar(Long id) {
        Agendamento ag = agRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento", "ID", id));

        if (ag.getStatus() == StatusAgendamento.CANCELADO || ag.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new BusinessException("Agendamento não pode ser cancelado pois seu status é " + ag.getStatus());
        }

        // TODO: Adicionar regra de antecedência mínima para cancelamento?

        ag.setStatus(StatusAgendamento.CANCELADO);
        Agendamento salvo = agRepo.save(ag);

        // Enviar email de cancelamento
        Cliente cliente = salvo.getCliente();
        Profissional prof = salvo.getProfissional();
        Servico serv = salvo.getServico();

        sendEmail(
                cliente.getEmail(),
                "Organizo - Agendamento Cancelado",
                String.format("Olá %s,\n\nInformamos que o seu agendamento para '%s' com %s em %s foi CANCELADO.\n\nSe precisar reagendar, entre em contato ou utilize nosso app.\nEquipe Organizo",
                        cliente.getNome(), serv.getNome(), prof.getNome(),
                        salvo.getDataHoraAgendada().format(formatter)),
                salvo.getId()
        );
        sendEmail(
                prof.getEmail(),
                "Organizo - Agendamento Cancelado",
                String.format("Olá %s,\n\nO agendamento de %s para o serviço '%s' em %s foi CANCELADO.\n\nAtenciosamente,\nEquipe Organizo",
                        prof.getNome(), cliente.getNome(), serv.getNome(),
                        salvo.getDataHoraAgendada().format(formatter)),
                salvo.getId()
        );

        return toDTO(salvo);
    }

    /**
     * Método auxiliar para enviar e-mail e logar erros.
     */
    private void sendEmail(String to, String subject, String text, Long agendamentoId) {
        try {
            emailService.sendSimpleMessage(to, subject, text);
            logger.info("Email enviado para {} sobre agendamento {}", to, agendamentoId);
        } catch (Exception e) {
            logger.error("Falha ao enviar email para {} sobre agendamento {}: {}", to, agendamentoId, e.getMessage(), e);
            // Não relançar a exceção para não impedir a operação principal
        }
    }

    /**
     * Converte entidade em DTO para resposta.
     */
    private AgendamentoDTO toDTO(Agendamento ag) {
        AgendamentoDTO dto = new AgendamentoDTO();
        dto.setId(ag.getId());
        dto.setClienteId(ag.getCliente().getId());
        dto.setProfissionalId(ag.getProfissional().getId());
        dto.setServicoId(ag.getServico().getId());
        dto.setDataHoraAgendada(ag.getDataHoraAgendada());
        dto.setStatus(ag.getStatus());
        dto.setNomeCliente(ag.getCliente().getNome());
        dto.setNomeProfissional(ag.getProfissional().getNome());
        dto.setNomeServico(ag.getServico().getNome());
        return dto;
    }
}


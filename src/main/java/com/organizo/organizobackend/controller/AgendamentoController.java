package com.organizo.organizobackend.controller;

import com.organizo.organizobackend.dto.AgendamentoDTO;
import com.organizo.organizobackend.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para gerenciar Agendamentos.
 * Aplica regras de autorização baseadas em roles e relação com o agendamento.
 */
@RestController
@RequestMapping("/api/agendamentos")
@Tag(name = "Agendamentos", description = "Endpoints para gerenciamento de agendamentos")
@SecurityRequirement(name = "bearerAuth")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @PostMapping
    @PreAuthorize("hasRole(\'CLIENTE\')") // Somente CLIENTE pode criar agendamento
    @Operation(summary = "Cria um novo agendamento (Requer CLIENTE)")
    @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos, data passada ou entidades relacionadas não encontradas")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    public ResponseEntity<AgendamentoDTO> criar(@Valid @RequestBody AgendamentoDTO dto) {
        // Idealmente, o clienteId deveria ser pego do usuário logado, não do DTO.
        // Ajuste a ser feito no service ou aqui.
        AgendamentoDTO novoAgendamento = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAgendamento);
    }

    @GetMapping
    @PreAuthorize("hasRole(\'ADMIN\')") // Somente ADMIN pode listar todos
    @Operation(summary = "Lista todos os agendamentos de forma paginada (Requer ADMIN)")
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    public ResponseEntity<Page<AgendamentoDTO>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @GetMapping("/cliente/{clienteId}")
    // Requer ADMIN OU (CLIENTE E ser o próprio cliente #clienteId)
    @PreAuthorize("hasRole(\'ADMIN\') or (hasRole(\'CLIENTE\') and @agendamentoSecurityService.isClienteOwner(#clienteId, principal.username))")
    @Operation(summary = "Lista agendamentos de um cliente específico (Requer ADMIN ou o próprio Cliente)")
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos do cliente retornada")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<Page<AgendamentoDTO>> listarPorCliente(
            @PathVariable Long clienteId,
            Pageable pageable) {
        return ResponseEntity.ok(service.listarPorCliente(clienteId, pageable));
    }

    @GetMapping("/profissional/{profissionalId}")
    // Requer ADMIN OU (PROFISSIONAL E ser o próprio profissional #profissionalId)
    @PreAuthorize("hasRole(\'ADMIN\') or (hasRole(\'PROFISSIONAL\') and @agendamentoSecurityService.isProfissionalOwner(#profissionalId, principal.username))")
    @Operation(summary = "Lista agendamentos de um profissional específico (Requer ADMIN ou o próprio Profissional)")
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos do profissional retornada")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Profissional não encontrado")
    public ResponseEntity<Page<AgendamentoDTO>> listarPorProfissional(
            @PathVariable Long profissionalId,
            Pageable pageable) {
        return ResponseEntity.ok(service.listarPorProfissional(profissionalId, pageable));
    }

    @PutMapping("/{id}/confirmar")
    // Requer ADMIN OU (PROFISSIONAL E ser o profissional do agendamento #id) OU (DONO_SALAO E ser dono do salão do profissional do agendamento #id)
    @PreAuthorize("hasRole(\'ADMIN\') or " +
            "(hasRole(\'PROFISSIONAL\') and @agendamentoSecurityService.isProfissionalOfAgendamento(#id, principal.username)) or " +
            "(hasRole(\'DONO_SALAO\') and @agendamentoSecurityService.isOwnerOfSalaoContainingAgendamento(#id, principal.username))")
    @Operation(summary = "Confirma um agendamento (Requer ADMIN, Profissional do Agendamento ou Dono do Salão)")
    @ApiResponse(responseCode = "200", description = "Agendamento confirmado")
    @ApiResponse(responseCode = "400", description = "Agendamento não está PENDENTE")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    public ResponseEntity<AgendamentoDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmar(id));
    }

    @PutMapping("/{id}/cancelar")
    // Requer ADMIN OU (CLIENTE E ser o cliente do agendamento #id) OU (PROFISSIONAL E ser o profissional do agendamento #id) OU (DONO_SALAO E ser dono do salão do profissional do agendamento #id)
    @PreAuthorize("hasRole(\'ADMIN\') or " +
            "(hasRole(\'CLIENTE\') and @agendamentoSecurityService.isClienteOfAgendamento(#id, principal.username)) or " +
            "(hasRole(\'PROFISSIONAL\') and @agendamentoSecurityService.isProfissionalOfAgendamento(#id, principal.username)) or " +
            "(hasRole(\'DONO_SALAO\') and @agendamentoSecurityService.isOwnerOfSalaoContainingAgendamento(#id, principal.username))")
    @Operation(summary = "Cancela um agendamento (Requer ADMIN, Cliente, Profissional do Agendamento ou Dono do Salão)")
    @ApiResponse(responseCode = "200", description = "Agendamento cancelado")
    @ApiResponse(responseCode = "400", description = "Agendamento não pode ser cancelado (status inválido)")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    public ResponseEntity<AgendamentoDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    // TODO: Criar um Bean @Service chamado 'agendamentoSecurityService' (ou adicionar métodos ao SalaoSecurityService)
    // com os métodos: isClienteOwner, isProfissionalOwner, isClienteOfAgendamento, isProfissionalOfAgendamento, isOwnerOfSalaoContainingAgendamento
}


package com.organizo.organizobackend.controller;

import com.organizo.organizobackend.dto.ClienteDTO;
import com.organizo.organizobackend.dto.PaginatedResponse;
import com.organizo.organizobackend.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clientes", description = "Gerenciamento de clientes")
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor // Substitui o construtor com @Autowired
@Slf4j
public class ClienteController {

    private final ClienteService service;

    @Operation(summary = "Lista clientes paginados", description = "Somente CLIENTE")
    @GetMapping
    public ResponseEntity<PaginatedResponse<ClienteDTO>> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        log.info("Requisição para listar clientes com paginação: {}", pageable);
        Page<ClienteDTO> page = service.listar(pageable);
        PaginatedResponse<ClienteDTO> resp = new PaginatedResponse<>(
                page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        );
        log.info("Retornando {} clientes de um total de {}", page.getNumberOfElements(), page.getTotalElements());
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Busca cliente por ID", description = "Somente CLIENTE")
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscar(@PathVariable Long id) {
        log.info("Requisição para buscar cliente por ID: {}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Cria um novo cliente", description = "Registrado sem autenticação prévia")
    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<ClienteDTO> criar(@Valid @RequestBody ClienteDTO dto) {
        log.info("Requisição para criar novo cliente com email: {}", dto.getEmail());
        ClienteDTO criado = service.criar(dto);
        log.info("Cliente criado com sucesso. ID: {}", criado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Remove cliente", description = "Somente CLIENTE pode deletar sua conta")
    @PreAuthorize("hasRole('CLIENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Requisição para deletar cliente com ID: {}", id);
        service.deletar(id);
        log.info("Cliente com ID: {} deletado com sucesso.", id);
        return ResponseEntity.noContent().build();
    }
}
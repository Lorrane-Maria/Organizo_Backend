package com.organizo.organizobackend.controller;

import com.organizo.organizobackend.dto.PaginatedResponse;
import com.organizo.organizobackend.dto.ProfissionalDTO;
import com.organizo.organizobackend.service.ProfissionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Endpoints REST para manipular Profissionais com paginação.
 */
@Tag(name = "Profissionais", description = "Gestão dos profissionais dos salões")
@RestController
@RequestMapping("/api")
public class ProfissionalController {

    private final ProfissionalService service;

    @Autowired
    public ProfissionalController(ProfissionalService service) {
        this.service = service;
    }

    @Operation(summary = "Cria um novo profissional", description = "Somente DONO_SALAO")
    @PreAuthorize("hasRole('DONO_SALAO')")
    @PostMapping("/saloes/{salaoId}/profissionais")
    public ResponseEntity<ProfissionalDTO> criar(
            @PathVariable Long salaoId,
            @RequestBody ProfissionalDTO dto) {
        ProfissionalDTO criado = service.criar(salaoId, dto);
        return ResponseEntity.status(201).body(criado);
    }

    @Operation(summary = "Lista profissionais paginados", description = "Somente PROFISSIONAL")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @GetMapping("/profissionais")
    public ResponseEntity<PaginatedResponse<ProfissionalDTO>> listar(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ProfissionalDTO> page = service.listar(pageable);
        PaginatedResponse<ProfissionalDTO> resp = new PaginatedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Lista profissionais de um salão", description = "Somente DONO_SALAO e PROFISSIONAL")
    @PreAuthorize("hasAnyRole('DONO_SALAO','PROFISSIONAL')")
    @GetMapping("/saloes/{salaoId}/profissionais")
    public ResponseEntity<PaginatedResponse<ProfissionalDTO>> listarPorSalao(
            @PathVariable("salaoId") Long salaoId,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<ProfissionalDTO> page = service.listarPorSalao(salaoId, pageable);
        PaginatedResponse<ProfissionalDTO> resp = new PaginatedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Busca profissional por ID", description = "Somente PROFISSIONAL")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @GetMapping("/profissionais/{id}")
    public ResponseEntity<ProfissionalDTO> buscar(
            @PathVariable Long id) {
        // salaoId validado implicitamente ou poderia checar se o profissional pertence ao salão
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Vincula serviços existentes a um profissional")
    @PreAuthorize("hasRole('DONO_SALAO')")
    @PutMapping("/profissionais/{id}/servicos")
    public ResponseEntity<ProfissionalDTO> vincularServicos(
            @PathVariable Long id,
            @RequestBody Set<Long> servicoIds
    ) {
        ProfissionalDTO atualizado = service.vincularServicos(id, servicoIds);
        return ResponseEntity.ok(atualizado);
    }

    /** Atualiza dados básicos de um profissional (sem lidar com serviços). */
    @Operation(summary = "Atualiza dados de um profissional", description = "Somente DONO_SALAO e PROFISSIONAL")
    @PreAuthorize("hasAnyRole('DONO_SALAO','PROFISSIONAL')")
    @PutMapping("/profissionais/{id}")
    public ResponseEntity<ProfissionalDTO> atualizar(
            @PathVariable Long id,
            @RequestBody ProfissionalDTO dto) {
        ProfissionalDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    /** Deleta um profissional. */
    @Operation(summary = "Remove um profissional", description = "Somente DONO_SALAO")
    @PreAuthorize("hasRole('DONO_SALAO')")
    @DeleteMapping("/profissionais/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
package com.organizo.organizobackend.controller;

import com.organizo.organizobackend.dto.PaginatedResponse;
import com.organizo.organizobackend.dto.ServicoDTO;
import com.organizo.organizobackend.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Serviços de Salão", description = "Listagem e criação de serviços vinculados a um salão")
@RestController
@RequestMapping("/api/saloes/{salaoId}/servicos")
public class SalaoServicoController {

    private final ServicoService servicoService;

    @Autowired
    public SalaoServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @Operation(summary = "Lista serviços de um salão", description = "Público")
    @GetMapping
    public ResponseEntity<PaginatedResponse<ServicoDTO>> listarPorSalao(
            @PathVariable Long salaoId,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<ServicoDTO> page = servicoService.listarPorSalao(salaoId, pageable);
        return ResponseEntity.ok(new PaginatedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        ));
    }

    @Operation(summary = "Cria um serviço atrelado a um salão", description = "Somente DONO_SALAO")
    @PreAuthorize("hasRole('DONO_SALAO')")
    @PostMapping
    public ResponseEntity<ServicoDTO> criar(
            @PathVariable Long salaoId,
            @Valid @RequestBody ServicoDTO dto) {

        ServicoDTO criado = servicoService.criar(salaoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
}

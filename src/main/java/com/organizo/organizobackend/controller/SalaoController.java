package com.organizo.organizobackend.controller;

import com.organizo.organizobackend.dto.SalaoDTO;
import com.organizo.organizobackend.service.SalaoService;
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
 * Controller para gerenciar Salões.
 * Aplica regras de autorização baseadas em roles e posse.
 */
@RestController
@RequestMapping("/api/saloes")
@Tag(name = "Salões", description = "Endpoints para gerenciamento de salões")
@SecurityRequirement(name = "bearerAuth") // Indica que a maioria dos endpoints requer autenticação JWT
public class SalaoController {

    @Autowired
    private SalaoService service;

    @GetMapping
    @Operation(summary = "Lista todos os salões de forma paginada (Público)")
    @ApiResponse(responseCode = "200", description = "Lista de salões retornada")
    public ResponseEntity<Page<SalaoDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um salão pelo ID (Público)")
    @ApiResponse(responseCode = "200", description = "Salão encontrado")
    @ApiResponse(responseCode = "404", description = "Salão não encontrado")
    public ResponseEntity<SalaoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('DONO_SALAO')") // Somente DONO_SALAO pode criar
    @Operation(summary = "Cria um novo salão (Requer DONO_SALAO)")
    @ApiResponse(responseCode = "201", description = "Salão criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou owner não encontrado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    public ResponseEntity<SalaoDTO> criar(@Valid @RequestBody SalaoDTO dto) {
        // A lógica de vincular ao owner logado pode ser feita no service
        // ou podemos pegar o ID do principal aqui e passar para o service.
        // Por enquanto, o service já busca o owner pelo dto.getOwnerId(),
        // mas idealmente, o ownerId deveria vir do usuário logado.
        // Vamos ajustar isso no service depois.
        SalaoDTO novoSalao = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoSalao);
    }

    @PutMapping("/{id}")
    // Requer ADMIN OU (DONO_SALAO E ser o dono do salão com ID #id)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DONO_SALAO') and @salaoSecurityService.isOwner(#id, principal.username))")
    @Operation(summary = "Atualiza um salão existente (Requer ADMIN ou Dono do Salão)")
    @ApiResponse(responseCode = "200", description = "Salão atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Salão ou novo owner não encontrado")
    public ResponseEntity<SalaoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody SalaoDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    // Requer ADMIN OU (DONO_SALAO E ser o dono do salão com ID #id)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DONO_SALAO') and @salaoSecurityService.isOwner(#id, principal.username))")
    @Operation(summary = "Deleta um salão (Requer ADMIN ou Dono do Salão)")
    @ApiResponse(responseCode = "204", description = "Salão deletado com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Salão não encontrado")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // TODO: Criar um Bean @Service chamado 'salaoSecurityService' com o método isOwner(Long salaoId, String username)
    // que verifica se o usuário (pelo username/email) é o dono do salão especificado.
}


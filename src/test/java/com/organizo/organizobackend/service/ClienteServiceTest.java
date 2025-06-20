package com.organizo.organizobackend.service;

import com.organizo.organizobackend.dto.ClienteDTO;
import com.organizo.organizobackend.exception.ResourceNotFoundException;
import com.organizo.organizobackend.mapper.ClienteMapper;
import com.organizo.organizobackend.model.Cliente;
import com.organizo.organizobackend.repository.ClienteRepository;
import com.organizo.organizobackend.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repo;

    @Mock
    private ClienteMapper mapper;

    @InjectMocks
    private ClienteServiceImpl service;

    @Test
    void criar_devePersistirERetornarDTO() {
        // --- ARRANGE (Preparação com Builder - muito mais limpo!) ---

        // 1. DTO de entrada que chega na requisição
        ClienteDTO dtoIn = ClienteDTO.builder()
                .nome("João")
                .sobrenome("Silva")
                .email("joao@ex.com")
                .telefone("11999998888")
                .build();

        // 2. Entidade que o mapper irá gerar (sem ID)
        Cliente entidade = new Cliente(null, "João", "Silva", "joao@ex.com", "11999998888", "senha_mock", "CLIENTE", null, null);

        // 3. Entidade que o repositório retorna após salvar (com ID e datas)
        Cliente salvo = new Cliente(1L, "João", "Silva", "joao@ex.com", "11999998888", "senha_mock", "CLIENTE", LocalDateTime.now(), LocalDateTime.now());

        // 4. DTO de saída que o mapper irá gerar no final
        ClienteDTO dtoOut = ClienteDTO.builder()
                .id(1L)
                .nome("João")
                .sobrenome("Silva")
                .email("joao@ex.com")
                .telefone("11999998888")
                .criadoEm(salvo.getCriadoEm())
                .atualizadoEm(salvo.getAtualizadoEm())
                .build();


        // --- STUBBING (Simulação do comportamento dos Mocks) ---
        when(mapper.toEntity(any(ClienteDTO.class))).thenReturn(entidade);
        when(repo.save(any(Cliente.class))).thenReturn(salvo);
        when(mapper.toDto(any(Cliente.class))).thenReturn(dtoOut);

        // --- ACT (Ação) ---
        ClienteDTO resp = service.criar(dtoIn);

        // --- ASSERT (Verificação) ---
        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        assertEquals("João", resp.getNome());
        assertEquals("joao@ex.com", resp.getEmail());

        // Verifica se os mocks foram chamados como esperado
        verify(mapper).toEntity(dtoIn);
        verify(repo).save(entidade);
        verify(mapper).toDto(salvo);
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarResourceNotFoundException() {
        // ARRANGE: Simula que o repositório não encontra nada para o ID 42
        when(repo.findById(42L)).thenReturn(Optional.empty());

        // ACT & ASSERT: Verifica se o serviço lança a exceção correta e específica
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(42L));

        // Verifica se o método findById foi chamado exatamente uma vez
        verify(repo, times(1)).findById(42L);
    }
}
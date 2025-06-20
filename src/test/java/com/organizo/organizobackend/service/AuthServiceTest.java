package com.organizo.organizobackend.service;

import com.organizo.organizobackend.model.Cliente;
import com.organizo.organizobackend.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    // Mocks: Versões falsas das dependências do AuthService
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    // Injeta os mocks acima na instância real do AuthService
    @InjectMocks
    private AuthService authService;

    // Dados de teste que usaremos em todos os testes
    private Cliente clienteDeTeste;
    private final String EMAIL_USUARIO = "teste@exemplo.com";
    private final String SENHA_USUARIO = "senha123";
    private final String SENHA_CRIPTOGRAFADA = "senhaCriptografadaXYZ";
    private final String ROLE_USUARIO = "USER";
    private final String TOKEN_FALSO = "token.jwt.falso";

    @BeforeEach
    void setUp() {
        // Antes de cada teste, cria um objeto Cliente para simular um usuário do banco
        clienteDeTeste = new Cliente();
        clienteDeTeste.setId(1L);
        clienteDeTeste.setEmail(EMAIL_USUARIO);
        clienteDeTeste.setSenha(SENHA_CRIPTOGRAFADA);
        clienteDeTeste.setRole(ROLE_USUARIO);
    }

    @Test
    void deveAutenticarComSucessoERetornarToken() {
        // --- ARRANGE (Preparação) ---
        // 1. Simula que o repositório encontra o cliente pelo email
        when(clienteRepository.findByEmail(EMAIL_USUARIO)).thenReturn(Optional.of(clienteDeTeste));
        // 2. Simula que a senha fornecida (não criptografada) bate com a do banco (criptografada)
        when(passwordEncoder.matches(SENHA_USUARIO, SENHA_CRIPTOGRAFADA)).thenReturn(true);
        // 3. Simula que o JwtService gera um token quando pedido
        when(jwtService.generateToken(EMAIL_USUARIO, ROLE_USUARIO)).thenReturn(TOKEN_FALSO);

        // --- ACT (Ação) ---
        // Executa o método que queremos testar
        String tokenGerado = authService.authenticateUserAndGenerateToken(EMAIL_USUARIO, SENHA_USUARIO);

        // --- ASSERT (Verificação) ---
        // Verifica se o token retornado não é nulo e é o mesmo que simulamos
        assertNotNull(tokenGerado);
        assertEquals(TOKEN_FALSO, tokenGerado);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
        // --- ARRANGE ---
        // Simula que o repositório NÃO encontra o cliente pelo email
        when(clienteRepository.findByEmail(EMAIL_USUARIO)).thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        // Verifica se o método lança a exceção BadCredentialsException quando chamado
        assertThrows(BadCredentialsException.class, () -> {
            authService.authenticateUserAndGenerateToken(EMAIL_USUARIO, SENHA_USUARIO);
        });
    }

    @Test
    void deveLancarExcecaoQuandoSenhaEstiverIncorreta() {
        // --- ARRANGE ---
        // 1. Simula que o repositório encontra o cliente
        when(clienteRepository.findByEmail(EMAIL_USUARIO)).thenReturn(Optional.of(clienteDeTeste));
        // 2. Simula que a senha fornecida NÃO bate com a do banco
        when(passwordEncoder.matches(SENHA_USUARIO, SENHA_CRIPTOGRAFADA)).thenReturn(false);

        // --- ACT & ASSERT ---
        // Verifica se o método lança a exceção BadCredentialsException
        assertThrows(BadCredentialsException.class, () -> {
            authService.authenticateUserAndGenerateToken(EMAIL_USUARIO, SENHA_USUARIO);
        });
    }
}
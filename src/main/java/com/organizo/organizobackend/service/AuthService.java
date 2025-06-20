package com.organizo.organizobackend.service;

import com.organizo.organizobackend.model.Cliente;
import com.organizo.organizobackend.repository.ClienteRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String authenticateUserAndGenerateToken(String email, String senha) {
        Optional<Cliente> clienteOptional = clienteRepository.findByEmail(email);

        if (clienteOptional.isEmpty()) {
            throw new BadCredentialsException("Credenciais inválidas: Usuário não encontrado.");
        }

        Cliente cliente = clienteOptional.get();

        if (!passwordEncoder.matches(senha, cliente.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas: Senha incorreta.");
        }

        return jwtService.generateToken(cliente.getEmail(), cliente.getRole());
    }
}
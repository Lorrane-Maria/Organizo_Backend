package com.organizo.organizobackend.service.impl;

import com.organizo.organizobackend.dto.AuthResponse;
import com.organizo.organizobackend.dto.LoginRequest;
import com.organizo.organizobackend.dto.RegistroRequest;
import com.organizo.organizobackend.enums.Role;
import com.organizo.organizobackend.exception.BusinessException;
import com.organizo.organizobackend.exception.ResourceNotFoundException;
import com.organizo.organizobackend.model.Usuario;
import com.organizo.organizobackend.repository.UsuarioRepository;
import com.organizo.organizobackend.service.UsuarioService;
import com.organizo.organizobackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Lógica de negócio para registro e login de usuário.
 * Utiliza exceções customizadas para tratamento de erros.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository repo,
                              BCryptPasswordEncoder encoder,
                              JwtUtil jwtUtil) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse registrar(RegistroRequest dto) {
        // Verifica se o e-mail já está cadastrado
        if (repo.findByEmail(dto.getEmail()).isPresent()) {
            // Lança exceção de negócio específica
            throw new BusinessException("E-mail já cadastrado: " + dto.getEmail());
        }

        // Converte string de role para enum (pode lançar IllegalArgumentException se inválido)
        Role papel;
        try {
            papel = Role.valueOf(dto.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Role inválida: " + dto.getRole() + ". Valores permitidos: CLIENTE, PROFISSIONAL, DONO_SALAO");
        }

        Usuario u = new Usuario(dto.getEmail(),
                encoder.encode(dto.getSenha()),
                dto.getNome(),
                papel);
        Usuario salvo = repo.save(u);
        String token = jwtUtil.gerarToken(salvo.getEmail());
        return new AuthResponse(token,
                salvo.getEmail(),
                salvo.getNome(),
                salvo.getRole().name());
    }

    @Override
    public AuthResponse autenticar(LoginRequest dto) {
        // Busca o usuário pelo e-mail
        Usuario u = repo.findByEmail(dto.getEmail())
                // Lança exceção específica se não encontrar
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "email", dto.getEmail()));

        // Verifica se a senha corresponde
        if (!encoder.matches(dto.getSenha(), u.getSenha())) {
            // Lança exceção de negócio específica
            throw new BusinessException("Senha inválida.");
        }
        String token = jwtUtil.gerarToken(u.getEmail());
        return new AuthResponse(token,
                u.getEmail(),
                u.getNome(),
                u.getRole().name());
    }
}
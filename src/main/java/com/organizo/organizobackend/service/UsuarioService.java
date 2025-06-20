package com.organizo.organizobackend.service;

import com.organizo.organizobackend.dto.AuthResponse;
import com.organizo.organizobackend.dto.LoginRequest;
import com.organizo.organizobackend.dto.RegistroRequest;

/**
 * Contrato para registro e autenticação de usuários.
 */
public interface UsuarioService {

    /**
     * Registra um novo usuário e retorna JWT.
     */
    AuthResponse registrar(RegistroRequest dto);

    /**
     * Autentica usuário existente e retorna JWT.
     */
    AuthResponse autenticar(LoginRequest dto);
}

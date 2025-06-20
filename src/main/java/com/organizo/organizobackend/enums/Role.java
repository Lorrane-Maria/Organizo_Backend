package com.organizo.organizobackend.enums;

/**
 * Define os papéis (roles) possíveis para um usuário no sistema.
 */
public enum Role {
    CLIENTE,        // Usuário que agenda serviços
    PROFISSIONAL,   // Usuário que presta serviços
    DONO_SALAO,     // Usuário proprietário de um ou mais salões
    ADMIN           // Superusuário com acesso total ao sistema
}

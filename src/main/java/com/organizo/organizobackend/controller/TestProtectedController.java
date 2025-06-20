package com.organizo.organizobackend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestProtectedController {

    @GetMapping("/hello")
    public String hello() {
        return "Olá! Você acessou um endpoint protegido com sucesso!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Bem-vindo, Administrador! Este é um recurso restrito.";
    }
}
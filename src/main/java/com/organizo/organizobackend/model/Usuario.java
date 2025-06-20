package com.organizo.organizobackend.model;

import com.organizo.organizobackend.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa um usuário do sistema (cliente ou dono de salão).
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha; // Será armazenada hashed (BCrypt)

    @Column(nullable = false, length = 50)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;                // Novo campo de papel

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    // ===========================================================
    // NOVO: um usuário (quando for DONO_SALAO) pode ter vários salões
    // ===========================================================
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Salao> saloes = new HashSet<>();

    /** Construtor padrão exigido pelo JPA */
    public Usuario() { }

    /** Construtor de conveniência */
    public Usuario(String email, String senha, String nome, Role role) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.role  = role;
    }

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // ===== Novo getter para salões =====
    public Set<Salao> getSaloes() {
        return saloes;
    }
    // ===== Hook JPA =====

    @PrePersist
    protected void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}

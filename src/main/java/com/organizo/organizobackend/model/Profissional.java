package com.organizo.organizobackend.model;

import com.organizo.organizobackend.enums.CargoProfissional;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa um profissional que trabalha em um salão e oferece serviços.
 */
@Entity
@Table(name = "profissional")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 50)
    private String sobrenome;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String telefone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "salao_id", nullable = false)
    private Salao salao;

    // víncula cargos e serviços oferecidos
    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private CargoProfissional cargo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "profissional_servico",
            joinColumns = @JoinColumn(name = "profissional_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private Set<Servico> servicos = new HashSet<>();

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    /** Construtor padrão exigido pelo JPA */
    public Profissional() { }

    // ===================== GETTERS e SETTERS =====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Salao getSalao() {
        return salao;
    }
    public void setSalao(Salao salao) {
        this.salao = salao;
    }

    public Set<Servico> getServicos() {
        return servicos;
    }
    public void setServicos(Set<Servico> servicos) {
        this.servicos = servicos;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public CargoProfissional getCargo() {
        return cargo;
    }

    public void setCargo(CargoProfissional cargo) {
        this.cargo = cargo;
    }

    // ===================== HOOKS DE PERSISTÊNCIA =====================

    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.criadoEm = now;
        this.atualizadoEm = now;
    }

    @PreUpdate
    protected void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}

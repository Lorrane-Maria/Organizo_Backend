package com.organizo.organizobackend.repository;

import com.organizo.organizobackend.model.Salao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para Salao.
 */
@Repository
public interface SalaoRepository extends JpaRepository<Salao, Long> {
    // Aqui você pode adicionar consultas customizadas, ex:
    // List<Salao> findByNomeContainingIgnoreCase(String nome);
}

package com.organizo.organizobackend.repository;

import com.organizo.organizobackend.model.Servico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Page<Servico> findBySalaoId(Long salaoId, Pageable pageable);
}
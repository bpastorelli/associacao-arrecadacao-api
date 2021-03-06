package com.associacao.arrecadacao.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Lancamento;

@Repository
@Transactional(readOnly = true)
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	List<Lancamento> findByResidenciaId(Long residenciaId);
	
	Optional<Lancamento> findByPeriodoAndResidenciaId(String periodo, Long residenciaId);
	
	Page<Lancamento> findByResidenciaId(Long residenciaId, Pageable pageable);
}
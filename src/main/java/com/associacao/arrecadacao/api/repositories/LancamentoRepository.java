package com.associacao.arrecadacao.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Lancamento;

@Transactional(readOnly = true)
@NamedQueries({
		@NamedQuery(name = "LancamentoRepository.findByMoradorId", 
				query = "SELECT lanc FROM Lancamento lanc WHERE lanc.morador_id = :moradorId") })

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	List<Lancamento> findByMoradorId(@Param("moradorId") Long moradorId);

	Page<Lancamento> findByMoradorId(@Param("moradorId") Long moradorId, Pageable pageable);
	
	Page<List<Lancamento>> findByResidenciaId(@Param("residenciaId") Long residenciaId, Pageable pageable);
}

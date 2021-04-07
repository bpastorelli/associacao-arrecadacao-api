package com.associacao.arrecadacao.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.VinculoVeiculo;

@Transactional(readOnly = true)
public interface VinculoVeiculoRepository extends JpaRepository<VinculoVeiculo, Long> {
	
	@Transactional(readOnly = true)
	Optional<VinculoVeiculo> findById(Long id);
	
	VinculoVeiculo findByVisitanteIdAndVeiculoId(Long visitanteId, Long veiculoId);
	
	List<VinculoVeiculo> findByVisitanteId(Long visitanteId);
	
	List<VinculoVeiculo> findByVeiculoId(Long veiculoId);

}

package com.associacao.arrecadacao.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.VinculoResidencia;

@Repository
public interface VinculoResidenciaRepository extends JpaRepository<VinculoResidencia, Long>  {
	
	@Transactional(readOnly = true)
	Optional<VinculoResidencia> findById(Long id);
	
	VinculoResidencia findByResidenciaIdAndMoradorId(Long residenciaId, Long moradorId);
	
	VinculoResidencia findByResidenciaIdOrMoradorId(Long residenciaId, Long moradorId);
	
	List<VinculoResidencia> findByMoradorId(Long moradorId);
	
	List<VinculoResidencia> findByResidenciaId(Long residenciaId);

}

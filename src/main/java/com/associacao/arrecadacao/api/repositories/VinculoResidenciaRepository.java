package com.associacao.arrecadacao.api.repositories;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.VinculoResidencia;

public interface VinculoResidenciaRepository extends JpaRepository<VinculoResidencia, Long>  {
	
	@Transactional(readOnly = true)
	Optional<VinculoResidencia> findById(Long id);
	
	VinculoResidencia findByResidenciaIdAndMoradorId(Long residenciaId, Long moradorId);
	
	VinculoResidencia findByMoradorId(Long moradorId);

}

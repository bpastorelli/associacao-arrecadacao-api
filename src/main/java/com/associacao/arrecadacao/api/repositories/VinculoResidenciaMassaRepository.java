package com.associacao.arrecadacao.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.VinculoResidenciaMassa;

@Repository
public interface VinculoResidenciaMassaRepository extends JpaRepository<VinculoResidenciaMassa, Long>  {
	
	@Transactional(readOnly = true)
	Optional<VinculoResidenciaMassa> findById(Long id);
	
	VinculoResidenciaMassa findByResidenciaIdAndMoradorId(Long residenciaId, Long moradorId);
	
	List<VinculoResidenciaMassa> findByMoradorId(Long moradorId);
	
	List<VinculoResidenciaMassa> findByResidenciaId(Long residenciaId);

}

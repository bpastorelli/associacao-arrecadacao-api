package com.associacao.arrecadacao.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Residencia;

public interface ResidenciaRepository extends JpaRepository<Residencia, Long> {
	
	@Transactional(readOnly = true)
	Optional<Residencia> findById(Long id);
	
	Residencia findByMatricula(String Matricula);

}

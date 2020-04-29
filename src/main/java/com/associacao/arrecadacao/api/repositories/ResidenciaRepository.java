package com.associacao.arrecadacao.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Residencia;

public interface ResidenciaRepository extends JpaRepository<Residencia, Long> {
	
	@Transactional(readOnly = true)
	Residencia findById(Long id);
	
	Residencia findByMatricula(String Matricula);
	
	Residencia findByIdOrMatricula(Long id, String matricula);
	
	Residencia findByEnderecoAndNumero(String endereco, String numero);

}

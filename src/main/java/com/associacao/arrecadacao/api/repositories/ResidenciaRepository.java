package com.associacao.arrecadacao.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Residencia;

public interface ResidenciaRepository extends JpaRepository<Residencia, Long> {
	
	@Transactional(readOnly = true)
	Residencia findById(Long id);
	
	@Transactional(readOnly = true)
	Page<Residencia> findByIdOrMatriculaOrEnderecoContainsOrNumero(Long id, String matricula, String endereco, String numero, Pageable pageable);
	
	Residencia findByMatricula(String Matricula);
	
	Page<Residencia> findByMatricula(String Matricula, Pageable pageable);
	
	Residencia findByIdOrMatricula(Long id, String matricula);
	
	Residencia findByEnderecoAndNumero(String endereco, String numero);

}

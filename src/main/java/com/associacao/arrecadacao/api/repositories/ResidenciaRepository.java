package com.associacao.arrecadacao.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Residencia;

@Repository
public interface ResidenciaRepository extends JpaRepository<Residencia, Long> {
	
	@Transactional(readOnly = true)
	Residencia findById(Long id);
	
	@Transactional(readOnly = true)
	Page<Residencia> findByIdOrEnderecoContainsOrNumero(Long id, String endereco, Long numero, Pageable pageable);
	
	Residencia findByEnderecoAndNumero(String endereco, Long numero);
	
	Residencia findByCepAndNumero(String cep, Long numero);

}

package com.associacao.arrecadacao.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Morador;

@Transactional(readOnly = true)
public interface MoradorRepository extends JpaRepository<Morador, Long> {

	Optional<Morador> findById(Long id);
	
	Morador findByCpf(String cpf);
	
	Morador findByEmail(String email);
	
	Morador findByCpfOrEmail(String cpf, String email);
}

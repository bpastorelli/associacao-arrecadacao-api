package com.associacao.arrecadacao.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Morador;

@Transactional(readOnly = true)
public interface MoradorRepository extends JpaRepository<Morador, Long> {
	
	Morador findByCpf(String cpf);
	
	Morador findByRg(String rg);
	
	List<Morador> findByEmail(String email);
	
	Morador findByCpfOrEmail(String cpf, String email);
}

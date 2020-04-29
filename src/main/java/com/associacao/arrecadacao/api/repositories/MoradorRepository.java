package com.associacao.arrecadacao.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Morador;

@Transactional(readOnly = true)
public interface MoradorRepository extends JpaRepository<Morador, Long> {
	
	Morador findByNome(String nome);
	
	Morador findByCpf(String cpf);
	
	Morador findByRg(String rg);
	
	Morador findByEmail(String email);
	
	Morador findByCpfOrEmail(String cpf, String email);
}

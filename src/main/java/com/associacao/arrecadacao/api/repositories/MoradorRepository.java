package com.associacao.arrecadacao.api.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Morador;

@Repository
@Transactional(readOnly = true)
public interface MoradorRepository extends JpaRepository<Morador, Long> {
	
	Morador findByNome(String nome);
	
	@Transactional(readOnly = true)
	Page<Morador> findByIdOrCpfOrRgOrNomeContainsOrEmail(Long id, String cpf, String rg, String nome, String email, Pageable pageable);
	
	List<Morador> findById(Long id);
	
	Morador findByCpf(String cpf);
	
	Morador findByRg(String rg);
	
	Morador findByEmail(String email);
	
	Morador findByCpfOrEmail(String cpf, String email);
	
	
}

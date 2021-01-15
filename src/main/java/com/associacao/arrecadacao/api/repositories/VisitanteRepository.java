package com.associacao.arrecadacao.api.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Visitante;

@Transactional(readOnly = true)
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
	
	
	Optional<Visitante> findById(Long id);
	
	Optional<Visitante> findByNome(String nome);
	
	Optional<Visitante> findByCpf(String cpf);
	
	Optional<Visitante> findByRg(String rg);
	
	@Transactional(readOnly = true)
	Page<Visitante> findByIdOrNomeContainsOrCpfOrRg(Long id, String nome, String cpf, String rg, Pageable pageable);

}

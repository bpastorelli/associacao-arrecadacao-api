package com.associacao.arrecadacao.api.access.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.access.entities.Funcionalidade;

@Repository
@Transactional(readOnly = true)
public interface FuncionalidadeRepository extends JpaRepository<Funcionalidade, Long> {
	
	Optional<Funcionalidade> findById(Long id);
	
	List<Funcionalidade> findByDescricao(String descricao);

}

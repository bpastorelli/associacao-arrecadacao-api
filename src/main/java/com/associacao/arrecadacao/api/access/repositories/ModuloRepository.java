package com.associacao.arrecadacao.api.access.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.access.entities.Modulo;

@Repository
@Transactional(readOnly = true)
public interface ModuloRepository extends JpaRepository<Modulo, Long> {
	
	Optional<Modulo> findById(Long id);
	
	Optional<Modulo> findByDescricao(String descricao);
	
	Page<Modulo> findByDescricao(String descricao, Pageable pageable);
	
	Page<Modulo> findAll(Pageable pageable);

}

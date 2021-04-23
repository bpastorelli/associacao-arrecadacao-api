package com.associacao.arrecadacao.api.access.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.access.entities.Acesso;

@Repository
@Transactional(readOnly = true)
public interface AcessoRepository extends JpaRepository<Acesso, Long> {
	
	List<Acesso> findByIdUsuario(Long idUsuario);
	
	Page<List<Acesso>> findByIdUsuario(Long idUsuario, Pageable pageable);
	
	Optional<Acesso> findById(Long id);

}

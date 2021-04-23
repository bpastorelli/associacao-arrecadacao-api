package com.associacao.arrecadacao.api.access.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.access.entities.Acesso;

@Repository
@Transactional(readOnly = true)
public interface AcessoRepository extends JpaRepository<Acesso, Long> {
	
	List<AcessoRepository> findByIdUsuario(Long idUsuario);
	
	Optional<Acesso> findById(Long id);

}

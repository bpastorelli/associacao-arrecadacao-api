package com.associacao.arrecadacao.api.access.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.access.entities.AcessoModulo;

@Repository
@Transactional(readOnly = true)
public interface AcessoModuloRepository extends JpaRepository<AcessoModulo, Long> {
	
	List<AcessoModulo> findByIdUsuario(Long idUsuario);
	
	Page<AcessoModulo> findByIdUsuario(Long idUsuario, Pageable pageable);
	
	Optional<AcessoModulo> findById(Long id);
	
	Optional<AcessoModulo> findByIdUsuarioAndIdModulo(Long idUsuario, Long idModulo);

}

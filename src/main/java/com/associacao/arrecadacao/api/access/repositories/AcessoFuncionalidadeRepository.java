package com.associacao.arrecadacao.api.access.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.access.entities.AcessoFuncionalidade;

@Repository
@Transactional(readOnly = true)
public interface AcessoFuncionalidadeRepository extends JpaRepository<AcessoFuncionalidade, Long> {
	
	List<AcessoFuncionalidade> findByIdUsuario(Long idUsuario);
	
	Page<AcessoFuncionalidade> findByIdUsuario(Long idUsuario, Pageable pageable);
	
	Optional<AcessoFuncionalidade> findById(Long id);
	
	Optional<AcessoFuncionalidade> findByIdUsuarioAndIdModuloAndIdFuncionalidade(Long idUsuario, Long idModulo, Long idFuncionalidade);

}

package com.associacao.arrecadacao.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Visita;

@Transactional(readOnly = true)
@NamedQueries({
		@NamedQuery(name = "VisitaRepository.findByPosicaoAndVisitanteRgAndVisitanteCpf", 
					query = "SELECT a FROM Visitante a "
							+ "WHERE a.visita.posicao = :posicao "
							+ "AND a.rg = :rg "
							+ "AND a.cpf = :cpf ")})
		
public interface VisitaRepository extends JpaRepository<Visita, Long> {
	
	Visita findById(Long id);
	
	List<Visita> findByPosicaoAndVisitanteRgAndVisitanteCpf(@Param("posicao") Integer posicao, @Param("rg") String rg, @Param("cpf") String cpf);
	
	@Transactional(readOnly = true)
	Page<Visita> findByPosicaoOrVisitanteRgOrVisitanteCpf(@Param("posicao") Integer posicao, @Param("rg") String rg, @Param("cpf") String cpf, Pageable pageable);
	
	@Transactional(readOnly = true)
	Page<Visita> findAll(Pageable pageable);

}

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
		@NamedQuery(name = "VisitaRepository.findByVisitanteRg", 
					query = "SELECT a FROM Visitante a WHERE a.rg = :rg")})

public interface VisitaRepository extends JpaRepository<Visita, Long> {
	
	List<Visita> findByVisitanteRg(@Param("rg") String rg);
	
	Page<Visita> findByVisitanteRg(@Param("rg") String rg, Pageable pageable);	

}

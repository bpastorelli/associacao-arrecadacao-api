package com.associacao.arrecadacao.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Residencia;

@Repository
@Transactional(readOnly = true)
@NamedQueries({
		@NamedQuery(name = "ProcessoCadastroRepository.findByResidenciaId", 
				query = "SELECT a FROM Morador a LEFT JOIN a.VinculoResidencia r WHERE r.residenciaId = :residenciaId") })
public interface ProcessoCadastroRepository extends JpaRepository<Residencia, Long> {

	List<Residencia> findById(Long residenciaId);
	
}

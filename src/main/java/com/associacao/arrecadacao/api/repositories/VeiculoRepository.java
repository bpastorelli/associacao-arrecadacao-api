package com.associacao.arrecadacao.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.associacao.arrecadacao.api.entities.Veiculo;

@Repository
@Transactional(readOnly = true)
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
	
	Veiculo findById(Long id);
	
	@Transactional(readOnly = true)
	Page<Veiculo> findByIdOrPlacaOrMarcaOrModelo(Long id, String placa, String marca, String modelo, Pageable pageable);
	
	Veiculo findByPlaca(String placa);

}

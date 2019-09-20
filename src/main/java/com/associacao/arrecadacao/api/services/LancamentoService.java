package com.associacao.arrecadacao.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.associacao.arrecadacao.api.entities.Lancamento;

public interface LancamentoService {
	
	/**
	 * Retorna um lancamento por ID da residencia.
	 * 
	 * @param residenciaId
	 * @param pageRequest
	 * @return Page<List<Lancamento>>
	 */
	Page<Lancamento> buscarPorResidenciaId(Long residenciaId, PageRequest pageRequest);
	
	/**
	 * Retorna um lancamento por ID da residencia.
	 * 
	 * @param residenciaId
	 * @return Page<Lancamento>
	 */
	List<Lancamento> buscarPorResidenciaId(Long residenciaId);
	
	/**
	 * Retorna um lancamento por ID.
	 * 
	 * @param id
	 * @return Optional<Lancamento>
	 */
	Optional<Lancamento> buscarPorId(Long id);
	
	/**
	 * Retorna um lancamento por Periodo.
	 * 
	 * @param periodo
	 * @return Optional<Lancamento>
	 */	
	Optional<Lancamento> buscarPorPeriodo(String periodo);
	
	/**
	 * Persiste um lancamento na base de dados.
	 * 
	 * @param lancamento
	 * @return Lancamento
	 */
	List<Lancamento> persistir(List<Lancamento> lancamento);
	
	/**
	 * Remove um lancamento da base de dados.
	 * 
	 * @param id
	 */
	void remover(Long id);
	
}
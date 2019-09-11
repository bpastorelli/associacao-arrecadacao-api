package com.associacao.arrecadacao.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.associacao.arrecadacao.api.entities.Lancamento;

public interface LancamentoService {

	/**
	 * Retorna um lancamento por ID do morador.
	 * 
	 * @param moradorId
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	Page<Lancamento> buscarPorMoradorId(Long moradorId, PageRequest pageRequest);
	
	/**
	 * Retorna um lancamento por ID.
	 * 
	 * @param id
	 * @return Optional<Lancamento>
	 */
	Optional<Lancamento> buscarPorId(Long id);
	
	/**
	 * Persiste um lancamento na base de dados.
	 * 
	 * @param lancamento
	 * @return Lancamento
	 */
	List<Lancamento> persistir(List<Lancamento> lancamentos);
	
	/**
	 * Retorna todos os lancamentos de uma residência
	 * 
	 * @param residenciaId
	 * @return
	 */
	Page<List<Lancamento>> buscarPorResidenciaId(Long residenciaId, PageRequest pageRequest);
	
	/**
	 * Remove um lancamento da base de dados.
	 * 
	 * @param id
	 */
	void remover(Long id);
	
}

package com.associacao.arrecadacao.api.access.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.associacao.arrecadacao.api.access.entities.Funcionalidade;

public interface FuncionalidadeService {
	
	
	/**
	 * Persistir funcionalidades
	 * 
	 * @param funcionalidades
	 * @return List<Funcionalidade>
	 */
	List<Funcionalidade> persistir(List<Funcionalidade> funcionalidades);
	
	/**
	 * Busca todos os registros de funcionalidade
	 * 
	 * @param pageRequest
	 * @return Page<Funcionalidade>
	 */
	Page<Funcionalidade> buscarTodos(PageRequest pageRequest);
	
	/**
	 * Busca funcionalidade por descrição
	 * 
	 * @param descricao
	 * @param pageRequest
	 * @return Page<Funcionalidade>
	 */
	Page<Funcionalidade> buscarPorDescricao(String descricao, PageRequest pageRequest);
	
	/**
	 * Busca uma funcionalidade por id
	 * 
	 * @param id
	 * @return Optional<Funcionalidade>
	 */
	Optional<Funcionalidade> buscarPorId(Long id);

}

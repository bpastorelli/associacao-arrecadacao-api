package com.associacao.arrecadacao.api.access.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.associacao.arrecadacao.api.access.entities.Modulo;

public interface ModuloService {
	
	
	/**
	 * Incluir módulos
	 * 
	 * @param modulos
	 * @return List<Modulo>
	 */
	List<Modulo> persistir(List<Modulo> modulos);
	
	/**
	 * Incluir um modulo
	 * 
	 * @param modulo
	 * @return Optional<Modulo>
	 */
	Optional<Modulo> persistir(Modulo modulo);
	
	/**
	 * Busca todos os registros de modulo
	 * 
	 * @param pageRequest
	 * @return Page<Modulo>
	 */
	Page<Modulo> buscarTodos(PageRequest pageRequest);
	
	/**
	 * Buscar por descrição
	 * 
	 * @param descricao
	 * @param pageRequest
	 * @return Page<Modulo>
	 */
	Page<Modulo> buscarPorDescricao(String descricao, PageRequest pageRequest);
	
	/**
	 * Buscar por descrição
	 * 
	 * @param descricao
	 * @return Optional<Modulo>
	 */
	Optional<Modulo> buscarPorDescricao(String descricao);
	
	/**
	 * Buscar modulo por id
	 * 
	 * @param id
	 * @return Optional<Modulo>
	 */
	Optional<Modulo> buscarPorId(Long id);

}

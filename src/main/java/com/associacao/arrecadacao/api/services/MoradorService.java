package com.associacao.arrecadacao.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.associacao.arrecadacao.api.entities.Morador;

public interface MoradorService {

	/**
	 * Cadastra um morador na base de dados.
	 * 
	 * @param morador
	 * @return Morador
	 */
	List<Morador> persistir(List<Morador> morador);
	
	/**
	 * Busca e retorna um morador dado o cpf.
	 * 
	 * @param cpf
	 * @return Optional<Morador>
	 */
	Optional<Morador> buscarPorCpf(String cpf);
	
	/**
	 * Busca e retorna um morador dado o nome.
	 * 
	 * @param nome
	 * @return Optional<Morador>
	 */
	Optional<Morador> buscarPorNome(String nome);
	
	/**
	 * Busca e retorna um morador dado o rg.
	 * 
	 * @param rg
	 * @return Optional<Morador>
	 */	
	Optional<Morador> buscarPorRg(String rg);
	
	/**
	 * Busca e retorna um morador dado o email.
	 * 
	 * @param email
	 * @return Optional<Morador>
	 */
	Optional<Morador> buscarPorEmail(String email);

	/**
	 * Busca e retorna um morador dado o id.
	 * 
	 * @param id
	 * @return Optional<Morador>
	 */
	Optional<Morador> buscarPorId(Long id);
	
	/**
	 * Busca todas os moradores Paginado
	 * @param pageRequest
	 * @return Page<Morador>
	 */
	Page<Morador> bucarTodos(PageRequest pageRequest);
	
	/**
	 * Busca moradores
	 * 
	 * @param id
	 * @param cpf
	 * @param rg
	 * @param nome
	 * @param email
	 * @param pageRequest
	 * @return Page<Morador>
	 */
	Page<Morador> buscarPorIdOrCpfOrRgOrNomeOrEmail(Long id, String cpf, String rg, String nome, String email, PageRequest pageRequest);
	
	
}

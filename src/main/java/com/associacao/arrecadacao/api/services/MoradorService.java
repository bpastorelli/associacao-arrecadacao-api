package com.associacao.arrecadacao.api.services;

import java.util.Optional;

import com.associacao.arrecadacao.api.entities.Morador;

public interface MoradorService {

	/**
	 * Cadastra um morador na base de dados.
	 * 
	 * @param morador
	 * @return Morador
	 */
	Morador persistir(Morador morador);
	
	/**
	 * Busca e retorna um morador dado o cpf.
	 * 
	 * @param cpf
	 * @return Optional<Morador>
	 */
	Optional<Morador> buscarPorCpf(String cpf);
	
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
	Optional<Morador> bucarPorEmail(String email);

	/**
	 * Busca e retorna um morador dado o id.
	 * 
	 * @param id
	 * @return Optional<Morador>
	 */
	Optional<Morador> buscarPorId(Long id);
	
}

package com.associacao.arrecadacao.api.services;

import java.util.Optional;

import com.associacao.arrecadacao.api.entities.Residencia;

public interface ResidenciaService {
	
	/**
	 * Retorna uma residência dado uma matricula.
	 * 
	 * @param matricula
	 * @return Optional<Residencia>
	 */
	Optional<Residencia> buscarPorMatricula(String matricula);
	
	/**
	 * Retorna uma residência dado um ID.
	 * 
	 * @param residenciaId
	 * @return Optional<Residencia>
	 */	
	Optional<Residencia> buscarPorId(Long id);
	
	/**
	 * Cadastra uma nova residencia na base de dados.
	 * 
	 * @param residencia
	 * @return Residencia
	 */
	Optional<Residencia> persistir(Residencia residencia);

}

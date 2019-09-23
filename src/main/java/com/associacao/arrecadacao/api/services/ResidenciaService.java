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
	Optional<Residencia> bucarPorMatricula(String matricula);
	
	/**
	 * Retorna uma residência dado um ID.
	 * 
	 * @param residenciaId
	 * @return Optional<Residencia>
	 */	
	Optional<Residencia> buscarPorResidenciaId(Long residenciaId);
	
	/**
	 * Cadastra uma nova residencia na base de dados.
	 * 
	 * @param residencia
	 * @return Residencia
	 */
	Residencia persistir(Residencia residencia);

}

package com.associacao.arrecadacao.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.associacao.arrecadacao.api.entities.Residencia;

public interface ResidenciaService {
	
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
	
	/**
	 * Retorna uma residência dado Endereço e Número.
	 * 
	 * @param endereco
	 * @param numero
	 * @return Optional<Residencia>
	 */	
	Optional<Residencia> bucarPorEnderecoAndNumero(String string, Long numero);
	
	/**
	 * Retorna uma residência por cep e número
	 * 
	 * @param cep
	 * @param numero
	 * @return
	 */
	Optional<Residencia> buscarPorCepAndNumero(String cep, Long numero);
		
	/**
	 * Busca todas as residências Paginado
	 * @param pageRequest
	 * @return Page<Residencia>
	 */
	Page<Residencia> bucarTodos(PageRequest pageRequest);
	

	/**
	 * Busca residencia pelo ID Paginado
	 * @param residenciaId
	 * @param pageRequest
	 * @return Page<Residencia>
	 */
	Page<Residencia> buscarPorIdOrEnderecoOrNumero(Long id, String endereco, Long numero, PageRequest pageRequest);

}

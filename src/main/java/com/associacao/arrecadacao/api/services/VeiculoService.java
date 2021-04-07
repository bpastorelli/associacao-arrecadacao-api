package com.associacao.arrecadacao.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.associacao.arrecadacao.api.entities.Veiculo;

public interface VeiculoService {
	
	
	/**
	 * Retorna um veiculo dado um ID.
	 * 
	 * @param veiculoId
	 * @return Optional<Residencia>
	 */	
	Optional<Veiculo> buscarPorId(Long id);
	
	/**
	 * Cadastra um novo veiculo na base de dados.
	 * 
	 * @param veiculo
	 * @return Veiculo
	 */
	Optional<Veiculo> persistir(Veiculo veiculo);
	
	/**
	 * Retorna um veiculo dado id, placa, marca ou modelo.
	 * 
	 * @param id
	 * @param placa
	 * @param marca
	 * @param modelo
	 * @return Optional<Veiculo>
	 */	
	Page<Veiculo> bucarPorIdAndPlacaAndMarcaAndModelo(Long id, String placa, String marca, String modelo, PageRequest pageRequest);
	
	/**
	 * Retorna um veiculo por placa
	 * 
	 * @param cep
	 * @return Optional<Veiculo>
	 */
	Optional<Veiculo> buscarPorPlaca(String placa);
		
	/**
	 * Busca todos os veiculos Paginado
	 * @param pageRequest
	 * @return Page<Veiculo>
	 */
	Page<Veiculo> bucarTodos(PageRequest pageRequest);

}

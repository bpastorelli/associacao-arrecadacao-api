package com.associacao.arrecadacao.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.associacao.arrecadacao.api.entities.Visitante;

public interface VisitanteService {
	
	
	/**
	 * Cadastra uma lista de visitantes.
	 * 
	 * @param visitantes
	 * @return Lit<Visitante>
	 */
	List<Visitante> persitir (List<Visitante> visitantes);
	
	/**
	 * Cadastra um visitante.
	 * 
	 * @param visitante
	 * @return Visitante
	 */	
	Optional<Visitante> persistir (Visitante visitante);
	
	/**
	 * Busca um visitante por CPF.
	 * 
	 * @param cpf
	 * @return Visitante
	 */
	Optional<Visitante> buscarPorCpf(String cpf);
	
	/**
	 * Busca um visitante por id.
	 * 
	 * @param id
	 * @return Visitante
	 */
	Optional<Visitante> buscarPorId(Long id);
	
	/**
	 * Busca um visitante por RG.
	 * 
	 * @param rg
	 * @return Visitante
	 */
	Optional<Visitante> buscarPorRg(String rg);
	
	/**
	 * Busca um visitante por Nome.
	 * 
	 * @param rg
	 * @return Visitante
	 */
	Optional<Visitante> buscarPorNome(String nome);
	
	/**
	 * Busca um visitante por id ou nome ou cpf ou rg.
	 * 
	 * @param id
	 * @param nome
	 * @param cpf
	 * @param rg
	 * @return Page<Visitante>
	 */	
	Page<Visitante> buscarPorIdOrNomeOrCpfOrRg(Long id, String nome, String cpf, String rg, PageRequest pageRequest);
	
	/**
	 * Busca todos os visitantes
	 * @param pageRequest
	 * @return Page<Visitante>
	 */
	Page<Visitante> buscarTodos(PageRequest pageRequest);

}

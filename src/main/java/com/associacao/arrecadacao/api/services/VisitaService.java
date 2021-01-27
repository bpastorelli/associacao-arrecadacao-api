package com.associacao.arrecadacao.api.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.associacao.arrecadacao.api.entities.Visita;

public interface VisitaService {
	
	/**
	 * Persistir uma visita
	 * @param visita
	 * @return
	 */
	Visita persistir(Visita visita);
	
	/**
	 * Buscar por id
	 * @param id
	 * @return
	 */
	Visita buscarPorId(Long id);
	
	/**
	 * Buscar por RG
	 * @param rg
	 * @return
	 */
	List<Visita> buscarPorPosicaoOrRgOrCpf(Integer posicao, String rg, String cpf, String nome);
	
	/**
	 * Buscar vistas paginado
	 * @param posicao
	 * @param rg
	 * @param cpf
	 * @param pageRequest
	 * @return
	 */
	Page<Visita> buscarPorPosicaoOrRgOrCpf(Integer posicao, String rg, String cpf, String nome, PageRequest pageRequest);
	
	/**
	 * Busca todas as visitas
	 * @param pageRequest
	 * @return
	 */
	Page<Visita> buscarTodos(PageRequest pageRequest);
	
	/**
	 * Busca visita máxima
	 * @param id
	 * @return
	 */
	Visita buscarPorVisitanteIdOrderByDataEntradaDesc(Long id);

}

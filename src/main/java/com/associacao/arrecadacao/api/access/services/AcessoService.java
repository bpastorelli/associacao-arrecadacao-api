package com.associacao.arrecadacao.api.access.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.associacao.arrecadacao.api.access.entities.Acesso;

public interface AcessoService {
	
	/**
	 * Cadastra os vinculos de acessos
	 * 
	 * @param acessos
	 * @return List<Acesso>
	 */
	List<Acesso> persistir(List<Acesso> acessos);
	
	/**
	 * Busca todos os acessos por usuarioId
	 * 
	 * @param usuarioId
	 * @param pageRequest
	 * @return Page<Acesso>
	 */
	Page<Acesso> buscarPorUsuarioId(Long usuarioId, PageRequest pageRequest);
	
	/**
	 * Busca tosos os acessos por usuarioId
	 * 
	 * @param usuarioId
	 * @return List<Acesso>
	 */
	List<Acesso> buscarPorUsuarioId(Long usuarioId);
	
	/**
	 * Busca um registro de acesso por id
	 * 
	 * @param id
	 * @return Optional<Acesso>
	 */
	Optional<Acesso> buscarPorId(Long id);
	

}

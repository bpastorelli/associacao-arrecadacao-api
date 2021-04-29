package com.associacao.arrecadacao.api.access.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.associacao.arrecadacao.api.access.entities.AcessoModulo;

public interface AcessoModuloService {
	
	/**
	 * Cadastra os vinculos de acessos
	 * 
	 * @param acessos
	 * @return List<Acesso>
	 */
	List<AcessoModulo> persistir(List<AcessoModulo> acessos);
	
	/**
	 * Busca todos os acessos por usuarioId
	 * 
	 * @param usuarioId
	 * @param pageRequest
	 * @return Page<Acesso>
	 */
	Page<AcessoModulo> buscarPorUsuarioId(Long usuarioId, PageRequest pageRequest);
	
	/**
	 * Busca todos os acessos por usuarioId
	 * 
	 * @param pageRequest
	 * @return Page<Acesso>
	 */
	Page<AcessoModulo> buscarTodos(PageRequest pageRequest);
	
	/**
	 * Busca tosos os acessos por usuarioId
	 * 
	 * @param usuarioId
	 * @return List<Acesso>
	 */
	List<AcessoModulo> buscarPorUsuarioId(Long usuarioId);
	
	/**
	 * Busca um registro de acesso por id
	 * 
	 * @param id
	 * @return Optional<Acesso>
	 */
	Optional<AcessoModulo> buscarPorId(Long id);
	
	/**
	 * Busca um acesso por id Usuario, id Modulo e id Funcionalidade
	 * 
	 * @param idUsuario
	 * @param idModulo
	 * @param idFuncionalidade
	 * @return Optional<Acesso>
	 */
	Optional<AcessoModulo> buscarPorIdUsuarioAndIdModulo(Long idUsuario, Long idModulo);
	

}

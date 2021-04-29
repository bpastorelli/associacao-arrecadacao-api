package com.associacao.arrecadacao.api.access.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.associacao.arrecadacao.api.access.entities.AcessoFuncionalidade;

public interface AcessoFuncionalidadeService {
	
	/**
	 * Cadastra os vinculos de acessos
	 * 
	 * @param acessos
	 * @return List<Acesso>
	 */
	List<AcessoFuncionalidade> persistir(List<AcessoFuncionalidade> acessos);
	
	/**
	 * Busca todos os acessos por usuarioId
	 * 
	 * @param usuarioId
	 * @param pageRequest
	 * @return Page<Acesso>
	 */
	Page<AcessoFuncionalidade> buscarPorUsuarioId(Long usuarioId, PageRequest pageRequest);
	
	/**
	 * Busca todos os acessos por usuarioId
	 * 
	 * @param pageRequest
	 * @return Page<Acesso>
	 */
	Page<AcessoFuncionalidade> buscarTodos(PageRequest pageRequest);
	
	/**
	 * Busca tosos os acessos por usuarioId
	 * 
	 * @param usuarioId
	 * @return List<Acesso>
	 */
	List<AcessoFuncionalidade> buscarPorUsuarioId(Long usuarioId);
	
	/**
	 * Busca um registro de acesso por id
	 * 
	 * @param id
	 * @return Optional<Acesso>
	 */
	Optional<AcessoFuncionalidade> buscarPorId(Long id);
	
	/**
	 * Busca um acesso por id Usuario, id Modulo e id Funcionalidade
	 * 
	 * @param idUsuario
	 * @param idModulo
	 * @param idFuncionalidade
	 * @return Optional<Acesso>
	 */
	Optional<AcessoFuncionalidade> buscarPorIdUsuarioAndIdModuloAndIdFuncionalidade(Long idUsuario, Long idModulo, Long idFuncionalidade);
	

}

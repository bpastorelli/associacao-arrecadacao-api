package com.associacao.arrecadacao.api.access.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.access.entities.AcessoFuncionalidade;
import com.associacao.arrecadacao.api.access.repositories.AcessoRepository;
import com.associacao.arrecadacao.api.access.services.AcessoFuncionalidadeService;

@Service
public class AcessoFuncionalidadeServiceImpl implements AcessoFuncionalidadeService {
	
	private static final Logger log = LoggerFactory.getLogger(AcessoFuncionalidadeServiceImpl.class);
	
	@Autowired
	private AcessoRepository acessoRepository;

	@Override
	public List<AcessoFuncionalidade> persistir(List<AcessoFuncionalidade> acessos) {
		log.info("Persistindo acessos");
		return this.acessoRepository.save(acessos);
	}

	@Override
	public Page<AcessoFuncionalidade> buscarPorUsuarioId(Long usuarioId, PageRequest pageRequest) {
		log.info("Buscando acessos por usuarioId", usuarioId);
		return this.acessoRepository.findByIdUsuario(usuarioId, pageRequest);
	}

	@Override
	public List<AcessoFuncionalidade> buscarPorUsuarioId(Long usuarioId) {
		log.info("Buscando acessos por usuarioId", usuarioId);
		return this.acessoRepository.findByIdUsuario(usuarioId);
	}

	@Override
	public Optional<AcessoFuncionalidade> buscarPorId(Long id) {
		log.info("Buscando acesso por id", id);
		return this.acessoRepository.findById(id);
	}

	@Override
	public Page<AcessoFuncionalidade> buscarTodos(PageRequest pageRequest) {
		log.info("Buscando todos os acessos");
		return this.acessoRepository.findAll(pageRequest);
	}

	@Override
	public Optional<AcessoFuncionalidade> buscarPorIdUsuarioAndIdModuloAndIdFuncionalidade(Long idUsuario, Long idModulo,
			Long idFuncionalidade) {
		log.info("Buscando acesso por id usuario, id módulo e id funcionalidade");
		return this.acessoRepository.findByIdUsuarioAndIdModuloAndIdFuncionalidade(idUsuario, idModulo, idFuncionalidade);
	}

}

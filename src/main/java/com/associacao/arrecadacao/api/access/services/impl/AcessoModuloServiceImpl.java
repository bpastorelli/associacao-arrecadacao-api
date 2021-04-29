package com.associacao.arrecadacao.api.access.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.access.entities.AcessoModulo;
import com.associacao.arrecadacao.api.access.repositories.AcessoModuloRepository;
import com.associacao.arrecadacao.api.access.services.AcessoModuloService;

@Service
public class AcessoModuloServiceImpl implements AcessoModuloService {
	
	private static final Logger log = LoggerFactory.getLogger(AcessoModuloServiceImpl.class);
	
	@Autowired
	private AcessoModuloRepository acessoModuloRepository;

	@Override
	public List<AcessoModulo> persistir(List<AcessoModulo> acessos) {
		log.info("Persistindo acessos");
		return this.acessoModuloRepository.save(acessos);
	}

	@Override
	public Page<AcessoModulo> buscarPorUsuarioId(Long usuarioId, PageRequest pageRequest) {
		log.info("Buscando acessos por usuarioId", usuarioId);
		return this.acessoModuloRepository.findByIdUsuario(usuarioId, pageRequest);
	}

	@Override
	public List<AcessoModulo> buscarPorUsuarioId(Long usuarioId) {
		log.info("Buscando acessos por usuarioId", usuarioId);
		return this.acessoModuloRepository.findByIdUsuario(usuarioId);
	}

	@Override
	public Optional<AcessoModulo> buscarPorId(Long id) {
		log.info("Buscando acesso por id", id);
		return this.acessoModuloRepository.findById(id);
	}

	@Override
	public Page<AcessoModulo> buscarTodos(PageRequest pageRequest) {
		log.info("Buscando todos os acessos");
		return this.acessoModuloRepository.findAll(pageRequest);
	}

	@Override
	public Optional<AcessoModulo> buscarPorIdUsuarioAndIdModulo(Long idUsuario, Long idModulo) {
		log.info("Buscando acesso por id usuario, id módulo e id funcionalidade");
		return this.acessoModuloRepository.findByIdUsuarioAndIdModulo(idUsuario, idModulo);
	}

}

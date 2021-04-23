package com.associacao.arrecadacao.api.access.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.access.entities.Funcionalidade;
import com.associacao.arrecadacao.api.access.repositories.FuncionalidadeRepository;
import com.associacao.arrecadacao.api.access.services.FuncionalidadeService;

@Service
public class FuncionalidadeServiceImpl implements FuncionalidadeService  {
	
	private static final Logger log = LoggerFactory.getLogger(FuncionalidadeServiceImpl.class);
	
	@Autowired
	private FuncionalidadeRepository funcionalidadeRepository;

	@Override
	public List<Funcionalidade> persistir(List<Funcionalidade> funcionalidades) {
		log.info("Persistindo funcionalidades");
		return this.funcionalidadeRepository.save(funcionalidades);
	}

	@Override
	public Page<Funcionalidade> buscarTodos(PageRequest pageRequest) {
		log.info("Buscando funcionalidades");
		return this.funcionalidadeRepository.findAll(pageRequest);
	}

	@Override
	public Page<Funcionalidade> buscarPorDescricao(String descricao, PageRequest pageRequest) {
		log.info("Buscando funcionalidade por descrição", descricao);
		return this.funcionalidadeRepository.findByDescricao(descricao, pageRequest);
	}

	@Override
	public Optional<Funcionalidade> buscarPorId(Long id) {
		log.info("Bucando funcionalidade por id", id);
		return this.funcionalidadeRepository.findById(id);
	}

}

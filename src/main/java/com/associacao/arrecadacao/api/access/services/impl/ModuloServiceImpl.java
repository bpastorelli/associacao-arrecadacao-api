package com.associacao.arrecadacao.api.access.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.access.entities.Modulo;
import com.associacao.arrecadacao.api.access.repositories.ModuloRepository;
import com.associacao.arrecadacao.api.access.services.ModuloService;

@Service
public class ModuloServiceImpl implements ModuloService {
	
	private static final Logger log = LoggerFactory.getLogger(ModuloServiceImpl.class);
	
	@Autowired
	private ModuloRepository moduloRepository;

	@Override
	public List<Modulo> persistir(List<Modulo> modulos) {
		log.info("Persistindo módulos");
		return this.moduloRepository.save(modulos);
	}

	@Override
	public Page<Modulo> buscarTodos(PageRequest pageRequest) {
		log.info("Buscando todos os módulos");
		return this.moduloRepository.findAll(pageRequest);
	}

	@Override
	public Page<Modulo> buscarPorDescricao(String descricao, PageRequest pageRequest) {
		log.info("Buscando módulo por descrição", descricao);
		return this.moduloRepository.findByDescricao(descricao, pageRequest);
	}

	@Override
	public Optional<Modulo> buscarPorId(Long id) {
		log.info("Buscando módulo por id", id);
		return this.moduloRepository.findById(id);
	}

	@Override
	public Optional<Modulo> buscarPorDescricao(String descricao) {
		log.info("Buscando módulo por descrição", descricao);
		return this.moduloRepository.findByDescricao(descricao);
	}
	
	

}

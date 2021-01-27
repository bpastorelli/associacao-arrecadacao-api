package com.associacao.arrecadacao.api.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.entities.Visita;
import com.associacao.arrecadacao.api.repositories.VisitaRepository;
import com.associacao.arrecadacao.api.services.VisitaService;

@Service
public class VisitaServiceImpl implements VisitaService {

	private static final Logger log = LoggerFactory.getLogger(VisitanteServiceImpl.class);
	
	@Autowired
	private VisitaRepository visitaRepository;
	
	@Override
	public Visita persistir(Visita visita) {
		log.info("Persistindo visita...");
		return visitaRepository.save(visita);
	}

	@Override
	public List<Visita> buscarPorPosicaoOrRgOrCpf(Integer posicao, String rg, String cpf) {
		log.info("Buscando visitas por RG...");
		return visitaRepository.findByPosicaoAndVisitanteRgAndVisitanteCpf(posicao, rg, cpf);
	}

	@Override
	public Visita buscarPorId(Long id) {
		log.info("Buscando visitas por id...");
		return visitaRepository.findById(id);
	}

	@Override
	public Page<Visita> buscarPorPosicaoOrRgOrCpf(Integer posicao, String rg, String cpf, PageRequest pageRequest) {
		log.info("Buscando visitas paginado...");
		return visitaRepository.findByPosicaoOrVisitanteRgOrVisitanteCpf(posicao, rg, cpf, pageRequest);
	}

	@Override
	public Page<Visita> buscarTodos(PageRequest pageRequest) {
		log.info("Buscando visitas paginado...");
		return visitaRepository.findAll(pageRequest);
	}

	@Override
	public Visita buscarPorVisitanteIdOrderByDataEntradaDesc(Long id) {
		log.info("Buscando visita máxima...");
		return visitaRepository.findFirstByVisitanteIdOrderByDataEntradaDesc(id);
	}

}

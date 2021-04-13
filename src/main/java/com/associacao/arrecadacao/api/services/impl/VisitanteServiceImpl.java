package com.associacao.arrecadacao.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.entities.Visitante;
import com.associacao.arrecadacao.api.repositories.VisitanteRepository;
import com.associacao.arrecadacao.api.services.VisitanteService;

@Service
public class VisitanteServiceImpl implements VisitanteService {
	
	private static final Logger log = LoggerFactory.getLogger(VisitanteServiceImpl.class);
	
	@Autowired
	private VisitanteRepository visitanteRepository;
	
	
	@CachePut(value="visitante")	
	public List<Visitante> persitir(List<Visitante> visitantes) {
		log.info("Persistir visitantes {}", visitantes);
		return visitanteRepository.save(visitantes);
	}

	@CachePut(value ="visitante")
	public Optional<Visitante> persistir(Visitante visitante) {
		log.info("Persistir visitante {}", visitante);
		return Optional.ofNullable(visitanteRepository.save(visitante));
	}

	@Override
	public Optional<Visitante> buscarPorCpf(String cpf) {
		log.info("Buscar por CPF {}", cpf);
		return visitanteRepository.findByCpf(cpf);
	}

	@Override
	public Optional<Visitante> buscarPorRg(String rg) {
		log.info("Buscar por RG {}", rg);
		return visitanteRepository.findByRg(rg);
	}

	@Override
	public Page<Visitante> buscarPorIdOrNomeOrCpfOrRg(Long id, String nome, String cpf, String rg,
			PageRequest pageRequest) {
		log.info("Buscar por id ou nome ou cpf ou rg {}", id, nome, cpf, rg);
		return visitanteRepository.findByIdOrNomeContainsOrCpfOrRg(id, nome, cpf, rg, pageRequest);
	}

	@Cacheable("visitante")
	public Page<Visitante> buscarTodos(PageRequest pageRequest) {
		log.info("Buscar todos os visitantes");
		return visitanteRepository.findAll(pageRequest); 	
	}

	@Override
	public Optional<Visitante> buscarPorNome(String nome) {
		log.info("Buscar por nome {}", nome);
		return visitanteRepository.findByNome(nome);
	}

	@Override
	public Optional<Visitante> buscarPorId(Long id) {
		log.info("Buscar por id {}", id);
		return visitanteRepository.findById(id);
	}

	@Override
	public Optional<Visitante> buscarPorRgOrCpf(String rg, String cpf) {
		log.info("Buscar por RG ou CPF {}", rg , cpf);
		return visitanteRepository.findByRgOrCpf(rg, cpf);
	}

}

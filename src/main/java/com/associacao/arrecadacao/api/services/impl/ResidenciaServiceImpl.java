package com.associacao.arrecadacao.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.repositories.ResidenciaRepository;
import com.associacao.arrecadacao.api.services.ResidenciaService;

@Service
public class ResidenciaServiceImpl implements ResidenciaService {
	
	private static final Logger log = LoggerFactory.getLogger(ResidenciaServiceImpl.class);

	@Autowired
	private ResidenciaRepository residenciaRepository;

	@CachePut("BuscarTodos")
	public Optional<Residencia> persistir(Residencia residencia) {
		log.info("Persistindo a residência: {}", residencia);
		return Optional.ofNullable(this.residenciaRepository.save(residencia));
	}

	@Override
	public Optional<Residencia> buscarPorId(Long id) {
		log.info("Buscando uma residência para o ID {}", id);
		return Optional.ofNullable(this.residenciaRepository.findById(id));
	}

	@Override
	public Optional<Residencia> bucarPorEnderecoAndNumero(String endereco, Long numero) {
		log.info("Buscando uma residência por Endereço e Número {}", endereco, numero);
		return Optional.ofNullable(this.residenciaRepository.findByEnderecoAndNumero(endereco, numero));
	}

	@Cacheable("BuscarTodos")
	public Page<Residencia> bucarTodos(PageRequest pageRequest) {
		log.info("Buscando uma residências paginado {}", pageRequest);
		return this.residenciaRepository.findAll(pageRequest);
	}
	
	@Override
	public Page<Residencia> buscarPorIdOrEnderecoOrNumero(Long id, String endereco, Long numero, PageRequest pageRequest) {
		log.info("Buscando uma residências paginado {}", pageRequest);
		return this.residenciaRepository.findByIdOrEnderecoContainsOrNumero(id, endereco, numero, pageRequest);
	}

	@Override
	public Optional<Residencia> buscarPorCepAndNumero(String cep, Long numero) {
		log.info("Buscando uma residência por Cep e Número {}", cep, numero);
		return Optional.ofNullable(this.residenciaRepository.findByCepAndNumero(cep, numero));
	}
}

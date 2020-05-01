package com.associacao.arrecadacao.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Override
	public Optional<Residencia> buscarPorMatricula(String matricula) {
		log.info("Buscando uma residência para a matricula {}", matricula);
		return Optional.ofNullable(residenciaRepository.findByMatricula(matricula));
	}

	@Override
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
	public Optional<Residencia> bucarPorIdOrMatricula(Long id, String matricula){
		log.info("Buscando uma residência para o ID {}", id);
		return Optional.ofNullable(this.residenciaRepository.findByIdOrMatricula(id, matricula));
	}

	@Override
	public Optional<Residencia> bucarPorEnderecoAndNumero(String endereco, String numero) {
		log.info("Buscando uma residência por Endereço e Número {}", endereco, numero);
		return Optional.ofNullable(this.residenciaRepository.findByEnderecoAndNumero(endereco, numero));
	}

	@Override
	public Page<Residencia> bucarTodos(PageRequest pageRequest) {
		log.info("Buscando uma residências paginado {}", pageRequest);
		return this.residenciaRepository.findAll(pageRequest);
	}
	
	@Override
	public Page<Residencia> buscarPorIdOrMatricula(Long id, String matricula, PageRequest pageRequest) {
		log.info("Buscando uma residências paginado {}", pageRequest);
		return this.residenciaRepository.findByIdOrMatricula(id, matricula, pageRequest);
	}
}

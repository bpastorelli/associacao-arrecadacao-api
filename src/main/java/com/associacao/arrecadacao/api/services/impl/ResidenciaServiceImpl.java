package com.associacao.arrecadacao.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	public Optional<Residencia> bucarPorMatricula(String matricula) {
		log.info("Buscando uma residência para a matricula {}", matricula);
		return Optional.ofNullable(residenciaRepository.findByMatricula(matricula));
	}

	@Override
	public Optional<Residencia> persistir(Residencia residencia) {
		log.info("Persistindo a residência: {}", residencia);
		return Optional.ofNullable(this.residenciaRepository.save(residencia));
	}

	@Override
	public Optional<Residencia> buscarPorResidenciaId(Long residenciaId) {
		log.info("Buscando uma residência para o ID {}", residenciaId);
		return this.residenciaRepository.findById(residenciaId);
	}
}

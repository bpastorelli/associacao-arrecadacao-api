package com.associacao.arrecadacao.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.entities.VinculoResidencia;
import com.associacao.arrecadacao.api.repositories.VinculoResidenciaRepository;
import com.associacao.arrecadacao.api.services.VinculoResidenciaService;

@Service
public class VinculoResidenciaImpl implements VinculoResidenciaService {

	private static final Logger log = LoggerFactory.getLogger(MoradorServiceImpl.class);
	
	@Autowired
	private VinculoResidenciaRepository vinculoResidenciaRepository;
	
	@Override
	public Optional<VinculoResidencia> buscarPorResidenciaIdAndMoradorId(Long residenciaId, Long moradorId) {
		log.info("Buscar vinculo por residencia ID e morador ID {}", residenciaId, moradorId);
		return Optional.ofNullable(this.vinculoResidenciaRepository.findByResidenciaIdAndMoradorId(residenciaId, moradorId));
	} 

	@Override
	public List<VinculoResidencia> persistir(List<VinculoResidencia> vinculoResidencia) {
		log.info("Persistir vinculo de Residência {}", vinculoResidencia);
		return this.vinculoResidenciaRepository.save(vinculoResidencia);
	}

	@Override
	public List<VinculoResidencia> buscarPorMoradorId(Long moradorId) {
		log.info("Buscar vinculo por morador ID {}", moradorId);
		return this.vinculoResidenciaRepository.findByMoradorId(moradorId);
	}

	@Override
	public List<VinculoResidencia> buscarPorResidenciaId(Long residenciaId) {
		log.info("Buscar vinculo por residencia ID {}", residenciaId);
		return this.vinculoResidenciaRepository.findByResidenciaId(residenciaId);
	}
}

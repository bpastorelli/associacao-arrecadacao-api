package com.associacao.arrecadacao.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.entities.VinculoResidenciaMassa;
import com.associacao.arrecadacao.api.repositories.VinculoResidenciaMassaRepository;
import com.associacao.arrecadacao.api.services.VinculoResidenciaMassaService;

@Service
public class VinculoResidenciaMassaImpl implements VinculoResidenciaMassaService {

	private static final Logger log = LoggerFactory.getLogger(MoradorServiceImpl.class);
	
	@Autowired
	private VinculoResidenciaMassaRepository vinculoResidenciaMassaRepository;
	
	@Override
	public Optional<VinculoResidenciaMassa> buscarPorResidenciaIdAndMoradorId(Long residenciaId, Long moradorId) {
		log.info("Buscar vinculo por residencia ID e morador ID {}", residenciaId, moradorId);
		return Optional.ofNullable(this.vinculoResidenciaMassaRepository.findByResidenciaIdAndMoradorId(residenciaId, moradorId));
	} 

	@Override
	public List<VinculoResidenciaMassa> persistir(List<VinculoResidenciaMassa> vinculoResidenciaMassa) {
		log.info("Persistir vinculo de Residência {}", vinculoResidenciaMassa);
		return this.vinculoResidenciaMassaRepository.save(vinculoResidenciaMassa);
	}

	@Override
	public List<VinculoResidenciaMassa> buscarPorMoradorId(Long moradorId) {
		log.info("Buscar vinculo por morador ID {}", moradorId);
		return this.vinculoResidenciaMassaRepository.findByMoradorId(moradorId);
	}

	@Override
	public List<VinculoResidenciaMassa> buscarPorResidenciaId(Long residenciaId) {
		log.info("Buscar vinculo por residencia ID {}", residenciaId);
		return this.vinculoResidenciaMassaRepository.findByResidenciaId(residenciaId);
	}

}

package com.associacao.arrecadacao.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.entities.VinculoVeiculo;
import com.associacao.arrecadacao.api.repositories.VinculoVeiculoRepository;
import com.associacao.arrecadacao.api.services.VinculoVeiculoService;

@Service
public class VinculoVeiculoServiceImpl implements VinculoVeiculoService {

	private static final Logger log = LoggerFactory.getLogger(VinculoVeiculoServiceImpl.class);
	
	@Autowired
	private VinculoVeiculoRepository vinculoVeiculoRepository;

	@Override
	public Optional<VinculoVeiculo> buscarPorVeiculoIdAndVisitanteId(Long veiculoId, Long visitanteId) {
		log.info("Buscando vinculo por veiculo id e visitante id");
		return Optional.ofNullable(this.vinculoVeiculoRepository.findByVisitanteIdAndVeiculoId(visitanteId, veiculoId));
	}

	@Override
	public List<VinculoVeiculo> buscarPorVisitanteId(Long visitanteId) {
		log.info("Buscando vinculo por visitante id {}", visitanteId);
		return this.vinculoVeiculoRepository.findByVisitanteId(visitanteId);
	}

	@Override
	public List<VinculoVeiculo> buscarPorVeiculoId(Long veiculoId) {
		log.info("Buscando vinculo por veiculo id {}", veiculoId);
		return this.vinculoVeiculoRepository.findByVeiculoId(veiculoId);
	}

	@Override
	public List<VinculoVeiculo> persistir(List<VinculoVeiculo> vinculoVeiculo) {
		log.info("Persistindo vinculo de veiculo id {}", vinculoVeiculo);
		return this.vinculoVeiculoRepository.save(vinculoVeiculo);
	}

	@Override
	public void remover(Long id) {
		log.info("Removendo vinculo por id {}", id);
		this.vinculoVeiculoRepository.delete(id);
	}
	
}

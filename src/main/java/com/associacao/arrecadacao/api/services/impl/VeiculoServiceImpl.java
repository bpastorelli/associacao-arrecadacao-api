package com.associacao.arrecadacao.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.entities.Veiculo;
import com.associacao.arrecadacao.api.repositories.VeiculoRepository;
import com.associacao.arrecadacao.api.services.VeiculoService;

@Service
public class VeiculoServiceImpl implements VeiculoService {
	
	private static final Logger log = LoggerFactory.getLogger(VeiculoServiceImpl.class);
	
	@Autowired
	private VeiculoRepository veiculoRepository;

	@Override
	public Optional<Veiculo> buscarPorId(Long id) {
		log.info("Buscando veiculo por id {}", id );
		return Optional.ofNullable(this.veiculoRepository.findById(id));
	}

	@Override
	public Optional<Veiculo> persistir(Veiculo veiculo) {
		log.info("Persistindo veiculo {}", veiculo);
		return Optional.ofNullable(this.veiculoRepository.save(veiculo));
	}

	@Override
	public Page<Veiculo> bucarPorIdAndPlacaAndMarcaAndModelo(Long id, String placa, String marca, String modelo, PageRequest pageRequest) {
		log.info("Buscando veiculo por id, placa, marca, modelo");
		return this.veiculoRepository.findByIdOrPlacaOrMarcaOrModelo(id, placa, marca, modelo, pageRequest);
	}

	@Override
	public Optional<Veiculo> buscarPorPlaca(String placa) {
		log.info("Buscando veiculo por placa {}", placa);
		return Optional.ofNullable(this.veiculoRepository.findByPlaca(placa));
	}

	@Override
	public Page<Veiculo> bucarTodos(PageRequest pageRequest) {
		log.info("Buscando todos veiculos");
		return this.veiculoRepository.findAll(pageRequest);
	}

}

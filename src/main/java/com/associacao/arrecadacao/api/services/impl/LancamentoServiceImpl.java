package com.associacao.arrecadacao.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.entities.Lancamento;
import com.associacao.arrecadacao.api.repositories.LancamentoRepository;
import com.associacao.arrecadacao.api.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Override
	public Page<Lancamento> buscarPorMoradorId(Long moradorId, PageRequest pageRequest) {
		log.info("Buscar lançamento para o morador ID {}", moradorId);
		return this.lancamentoRepository.findByMoradorId(moradorId, pageRequest);
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Buscar lançamento por ID {}", id);
		return Optional.ofNullable(this.lancamentoRepository.findOne(id));
	}

	@Override
	public Lancamento persistir(Lancamento lancamento) {
		log.info("Persistindo o lançamento {}", lancamento);
		return this.lancamentoRepository.save(lancamento);
	}

	@Override
	public void remover(Long id) {
		log.info("Removendo o lancamento ID {}", id);
		this.lancamentoRepository.delete(id);
	}

	@Override
	public Page<Lancamento> buscarPorResidenciaId(Long residenciaId, PageRequest pageRequest) {
		log.info("Buscar lançamento por ID da residencia paginado {}", residenciaId);
		return this.lancamentoRepository.findByResidenciaId(residenciaId, pageRequest);
	}

	@Override
	public List<Lancamento> buscarPorResidenciaId(Long residenciaId) {
		log.info("Buscar lançamento por ID da residencia {}", residenciaId);
		return this.lancamentoRepository.findByResidenciaId(residenciaId);
	}
}
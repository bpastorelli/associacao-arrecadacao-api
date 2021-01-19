package com.associacao.arrecadacao.api.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.entities.Visita;
import com.associacao.arrecadacao.api.repositories.VisitaRepository;
import com.associacao.arrecadacao.api.services.VisitaService;

@Service
public class VisitaServiceImpl implements VisitaService {

	private static final Logger log = LoggerFactory.getLogger(VisitanteServiceImpl.class);
	
	@Autowired
	private VisitaRepository serviceRepository;
	
	@Override
	public Visita persistir(Visita visita) {
		log.info("Persistindo visita...");
		return serviceRepository.save(visita);
	}

	@Override
	public List<Visita> buscarPorRgOrCpfAndPosicao(String rg, String cpf, Long posicao) {
		log.info("Buscando visitas por RG...");
		return serviceRepository.findByVisitanteRgOrVisitanteCpfAndPosicao(rg, cpf, posicao);
	}

}

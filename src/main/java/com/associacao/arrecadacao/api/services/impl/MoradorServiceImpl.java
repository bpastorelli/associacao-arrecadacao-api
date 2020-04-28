package com.associacao.arrecadacao.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.repositories.MoradorRepository;
import com.associacao.arrecadacao.api.services.MoradorService;

@Service
public class MoradorServiceImpl implements MoradorService {

	private static final Logger log = LoggerFactory.getLogger(MoradorServiceImpl.class);
	
	@Autowired
	private MoradorRepository moradorRepository;

	@Override
	public List<Morador> persistir(List<Morador> moradores) {
		log.info("Persistir morador {}", moradores);
		return this.moradorRepository.save(moradores);
	}

	@Override
	public Optional<Morador> buscarPorCpf(String cpf) {
		log.info("Buscando morador pelo CPF {}", cpf);
		return Optional.ofNullable(this.moradorRepository.findByCpf(cpf));
	}

	@Override
	public Optional<Morador> buscarPorRg(String rg) {
		log.info("Buscando morador pelo RG {}", rg);
		return Optional.ofNullable(this.moradorRepository.findByRg(rg));
	}

	@Override
	public Optional<Morador> buscarPorEmail(String email) {
		log.info("Buscando morador pelo E-mail {}", email);
		return Optional.ofNullable(this.moradorRepository.findByEmail(email));
	}

	@Override
	public Optional<Morador> buscarPorId(Long id) {
		log.info("Buscando morador pelo ID {}", id);
		return Optional.ofNullable(this.moradorRepository.findOne(id));
	}

	@Override
	public Optional<Morador> buscarPorNome(String nome) {
		log.info("Buscando morador pelo Nome {}", nome);
		return Optional.ofNullable(this.moradorRepository.findByNome(nome));
	}
}

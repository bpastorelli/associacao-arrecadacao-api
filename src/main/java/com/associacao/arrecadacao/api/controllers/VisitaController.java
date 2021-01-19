package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.associacao.arrecadacao.api.dtos.VisitaDto;
import com.associacao.arrecadacao.api.entities.Visita;
import com.associacao.arrecadacao.api.entities.VisitaResponse;
import com.associacao.arrecadacao.api.entities.Visitante;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.VisitaService;
import com.associacao.arrecadacao.api.services.VisitanteService;
import com.associacao.arrecadacao.api.utils.Utils;

@RestController
@RequestMapping("/associados/visita")
@CrossOrigin(origins = "*")
public class VisitaController {
	
	private static final Logger log = LoggerFactory.getLogger(VisitaController.class);
	
	@Autowired
	private VisitaService visitaService;
	
	@Autowired
	private VisitanteService visitanteService;
	
	
	public VisitaController() {
		
	}	
	
	/**
	 * Cadastra uma visita
	 * @param visitaDto
	 * @param result
	 * @return Visita
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("/incluir")
	public ResponseEntity<?> cadastrarVisita(@Valid @RequestBody VisitaDto visitaDto,
												BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Preparando dados para cadastro de visita", visitaDto);
		Response<VisitaResponse> response = new Response<VisitaResponse>();
		
		Visita visita = new Visita();
		this.converterVisitaDtoParaVisita(visitaDto, visita, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do(s) visita(s): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		this.visitaService.persistir(visita);
		response.setData(this.converterDtoParaVisitaResponse(visita));
		return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}
	
	public Visita converterVisitaDtoParaVisita(VisitaDto visitaDto, Visita visita, BindingResult result) {
		
		Optional<Visitante> visitante = visitanteService.buscarPorRg(visitaDto.getRg());
	
		visita.setVisitante(visitante.get());
		visita.setResidenciaId(visitaDto.getResidenciaId());
		
		return visita;
		
	}
	
	
	public VisitaResponse converterDtoParaVisitaResponse(Visita visita){
		
		VisitaResponse visitaResponse = new VisitaResponse();
		visitaResponse.setId(visita.getId());
		visitaResponse.setNome(visita.getVisitante().getNome());
		visitaResponse.setRg(visita.getVisitante().getRg());
		visitaResponse.setDataEntrada(Utils.dateFormat(visita.getDataEntrada(), "dd/MM/yyyy"));
		visitaResponse.setResidenciaId(visita.getResidenciaId());
		return visitaResponse;
	}
	

}

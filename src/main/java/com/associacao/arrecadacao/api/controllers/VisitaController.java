package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.associacao.arrecadacao.api.dtos.VisitaDto;
import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.entities.Visita;
import com.associacao.arrecadacao.api.entities.VisitaResponse;
import com.associacao.arrecadacao.api.entities.Visitante;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.ResidenciaService;
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
	
	@Autowired
	private ResidenciaService residenciaService; 
	
	
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
			log.error("Erro validando dados para cadastro de visita(s): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		this.visitaService.persistir(visita);
		response.setData(this.converterDtoParaVisitaResponse(visita));
		return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}
	
	public Visita converterVisitaDtoParaVisita(VisitaDto visitaDto, Visita visita, BindingResult result) {
		
		Optional<Visitante> visitante = null;
				
		if(visitaDto.getCpf().equals("") && visitaDto.getRg().equals("")) {
			result.addError(new ObjectError("visita", " Você deve infomar ao menos o CPF ou RG do visitante" ));
		}else {
			if(!visitaDto.getRg().equals(""))
				visitante = visitanteService.buscarPorRg(visitaDto.getRg());
			else
				visitante = visitanteService.buscarPorCpf(visitaDto.getCpf());			
		}
		
		if(!result.hasErrors()) {
			
			Long posicao = (long) 1;
			List<Visita> listVisitas = visitaService.buscarPorRgOrCpfAndPosicao(visitante.get().getRg(), visitante.get().getCpf(), posicao);
			
			if(listVisitas.size() > 0) {
				result.addError(new ObjectError("visita", " Este visitante já possui " + listVisitas.size() + " registro(s) ativo(s) de entrada!" ));	
			}
		}
		
		
		if(!result.hasErrors()) {
			visita.setVisitante(visitante.get());
			visita.setResidenciaId(visitaDto.getResidenciaId());
		}
		
		return visita;
		
	}
	
	
	public VisitaResponse converterDtoParaVisitaResponse(Visita visita){
		
		Optional<Visitante> visitante = visitanteService.buscarPorRg(visita.getVisitante().getRg());
		Optional<Residencia> residencia = residenciaService.bucarPorIdOrMatricula(visita.getResidenciaId(), null);
		
		VisitaResponse visitaResponse = new VisitaResponse();
		visitaResponse.setId(visita.getId());
		visitaResponse.setNome(visitante.get().getNome());
		visitaResponse.setRg(visitante.get().getRg());
		visitaResponse.setCpf(visitante.get().getCpf() != null ? visitante.get().getCpf() : "");
		visitaResponse.setDataEntrada(Utils.dateFormat(visita.getDataEntrada(), "dd/MM/yyyy"));
		visitaResponse.setHoraEntrada(visita.getHoraEntrada().toString());
		visitaResponse.setDataSaida(visita.getDataSaida() != null ? Utils.dateFormat(visita.getDataSaida(), "dd/MM/yyyy") : "" );
		visitaResponse.setHoraSaida(visita.getHoraSaida() != null ? visita.getHoraSaida().toString() : "" );
		visitaResponse.setEndereco(residencia.get().getEndereco());
		visitaResponse.setNumero(residencia.get().getNumero().toString());
		visitaResponse.setBairro(residencia.get().getBairro());
		visitaResponse.setCidade(residencia.get().getCidade());
		visitaResponse.setUf(residencia.get().getUf());
		
		return visitaResponse;
	}
	

}

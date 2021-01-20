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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.associacao.arrecadacao.api.dtos.EncerraVisitaDto;
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
	
	@PutMapping("/encerrar")
	public ResponseEntity<?> encerrarVisita(@Valid @RequestBody EncerraVisitaDto encerraVisitaDto,
											BindingResult result) {
		
		log.info("Preparando dados para atualizar a visita");
		Response<VisitaResponse> response = new Response<VisitaResponse>();
		
		Visita visita = this.visitaService.buscarPorId(encerraVisitaDto.getId());
		
		
		
		this.visitaService.persistir(visita);
		response.setData(this.converterDtoParaVisitaResponse(visita));
		return ResponseEntity.status(HttpStatus.OK).body(response.getData());
		
	}
	
	
	public Visita converterVisitaDtoParaVisita(VisitaDto visitaDto, Visita visita, BindingResult result) {
		
		Optional<Visitante> visitante = null;
		Optional<Residencia> residencia = this.residenciaService.buscarPorId(visitaDto.getResidenciaId());
		
		if(!residencia.isPresent())
			result.addError(new ObjectError("visita", " Código de residência " + visitaDto.getResidenciaId() + " inexistente"));
				
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
			List<Visita> listVisitas = visitaService.buscarPorIdOrRgOrCpfAndPosicao(visita.getId(), visitante.get().getRg(), visitante.get().getCpf(), posicao);
			
			if(listVisitas.size() > 0) {
				result.addError(new ObjectError("visita", " Este visitante já possui " + listVisitas.size() + " registro(s) ativo(s) de entrada!" ));	
			}
		}		
		
		if(!result.hasErrors()) {
			visita.setVisitante(visitante.get());
			visita.setResidencia(residencia.get());
		}
		
		return visita;
		
	}
	
	
	public VisitaResponse converterDtoParaVisitaResponse(Visita visita){
		
		
		VisitaResponse visitaResponse = new VisitaResponse();
		visitaResponse.setId(visita.getId());
		visitaResponse.setNome(visita.getVisitante().getNome());
		visitaResponse.setRg(visita.getVisitante().getRg());
		visitaResponse.setCpf(visita.getVisitante().getCpf() != null ? visita.getVisitante().getCpf() : "");
		visitaResponse.setDataEntrada(Utils.dateFormat(visita.getDataEntrada(), "dd/MM/yyyy"));
		visitaResponse.setHoraEntrada(visita.getHoraEntrada().toString());
		visitaResponse.setDataSaida(visita.getDataSaida() != null ? Utils.dateFormat(visita.getDataSaida(), "dd/MM/yyyy") : "" );
		visitaResponse.setHoraSaida(visita.getHoraSaida() != null ? visita.getHoraSaida().toString() : "" );
		visitaResponse.setEndereco(visita.getResidencia().getEndereco() != null ? visita.getResidencia().getEndereco() : "");
		visitaResponse.setNumero(visita.getResidencia().getNumero().toString() != null ? visita.getResidencia().getNumero().toString() : "");
		visitaResponse.setBairro(visita.getResidencia().getBairro());
		visitaResponse.setCidade(visita.getResidencia().getCidade());
		visitaResponse.setUf(visita.getResidencia().getUf());
		
		return visitaResponse;
	}
	

}

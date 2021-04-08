package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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

import com.associacao.arrecadacao.api.dtos.VinculoVeiculoDto;
import com.associacao.arrecadacao.api.entities.Veiculo;
import com.associacao.arrecadacao.api.entities.VinculoVeiculo;
import com.associacao.arrecadacao.api.entities.Visitante;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.VinculoVeiculoService;

@RestController
@RequestMapping("/associados/veiculo/vinculo")
@CrossOrigin(origins = "*")
public class VinculoVeiculoController {
	
	private static final Logger log = LoggerFactory.getLogger(VinculoVeiculoController.class);
	
	@Autowired
	private VinculoVeiculoService vinculoVeiculoService;
	
	public VinculoVeiculoController() {
		
	}
	
	@PostMapping(value = "/novo")
	public ResponseEntity<?> vincularVeiculo(
			@Valid @RequestBody VinculoVeiculoDto vinculoRequestBody, BindingResult result) throws NoSuchAlgorithmException {
		
		Response<VinculoVeiculo> response = new Response<VinculoVeiculo>();
		validarDadosExistentes(vinculoRequestBody, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de veiculo: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		List<VinculoVeiculo> listVinculo = new ArrayList<VinculoVeiculo>();
	
		listVinculo.add(converterVinculoVeiculoDto(vinculoRequestBody));
		this.vinculoVeiculoService.persistir(listVinculo);
		
		response.setData(listVinculo.get(0));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	public void validarDadosExistentes(VinculoVeiculoDto vinculo, BindingResult result){
		
		this.vinculoVeiculoService.buscarPorVeiculoIdAndVisitanteId(vinculo.getVeiculoId(), vinculo.getVisitanteId()).
			ifPresent(res -> result.addError(new ObjectError("vinculo", "Vinculo de veiculo já existente")));
		
	}
	
	public VinculoVeiculo converterVinculoVeiculoDto(VinculoVeiculoDto dto) {
		
		Veiculo veiculo = new Veiculo();
		Visitante visitante = new Visitante();
		
		veiculo.setId(dto.getVeiculoId());
		visitante.setId(dto.getVisitanteId());
		
		VinculoVeiculo vinculo = new VinculoVeiculo();
		vinculo.setVeiculo(veiculo);
		vinculo.setVisitante(visitante);
		
		return vinculo;
		
	}

}

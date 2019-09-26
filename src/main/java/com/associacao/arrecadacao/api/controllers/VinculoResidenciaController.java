package com.associacao.arrecadacao.api.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.associacao.arrecadacao.api.dtos.VinculoResidenciaDto;
import com.associacao.arrecadacao.api.entities.VinculoResidencia;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.VinculoResidenciaService;

@RestController
@RequestMapping("/associados/vinculo-residencia")
@CrossOrigin(origins = "*")
public class VinculoResidenciaController {
	
	private static final Logger log = LoggerFactory.getLogger(VinculoResidenciaController.class);
	
	@Autowired
	private VinculoResidenciaService vinculoResidenciaService;
	
	public VinculoResidenciaController() {
		
	}
	
	@PostMapping
	public ResponseEntity<Response<VinculoResidenciaDto>> cadastrar(@Valid @RequestBody VinculoResidenciaDto vinculoResidenciaDto, 
			BindingResult result){
	
		log.info("Vinculando um morador a uma residência: {}", vinculoResidenciaDto.toString());
		
		Response<VinculoResidenciaDto> response = new Response<VinculoResidenciaDto>();
		
		List<VinculoResidencia> vinculos = this.converterDtoParaVinculoResidencia(vinculoResidenciaDto);
		
		validarDadosExistentes(vinculoResidenciaDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para vinculo da residência: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);	
		}
		
		this.vinculoResidenciaService.persistir(vinculos);
		
		response.setData(this.converterVinculoResidenciaDto(vinculos));
		return ResponseEntity.ok(response);
	}

	/**
	 * Converter o VinculoResidenciaDto para Residencia.
	 * 
	 * @param vinculoResidenciaDto
	 * @return VinculoResidencia
	 */
	private List<VinculoResidencia> converterDtoParaVinculoResidencia(VinculoResidenciaDto vinculoResidenciaDto) {
		
		List<VinculoResidencia> vinculos = new ArrayList<VinculoResidencia>();
		
		vinculoResidenciaDto.getVinculoResidencia().forEach(p -> {			
			VinculoResidencia vinculo = new VinculoResidencia();
			vinculo.setMoradorId(p.getMoradorId());
			vinculo.setResidenciaId(p.getResidenciaId());
			vinculos.add(vinculo);
		});
		return vinculos;
	}
	
	private void validarDadosExistentes(VinculoResidenciaDto vinculoResidenciaDto, BindingResult result) {
		
		vinculoResidenciaDto.getVinculoResidencia().forEach(p -> {
			this.vinculoResidenciaService.buscarPorResidenciaIdAndMoradorId(p.getResidenciaId(), p.getMoradorId())
				.ifPresent(res -> result.addError(new ObjectError("vinculor residencia", "Vinculo para residência já existente")));			
		});
	}
	
	/**
	 * Converter Vinculo de Residencia em VinculoResidenciaDto
	 * 
	 * @param vinculos
	 * @return VinculoResidenciaDto
	 */
	public VinculoResidenciaDto converterVinculoResidenciaDto(List<VinculoResidencia> vinculos) {
		
		VinculoResidenciaDto vinculoResidenciaDto = new VinculoResidenciaDto();
		vinculoResidenciaDto.setVinculoReidencia(vinculos);
		
		return vinculoResidenciaDto;
	}
}

package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.associacao.arrecadacao.api.dtos.VinculoResidenciaDto;
import com.associacao.arrecadacao.api.dtos.VinculoResidenciaMassaDto;
import com.associacao.arrecadacao.api.entities.VinculoResidencia;
import com.associacao.arrecadacao.api.entities.VinculoResidenciaMassa;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.VinculoResidenciaMassaService;
import com.associacao.arrecadacao.api.services.VinculoResidenciaService;

@RestController
@RequestMapping("/associados/vinculo-residencia")
@CrossOrigin(origins = "*")
public class VinculoResidenciaController {
	
	private static final Logger log = LoggerFactory.getLogger(VinculoResidenciaController.class);

	@Autowired
	private VinculoResidenciaService vinculoResidenciaService;
	
	@Autowired
	private VinculoResidenciaMassaService vinculoResidenciaMassaService;
	
	public VinculoResidenciaController() {
		
	}
	
	@PostMapping(value = "/residencia/{residenciaId}")
	public ResponseEntity<Response<VinculoResidenciaMassaDto>> cadastrar(@PathVariable("residenciaId") Long residenciaId, @Valid @RequestBody VinculoResidenciaMassaDto vinculoResidenciaMassaDto, 
			BindingResult result)throws NoSuchAlgorithmException{
	
		log.info("Vinculando um morador a uma residência: {}", vinculoResidenciaMassaDto.toString());
		
		Response<VinculoResidenciaMassaDto> response = new Response<VinculoResidenciaMassaDto>();
		
		vinculoResidenciaMassaDto.getVinculosMassa().forEach(p -> p.setResidenciaId(residenciaId));
		
		List<VinculoResidenciaMassa> vinculos = this.converterDtoParaVinculoResidencia(vinculoResidenciaMassaDto);
		validarDadosExistentes(vinculoResidenciaMassaDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para vinculo da residência: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);	
		}
		
		this.vinculoResidenciaMassaService.persistir(vinculos);
		
		response.setData(this.converterVinculoResidenciaMassaDto(vinculos));
		return ResponseEntity.ok(response);
	}

	/**
	 * Consulta um vinculo de residência a um morador.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Lancamento>>
	 */
	@GetMapping(value = "/residencia/{residenciaId}/morador/{moradorId}")
	public ResponseEntity<Response<VinculoResidenciaDto>> consultarVinculoResidenciaIdAndMoradorId(
			@PathVariable("residenciaId") Long residenciaId,
			@PathVariable("moradorId") Long moradorId) {
		
		log.info("Consultando vinculo para o morador ID: {}", moradorId);
		Response<VinculoResidenciaDto> response = new Response<VinculoResidenciaDto>();
		Optional<VinculoResidencia> vinculo = this.vinculoResidenciaService.buscarPorResidenciaIdAndMoradorId(residenciaId, moradorId);

		if (!vinculo.isPresent()) {
			log.info("Erro ao remover devido ao vinculo para o morador ID: {} não existir.", moradorId);
			response.getErrors().add("Erro ao remover vinculo. Registro não encontrado para o morador ID " + moradorId);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterVinculoResidenciaDto(vinculo).get());
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Remove um vinculo de residência a um morador.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Lancamento>>
	 */
	@DeleteMapping(value = "/residencia/{residenciaId}/morador/{moradorId}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remover(@PathVariable("residenciaId") Long residenciaId,
			@PathVariable("moradorId") Long moradorId) {
		log.info("Removendo vinculo para o morador ID: {}", moradorId);
		Response<String> response = new Response<String>();
		Optional<VinculoResidencia> vinculo = this.vinculoResidenciaService.buscarPorResidenciaIdAndMoradorId(residenciaId, moradorId);

		if (!vinculo.isPresent()) {
			log.info("Erro ao remover devido ao vinculo para o morador ID: {} não existir.", moradorId);
			response.getErrors().add("Erro ao remover vinculo. Registro não encontrado para o morador ID " + moradorId);
			return ResponseEntity.badRequest().body(response);
		}

		this.vinculoResidenciaService.remover(vinculo.get().getId());
		return ResponseEntity.ok(new Response<String>());
	}
	
	/**
	 * Converter o VinculoResidenciaDto para Residencia.
	 * 
	 * @param vinculoResidenciaDto
	 * @return VinculoResidencia
	 */
	private List<VinculoResidenciaMassa> converterDtoParaVinculoResidencia(VinculoResidenciaMassaDto vinculoResidenciaMassaDto) {
		
		List<VinculoResidenciaMassa> vinculos = new ArrayList<VinculoResidenciaMassa>();
		
		vinculoResidenciaMassaDto.getVinculosMassa().forEach(p -> {			
			VinculoResidenciaMassa vinculo = new VinculoResidenciaMassa();
			vinculo.setMoradorId(p.getMoradorId());
			vinculo.setResidenciaId(p.getResidenciaId());
			vinculos.add(vinculo);
		});
		return vinculos;
	}
	
	private void validarDadosExistentes(VinculoResidenciaMassaDto vinculoResidenciaMassaDto, BindingResult result) {
		
		vinculoResidenciaMassaDto.getVinculosMassa().forEach(p -> {
			this.vinculoResidenciaMassaService.buscarPorResidenciaIdAndMoradorId(p.getResidenciaId(), p.getMoradorId())
				.ifPresent(res -> result.addError(new ObjectError("vinculor residencia", "Vinculo para residência já existente.")));			
		});
	}
	
	/**
	 * Converter Vinculo de Residencia em VinculoResidenciaDto
	 * 
	 * @param vinculo
	 * @return VinculoResidenciaDto
	 */
	public Optional<VinculoResidenciaDto> converterVinculoResidenciaDto(Optional<VinculoResidencia> vinculo) {
		
		VinculoResidenciaDto vinculoResidenciaDto = new VinculoResidenciaDto();
		vinculoResidenciaDto.setVinculos(vinculo);
		
		return Optional.ofNullable(vinculoResidenciaDto);
	}
	
	/**
	 * Converter Vinculo de Residencia em VinculoResidenciaDto
	 * 
	 * @param vinculos
	 * @return VinculoResidenciaMassaDto
	 */
	public VinculoResidenciaMassaDto converterVinculoResidenciaMassaDto(List<VinculoResidenciaMassa> vinculos) {
		
		VinculoResidenciaMassaDto vinculoResidenciaMassaDto = new VinculoResidenciaMassaDto();
		vinculoResidenciaMassaDto.setVinculosMassa(vinculos);
		
		return vinculoResidenciaMassaDto;
	}
}

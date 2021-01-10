package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.associacao.arrecadacao.api.dtos.MoradoresResidenciaDto;
import com.associacao.arrecadacao.api.dtos.VinculoResidenciaMassaDto;
import com.associacao.arrecadacao.api.dtos.VinculosResidenciaResponseDto;
import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.entities.ResidenciaResponse;
import com.associacao.arrecadacao.api.entities.VinculoResidencia;
import com.associacao.arrecadacao.api.entities.VinculoResidenciaMassa;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.MoradorService;
import com.associacao.arrecadacao.api.services.ResidenciaService;
import com.associacao.arrecadacao.api.services.VinculoResidenciaMassaService;
import com.associacao.arrecadacao.api.services.VinculoResidenciaService;
import com.associacao.arrecadacao.api.utils.Utils;

@RestController
@RequestMapping("/associados/vinculo-residencia")
@CrossOrigin(origins = "*")
public class VinculoResidenciaController {
	
	private static final Logger log = LoggerFactory.getLogger(VinculoResidenciaController.class);

	@Autowired
	private VinculoResidenciaService vinculoResidenciaService;
	
	@Autowired
	private VinculoResidenciaMassaService vinculoResidenciaMassaService;
	
	@Autowired
	private ResidenciaService residenciaService;
	
	@Autowired
	private MoradorService moradorService;
	
	public VinculoResidenciaController() {
		
	}
	
	@PostMapping(value = "/residencia/{residenciaId}")
	public ResponseEntity<Response<VinculoResidenciaMassaDto>> cadastrar(@PathVariable("residenciaId") Long residenciaId, @Valid @RequestBody VinculoResidenciaMassaDto vinculoResidenciaMassaDto, 
			BindingResult result)throws NoSuchAlgorithmException{
	
		log.info("Vinculando um morador a uma residência: {}", vinculoResidenciaMassaDto.toString());
		
		Response<VinculoResidenciaMassaDto> response = new Response<VinculoResidenciaMassaDto>();
		
		vinculoResidenciaMassaDto.getVinculosMassa().forEach(p -> p.setResidenciaId(residenciaId));
		
		List<VinculoResidenciaMassa> vinculos = this.converterDtoParaVinculoResidencia(vinculoResidenciaMassaDto);
		this.validarDadosExistentes(vinculoResidenciaMassaDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para vinculo da residência: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);	
		}
		
		this.vinculoResidenciaMassaService.persistir(vinculos);
		
		response.setData(this.converterVinculoResidenciaMassaDto(vinculos));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Consulta um vinculo de residência a um morador.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Lancamento>>
	 */
	@GetMapping(value = "/residencia/{residenciaId}/morador/{moradorId}")
	public ResponseEntity<Response<VinculosResidenciaResponseDto>> consultarVinculoResidenciaIdAndMoradorId(
			@PathVariable("residenciaId") Long residenciaId,
			@PathVariable("moradorId") Long moradorId) {
		
		log.info("Consultando vinculo para o morador ID: {}", moradorId);
		Response<VinculosResidenciaResponseDto> response = new Response<VinculosResidenciaResponseDto>();
		Optional<VinculoResidencia> vinculo = this.vinculoResidenciaService.buscarPorResidenciaIdAndMoradorId(residenciaId, moradorId);

		if (!vinculo.isPresent()) {
			log.info("Erro ao consultar devido ao vinculo para o morador ID: {} não existir.", moradorId);
			response.getErrors().add("Erro ao consultar vinculo. Registro não encontrado para o morador ID " + moradorId);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterVinculoResidenciaDto(vinculo).get());
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Consulta um vinculo de residência a um morador.
	 * 
	 * @param residenciaId
	 * @return ResponseEntity<Response<VinculosResidenciaResponseDto>>
	 */
	@GetMapping(value = "/residencia/{residenciaId}")
	public ResponseEntity<Response<VinculosResidenciaResponseDto>> consultarVinculoResidenciaIdAndMoradorId(
			@PathVariable("residenciaId") Long residenciaId) {
		
		log.info("Consultando vinculo para o morador ID: {}", residenciaId);
		Response<VinculosResidenciaResponseDto> response = new Response<VinculosResidenciaResponseDto>();
		List<VinculoResidencia> vinculo = this.vinculoResidenciaService.buscarPorResidenciaId(residenciaId);

		if (vinculo.size() == 0) {
			log.info("Erro ao consultar devido ao vinculo para a residencia ID: {} não existir.", residenciaId);
			response.getErrors().add("Registro não encontrado para a residencia ID " + residenciaId);
			return ResponseEntity.status(404).body(response);
		}

		response.setData(this.converterVinculoResidenciaDto(vinculo).get());
		return ResponseEntity.status(200).body(response);
	}
	
	/**
	 * Consulta um vinculo de residência a um morador.
	 * 
	 * @param residenciaId
	 * @return ResponseEntity<Response<VinculosResidenciaResponseDto>>
	 */
	@GetMapping(value = "moradores/residencia/{residenciaId}")
	public ResponseEntity<?> consultarMoradoresResidencia(
			@PathVariable("residenciaId") Long residenciaId) {
		
		log.info("Consultando vinculo para o morador ID: {}", residenciaId);
		Response<MoradoresResidenciaDto> response = new Response<MoradoresResidenciaDto>();
		List<VinculoResidencia> vinculo = this.vinculoResidenciaService.buscarPorResidenciaId(residenciaId);

		if (vinculo.size() == 0) {
			log.info("Erro ao consultar devido ao vinculo para a residencia ID: {} não existir.", residenciaId);
			response.getErrors().add("Registro não encontrado para a residencia ID " + residenciaId);
			return ResponseEntity.status(404).body(response);
		}
		
		response.setData(this.converterVinculoResidenciaParaMoradoresResidenciaDto(vinculo).get());
		return new ResponseEntity<>(response.getData().getMoradores(), HttpStatus.OK);
		
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
		
		//Valida se a residencia existe
		vinculoResidenciaMassaDto.getVinculosMassa().forEach(p -> {
			if(!this.residenciaService.buscarPorId(p.getResidenciaId()).isPresent())
				result.addError(new ObjectError("vinculor residencia", "Código de Residência " + p.getResidenciaId() + " inexistente.") );			
		});		
		
		//Valida se o morador existe
		vinculoResidenciaMassaDto.getVinculosMassa().forEach(p -> {
			if(!this.moradorService.buscarPorId(p.getMoradorId()).isPresent() )
				result.addError(new ObjectError("vinculor residencia", "Código de Morador inexistente."));			
		});	
		
		//Valida se já existe um vinculo para a mesma residência
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
	public Optional<VinculosResidenciaResponseDto> converterVinculoResidenciaDto(Optional<VinculoResidencia> vinculo) {
		
		VinculosResidenciaResponseDto vinculosResidenciaResponseDto = new VinculosResidenciaResponseDto();
		
		ResidenciaResponse residencia = new ResidenciaResponse();
		residencia.setId(vinculo.get().getResidencia().getId());
		residencia.setMatricula(vinculo.get().getResidencia().getMatricula());
		residencia.setEndereco(vinculo.get().getResidencia().getEndereco());
		residencia.setNumero(vinculo.get().getResidencia().getNumero());
		residencia.setBairro(vinculo.get().getResidencia().getBairro());
		residencia.setCep(vinculo.get().getResidencia().getCep());
		residencia.setCidade(vinculo.get().getResidencia().getCidade());
		residencia.setUf(vinculo.get().getResidencia().getUf());
		residencia.setDataCriacao(Utils.dateFormat(vinculo.get().getResidencia().getDataCriacao(),"dd/MM/yyyy"));
		residencia.setDataAtualizacao(Utils.dateFormat(vinculo.get().getResidencia().getDataAtualizacao(),"dd/MM/yyyy"));
		
		List<Morador> moradores = new ArrayList<Morador>();
		moradores.add(vinculo.get().getMorador());
		
		vinculosResidenciaResponseDto.setResidencia(residencia);
		vinculosResidenciaResponseDto.setMoradores(moradores);
		
		return Optional.ofNullable(vinculosResidenciaResponseDto);
		
	}
	
	/**
	 * Converter Vinculo de Residencia em MoradoresResidenciaDto
	 * 
	 * @param vinculo
	 * @return MoradoresResidenciaDto
	 */
	public Optional<MoradoresResidenciaDto> converterVinculoResidenciaParaMoradoresResidenciaDto(List<VinculoResidencia> vinculos) {
		
		MoradoresResidenciaDto moradoresResidenciaDto = new MoradoresResidenciaDto();
		
		List<Morador> moradores = new ArrayList<Morador>();
		
		vinculos.forEach(m -> {
			m.getMorador().setResidenciaId(m.getResidencia().getId());
			moradores.add(m.getMorador());			
		});
		
		moradoresResidenciaDto.setMoradores(moradores);
		
		return Optional.ofNullable(moradoresResidenciaDto);
		
	}
	
	/**
	 * Converter Vinculos de Residencia em VinculoResidenciaDto
	 * 
	 * @param vinculos
	 * @return VinculoResidenciaDto
	 */
	public Optional<VinculosResidenciaResponseDto> converterVinculoResidenciaDto(List<VinculoResidencia> vinculos) {
		
		VinculosResidenciaResponseDto vinculosResidenciaResponseDto = new VinculosResidenciaResponseDto();
		
		ResidenciaResponse residencia = new ResidenciaResponse();
		residencia.setId(vinculos.get(0).getResidencia().getId());
		residencia.setMatricula(vinculos.get(0).getResidencia().getMatricula());
		residencia.setEndereco(vinculos.get(0).getResidencia().getEndereco());
		residencia.setNumero(vinculos.get(0).getResidencia().getNumero());
		residencia.setBairro(vinculos.get(0).getResidencia().getBairro());
		residencia.setCep(vinculos.get(0).getResidencia().getCep());
		residencia.setCidade(vinculos.get(0).getResidencia().getCidade());
		residencia.setUf(vinculos.get(0).getResidencia().getUf());
		residencia.setDataCriacao(Utils.dateFormat(vinculos.get(0).getResidencia().getDataCriacao(),"dd/MM/yyyy"));
		residencia.setDataAtualizacao(Utils.dateFormat(vinculos.get(0).getResidencia().getDataAtualizacao(),"dd/MM/yyyy"));
		
		List<Morador> moradores = new ArrayList<Morador>();
		
		vinculos.forEach(m -> {
			m.getMorador().setResidenciaId(m.getResidencia().getId());
			moradores.add(m.getMorador());			
		});
		
		vinculosResidenciaResponseDto.setResidencia(residencia);
		vinculosResidenciaResponseDto.setMoradores(moradores);
		
		return Optional.ofNullable(vinculosResidenciaResponseDto);
		
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

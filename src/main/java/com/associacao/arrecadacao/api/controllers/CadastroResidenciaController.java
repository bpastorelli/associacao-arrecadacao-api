package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.associacao.arrecadacao.api.dtos.AtualizaResidenciaDto;
import com.associacao.arrecadacao.api.dtos.CadastroResidenciaDto;
import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.ResidenciaService;

@RestController
@RequestMapping("/associados/residencia")
@CrossOrigin(origins = "*")
class CadastroResidenciaController {
	
	private static final Logger log = LoggerFactory.getLogger(CadastroResidenciaController.class);
	
	@Autowired
	private ResidenciaService residenciaService;
	
	public CadastroResidenciaController() {
		
	}
	
	@PostMapping
	public ResponseEntity<Response<CadastroResidenciaDto>> cadastrar(@Valid @RequestBody CadastroResidenciaDto cadastroResidenciaDto,
			BindingResult result ){
		
		log.info("Cadastrando uma residência: {}", cadastroResidenciaDto.toString());
		
		Response<CadastroResidenciaDto> response = new Response<CadastroResidenciaDto>();
		
		Residencia residencia = this.converterDtoParaResidencia(cadastroResidenciaDto);
		validarDadosExistentes(cadastroResidenciaDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro da residência: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.residenciaService.persistir(residencia);
		
		response.setData(this.converterCadastroResidenciaDto(residencia));
		return ResponseEntity.ok(response);
		
	}
	
	
	/**
	 * Atualiza os dados de uma residência.
	 * 
	 * @param id
	 * @param residenciaDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroResidenciaDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<CadastroResidenciaDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody AtualizaResidenciaDto residenciaDto, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Atualizando residência: {}", residenciaDto.toString());
		Response<CadastroResidenciaDto> response = new Response<CadastroResidenciaDto>();
		
		Optional<Residencia> residencia = this.residenciaService.buscarPorId(id);
		if (!residencia.isPresent()) {
			result.addError(new ObjectError("residencia", "Residência não encontrada."));
		}
		
		this.atualizarDadosResidencia(residencia.get(), residenciaDto, result);
		
		if (result.hasErrors()) {
			log.error("Erro validando residência: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.residenciaService.persistir(residencia.get());
		response.setData(this.converterCadastroResidenciaDto(residencia.get()));

		return ResponseEntity.ok(response);
		
	}
	
	/**
	 * Busca uma residência pelo ID ou pel Matricula.
	 * 
	 * @param id
	 * @param matricula
	 * @param result
	 * @return ResponseEntity<Response<CadastroResidenciaDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@GetMapping(value = "/id/{id}/matricula/{matricula}")
	public ResponseEntity<Response<CadastroResidenciaDto>> buscarPorId(@PathVariable("id") Long id, @PathVariable("matricula") String matricula) throws NoSuchAlgorithmException {
		
		log.info("Buscando residência: {}", id);
		Response<CadastroResidenciaDto> response = new Response<CadastroResidenciaDto>();
		
		Optional<Residencia> residencia = this.residenciaService.bucarPorIdOrMatricula(id, matricula);
		if (!residencia.isPresent()) {
			log.info("Residência não encontrada para o ID: {}", id);
			response.getErrors().add("Residência não encontrada para o ID " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.converterCadastroResidenciaDto(residencia.get()));
		return ResponseEntity.ok(response);
		
	}
	
	/**
	 * Converter o CadastroResidenciaDto para Residencia.
	 * 
	 * @param cadastroResidenciaDto
	 * @return Residencia
	 */
	private Residencia converterDtoParaResidencia(CadastroResidenciaDto cadastroResidenciaDto) {
		
		Residencia residencia = new Residencia();
		residencia.setMatricula(cadastroResidenciaDto.getMatricula());
		residencia.setEndereco(cadastroResidenciaDto.getEndereco());
		residencia.setNumero(cadastroResidenciaDto.getNumero());
		residencia.setBairro(cadastroResidenciaDto.getBairro());
		residencia.setCep(cadastroResidenciaDto.getCep());
		residencia.setCidade(cadastroResidenciaDto.getCidade());
		residencia.setUf(cadastroResidenciaDto.getUf());
		return residencia;
	}
	
	private void validarDadosExistentes(CadastroResidenciaDto cadastroResidenciaDto, BindingResult result) {
		
		this.residenciaService.buscarPorMatricula(cadastroResidenciaDto.getMatricula())
				.ifPresent(res -> result.addError(new ObjectError("residencia", "Residência já existente")));
		
		this.residenciaService.bucarPorEnderecoAndNumero(cadastroResidenciaDto.getEndereco(), cadastroResidenciaDto.getNumero())
				.ifPresent(res -> result.addError(new ObjectError("residencia", "Endereço já existente")));
		
	}
	
	/**
	 * Converter o objeto tipo Residencia para o tipo CadastroResidenciaDto.
	 * 
	 * @param residencia
	 * @return CadastroResidenciaDto
	 */
	private CadastroResidenciaDto converterCadastroResidenciaDto(Residencia residencia) {
		
		CadastroResidenciaDto cadastroResidenciaDto = new CadastroResidenciaDto();
		cadastroResidenciaDto.setId(residencia.getId());
		cadastroResidenciaDto.setMatricula(residencia.getMatricula());
		cadastroResidenciaDto.setEndereco(residencia.getEndereco());
		cadastroResidenciaDto.setNumero(residencia.getNumero());
		cadastroResidenciaDto.setBairro(residencia.getBairro());
		cadastroResidenciaDto.setCep(residencia.getCep());
		cadastroResidenciaDto.setCidade(residencia.getCidade());
		cadastroResidenciaDto.setUf(residencia.getUf());
		cadastroResidenciaDto.setCidade(residencia.getCidade());
		return cadastroResidenciaDto;
	}

	/**
	 * Atualiza os dados da residência com base nos dados encontrados no DTO.
	 * 
	 * @param reseidencia
	 * @param residenciaDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosResidencia(Residencia residencia, AtualizaResidenciaDto residenciaDto, BindingResult result)
			throws NoSuchAlgorithmException {
		
		residencia.setEndereco(residenciaDto.getEndereco());
		residencia.setNumero(residenciaDto.getNumero());
		residencia.setBairro(residenciaDto.getBairro());
		residencia.setCep(residenciaDto.getCep());
		residencia.setCidade(residenciaDto.getCidade());
		residencia.setUf(residenciaDto.getUf());

	}
}

package com.associacao.arrecadacao.api.controllers;

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
		
		this.residenciaService.bucarPorMatricula(cadastroResidenciaDto.getMatricula())
				.ifPresent(res -> result.addError(new ObjectError("residencia", "Residência já existente")));
		
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

}

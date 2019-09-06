package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;
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

import com.associacao.arrecadacao.api.dtos.CadastroResidenciaDto;
import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.MoradorService;
import com.associacao.arrecadacao.api.services.ResidenciaService;

@RestController
@RequestMapping("/api/residencia")
@CrossOrigin(origins = "*")
public class CadastroResidenciaController {

	private static final Logger log = LoggerFactory.getLogger(CadastroResidenciaController.class);
	
	@Autowired
	private MoradorService moradorService;
	
	@Autowired
	private ResidenciaService residenciaService;
	
	public CadastroResidenciaController() {
	}
	
	@PostMapping
	public ResponseEntity<Response<CadastroResidenciaDto>> cadastrar(@Valid @RequestBody CadastroResidenciaDto cadastroResidenciaDto,
			BindingResult result) throws NoSuchAlgorithmException{
		log.info("Cadastrando residência: {}", cadastroResidenciaDto.toString());
		Response<CadastroResidenciaDto> response = new Response<CadastroResidenciaDto>();
		
		validarDadosExistentes(cadastroResidenciaDto, result);
		Residencia residencia = this.converterDtoParaResidencia(cadastroResidenciaDto);
		List<Morador> moradores = this.converterDtoParaMorador(cadastroResidenciaDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados de cadastro de residência: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.residenciaService.persistir(residencia);
		moradores.forEach(p -> p.setResidencia(residencia));
		this.moradorService.persistir(moradores);
		
		response.setData(this.converterCadastroResidenciaDto(residencia));
		return ResponseEntity.ok(response);
	}
	
	private void validarDadosExistentes(CadastroResidenciaDto cadastroResidenciaDto, BindingResult result) {
		this.residenciaService.bucarPorMatricula(cadastroResidenciaDto.getMatricula())
				.ifPresent(res -> result.addError(new ObjectError("residencia", "Residência já existente")));
		
		for(Morador morador : cadastroResidenciaDto.getMoradores()) {
			this.moradorService.buscarPorCpf(morador.getCpf())
					.ifPresent(res -> result.addError(new ObjectError("morador", "CPF " + morador.getCpf() + " já existente")));
		}
		
		for(Morador morador : cadastroResidenciaDto.getMoradores()) {
			this.moradorService.buscarPorCpf(morador.getRg())
					.ifPresent(res -> result.addError(new ObjectError("morador", "RG " + morador.getRg() + " já existente")));
		}
		
		for(Morador morador : cadastroResidenciaDto.getMoradores()) {
			this.moradorService.bucarPorEmail(morador.getEmail())
					.ifPresent(res -> result.addError(new ObjectError("morador", "E-mail " + morador.getEmail() + " já existente")));
		}
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
		residencia.setBairro(cadastroResidenciaDto.getEndereco());
		residencia.setNumero(cadastroResidenciaDto.getNumero());
		residencia.setBairro(cadastroResidenciaDto.getBairro());
		residencia.setCep(cadastroResidenciaDto.getCep());
		residencia.setCidade(cadastroResidenciaDto.getCidade());
		residencia.setUf(cadastroResidenciaDto.getUf());
		residencia.setMoradores(cadastroResidenciaDto.getMoradores());
		return residencia;
	}
	
	/**
	 * Converter o CadastroResidenciaDto para Morador.
	 * 
	 * @param cadastroResidenciaDto
	 * @return Morador
	 */
	private List<Morador> converterDtoParaMorador(CadastroResidenciaDto cadastroResidenciaDto, BindingResult result) {
		
		List<Morador> moradores = new ArrayList<Morador>();
		
		for(Morador morador : cadastroResidenciaDto.getMoradores()) {
			moradores.add(morador);
		}

		return moradores;
	}
	
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
		cadastroResidenciaDto.setMoradores(residencia.getMoradores());
		return cadastroResidenciaDto;
	}
	
}

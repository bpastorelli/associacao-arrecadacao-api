package com.associacao.arrecadacao.api.access.controllers;

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

import com.associacao.arrecadacao.api.access.dtos.CadastroModuloDto;
import com.associacao.arrecadacao.api.access.entities.Modulo;
import com.associacao.arrecadacao.api.access.services.ModuloService;
import com.associacao.arrecadacao.api.response.Response;

@RestController
@RequestMapping("/associados/modulo")
@CrossOrigin(origins = "*")
public class ModuloController {
	
	private static final Logger log = LoggerFactory.getLogger(ModuloController.class);
	
	@Autowired
	private ModuloService moduloService;
	
	public ModuloController() {
		
		
	}
	
	@PostMapping(value = "/incluir")
	public ResponseEntity<?> cadastrar(@Valid @RequestBody List<CadastroModuloDto> requestBody, 
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Cadastro de módulos: {}", requestBody.toString());
		Response<List<Modulo>> response = new Response<List<Modulo>>();
		
		List<Modulo> modulos = validarDadosPost(requestBody, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de módulos: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		modulos = this.moduloService.persistir(modulos);
		
		response.setData(modulos);
		return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}
	
	public List<Modulo> validarDadosPost(List<CadastroModuloDto> dto, BindingResult result) {
		
		List<Modulo> modulos = new ArrayList<Modulo>();
		
		dto.forEach(m -> {	
			if(this.moduloService.buscarPorDescricao(m.getDescricao()).isPresent()) {				
				result.addError(new ObjectError("modulo","Módulo " + m.getDescricao() + " já existe"));
			}
			else
			{
				Modulo modulo = new Modulo();
				modulo.setDescricao(m.getDescricao());
				modulo.setPathModulo(m.getPathModulo());				
				modulos.add(modulo);
			}	
		});	
		
		return modulos;
	}

}

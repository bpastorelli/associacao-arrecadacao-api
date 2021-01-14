package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;

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

import com.associacao.arrecadacao.api.commons.ValidaCPF;
import com.associacao.arrecadacao.api.dtos.CadastroVisitanteDto;
import com.associacao.arrecadacao.api.entities.Visitante;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.VisitanteService;

@RestController
@RequestMapping("associados/visitante")
@CrossOrigin("*")
public class VisitanteController {
	
	private static final Logger log = LoggerFactory.getLogger(VisitanteController.class);
	
	@Autowired
	private VisitanteService visitanteService;

	public VisitanteController() {
		
		
	}
	
	/**
	 * Cadastra um visitate
	 * @param cadastroVisitanteDto
	 * @param result
	 * @return Morador
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("/incluir")
	public ResponseEntity<?> cadastrarVisitante(@Valid @RequestBody Visitante visitante,
												BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Preparando dados para cadastro de visitante", visitante);
		Response<Visitante> response = new Response<Visitante>();
		
		validarDadosExistentes(visitante, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do(s) visitante(s): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		this.visitanteService.persistir(visitante);
		response.setData(visitante);
		return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}
	
	//Validação dos dados informados no dto
	public void validarDadosExistentes(CadastroVisitanteDto cadastroVisitanteDto, BindingResult result) {
		
		if(cadastroVisitanteDto.getVisitantes().size() == 0)
			result.addError(new ObjectError("visitante", " Não existem dados para inclusão"));
		
		cadastroVisitanteDto.getVisitantes().forEach(v -> {
			
			if(v.getNome().isEmpty())
				result.addError(new ObjectError("visitante", " O campo nome é obrigatório"));
			
			if(!v.getCpf().isEmpty()) {
				if(!ValidaCPF.isCPF(v.getCpf()))
					result.addError(new ObjectError("visitante", " O campo RG é obrigatório"));			
			}
			
			this.visitanteService.buscarPorRg(v.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o rg "+ v.getRg() +"")));
			
			this.visitanteService.buscarPorCpf(v.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o cpf "+ v.getCpf() +"")));
			
			this.visitanteService.buscarPorNome(v.getNome())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o nome "+ v.getNome() +"")));
			
		});
		
	}
	
	//Validação dos dados informados no dto para um cadastro
	public void validarDadosExistentes(Visitante visitante, BindingResult result) {
		
	
			if(visitante.getNome().isEmpty())
				result.addError(new ObjectError("visitante", " O campo nome é obrigatório"));
			
			if(!visitante.getCpf().isEmpty()) {
				if(!ValidaCPF.isCPF(visitante.getCpf()))
					result.addError(new ObjectError("visitante", " O campo RG é obrigatório"));			
			}
			
			this.visitanteService.buscarPorRg(visitante.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o rg "+ visitante.getRg() +"")));
			
			this.visitanteService.buscarPorCpf(visitante.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o cpf "+ visitante.getCpf() +"")));
			
			this.visitanteService.buscarPorNome(visitante.getNome())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o nome "+ visitante.getNome() +"")));
			
		
	}
}

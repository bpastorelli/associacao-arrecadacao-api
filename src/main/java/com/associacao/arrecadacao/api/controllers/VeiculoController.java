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

import com.associacao.arrecadacao.api.dtos.VeiculoDto;
import com.associacao.arrecadacao.api.entities.Veiculo;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.VeiculoService;
import com.associacao.arrecadacao.api.services.VinculoVeiculoService;
import com.associacao.arrecadacao.api.services.VisitanteService;

@RestController
@RequestMapping("/associados/veiculo")
@CrossOrigin(origins = "*")
public class VeiculoController {
	
	private static final Logger log = LoggerFactory.getLogger(VeiculoController.class);
	
	@Autowired
	private VeiculoService veiculoService;
	
	@Autowired
	private VisitanteService visitanteService;
	
	@Autowired
	private VinculoVeiculoService vinculoVeiculoService;
	
	public VeiculoController() {
		
	}
	
	@PostMapping(value = "/novo")
	public ResponseEntity<?> CadastrarNovo(
			@Valid @RequestBody VeiculoDto veiculoRequestBody, 
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Cadastro de veiculo: {}", veiculoRequestBody.toString());
		Response<Veiculo> response = new Response<Veiculo>();
		
		validarDadosExistentes(veiculoRequestBody, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de veiculo: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		Veiculo veiculo = converterDtoVeiculo(veiculoRequestBody);
		this.veiculoService.persistir(veiculo);
		
		response.setData(veiculo);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	public void validarDadosExistentes(VeiculoDto dto, BindingResult result) {
		
		if(dto.getPlaca() == null) 
			result.addError(new ObjectError("veiculo", " Não existem veículos para cadastro!"));
		
		this.veiculoService.buscarPorPlaca(dto.getPlaca()).
			ifPresent(res -> result.addError(new ObjectError("veiculo", "A placa informada (" + dto.getPlaca() + ") já existe para o veiculo id " + res.getId() + "!") ));
		
		if(!this.visitanteService.buscarPorId(dto.getVisitanteId()).isPresent())
			result.addError(new ObjectError("veiculo", "Visitante inexistente!"));		
		
		this.vinculoVeiculoService.buscarPorPlacaAndVisitanteId(dto.getPlaca(), dto.getVisitanteId()).
			ifPresent(res -> result.addError(new ObjectError("veiculo", "Veiculo de placa " + dto.getPlaca() + " já vinculado para esta pessoa!")));
	
	}
	
	public Veiculo converterDtoVeiculo(VeiculoDto dto) {
		
		Veiculo veiculo = new Veiculo();
		veiculo.setPlaca(dto.getPlaca());
		veiculo.setMarca(dto.getMarca());
		veiculo.setModelo(dto.getModelo());
		veiculo.setAno(dto.getAno());
		
		return veiculo;
		
	}

}

package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.associacao.arrecadacao.api.dtos.AtualizaVeiculoDto;
import com.associacao.arrecadacao.api.dtos.VeiculoDto;
import com.associacao.arrecadacao.api.entities.Veiculo;
import com.associacao.arrecadacao.api.entities.VinculoVeiculo;
import com.associacao.arrecadacao.api.entities.Visitante;
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
		
		Veiculo veiculo = converterVeiculoDto(veiculoRequestBody);
		Visitante visitante = new Visitante(); 
		visitante.setId(veiculoRequestBody.getVisitanteId());
		this.veiculoService.persistir(veiculo);
		
		List<VinculoVeiculo> vinculos = new ArrayList<VinculoVeiculo>();
		VinculoVeiculo vinculo = new VinculoVeiculo();
		vinculo.setVeiculo(veiculo);
		vinculo.setVisitante(visitante);
		vinculos.add(vinculo);
		
		this.validarVinculoVeiculo(veiculo, visitante, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de vinculo: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		this.vinculoVeiculoService.persistir(vinculos);
		
		response.setData(veiculo);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	@PutMapping(value = "/veiculo/{id}")
	public ResponseEntity<?> CadastrarNovo(
			@PathVariable("id") Long id,
			@Valid @RequestBody AtualizaVeiculoDto veiculoRequestBody, 
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Cadastro de veiculo: {}", veiculoRequestBody.toString());
		Response<Veiculo> response = new Response<Veiculo>();
		
		Optional<Veiculo> veiculo = this.veiculoService.buscarPorId(id);
		atualizaVeiculo(veiculoRequestBody, veiculo.get(), result);
		
		this.veiculoService.persistir(veiculo.get());
		
		response.setData(veiculo.get());
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
		
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarVeiculos(
			@RequestParam(value = "placa", defaultValue = "null") String placa,
			@RequestParam(value = "marca", defaultValue = "null") String marca,
			@RequestParam(value = "modelo", defaultValue = "null") String modelo,
			@RequestParam(value = "ano", defaultValue = "0") Integer ano,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
			@RequestParam(value = "size", defaultValue = "10") int size) throws NoSuchAlgorithmException {
		
		log.info("Buscando veiculos...");
		
		PageRequest pageRequest = new PageRequest(pag, size, Direction.valueOf(dir), ord);
		Page<Veiculo> veiculos = null;
		
		if(!placa.replace("-", "").equals("null") || !marca.equals("null") || !modelo.equals("null") || ano != 0)
			veiculos = this.veiculoService.bucarPorIdAndPlacaAndMarcaAndModelo(0L, placa.replace("-", ""), marca, modelo, pageRequest);
		else
			veiculos = this.veiculoService.bucarTodos(pageRequest);
		
		if (veiculos.getSize() == 0) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.status(404).body("A consulta não retornou dados!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(veiculos.getContent());
		
	}
	
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<?> buscarVeiculoPorId(
			@PathVariable("id") Long id) throws NoSuchAlgorithmException {
		
		log.info("Buscando veiculo...");
		Response<Veiculo> response = new Response<Veiculo>();
		
		Optional<Veiculo> veiculo = null;
		veiculo = this.veiculoService.buscarPorId(id);
		if(!veiculo.isPresent()) {
			log.info("Veiculo não encontrado para o ID: {}", id);
			response.getErrors().add("Veículo não encontrada para o ID " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(veiculo);
		
	}
	
	@GetMapping(value = "/placa/{placa}")
	public ResponseEntity<?> buscarVeiculoPorPlaca(
			@PathVariable("placa") String placa) throws NoSuchAlgorithmException {
		
		log.info("Buscando veiculo...");
		Response<Veiculo> response = new Response<Veiculo>();
		
		Optional<Veiculo> veiculo = null;
		veiculo = this.veiculoService.buscarPorPlaca(placa.replace("-", ""));
		/*if(!veiculo.isPresent()) {
			log.info("Veiculo não encontrado para o ID: {}", placa.replace("-", ""));
			response.getErrors().add("Veículo não encontrada para a placa " + placa);
			return ResponseEntity.badRequest().body(response);
		}*/
		
		return ResponseEntity.status(HttpStatus.OK).body(veiculo);
		
	}
	
	public void validarDadosExistentes(VeiculoDto dto, BindingResult result) {
		
		if(dto.getPlaca() == null) 
			result.addError(new ObjectError("veiculo", " Não existem veículos para cadastro!"));
		
		this.veiculoService.buscarPorPlaca(dto.getPlaca().replace("-", "")).
			ifPresent(res -> result.addError(new ObjectError("veiculo", "A placa informada (" + dto.getPlaca() + ") já existe para o veiculo id " + res.getId() + "!") ));
		
		if(!this.visitanteService.buscarPorId(dto.getVisitanteId()).isPresent())
			result.addError(new ObjectError("veiculo", "Visitante inexistente!"));		
		
		this.vinculoVeiculoService.buscarPorPlacaAndVisitanteId(dto.getPlaca().replace("-", ""), dto.getVisitanteId()).
			ifPresent(res -> result.addError(new ObjectError("veiculo", "Veiculo de placa " + dto.getPlaca() + " já vinculado para esta pessoa!")));
	
	}
	
	public void validarVinculoVeiculo(Veiculo veiculo, Visitante visitante, BindingResult result) {
		
		this.vinculoVeiculoService.buscarPorVeiculoIdAndVisitanteId(veiculo.getId(), visitante.getId())
			.ifPresent(res -> result.addError(new ObjectError("vinculo","O veiculo já está associado para este visitante")));
		
		if(this.veiculoService.buscarPorId(veiculo.getId()).get().getPlaca().isEmpty())
			result.addError(new ObjectError("vinculo","O veiculo não existe"));
		
		if(this.visitanteService.buscarPorId(visitante.getId()).get().getNome().isEmpty())
			result.addError(new ObjectError("vinculo","O visitante não existe"));
		
	}
	
	public Veiculo converterVeiculoDto(VeiculoDto dto) {
		
		Veiculo veiculo = new Veiculo();
		veiculo.setPlaca(dto.getPlaca().toUpperCase());
		veiculo.setMarca(dto.getMarca().toUpperCase());
		veiculo.setModelo(dto.getModelo().toUpperCase());
		veiculo.setCor(dto.getCor().toUpperCase());
		veiculo.setAno(dto.getAno());
		
		return veiculo;
		
	}
	
	public Veiculo converterAtualizaVeiculoDto(AtualizaVeiculoDto dto) {
		
		Veiculo veiculo = new Veiculo();
		veiculo.setMarca(dto.getMarca().toUpperCase());
		veiculo.setModelo(dto.getModelo().toUpperCase());
		veiculo.setCor(dto.getCor().toUpperCase());
		veiculo.setAno(dto.getAno());
		
		return veiculo;
		
	}
	
	public void atualizaVeiculo(AtualizaVeiculoDto dto, Veiculo veiculo, BindingResult result) {
		
		veiculo.setMarca(dto.getMarca().toUpperCase());
		veiculo.setModelo(dto.getModelo().toUpperCase());
		veiculo.setCor(dto.getCor().toUpperCase());
		veiculo.setAno(dto.getAno());
		
	}

}

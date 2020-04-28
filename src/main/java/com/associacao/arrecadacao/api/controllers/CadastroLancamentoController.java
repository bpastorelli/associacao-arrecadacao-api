package com.associacao.arrecadacao.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.associacao.arrecadacao.api.dtos.CadastroLancamentoDto;
import com.associacao.arrecadacao.api.dtos.LancamentoResponseDto;
import com.associacao.arrecadacao.api.entities.Lancamento;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.LancamentoService;
import com.associacao.arrecadacao.api.services.ResidenciaService;
import com.associacao.arrecadacao.api.utils.Utils;

@RestController
@RequestMapping("/associados/lancamento")
@CrossOrigin(origins = "*")
public class CadastroLancamentoController {
	
	private static final Logger log = LoggerFactory.getLogger(CadastroLancamentoController.class);
	
	@Autowired
	private ResidenciaService residenciaService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
	
	public CadastroLancamentoController() {
		
	}
	
	@PostMapping("/residencia/{residenciaId}")
	public ResponseEntity<Response<CadastroLancamentoDto>> cadastrar(@PathVariable("residenciaId") Long residenciaId, @Valid @RequestBody CadastroLancamentoDto cadastroLancamentoDto,
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Cadastrando um lançamento: {}", cadastroLancamentoDto.toString());
		
		Response<CadastroLancamentoDto> response = new Response<CadastroLancamentoDto>();
		
		cadastroLancamentoDto.getLancamentos().forEach(p -> p.setResidenciaId(residenciaId));
		
		List<Lancamento> lancamentos = this.converterDtoParaLancamento(cadastroLancamentoDto);
		validarDadosExistentes(cadastroLancamentoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do processo: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.lancamentoService.persistir(lancamentos);
		response.setData(this.converterCadastroLancamentoDto(lancamentos));
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna a listagem de lançamentos de uma residência.
	 * 
	 * @param residenciaId
	 * @return ResponseEntity<Response<CadastroLancamentoDto>>
	 */
	@GetMapping(value = "/residencia/{residenciaId}")
	public ResponseEntity<Response<Page<LancamentoResponseDto>>> listarPorResidenciaId(
			@PathVariable("residenciaId") Long residenciaId,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		log.info("Buscando lançamentos por ID da residência: {}, página: {}", residenciaId, pag);
		Response<Page<LancamentoResponseDto>> response = new Response<Page<LancamentoResponseDto>>();

		PageRequest pageRequest = new PageRequest(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		Page<Lancamento> lancamentos = this.lancamentoService.buscarPorResidenciaId(residenciaId, pageRequest);
		Page<LancamentoResponseDto> lancamentosDto = lancamentos.map(lancamento -> this.converterDtoParaLancamento(lancamento));

		response.setData(lancamentosDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna um lançamento de uma residência.
	 * 
	 * @param periodo
	 * @param residenciaId
	 * @return ResponseEntity<Response<LancamentoResponseDto>>
	 */
	@GetMapping()
	public ResponseEntity<Response<LancamentoResponseDto>> listarPorPeriodoAndResidenciaId(
			@RequestParam(value = "periodo", defaultValue = "") String periodo,
			@RequestParam(value = "residenciaId", defaultValue = "0") Long residenciaId) {
		
		log.info("Buscando lançamentos por ID da residencia: {}, periodo: {}", residenciaId, periodo);
		Response<LancamentoResponseDto> response = new Response<LancamentoResponseDto>();

		Optional<Lancamento> lancamentos = this.lancamentoService.buscarPorPeriodoAndResidenciaId(periodo, residenciaId);
		
		if (!lancamentos.isPresent()) {
			log.info("Erro ao buscar um lançamento Residencia ID: {} e Periodo {}: ser inválido.", residenciaId, periodo);
			response.getErrors().add("Erro ao buscar lançamento. Registro não encontrado para residencia ID: " + residenciaId + " e periodo: " + periodo + ".");
			return ResponseEntity.badRequest().body(response);
		}
		
		LancamentoResponseDto lancamentosDto =  this.converterDtoParaLancamento(lancamentos.get());

		response.setData(lancamentosDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Remove um lançamento por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Lancamento>>
	 */
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo lançamento: {}", id);
		Response<String> response = new Response<String>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

		if (!lancamento.isPresent()) {
			log.info("Erro ao remover devido ao lançamento ID: {} ser inválido.", id);
			response.getErrors().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.lancamentoService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	/**
	 * Converter o CadastroLancamentoDto para Lançamento.
	 * 
	 * @param cadastroLancamentoDto
	 * @return Lancamento
	 */
	private List<Lancamento> converterDtoParaLancamento(CadastroLancamentoDto cadastroLancamentoDto) {
		
		List<Lancamento> lancamentos = new ArrayList<Lancamento>();
		for(Lancamento lancamento : cadastroLancamentoDto.getLancamentos()) {
			Lancamento item = new Lancamento();
			item.setPeriodo(lancamento.getPeriodo());
			item.setValor(lancamento.getValor());
			item.setResidenciaId(lancamento.getResidenciaId());
			lancamentos.add(item);
		}
		
		return lancamentos;
	}
	
	/**
	 * Converter o LancamentoResponseDto para Lançamento.
	 * 
	 * @param LancamentoResponseDto
	 * @return Lancamento
	 */
	private LancamentoResponseDto converterDtoParaLancamento(Lancamento lancamento) {
	
		LancamentoResponseDto item = new LancamentoResponseDto();
		item.setId(lancamento.getId());
		item.setPeriodo(lancamento.getPeriodo());
		item.setValor(lancamento.getValor());
		item.setDataAtualizacao(Utils.dateFormat(lancamento.getDataAtualizacao(),"dd/MM/yyyy"));
		item.setDataCriacao(Utils.dateFormat(lancamento.getDataCriacao(),"dd/MM/yyyy"));
		item.setDataPagamento(Utils.dateFormat(lancamento.getDataPagamento(),"dd/MM/yyyy"));
		item.setResidenciaId(lancamento.getResidenciaId());
		
		return item;
	}
	
	/**
	 * Converter a lista de lancamentos em CadastroLancamentoDto
	 * 
	 * @param lancamentos
	 * @return CadastroLancamentoDto
	 */
	public CadastroLancamentoDto converterCadastroLancamentoDto(List<Lancamento> lancamentos) {
		
		CadastroLancamentoDto cadastroLancamentoDto = new CadastroLancamentoDto();
		cadastroLancamentoDto.setLancamentos(lancamentos);
		
		return cadastroLancamentoDto;
		
	}
	
	private void validarDadosExistentes(CadastroLancamentoDto cadastroLancamentoDto, BindingResult result) {

		for(Lancamento lancamento : cadastroLancamentoDto.getLancamentos()) {
			
			if(!residenciaService.buscarPorId(lancamento.getResidenciaId()).isPresent())
				result.addError(new ObjectError("lancamento", "O código de residencia " + lancamento.getResidenciaId() + " não existe."));			
			
			if(lancamentoService.buscarPorPeriodoAndResidenciaId(lancamento.getPeriodo(), lancamento.getResidenciaId()).isPresent())
				result.addError(new ObjectError("lancamento", "O lançamento para o Periodo " + lancamento.getPeriodo() + " já existe."));
				
			if(lancamento.getPeriodo().isEmpty())
				result.addError(new ObjectError("lancamento", "O campo Periodo é obrigatório."));
				
			if(lancamento.getPeriodo().length() < 7)
				result.addError(new ObjectError("lancamento", "O campo Periodo deve conter 7 caracteres (MM/YYYY) Exemplo: 09/2019."));	
				
			if(lancamento.getPeriodo().length() > 7)
				result.addError(new ObjectError("lancamento", "O campo Periodo deve conter 7 caracteres (MM/YYYY) Exemplo: 09/2019."));
				
			if(lancamento.getValor() == new BigDecimal(0L)) 
				result.addError(new ObjectError("lancamento", "O campo Valor não pode ser zero."));

			if(cadastroLancamentoDto.getLancamentos().stream()
				.filter(p -> p.getPeriodo().equals(lancamento.getPeriodo()))
				.filter(x -> x.getValor().equals(lancamento.getValor()))
				.count() > 1) 
			result.addError(new ObjectError("lancamento", "O lancamento para o periodo " + lancamento.getPeriodo() + " no valor de " + lancamento.getValor() + " está duplicado."));
			
		}
		
	}

}

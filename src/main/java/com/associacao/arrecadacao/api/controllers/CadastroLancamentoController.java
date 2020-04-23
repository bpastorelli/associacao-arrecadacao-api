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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.associacao.arrecadacao.api.dtos.CadastroLancamentoDto;
import com.associacao.arrecadacao.api.entities.Lancamento;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.LancamentoService;
import com.associacao.arrecadacao.api.services.ResidenciaService;

@RestController
@RequestMapping("/associados/lancamento")
@CrossOrigin(origins = "*")
public class CadastroLancamentoController {
	
	private static final Logger log = LoggerFactory.getLogger(CadastroLancamentoController.class);
	
	@Autowired
	private ResidenciaService residenciaService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	public CadastroLancamentoController() {
		
	}
	
	@PostMapping("/{residenciaId}")
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

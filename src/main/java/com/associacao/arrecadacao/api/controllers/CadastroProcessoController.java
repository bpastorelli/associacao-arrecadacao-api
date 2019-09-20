package com.associacao.arrecadacao.api.controllers;

import java.math.BigDecimal;
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
import com.associacao.arrecadacao.api.entities.Lancamento;
import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.enums.PerfilEnum;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.LancamentoService;
import com.associacao.arrecadacao.api.services.MoradorService;
import com.associacao.arrecadacao.api.services.ResidenciaService;

@RestController
@RequestMapping("/associados/processo")
@CrossOrigin(origins = "*")
public class CadastroProcessoController {

	private static final Logger log = LoggerFactory.getLogger(CadastroProcessoController.class);
	
	@Autowired
	private MoradorService moradorService;
	
	@Autowired
	private ResidenciaService residenciaService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	public CadastroProcessoController() {
	}
	
	@PostMapping
	public ResponseEntity<Response<CadastroResidenciaDto>> cadastrar(@Valid @RequestBody CadastroResidenciaDto cadastroResidenciaDto,
			BindingResult result) throws NoSuchAlgorithmException{
		log.info("Cadastrando processo de associado: {}", cadastroResidenciaDto.toString());
		Response<CadastroResidenciaDto> response = new Response<CadastroResidenciaDto>();
		
		Residencia residencia = this.converterDtoParaResidencia(cadastroResidenciaDto);
		List<Morador> moradores = this.converterDtoParaMorador(cadastroResidenciaDto);
		List<Lancamento> lancamentos = this.converterDtoParaLancamento(cadastroResidenciaDto);
		cadastroResidenciaDto.setMoradores(moradores);
		cadastroResidenciaDto.setLancamentos(lancamentos);
		validarDadosExistentes(cadastroResidenciaDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do processo: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.residenciaService.persistir(residencia);
		moradores.forEach(p -> p.setResidencia(residencia.getId()));
		this.moradorService.persistir(moradores);
		lancamentos.forEach(p -> p.setResidenciaId(residencia.getId()));
		this.lancamentoService.persistir(lancamentos);
		
		response.setData(this.converterCadastroProcessoDto(residencia, moradores, lancamentos));
		return ResponseEntity.ok(response);
	}
	
	private void validarDadosExistentes(CadastroResidenciaDto cadastroResidenciaDto, BindingResult result) {
		this.residenciaService.bucarPorMatricula(cadastroResidenciaDto.getMatricula())
				.ifPresent(res -> result.addError(new ObjectError("residencia", "Residência já existente")));
		
		if(cadastroResidenciaDto.getMoradores().size() == 0) {
			result.addError(new ObjectError("morador", "Você deve informar ao menos um morador."));
		}
		
		for(Morador morador : cadastroResidenciaDto.getMoradores()) {
			if(morador.getNome().isEmpty())
				result.addError(new ObjectError("morador", "O campo Nome é obrigatório."));
			
			if(morador.getCpf().isEmpty())
				result.addError(new ObjectError("morador", "O campo CPF é obrigatório."));
			
			if(morador.getSenha().isEmpty())
				result.addError(new ObjectError("morador", "O campo Senha é obrigatório."));
			
			if(morador.getRg().isEmpty())
				result.addError(new ObjectError("morador", "O campo RG é obrigatório."));
			
			if(morador.getEmail().isEmpty())
				result.addError(new ObjectError("morador", "O campo e-mail é obrigatório."));
			
			if(morador.getTelefone().isEmpty() && morador.getCelular().isEmpty())
				result.addError(new ObjectError("morador", "Você deve informar um número de telefone ou celular."));
			
			if(morador.getPerfil().toString().isEmpty())
				result.addError(new ObjectError("morador", "O campo Perfi é obrigatório."));
		}
		
		if(cadastroResidenciaDto.getLancamentos().size() > 0) {
			for(Lancamento lancamento : cadastroResidenciaDto.getLancamentos()) {
				if(lancamento.getPeriodo().isEmpty())
					result.addError(new ObjectError("lancamento", "O campo Periodo é obrigatório."));
				
				if(lancamento.getPeriodo().length() < 7)
					result.addError(new ObjectError("lancamento", "O campo Periodo deve conter 7 caracteres (MM/YYYY) Exemplo: 09/2019."));	
				
				if(lancamento.getPeriodo().length() > 7)
					result.addError(new ObjectError("lancamento", "O campo Periodo deve conter 7 caracteres (MM/YYYY) Exemplo: 09/2019."));
				
				if(lancamento.getValor() == new BigDecimal(0L)) 
					result.addError(new ObjectError("lancamento", "O campo Valor não pode ser zero."));

			}
		}
		
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
		
		for(Lancamento lancamento : cadastroResidenciaDto.getLancamentos()) {
			this.lancamentoService.buscarPorPeriodo(lancamento.getPeriodo())
					.ifPresent(res -> result.addError(new ObjectError("lancamento", "Periodo " + lancamento.getPeriodo() + " já existente")));
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
		residencia.setEndereco(cadastroResidenciaDto.getEndereco());
		residencia.setNumero(cadastroResidenciaDto.getNumero());
		residencia.setBairro(cadastroResidenciaDto.getBairro());
		residencia.setCep(cadastroResidenciaDto.getCep());
		residencia.setCidade(cadastroResidenciaDto.getCidade());
		residencia.setUf(cadastroResidenciaDto.getUf());
		return residencia;
	}
	
	/**
	 * Converter o CadastroResidenciaDto para Morador.
	 * 
	 * @param cadastroResidenciaDto
	 * @return Morador
	 */
	public List<Morador> converterDtoParaMorador(CadastroResidenciaDto cadastroResidenciaDto){
		
		List<Morador> moradores = new ArrayList<Morador>();
		for(Morador morador : cadastroResidenciaDto.getMoradores()) {
			Morador item = new Morador();
			item.setNome(morador.getNome());
			item.setCpf(morador.getCpf());
			item.setRg(morador.getRg());
			item.setEmail(morador.getEmail());
			item.setSenha(morador.getCpf().substring(0, 6));
			item.setPerfil(PerfilEnum.ROLE_USUARIO);
			item.setTelefone(morador.getTelefone());
			item.setCelular(morador.getCelular());
			item.setResidencia(null);
			moradores.add(item);
		}
		
		return moradores;
	}
	
	/**
	 * Converter o CadastroResidenciaDto para Lancamento.
	 * 
	 * @param cadastroResidenciaDto
	 * @return Lancamento
	 */
	public List<Lancamento> converterDtoParaLancamento(CadastroResidenciaDto cadastroResidenciaDto){
		
		List<Lancamento> lancamentos = new ArrayList<Lancamento>();
		for(Lancamento lancamento : cadastroResidenciaDto.getLancamentos()) {
			Lancamento item = new Lancamento();
			item.setPeriodo(lancamento.getPeriodo());
			item.setValor(lancamento.getValor());
			item.setResidenciaId(lancamento.getResidenciaId());
			lancamentos.add(item);
		}
		
		return lancamentos;
	}
	
	/**
	 * Converter o objeto tipo Residencia para o tipo CadastroResidenciaDto.
	 * 
	 * @param residencia
	 * @return CadastroResidenciaDto
	 */
	private CadastroResidenciaDto converterCadastroProcessoDto(Residencia residencia, List<Morador> moradores, List<Lancamento> lancamentos) {
		
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
		cadastroResidenciaDto.setMoradores(moradores);
		cadastroResidenciaDto.setLancamentos(lancamentos);
		return cadastroResidenciaDto;
	}
	
}
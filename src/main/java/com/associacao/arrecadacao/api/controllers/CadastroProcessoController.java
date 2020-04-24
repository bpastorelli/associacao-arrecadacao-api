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

import com.associacao.arrecadacao.api.commons.ValidaCPF;
import com.associacao.arrecadacao.api.dtos.CadastroProcessoDto;
import com.associacao.arrecadacao.api.entities.Lancamento;
import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.entities.VinculoResidencia;
import com.associacao.arrecadacao.api.enums.PerfilEnum;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.LancamentoService;
import com.associacao.arrecadacao.api.services.MoradorService;
import com.associacao.arrecadacao.api.services.ResidenciaService;
import com.associacao.arrecadacao.api.services.VinculoResidenciaService;
import com.associacao.arrecadacao.api.utils.PasswordUtils;

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
	private VinculoResidenciaService vinculoResidenciaService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	public CadastroProcessoController() {
	}
	
	/**
	 * Adiciona um novo processo de cadastro.
	 * 
	 * @param cadastroProcessoDto
	 * @param result
	 * @return ResponseEnity<Response<CadastroProcessoDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<CadastroProcessoDto>> cadastrar(@Valid @RequestBody CadastroProcessoDto cadastroProcessoDto,
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Cadastrando processo de associado: {}", cadastroProcessoDto.toString());
		Response<CadastroProcessoDto> response = new Response<CadastroProcessoDto>();
		
		Residencia residencia = this.converterDtoParaResidencia(cadastroProcessoDto);
		List<Morador> moradores = this.converterDtoParaMorador(cadastroProcessoDto);
		List<Lancamento> lancamentos = this.converterDtoParaLancamento(cadastroProcessoDto);
		List<VinculoResidencia> vinculos = new ArrayList<VinculoResidencia>();
		cadastroProcessoDto.setMoradores(moradores);
		cadastroProcessoDto.setLancamentos(lancamentos);
		validarDadosExistentes(cadastroProcessoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do processo: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.residenciaService.persistir(residencia);
		moradores.forEach(p -> p.setResidenciaId(residencia.getId()));
		this.moradorService.persistir(moradores);
		vinculos = this.converterDtoParaVinculoResidencia(moradores, residencia.getId());
		this.vinculoResidenciaService.persistir(vinculos);
		lancamentos.forEach(p -> p.setResidenciaId(residencia.getId()));
		this.lancamentoService.persistir(lancamentos);
		
		response.setData(this.converterCadastroProcessoDto(residencia, moradores, lancamentos));
		return ResponseEntity.ok(response);
	}
	
	private void validarDadosExistentes(CadastroProcessoDto cadastroResidenciaDto, BindingResult result) {
		
		this.residenciaService.buscarPorMatricula(cadastroResidenciaDto.getMatricula())
				.ifPresent(res -> result.addError(new ObjectError("residencia", "Residência já existente")));
		
		if(cadastroResidenciaDto.getMoradores().size() == 0) {
			result.addError(new ObjectError("morador", "Você deve informar ao menos um morador."));
		}
		
		for(Morador morador : cadastroResidenciaDto.getMoradores()) {
			if(morador.getNome().isEmpty())
				result.addError(new ObjectError("morador", "O campo Nome é obrigatório."));
			
			if(morador.getCpf().isEmpty())
				result.addError(new ObjectError("morador", "O campo CPF é obrigatório."));
			
			if(!ValidaCPF.isCPF(morador.getCpf()))
				result.addError(new ObjectError("morador", "CPF inválido."));
			
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

				if(cadastroResidenciaDto.getLancamentos().stream()
						.filter(p -> p.getPeriodo().equals(lancamento.getPeriodo()))
						.filter(x -> x.getValor().equals(lancamento.getValor()))
						.count() > 1) 
					result.addError(new ObjectError("lancamento", "O lancamento para o periodo " + lancamento.getPeriodo() + " no valor de " + lancamento.getValor() + " está duplicado."));
			}
			
		}
		
		cadastroResidenciaDto.getMoradores().forEach(morador ->{
			this.moradorService.buscarPorCpf(morador.getCpf())
				.ifPresent(res -> result.addError(new ObjectError("morador", "CPF " + morador.getCpf() + " já existente")));	
		});
		
		cadastroResidenciaDto.getMoradores().forEach(morador ->{
			this.moradorService.buscarPorRg(morador.getRg())
				.ifPresent(res -> result.addError(new ObjectError("morador", "RG " + morador.getRg() + " já existente")));	
		});
	
		cadastroResidenciaDto.getMoradores().forEach(morador ->{
			this.moradorService.bucarPorEmail(morador.getEmail())
				.ifPresent(res -> result.addError(new ObjectError("morador", "E-mail " + morador.getEmail() + " já existente")));	
		});
		
		//Valida se o  não está duplicado na requisição.
		cadastroResidenciaDto.getMoradores().forEach(morador -> {
			if(cadastroResidenciaDto.getMoradores()
					.stream()
					.filter(pessoa -> pessoa.getCpf()
					.equals(morador.getCpf())).count() > 1)
				result.addError(new ObjectError("morador", "CPF " + morador.getCpf() + " está duplicado."));
		});		
		
		//Valida se o RG não está duplicado na requisição.
		cadastroResidenciaDto.getMoradores().forEach(morador -> {
			if(cadastroResidenciaDto.getMoradores()
					.stream()
					.filter(pessoa -> pessoa.getRg()
					.equals(morador.getRg())).count() > 1)
				result.addError(new ObjectError("morador", "RG " + morador.getRg() + " está duplicado."));
		});
		
		//Valida se o RG não está duplicado na requisição.
		cadastroResidenciaDto.getMoradores().forEach(morador -> {
			if(cadastroResidenciaDto.getMoradores()
					.stream()
					.filter(pessoa -> pessoa.getEmail()
					.equals(morador.getEmail())).count() > 1) {
				result.addError(new ObjectError("morador", "E-mail " + morador.getEmail() + " está duplicado."));				
			}
		});
		
		//Valida se o Periodo não está duplicado na requisição.
		cadastroResidenciaDto.getLancamentos().forEach(lance -> {
			if(cadastroResidenciaDto.getLancamentos()
					.stream()
					.filter(lancamento -> lancamento.getPeriodo()
					.equals(lance.getPeriodo())).count() > 1) {
				result.addError(new ObjectError("lançamento", "Período " + lance.getPeriodo() + " está duplicado."));				
			}
		});
		
	}
	
	/**
	 * Converter o CadastroProcessoDto para Residencia.
	 * 
	 * @param cadastroProcessoDto
	 * @return Residencia
	 */
	private Residencia converterDtoParaResidencia(CadastroProcessoDto cadastroProcessoDto) {
		
		Residencia residencia = new Residencia();
		residencia.setMatricula(cadastroProcessoDto.getMatricula());
		residencia.setEndereco(cadastroProcessoDto.getEndereco());
		residencia.setNumero(cadastroProcessoDto.getNumero());
		residencia.setBairro(cadastroProcessoDto.getBairro());
		residencia.setCep(cadastroProcessoDto.getCep());
		residencia.setCidade(cadastroProcessoDto.getCidade());
		residencia.setUf(cadastroProcessoDto.getUf());
		return residencia;
	}
	
	/**
	 * Converter o CadastroProcessoDto para Morador.
	 * 
	 * @param cadastroResidenciaDto
	 * @return Morador
	 */
	public List<Morador> converterDtoParaMorador(CadastroProcessoDto cadastroProcessoDto){
		
		List<Morador> moradores = new ArrayList<Morador>();
		for(Morador morador : cadastroProcessoDto.getMoradores()) {
			Morador item = new Morador();
			item.setNome(morador.getNome());
			item.setCpf(morador.getCpf());
			item.setRg(morador.getRg());
			item.setEmail(morador.getEmail());
			item.setSenha(PasswordUtils.gerarBCrypt(morador.getCpf().substring(0, 6)));
			item.setPerfil(PerfilEnum.ROLE_USUARIO);
			item.setTelefone(morador.getTelefone());
			item.setCelular(morador.getCelular());
			moradores.add(item);
		}
		
		return moradores;
	}
	
	/**
	 * Converter o CadastroProcessoDto para Lancamento.
	 * 
	 * @param cadastroResidenciaDto
	 * @return Lancamento
	 */
	public List<Lancamento> converterDtoParaLancamento(CadastroProcessoDto cadastroProcessoDto){
		
		List<Lancamento> lancamentos = new ArrayList<Lancamento>();
		for(Lancamento lancamento : cadastroProcessoDto.getLancamentos()) {
			Lancamento item = new Lancamento();
			item.setPeriodo(lancamento.getPeriodo());
			item.setValor(lancamento.getValor());
			item.setResidenciaId(lancamento.getResidenciaId());
			lancamentos.add(item);
		}
		
		return lancamentos;
	}
	
	/**
	 * Converter o CadastroProcessoDto para VinculoResidencia.
	 * 
	 * @param moradores
	 * @param residenciaId
	 * @return VinculoResidencia
	 */
	public List<VinculoResidencia> converterDtoParaVinculoResidencia(List<Morador> moradores, Long residenciaId){
		
		List<VinculoResidencia> vinculos = new ArrayList<VinculoResidencia>();
		
		moradores.forEach(m -> {
			VinculoResidencia vinculo = new VinculoResidencia();
			vinculo.setMoradorId(m.getId());
			vinculo.setResidenciaId(residenciaId);
			vinculos.add(vinculo);
		});
		return vinculos;
	}
	
	/**
	 * Converter o objeto tipo Residencia para o tipo CadastroProcessoDto.
	 * 
	 * @param residencia
	 * @return CadastroProcessoDto
	 */
	private CadastroProcessoDto converterCadastroProcessoDto(Residencia residencia, List<Morador> moradores, List<Lancamento> lancamentos) {
		
		CadastroProcessoDto cadastroProcessoDto = new CadastroProcessoDto();
		cadastroProcessoDto.setId(residencia.getId());
		cadastroProcessoDto.setMatricula(residencia.getMatricula());
		cadastroProcessoDto.setEndereco(residencia.getEndereco());
		cadastroProcessoDto.setNumero(residencia.getNumero());
		cadastroProcessoDto.setBairro(residencia.getBairro());
		cadastroProcessoDto.setCep(residencia.getCep());
		cadastroProcessoDto.setCidade(residencia.getCidade());
		cadastroProcessoDto.setUf(residencia.getUf());
		cadastroProcessoDto.setCidade(residencia.getCidade());
		cadastroProcessoDto.setMoradores(moradores);
		cadastroProcessoDto.setLancamentos(lancamentos);
		return cadastroProcessoDto;
	}
	
}
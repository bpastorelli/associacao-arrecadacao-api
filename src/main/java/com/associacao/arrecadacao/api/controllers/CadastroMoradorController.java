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

import com.associacao.arrecadacao.api.commons.ValidaCPF;
import com.associacao.arrecadacao.api.dtos.CadastroMoradorDto;
import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.entities.VinculoResidencia;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.MoradorService;
import com.associacao.arrecadacao.api.services.ResidenciaService;
import com.associacao.arrecadacao.api.services.VinculoResidenciaService;

@RestController
@RequestMapping("/associados/morador")
@CrossOrigin(origins = "*")
public class CadastroMoradorController {
	
	private static final Logger log = LoggerFactory.getLogger(CadastroMoradorController.class);
	
	@Autowired
	private MoradorService moradorService;
	
	@Autowired
	private ResidenciaService residenciaService;
	
	@Autowired
	private VinculoResidenciaService vinculoResidenciaService;
	
	public CadastroMoradorController() {
		
	}
	
	@PostMapping
	public ResponseEntity<Response<CadastroMoradorDto>> cadastrar(@Valid @RequestBody CadastroMoradorDto cadastroMoradorDto, 
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Cadastrando de morador: {}", cadastroMoradorDto.toString());
		Response<CadastroMoradorDto> response = new Response<CadastroMoradorDto>();
		
		List<Morador> moradores = this.converterDtoParaMorador(cadastroMoradorDto);
		validarDadosExistentes(cadastroMoradorDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do(s) morador(es): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.moradorService.persistir(moradores);
		this.vinculoResidenciaService.persistir(this.converterDtoParaVinculoResidencia(cadastroMoradorDto));
		response.setData(this.converterCadastroMoradorDto(moradores));
		return ResponseEntity.ok(response);
		
	}
	
	private void validarDadosExistentes(CadastroMoradorDto cadastroMoradorDto, BindingResult result) {
		
		if(cadastroMoradorDto.getMoradores().size() == 0) {
			result.addError(new ObjectError("morador", "Você deve informar ao menos um morador."));
		}
		
		for(Morador morador : cadastroMoradorDto.getMoradores()) {
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
		
		for(Morador morador : cadastroMoradorDto.getMoradores()) {
			this.moradorService.buscarPorCpf(morador.getCpf())
				.ifPresent(res -> result.addError(new ObjectError("morador", "CPF " + morador.getCpf() + " já existente")));
		}
		
		for(Morador morador : cadastroMoradorDto.getMoradores()) {
			this.moradorService.buscarPorRg(morador.getRg())
				.ifPresent(res -> result.addError(new ObjectError("morador", "RG " + morador.getRg() + " já existente")));
		}
		
		for(Morador morador : cadastroMoradorDto.getMoradores()) {
			if(this.moradorService.bucarPorEmail(morador.getEmail()).size() > 0)
				result.addError(new ObjectError("morador", "E-mail " + morador.getEmail() + " já existente"));
		}
		
	}
	
	/**
	 * Converter o CadastroProcessoDto para Morador.
	 * 
	 * @param cadastroResidenciaDto
	 * @return Morador
	 */
	public List<Morador> converterDtoParaMorador(CadastroMoradorDto cadastroMoradorDto){
		
		List<Morador> moradores = new ArrayList<Morador>();
		for(Morador morador : cadastroMoradorDto.getMoradores()) {
			Morador item = new Morador();
			item.setNome(morador.getNome());
			item.setCpf(morador.getCpf());
			item.setRg(morador.getRg());
			item.setEmail(morador.getEmail());
			item.setSenha(morador.getCpf().substring(0, 6));
			item.setPerfil(morador.getPerfil());
			item.setTelefone(morador.getTelefone());
			item.setCelular(morador.getCelular());
			moradores.add(item);
		}
		
		return moradores;
	}

	/**
	 * Converter o objeto tipo Residencia para o tipo CadastroProcessoDto.
	 * 
	 * @param residencia
	 * @return CadastroMoradorDto
	 */
	private CadastroMoradorDto converterCadastroMoradorDto(List<Morador> moradores) {
		
		CadastroMoradorDto dto = new CadastroMoradorDto();
		List<Morador> lista = new ArrayList<Morador>();
		
		dto.getMoradores().forEach(m ->{
			moradores.forEach(p ->{
				if(m.getCpf().equals(p.getCpf())) {					
					Morador morador = new Morador();
					morador.setId(p.getId());
					morador.setNome(p.getNome());
					morador.setCpf(p.getCpf());
					morador.setRg(p.getRg());
					morador.setEmail(p.getEmail());
					morador.setSenha(p.getSenha());
					morador.setPerfil(p.getPerfil());
					morador.setTelefone(p.getTelefone());
					morador.setCelular(p.getCelular());
					morador.setDataAtualizacao(p.getDataAtualizacao());
					morador.setDataCriacao(p.getDataCriacao());
					
					if(!lista.contains(morador))
						lista.add(morador);
				}
			});
		});
		
		dto.setMoradores(lista);
		return dto;
	}
	
	/**
	 * Converter o CadastroProcessoDto para VinculoResidencia.
	 * 
	 * @param moradores
	 * @param residenciaId
	 * @return VinculoResidencia
	 */
	public List<VinculoResidencia> converterDtoParaVinculoResidencia(CadastroMoradorDto cadastroMoradorDto){
		
		List<VinculoResidencia> vinculos = new ArrayList<VinculoResidencia>();
		cadastroMoradorDto.getMoradores().forEach(m -> {
			VinculoResidencia vinculo = new VinculoResidencia();
			vinculo.setMoradorId(this.moradorService.buscarPorCpf(m.getCpf()).get().getId());
			vinculos.add(vinculo);
		});
		return vinculos;
	}
}

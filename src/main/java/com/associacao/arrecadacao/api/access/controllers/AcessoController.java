package com.associacao.arrecadacao.api.access.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.associacao.arrecadacao.api.access.dtos.CadastroAcessoDto;
import com.associacao.arrecadacao.api.access.entities.Acesso;
import com.associacao.arrecadacao.api.access.services.AcessoService;
import com.associacao.arrecadacao.api.access.services.FuncionalidadeService;
import com.associacao.arrecadacao.api.access.services.ModuloService;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.MoradorService;

@RestController
@RequestMapping("/associados/acesso")
@CrossOrigin(origins = "*")
public class AcessoController {
	
	private static final Logger log = LoggerFactory.getLogger(AcessoController.class);
	
	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private MoradorService moradorService;
	
	@Autowired
	private ModuloService moduloService;
	
	@Autowired
	private FuncionalidadeService funcionalidadeService;
	
	public AcessoController() {
		
	}
	
	@PostMapping(value = "/incluir")
	public ResponseEntity<?> cadastrar(@Valid @RequestBody List<CadastroAcessoDto> cadastroAcessoDto, 
									BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastro de acessos: {}", cadastroAcessoDto.toString());
		Response<List<Acesso>> response = new Response<List<Acesso>>();
		
		List<Acesso> acessos = validarDados(cadastroAcessoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de acessos: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		acessos = this.acessoService.persistir(acessos);
		
		response.setData(acessos);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	@GetMapping("/filtro")
	public ResponseEntity<?> buscarAcessos(			
			@RequestParam(value = "idUsuario", defaultValue = "0") Long idUsuario,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
			@RequestParam(value = "size", defaultValue = "10") int size) throws NoSuchAlgorithmException {
		
		log.info("Buscando acessos..");
		
		PageRequest pageRequest = new PageRequest(pag, size, Direction.valueOf(dir), ord);
		Page<List<Acesso>> acessos = null;
		
		if(idUsuario != 0 && idUsuario != null)
			acessos =  acessoService.buscarPorUsuarioId(idUsuario, pageRequest);
		else
			acessos = acessoService.buscarTodos(pageRequest);
		
		if (acessos.getSize() == 0) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.status(404).body("A consulta não retornou dados!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(acessos.getContent());
		
	}
	
	public List<Acesso> validarDados(List<CadastroAcessoDto> listDto, BindingResult result) {
		
		List<Acesso> listAcesso = new ArrayList<Acesso>();
		
		listDto.forEach(d -> {
			
			if(!this.moradorService.buscarPorId(d.getIdUsuario()).isPresent()) {
				result.addError(new ObjectError("morador", "Usuário inexistente para o código " + d.getIdUsuario()));
			}
			
			if(!this.moduloService.buscarPorId(d.getIdModulo()).isPresent()) {
				result.addError(new ObjectError("módulo", "Módulo inexistente para o código " + d.getIdModulo()));
			}
			
			if(!this.funcionalidadeService.buscarPorId(d.getIdFuncionalidade()).isPresent()) {
				result.addError(new ObjectError("funcionalidade", "Funcionalidade inexistente para o código " + d.getIdFuncionalidade()));
			}
			
			if(!result.hasErrors()) {
				Acesso acesso = new Acesso();
				acesso.setIdUsuario(d.getIdUsuario());	
				acesso.setIdModulo(d.getIdModulo());
				acesso.setIdFuncionalidade(d.getIdFuncionalidade());
				listAcesso.add(acesso);
			}
			
		});
		
		return listAcesso;
		
	}
	
	

}

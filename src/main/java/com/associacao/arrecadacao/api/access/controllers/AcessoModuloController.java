package com.associacao.arrecadacao.api.access.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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

import com.associacao.arrecadacao.api.access.dtos.AtualizaAcessoModuloDto;
import com.associacao.arrecadacao.api.access.dtos.CadastroAcessoModuloDto;
import com.associacao.arrecadacao.api.access.entities.AcessoModulo;
import com.associacao.arrecadacao.api.access.services.AcessoModuloService;
import com.associacao.arrecadacao.api.access.services.ModuloService;
import com.associacao.arrecadacao.api.dtos.AtualizaMoradorDto;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.MoradorService;

@RestController
@RequestMapping("/associados/acessoModulo")
@CrossOrigin(origins = "*")
public class AcessoModuloController {
	
	private static final Logger log = LoggerFactory.getLogger(AcessoModuloController.class);
	
	@Autowired
	private ModuloService moduloService;
	
	@Autowired
	private MoradorService moradorService;
	
	@Autowired
	private AcessoModuloService acessoModuloService;
	
	
	public AcessoModuloController() {
		
	}
	
	@PostMapping(value = "/incluir")
	public ResponseEntity<?> cadastrar(@Valid @RequestBody List<CadastroAcessoModuloDto> cadastroAcessoDto, 
									BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastro de acessos: {}", cadastroAcessoDto.toString());
		Response<List<AcessoModulo>> response = new Response<List<AcessoModulo>>();
		
		List<AcessoModulo> acessos = validarDadosPost(cadastroAcessoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de acessos: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		acessos = this.acessoModuloService.persistir(acessos);
		
		response.setData(acessos);
		return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}
	
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarAcessos(			
			@RequestParam(value = "idUsuario", defaultValue = "0") Long idUsuario,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
			@RequestParam(value = "size", defaultValue = "10") int size) throws NoSuchAlgorithmException {
		
		log.info("Buscando acessos..");
		
		PageRequest pageRequest = new PageRequest(pag, size, Direction.valueOf(dir), ord);
		Page<AcessoModulo> acessos = null;
		
		if(idUsuario != 0 && idUsuario != null)
			acessos =  acessoModuloService.buscarPorUsuarioId(idUsuario, pageRequest);
		else
			acessos = acessoModuloService.buscarTodos(pageRequest);
		
		if (acessos.getSize() == 0) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.status(404).body("A consulta não retornou dados!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(acessos.getContent());
		
	}
	
	@GetMapping(value = "/idUsuario/{idUsuario}")
	public ResponseEntity<?> buscarAcessosPorUsuario(			
			@PathVariable("idUsuario") Long idUsuario,
			@Valid @RequestBody AtualizaMoradorDto moradorDto, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Buscando acessos do usuário {}", idUsuario);
		
		List<AcessoModulo> acessos = new ArrayList<AcessoModulo>();
		
		if(idUsuario != 0 && idUsuario != null)
			acessos =  acessoModuloService.buscarPorUsuarioId(idUsuario);
		
		if (acessos.size() == 0) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.status(404).body("A consulta não retornou dados!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(acessos);
		
	}
	
	@PutMapping(value = "/idUsuario/{idUsuario}")
	public ResponseEntity<?> atualizar(	
									@PathVariable("idUsuario") Long idUsuario,
									@Valid @RequestBody List<AtualizaAcessoModuloDto> acessoRequestBody,
									BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Aatualização de acessos: {}", acessoRequestBody.toString());
		Response<List<AcessoModulo>> response = new Response<List<AcessoModulo>>();
		
		List<AcessoModulo> acessos = this.acessoModuloService.buscarPorUsuarioId(idUsuario);
		acessos = validarDadosPut(acessoRequestBody, acessos, idUsuario, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de acessos: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
			
		acessos = this.acessoModuloService.persistir(acessos);
		
		response.setData(acessos);
		return ResponseEntity.status(HttpStatus.OK).body(response.getData());
		
	}
	
	public List<AcessoModulo> validarDadosPost(List<CadastroAcessoModuloDto> listDto, BindingResult result) {
		
		List<AcessoModulo> listAcesso = new ArrayList<AcessoModulo>();
		
		listDto.forEach(d -> {
			
			if(!this.moradorService.buscarPorId(d.getIdUsuario()).isPresent()) {
				result.addError(new ObjectError("morador", "Usuário inexistente para o código " + d.getIdUsuario()));
			}
			
			if(!this.moduloService.buscarPorId(d.getIdModulo()).isPresent()) {
				result.addError(new ObjectError("módulo", "Módulo inexistente para o código " + d.getIdModulo()));
			}
			
			if(this.acessoModuloService.buscarPorIdUsuarioAndIdModulo(d.getIdUsuario(), d.getIdModulo()).isPresent()) {
				result.addError(new ObjectError("acesso", "Funcionalidade e módulo já existente para este usuário"));
			}
			
			if(!result.hasErrors()) {
				AcessoModulo acesso = new AcessoModulo();
				acesso.setIdUsuario(d.getIdUsuario());	
				acesso.setIdModulo(d.getIdModulo());
				acesso.setAcesso(d.isAcesso());
				listAcesso.add(acesso);
			}
			
		});
		
		return listAcesso;
		
	}
	
	public List<AcessoModulo> validarDadosPut(List<AtualizaAcessoModuloDto> listDto, List<AcessoModulo> listAcessos, Long idUsuario, BindingResult result) {
		
		List<AcessoModulo> listAcessosPut = new ArrayList<AcessoModulo>();
		
		listDto.forEach(d -> {
			
			if(!this.moradorService.buscarPorId(idUsuario).isPresent()) {
				result.addError(new ObjectError("morador", "Usuário inexistente para o código " + idUsuario));
			}
			
			if(!this.moduloService.buscarPorId(d.getIdModulo()).isPresent()) {
				result.addError(new ObjectError("módulo", "Módulo inexistente para o código " + d.getIdModulo()));
			}
			
		});
		
		//Se não houverem erros monta a lista de persistencia
		if(!result.hasErrors()) {
			listAcessosPut = this.atualizaAcesso(listAcessos, listDto, idUsuario);
		}
		
		return listAcessosPut;
		
	}
	
	public List<AcessoModulo> atualizaAcesso(List<AcessoModulo> acessos, List<AtualizaAcessoModuloDto> acessosDto, Long idUsuario) {
		
		List<AcessoModulo> listAcessos = new ArrayList<AcessoModulo>();
		
		acessosDto.forEach(a -> {
			
			AcessoModulo acesso = new AcessoModulo();
			
			List<AcessoModulo> result = acessos.stream()
						.filter(item -> item.getIdModulo() == a.getIdModulo())
						.collect(Collectors.toList());
			
			if(result.size() > 0) {	
			
				acesso.setId(result.get(0).getId());
				acesso.setIdUsuario(result.get(0).getIdUsuario());
				acesso.setIdModulo(result.get(0).getIdModulo());
				acesso.setDataCadastro(result.get(0).getDataCadastro());
				acesso.setPosicao(result.get(0).getPosicao());
				acesso.setAcesso(a.isAcesso());
				
				listAcessos.add(acesso);
				
			}else {
				
				acesso.setIdUsuario(idUsuario);
				acesso.setIdModulo(a.getIdModulo());
				acesso.setAcesso(a.isAcesso());
				acesso.setDataCadastro(new Date());
				
				listAcessos.add(acesso);
				
			}			
			
		});
		
		return listAcessos;
		
	}
	
}

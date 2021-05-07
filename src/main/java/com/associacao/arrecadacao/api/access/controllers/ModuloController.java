package com.associacao.arrecadacao.api.access.controllers;

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

import com.associacao.arrecadacao.api.access.dtos.AtualizaModuloDto;
import com.associacao.arrecadacao.api.access.dtos.CadastroModuloDto;
import com.associacao.arrecadacao.api.access.entities.Modulo;
import com.associacao.arrecadacao.api.access.services.ModuloService;
import com.associacao.arrecadacao.api.response.Response;

@RestController
@RequestMapping("/associados/modulo")
@CrossOrigin(origins = "*")
public class ModuloController {
	
	private static final Logger log = LoggerFactory.getLogger(ModuloController.class);
	
	@Autowired
	private ModuloService moduloService;
	
	public ModuloController() {
		
		
	}
	
	@PostMapping(value = "/incluir")
	public ResponseEntity<?> cadastrar(
			@Valid @RequestBody List<CadastroModuloDto> requestBody, 
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Cadastro de módulos: {}", requestBody.toString());
		Response<List<Modulo>> response = new Response<List<Modulo>>();
		
		List<Modulo> modulos = validarDadosPost(requestBody, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de módulos: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		modulos = this.moduloService.persistir(modulos);
		
		response.setData(modulos);
		return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}
	
	@PutMapping(value = "/idModulo/{idModulo}")
	public ResponseEntity<?> atualizar(
			@PathVariable("idModulo") Long idModulo,
			@Valid @RequestBody AtualizaModuloDto requestBody, 
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Atualização de módulo: {}", requestBody.toString());
		Response<Modulo> response = new Response<Modulo>();
		
		Optional<Modulo> modulo = this.moduloService.buscarPorId(idModulo);
		modulo = validarDadosPut(modulo.get(), requestBody, idModulo, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de módulos: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		modulo = this.moduloService.persistir(modulo.get());
		
		response.setData(modulo.get());
		return ResponseEntity.status(HttpStatus.OK).body(response.getData());
		
	}
	
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarModuloFiltro(
			@RequestParam(value = "id", defaultValue = "0") Long id,
			@RequestParam(value = "descricao", defaultValue = "null") String descricao,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
			@RequestParam(value = "size", defaultValue = "10") int size) throws NoSuchAlgorithmException {
		
		log.info("Buscando modulos...");
		PageRequest pageRequest = new PageRequest(pag, size, Direction.valueOf(dir), ord);
		
		Page<Modulo> modulos = null;
		
		if(id == 0 && descricao.equals("null"))
			modulos = this.moduloService.buscarTodos(pageRequest);
		else 			
			modulos = this.moduloService.buscarPorDescricao(descricao, pageRequest);
		
		if (modulos.getSize() == 0) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.badRequest().body("A consulta não retornou dados!");
		}
		
		return new ResponseEntity<>(modulos.getContent(), HttpStatus.OK);
		
	}
	
	public List<Modulo> validarDadosPost(List<CadastroModuloDto> dto, BindingResult result) {
		
		List<Modulo> modulos = new ArrayList<Modulo>();
		
		dto.forEach(m -> {	
			if(this.moduloService.buscarPorDescricao(m.getDescricao()).isPresent()) {				
				result.addError(new ObjectError("modulo","Módulo '" + m.getDescricao() + "' já existe"));
			}
			else
			{
				Modulo modulo = new Modulo();
				modulo.setDescricao(m.getDescricao());
				modulo.setPathModulo(m.getPathModulo());				
				modulos.add(modulo);
			}	
		});	
		
		return modulos;
	}
	
	public Optional<Modulo> validarDadosPut(Modulo modulo, AtualizaModuloDto dto, Long idModulo, BindingResult result) {
		
		Optional<Modulo> m = this.moduloService.buscarPorDescricao(dto.getDescricao());
		
		if(!m.isPresent()) {
			modulo.setDescricao(dto.getDescricao());
			modulo.setPathModulo(dto.getPathModulo());
			modulo.setPosicao(dto.getPosicao());
		}
		else if(m.get().getDescricao().toLowerCase().trim() == modulo.getDescricao().toLowerCase().trim() 
				&& m.get().getId() != idModulo) {				
			result.addError(new ObjectError("modulo","Módulo '" + dto.getDescricao() + "' já existe"));
		}
		else
		{
			modulo.setDescricao(dto.getDescricao());
			modulo.setPathModulo(dto.getPathModulo());
			modulo.setPosicao(dto.getPosicao());
		}	
		
		return Optional.ofNullable(modulo);
	}

}

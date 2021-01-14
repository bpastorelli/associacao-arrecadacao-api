package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;

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

import com.associacao.arrecadacao.api.commons.ValidaCPF;
import com.associacao.arrecadacao.api.dtos.CadastroVisitanteDto;
import com.associacao.arrecadacao.api.entities.Visitante;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.VisitanteService;

@RestController
@RequestMapping("associados/visitante")
@CrossOrigin("*")
public class VisitanteController {
	
	private static final Logger log = LoggerFactory.getLogger(VisitanteController.class);
	
	@Autowired
	private VisitanteService visitanteService;

	public VisitanteController() {
		
		
	}
	
	/**
	 * Cadastra um visitate
	 * @param cadastroVisitanteDto
	 * @param result
	 * @return Morador
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("/incluir")
	public ResponseEntity<?> cadastrarVisitante(@Valid @RequestBody Visitante visitante,
												BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Preparando dados para cadastro de visitante", visitante);
		Response<Visitante> response = new Response<Visitante>();
		
		validarDadosExistentes(visitante, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do(s) visitante(s): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		this.visitanteService.persistir(visitante);
		response.setData(visitante);
		return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}
	
	/**
	 * 
	 * @param id id o visitante
	 * @param nome nome do visitante
	 * @param rg rg do visitante
	 * @param cpf cpf do visitante
	 * @param pag pagina atual
	 * @param ord ordenar pelo campo
	 * @param dir ordem de crescente ASC ou decrescente DESC
	 * @param size registros por pagina
	 * @return List<Visitante>
	 * @throws NoSuchAlgorithmException
	 */
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarVisitantesFiltro(
			@RequestParam(value = "id", defaultValue = "0") Long id,
			@RequestParam(value = "nome", defaultValue = "null") String nome,
			@RequestParam(value = "rg", defaultValue = "null") String rg,
			@RequestParam(value = "cpf", defaultValue = "null") String cpf,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
			@RequestParam(value = "size", defaultValue = "10") int size) throws NoSuchAlgorithmException{
		
		log.info("Buscando visitantes...");
		
		PageRequest pageRequest = new PageRequest(pag, size, Direction.valueOf(dir), ord);
		
		Page<Visitante> visitantes = null;
		
		if(id != 0 || !nome.equals("null") || !rg.equals("null") || !cpf.equals("null"))
			visitantes = this.visitanteService.buscarPorIdOrNomeOrCpfOrRg(id, nome, cpf, rg, pageRequest);
		else
			visitantes = this.visitanteService.buscarTodos(pageRequest);
		
		if (visitantes.getSize() == 0) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.status(404).body("A consulta não retornou dados!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(visitantes.getContent());
		
	}
	
	//Validação dos dados informados no dto
	public void validarDadosExistentes(CadastroVisitanteDto cadastroVisitanteDto, BindingResult result) {
		
		if(cadastroVisitanteDto.getVisitantes().size() == 0)
			result.addError(new ObjectError("visitante", " Não existem dados para inclusão"));
		
		cadastroVisitanteDto.getVisitantes().forEach(v -> {
			
			if(v.getNome().isEmpty())
				result.addError(new ObjectError("visitante", " O campo nome é obrigatório"));
			
			if(!v.getCpf().isEmpty()) {
				if(!ValidaCPF.isCPF(v.getCpf()))
					result.addError(new ObjectError("visitante", " O campo RG é obrigatório"));			
			}
			
			this.visitanteService.buscarPorRg(v.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o rg "+ v.getRg() +"")));
			
			this.visitanteService.buscarPorCpf(v.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o cpf "+ v.getCpf() +"")));
			
			this.visitanteService.buscarPorNome(v.getNome())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o nome "+ v.getNome() +"")));
			
		});
		
	}
	
	//Validação dos dados informados no dto para um cadastro
	public void validarDadosExistentes(Visitante visitante, BindingResult result) {
		
	
			if(visitante.getNome().isEmpty())
				result.addError(new ObjectError("visitante", " O campo nome é obrigatório"));
			
			if(!visitante.getCpf().isEmpty()) {
				if(!ValidaCPF.isCPF(visitante.getCpf()))
					result.addError(new ObjectError("visitante", " O campo RG é obrigatório"));			
			}
			
			this.visitanteService.buscarPorRg(visitante.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o rg "+ visitante.getRg() +"")));
			
			this.visitanteService.buscarPorCpf(visitante.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o cpf "+ visitante.getCpf() +"")));
			
			this.visitanteService.buscarPorNome(visitante.getNome())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o nome "+ visitante.getNome() +"")));
			
		
	}
	
}

package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;
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

import com.associacao.arrecadacao.api.commons.ValidaCPF;
import com.associacao.arrecadacao.api.dtos.AtualizaVisitanteDto;
import com.associacao.arrecadacao.api.dtos.VisitanteDto;
import com.associacao.arrecadacao.api.entities.Visita;
import com.associacao.arrecadacao.api.entities.Visitante;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.VisitaService;
import com.associacao.arrecadacao.api.services.VisitanteService;
import com.associacao.arrecadacao.api.utils.Utils;

@RestController
@RequestMapping("associados/visitante")
@CrossOrigin("*")
public class VisitanteController {
	
	private static final Logger log = LoggerFactory.getLogger(VisitanteController.class);
	
	@Autowired
	private VisitaService visitaService;
	
	@Autowired
	private VisitanteService visitanteService;

	public VisitanteController() {
		
		
	}
	
	/**
	 * Cadastra um visitante
	 * @param cadastroVisitanteDto
	 * @param result
	 * @return Morador
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("/incluir")
	public ResponseEntity<?> cadastrarVisitante(@Valid @RequestBody VisitanteDto visitanteDto,
												BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Preparando dados para cadastro de visitante", visitanteDto);
		Visitante visitante = new Visitante();
		Response<Visitante> response = new Response<Visitante>();		
		
		this.validarDadosExistentes(visitanteDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do(s) visitante(s): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		atualizarDadosVisitante(visitanteDto, visitante, result);
		
		this.visitanteService.persistir(visitante);
		response.setData(visitante);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	/**
	 * Atualiza um visitante
	 * @param cadastroVisitanteDto
	 * @param result
	 * @return Morador
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarVisitante(@PathVariable("id") Long id,
												@Valid @RequestBody AtualizaVisitanteDto visitanteDto,
												BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Preparando dados para atualização de visitante", visitanteDto);
		Response<Visitante> response = new Response<Visitante>();
		
		Optional<Visitante> visitante = this.visitanteService.buscarPorId(id);
		if (!visitante.isPresent()) {
			result.addError(new ObjectError("visitante", "  Visitante não encontrado"));
			return ResponseEntity.status(404).body(response.getErrors());
		}
		
		this.atualizarDadosVisitante(visitanteDto, visitante.get(), result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do(s) visitante(s): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		this.visitanteService.persistir(visitante.get());
		response.setData(visitante.get());
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
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
			
		//Busca a data da última visita do visitante
		visitantes.forEach(v -> {
			Optional<Visita> visita = null;
			visita = visitaService.buscarPorVisitanteIdOrderByDataEntradaDesc(v.getId());
			if(visita.isPresent()) {
				v.setUltimaVisita(Utils.dateFormat(visita.get().getDataEntrada(),"dd/MM/yyyy"));				
			}
		});		
		
		if (visitantes.getSize() == 0) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.status(404).body("A consulta não retornou dados!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(visitantes.getContent());
		
	}
	
	/**
	 * 
	 * @param rg rg do visitante
	 * @param cpf cpf do visitante
	 * @return Visitante
	 * @throws NoSuchAlgorithmException
	 */
	@GetMapping(value = "/busca")
	public ResponseEntity<?> buscarVisitante(
			@RequestParam(value = "rg", defaultValue = "null") String rg,
			@RequestParam(value = "cpf", defaultValue = "null") String cpf) throws NoSuchAlgorithmException{
		
		log.info("Buscando visitante...");
		
		Response<Visitante> response = new Response<Visitante>();
		Optional<Visitante> visitante = null;
		
		if(!rg.equals("null") || !cpf.equals("null"))
			visitante = this.visitanteService.buscarPorRgOrCpf(rg, cpf);
		
		if(rg.equals("null") && cpf.equals("null")) {
			log.info("Não existem parâmetros para busca");
			response.getErrors().add(" Você deve informar ao menos um parâmetro de busca (RG ou CPF)!");
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		if (!visitante.isPresent()) {
			log.info("A consulta não retornou dados");
			response.getErrors().add(" Visitante não encontrado!");
			return ResponseEntity.status(404).body(response);
		}
		
		response.setData(visitante.get());
		return ResponseEntity.status(HttpStatus.OK).body(response.getData());
		
	}
	
	//Validação dos dados informados no dto
	public void validarDadosExistentes(VisitanteDto dto, BindingResult result) {
			
			if(dto.getNome().isEmpty())
				result.addError(new ObjectError("visitante", " O campo nome é obrigatório"));
			
			if(!dto.getCpf().isEmpty()) {
				if(!ValidaCPF.isCPF(dto.getCpf()))
					result.addError(new ObjectError("visitante", " O campo RG é obrigatório"));			
			}
			
			this.visitanteService.buscarPorRg(dto.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o rg "+ dto.getRg() +"")));
			
			this.visitanteService.buscarPorCpf(dto.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o cpf "+ dto.getCpf() +"")));
			
			this.visitanteService.buscarPorNome(dto.getNome())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Vistante já cadastrado para o nome "+ dto.getNome() +"")));
			
		
	}
	
	//Validação dos dados informados no dto para um cadastro para PUT
	public void atualizarDadosVisitante(AtualizaVisitanteDto visitanteDto, Visitante visitante, BindingResult result) {
		

			if(visitanteDto.getNome().isEmpty())
				result.addError(new ObjectError("visitante", " O campo nome é obrigatório"));
			
			if(!visitanteDto.getCpf().isEmpty()) {
				if(!ValidaCPF.isCPF(visitanteDto.getCpf()))
					result.addError(new ObjectError("visitante", " CPF inválido"));
			}
			
			if(!result.hasErrors()) {
				
				visitante.setNome(visitanteDto.getNome().toUpperCase());
				visitante.setRg(visitante.getRg());
				visitante.setCpf(visitanteDto.getCpf());
				visitante.setCep(visitanteDto.getCep());
				visitante.setEndereco(visitanteDto.getEndereco().toUpperCase());
				visitante.setNumero(visitanteDto.getNumero());
				visitante.setComplemento(visitanteDto.getComplemento() != null ? visitanteDto.getComplemento().toUpperCase() : null);
				visitante.setBairro(visitanteDto.getBairro().toUpperCase());
				visitante.setCidade(visitanteDto.getCidade().toUpperCase());
				visitante.setUf(visitanteDto.getUf().toUpperCase());
				visitante.setTelefone(visitanteDto.getTelefone());
				visitante.setCelular(visitanteDto.getCelular());
				visitante.setPosicao(visitanteDto.getPosicao());
			}
		
	}
	
	//Validação dos dados informados no dto para um cadastro para POST
	public void atualizarDadosVisitante(VisitanteDto visitanteDto, Visitante visitante, BindingResult result) {
		

			if(visitanteDto.getNome().isEmpty())
				result.addError(new ObjectError("visitante", " O campo nome é obrigatório"));
			
			if(!visitanteDto.getCpf().isEmpty()) {
				if(!ValidaCPF.isCPF(visitanteDto.getCpf()))
					result.addError(new ObjectError("visitante", " CPF inválido"));
			}
			
			if(!result.hasErrors()) {
				
				visitante.setNome(visitanteDto.getNome().toUpperCase());
				visitante.setRg(visitanteDto.getRg());
				visitante.setCpf(visitanteDto.getCpf());
				visitante.setCep(visitanteDto.getCep());
				visitante.setEndereco(visitanteDto.getEndereco().toUpperCase());
				visitante.setNumero(visitanteDto.getNumero());
				visitante.setComplemento(visitanteDto.getComplemento() != null ? visitanteDto.getComplemento().toUpperCase() : null);
				visitante.setBairro(visitanteDto.getBairro().toUpperCase());
				visitante.setCidade(visitanteDto.getCidade().toUpperCase());
				visitante.setUf(visitanteDto.getUf().toUpperCase());
				visitante.setTelefone(visitanteDto.getTelefone());
				visitante.setCelular(visitanteDto.getCelular());
				
			}
		
	}
	
	//Validação dos dados informados no dto para um cadastro
	public void validarDadosExistentes(Visitante visitante, BindingResult result) {
		

			if(visitante.getNome().isEmpty())
				result.addError(new ObjectError("visitante", " O campo nome é obrigatório"));
			
			if(visitante.getRg().isEmpty()) {
				result.addError(new ObjectError("visitante", " O campo RG é obrigatório"));			
			}
			
			if(!visitante.getCpf().isEmpty()) {
				if(!ValidaCPF.isCPF(visitante.getCpf())) 				
					result.addError(new ObjectError("visitante", " CPF inválido"));			
			}
			
			this.visitanteService.buscarPorRg(visitante.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Visitante já cadastrado para o rg "+ visitante.getRg() +"")));				

			
			this.visitanteService.buscarPorCpf(visitante.getRg())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Visitante já cadastrado para o cpf "+ visitante.getCpf() +"")));
			
			this.visitanteService.buscarPorNome(visitante.getNome())
				.ifPresent(res -> result.addError(new ObjectError("visitante", " Visitante já cadastrado para o nome "+ visitante.getNome() +"")));
		
	}
	
}

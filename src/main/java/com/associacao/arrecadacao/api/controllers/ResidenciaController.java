package com.associacao.arrecadacao.api.controllers;

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

import com.associacao.arrecadacao.api.dtos.AtualizaResidenciaDto;
import com.associacao.arrecadacao.api.dtos.CadastroNovaResidenciaDto;
import com.associacao.arrecadacao.api.dtos.CadastroResidenciaDto;
import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.entities.VinculoResidencia;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.MoradorService;
import com.associacao.arrecadacao.api.services.ResidenciaService;
import com.associacao.arrecadacao.api.services.VinculoResidenciaService;

@RestController
@RequestMapping("/associados/residencia")
@CrossOrigin(origins = "*")
class ResidenciaController {
	
	private static final Logger log = LoggerFactory.getLogger(ResidenciaController.class);
	
	@Autowired
	private MoradorService moradorService;
	
	@Autowired
	private ResidenciaService residenciaService;
	
	@Autowired
	private VinculoResidenciaService vinculoResidenciaService;
	
	
	public ResidenciaController() {
		
	}
	
	@PostMapping
	public ResponseEntity<?> cadastrar(@Valid @RequestBody CadastroResidenciaDto cadastroResidenciaDto,
			BindingResult result ){
		
		log.info("Cadastrando uma residência: {}", cadastroResidenciaDto.toString());
		
		Response<CadastroResidenciaDto> response = new Response<CadastroResidenciaDto>();
		
		Residencia residencia = this.converterDtoParaResidencia(cadastroResidenciaDto);
		validarDadosExistentes(cadastroResidenciaDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro da residência: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		this.residenciaService.persistir(residencia);
		
		response.setData(this.converterCadastroResidenciaDto(residencia));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	@PostMapping("/nova")
	public ResponseEntity<?> cadastrarNova(@Valid @RequestBody CadastroNovaResidenciaDto cadastroNovaResidenciaDto,
			BindingResult result ){
		
		log.info("Cadastrando uma residência: {}", cadastroNovaResidenciaDto.toString());
		
		Response<CadastroResidenciaDto> response = new Response<CadastroResidenciaDto>();
		
		Residencia residencia = this.converterNovaDtoParaResidencia(cadastroNovaResidenciaDto);
		validarDadosExistentes(cadastroNovaResidenciaDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro da residência: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		Optional<Morador> morador = moradorService.buscarPorId(cadastroNovaResidenciaDto.getMoradorId());
		Optional<Residencia> residenciaBusca = residenciaService.buscarPorCepAndNumero(cadastroNovaResidenciaDto.getCep(), cadastroNovaResidenciaDto.getNumero());
		if(!residenciaBusca.isPresent())
			this.residenciaService.persistir(residencia);
		else
			residencia.setId(residenciaBusca.get().getId());
		
		this.vinculoResidenciaService.persistir(this.converterParaVinculoResidencia(morador.get(), residencia));
		response.setData(this.converterCadastroResidenciaDto(residencia));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	
	/**
	 * Atualiza os dados de uma residência.
	 * 
	 * @param id
	 * @param residenciaDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroResidenciaDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody AtualizaResidenciaDto residenciaDto, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Atualizando residência: {}", residenciaDto.toString());
		Response<CadastroResidenciaDto> response = new Response<CadastroResidenciaDto>();
		
		Optional<Residencia> residencia = this.residenciaService.buscarPorId(id);
		if (!residencia.isPresent()) {
			result.addError(new ObjectError("residencia", " Residência não encontrada."));
		}
		
		this.atualizarDadosResidencia(residencia.get(), residenciaDto, result);
		
		if (result.hasErrors()) {
			log.error("Erro validando residência: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		this.residenciaService.persistir(residencia.get());
		response.setData(this.converterCadastroResidenciaDto(residencia.get()));

		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	/**
	 * Busca uma residência pelo ID ou pel Matricula.
	 * 
	 * @param id
	 * @param matricula
	 * @param result
	 * @return ResponseEntity<Response<CadastroResidenciaDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@GetMapping(value = "/id/{id}/matricula/{matricula}")
	public ResponseEntity<Response<CadastroResidenciaDto>> buscarPorIdOrMatricula(@PathVariable("id") Long id, @PathVariable("matricula") String matricula) throws NoSuchAlgorithmException {
		
		log.info("Buscando residência: {}", id);
		Response<CadastroResidenciaDto> response = new Response<CadastroResidenciaDto>();
		
		Optional<Residencia> residencia = this.residenciaService.bucarPorIdOrMatricula(id, matricula);
		if (!residencia.isPresent()) {
			log.info("Residência não encontrada para o ID: {}", id);
			response.getErrors().add("Residência não encontrada para o ID " + id);
			return ResponseEntity.status(404).body(response);
		}
		
		response.setData(this.converterCadastroResidenciaDto(residencia.get()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	/**
	 * Busca uma residência pelo ID ou pel Matricula.
	 * 
	 * @param result
	 * @return ResponseEntity<?>
	 * @throws NoSuchAlgorithmException
	 */
	@GetMapping("/filtro")
	public ResponseEntity<?> buscarResidenciaPaginado(
			@RequestParam(value = "id", defaultValue = "0") Long id,
			@RequestParam(value = "matricula", defaultValue = "null") String matricula,
			@RequestParam(value = "endereco", defaultValue = "null") String endereco,
			@RequestParam(value = "numero", defaultValue = "0") Long numero,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
			@RequestParam(value = "size", defaultValue = "10") int qtdPorPagina) throws NoSuchAlgorithmException {		
		
		PageRequest pageRequest = new PageRequest(pag, qtdPorPagina, Direction.valueOf(dir), ord);
		
		Page<Residencia> residencias;
		
		if(id != 0 || !matricula.equals("null") || !endereco.equals("null") || numero != 0)
			residencias = this.residenciaService.buscarPorIdOrMatriculaOrEnderecoOrNumero(id, matricula, endereco, numero, pageRequest);
		else
			residencias = this.residenciaService.bucarTodos(pageRequest);
		
		if (residencias.getSize() == 0) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.status(404).body("A consulta não retornou dados!");
		}
		
		Page<CadastroResidenciaDto> residenciasDto = residencias.map(residencia -> this.converterCadastroResidenciaDto(residencia));
		
		return new ResponseEntity<>(residenciasDto.getContent(), HttpStatus.OK);
		
	}
	
	
	/**
	 * Converter para VinculoResidencia.
	 * 
	 * @param morador
	 * @param residencia
	 * @return VinculoResidencia
	 */
	public List<VinculoResidencia> converterParaVinculoResidencia(Morador morador, Residencia residencia){
		
		
		List<VinculoResidencia> listVinculo = new ArrayList<VinculoResidencia>();
		
		VinculoResidencia vinculo = new VinculoResidencia();
		
		vinculo.setMorador(morador);
		vinculo.setResidencia(residencia);
		
		listVinculo.add(vinculo);
		
		return listVinculo;
		
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
		residencia.setComplemento(cadastroResidenciaDto.getComplemento());
		residencia.setBairro(cadastroResidenciaDto.getBairro());
		residencia.setCep(cadastroResidenciaDto.getCep());
		residencia.setCidade(cadastroResidenciaDto.getCidade());
		residencia.setUf(cadastroResidenciaDto.getUf());
		return residencia;
	}
	
	/**
	 * Converter o CadastroNovaResidenciaDto para Residencia.
	 * 
	 * @param cadastroNovaResidenciaDto
	 * @return Residencia
	 */
	private Residencia converterNovaDtoParaResidencia(CadastroNovaResidenciaDto cadastroNovaResidenciaDto) {
		
		Residencia residencia = new Residencia();
		residencia.setMatricula(cadastroNovaResidenciaDto.getMatricula());
		residencia.setEndereco(cadastroNovaResidenciaDto.getEndereco());
		residencia.setNumero(cadastroNovaResidenciaDto.getNumero());
		residencia.setComplemento(cadastroNovaResidenciaDto.getComplemento());
		residencia.setBairro(cadastroNovaResidenciaDto.getBairro());
		residencia.setCep(cadastroNovaResidenciaDto.getCep());
		residencia.setCidade(cadastroNovaResidenciaDto.getCidade());
		residencia.setUf(cadastroNovaResidenciaDto.getUf());
		return residencia;
	}
	
	private void validarDadosExistentes(CadastroResidenciaDto cadastroResidenciaDto, BindingResult result) {
		
		this.residenciaService.buscarPorCepAndNumero(cadastroResidenciaDto.getCep(), cadastroResidenciaDto.getNumero())
				.ifPresent(res -> result.addError(new ObjectError("residencia", " Endereço já existente")));
		
	}
	
	private void validarDadosExistentes(CadastroNovaResidenciaDto cadastroNovaResidenciaDto, BindingResult result) {
		
		if(!this.moradorService.buscarPorId(cadastroNovaResidenciaDto.getMoradorId()).isPresent())
				result.addError(new ObjectError("residencia", " O morador código " + cadastroNovaResidenciaDto.getMoradorId() + " não existe" ));
		
	}
	
	/**
	 * Converter o objeto tipo Residencia para o tipo CadastroResidenciaDto.
	 * 
	 * @param residencia
	 * @return CadastroResidenciaDto
	 */
	private CadastroResidenciaDto converterCadastroResidenciaDto(Residencia residencia) {
		
		CadastroResidenciaDto cadastroResidenciaDto = new CadastroResidenciaDto();
		cadastroResidenciaDto.setId(residencia.getId());
		cadastroResidenciaDto.setMatricula(residencia.getMatricula());
		cadastroResidenciaDto.setEndereco(residencia.getEndereco());
		cadastroResidenciaDto.setNumero(residencia.getNumero());
		cadastroResidenciaDto.setComplemento(residencia.getComplemento());
		cadastroResidenciaDto.setBairro(residencia.getBairro());
		cadastroResidenciaDto.setCep(residencia.getCep());
		cadastroResidenciaDto.setCidade(residencia.getCidade());
		cadastroResidenciaDto.setUf(residencia.getUf());
		cadastroResidenciaDto.setCidade(residencia.getCidade());
		return cadastroResidenciaDto;
	}

	/**
	 * Atualiza os dados da residência com base nos dados encontrados no DTO.
	 * 
	 * @param reseidencia
	 * @param residenciaDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosResidencia(Residencia residencia, AtualizaResidenciaDto residenciaDto, BindingResult result)
			throws NoSuchAlgorithmException {	
		
		Optional<Residencia> residenciaAtual = this.residenciaService.buscarPorId(residencia.getId());
		
		this.residenciaService.buscarPorMatricula(residenciaDto.getMatricula())
			.ifPresent(res -> result.addError(new ObjectError("residencia", " Residência já existente")));

		if(residenciaAtual.isPresent())
			if(residenciaDto.getCep() != residenciaAtual.get().getCep() && residenciaDto.getNumero() != residenciaAtual.get().getNumero())
				this.residenciaService.buscarPorCepAndNumero(residenciaDto.getCep(), residenciaDto.getNumero())
					.ifPresent(res -> result.addError(new ObjectError("residencia", " Endereço já existente para a residência codigo " + res.getId() +"")));
		
		if(!result.hasErrors()) {
			
			residencia.setEndereco(residenciaDto.getEndereco());
			residencia.setNumero(residenciaDto.getNumero());
			residencia.setComplemento(residenciaDto.getComplemento());
			residencia.setBairro(residenciaDto.getBairro());
			residencia.setCep(residenciaDto.getCep());
			residencia.setCidade(residenciaDto.getCidade());
			residencia.setUf(residenciaDto.getUf());
			
		}
		
	}
}

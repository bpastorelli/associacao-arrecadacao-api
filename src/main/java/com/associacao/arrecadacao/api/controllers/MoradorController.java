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

import com.associacao.arrecadacao.api.commons.ValidaCPF;
import com.associacao.arrecadacao.api.dtos.AtualizaMoradorDto;
import com.associacao.arrecadacao.api.dtos.CadastroMoradorDto;
import com.associacao.arrecadacao.api.dtos.CadastroMoradorResponseDto;
import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.entities.VinculoResidencia;
import com.associacao.arrecadacao.api.enums.PerfilEnum;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.MoradorService;
import com.associacao.arrecadacao.api.services.ResidenciaService;
import com.associacao.arrecadacao.api.services.VinculoResidenciaService;
import com.associacao.arrecadacao.api.utils.PasswordUtils;
import com.associacao.arrecadacao.api.utils.Utils;

@RestController
@RequestMapping("/associados/morador")
@CrossOrigin(origins = "*")
public class MoradorController {
	
	private static final Logger log = LoggerFactory.getLogger(MoradorController.class);
	
	@Autowired
	private MoradorService moradorService;
	
	@Autowired
	private ResidenciaService residenciaService;	
	
	@Autowired
	private VinculoResidenciaService vinculoResidenciaService;
	
	public MoradorController() {
		
	}
	
	@PostMapping(value = "/residencia/{residenciaId}")
	public ResponseEntity<Response<CadastroMoradorDto>> cadastroVinculado(@PathVariable("residenciaId") Long residenciaId, 
			@Valid @RequestBody CadastroMoradorDto cadastroMoradorDto, 
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Cadastro de morador: {}", cadastroMoradorDto.toString());
		Response<CadastroMoradorDto> response = new Response<CadastroMoradorDto>();
		
		cadastroMoradorDto.getMoradores().forEach(p -> p.setResidenciaId(residenciaId));
		List<Morador> moradores = this.converterDtoParaMorador(cadastroMoradorDto);
		validarDadosExistentes(cadastroMoradorDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do(s) morador(es): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.moradorService.persistir(moradores);
		cadastroMoradorDto.setMoradores(moradores);
		this.vinculoResidenciaService.persistir(this.converterDtoParaVinculoResidencia(cadastroMoradorDto));
		response.setData(this.converterCadastroMoradorDto(moradores));
		return ResponseEntity.ok(response);
		
	}
	
	@PostMapping(value = "/novo")
	public ResponseEntity<?> cadastrar( 
			@Valid @RequestBody CadastroMoradorDto cadastroMoradorDto, 
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Cadastro de morador: {}", cadastroMoradorDto.toString());
		Response<CadastroMoradorDto> response = new Response<CadastroMoradorDto>();
		
		List<Morador> moradores = this.converterDtoParaMorador(cadastroMoradorDto);
		validarDadosExistentes(cadastroMoradorDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do(s) morador(es): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.moradorService.persistir(moradores);
		response.setData(this.converterCadastroMoradorDto(moradores));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	/**
	 * Cria um morador.
	 * 
	 * @param Morador
	 * @return ResponseEntity<Response<CadastroMoradorDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping(value = "/incluir")
	public ResponseEntity<Response<Morador>> cadastrarMorador(
			@Valid @RequestBody Morador moradorRequestBody, 
			BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Cadastro de morador: {}", moradorRequestBody.toString());
		
		Response<Morador> response = new Response<Morador>();
		
		Morador morador = this.converterDtoParaMorador(moradorRequestBody);
		
		CadastroMoradorDto dto = new CadastroMoradorDto();
		
		List<Morador> listMorador = new ArrayList<Morador>();
		listMorador.add(morador);
		dto.setMoradores(listMorador);
		
		validarDadosExistentes(dto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro do(s) morador(es): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.moradorService.persistir(listMorador);
		dto.setMoradores(listMorador);
		this.vinculoResidenciaService.persistir(this.converterDtoParaVinculoResidencia(dto));
		response.setData(morador);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	/**
	 * Atualiza os dados de um morador.
	 * 
	 * @param id
	 * @param moradorDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroMoradorDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/morador/{id}")
	public ResponseEntity<?> atualizarMorador(@PathVariable("id") Long id,
			@Valid @RequestBody AtualizaMoradorDto moradorDto, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Atualizando morador: {}", moradorDto.toString());
		Response<Morador> response = new Response<Morador>();
		
		Optional<Morador> morador = this.moradorService.buscarPorId(id);
		if (!morador.isPresent()) {
			result.addError(new ObjectError("morador", " Morador não encontrado pelo ID " + id));
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));			
			return ResponseEntity.status(404).body(response.getErrors());
		}	
		
		this.atualizarDadosMorador(morador.get(), moradorDto, result);
		
		if (result.hasErrors()) {
			log.error("Erro validando morador: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		List<Morador> list = new ArrayList<Morador>();
		list.add(morador.get());		
		
		this.moradorService.persistir(list);
		response.setData(morador.get());

		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	/**
	 * Atualiza os dados de uma residência.
	 * 
	 * @param id
	 * @param moradorDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroMoradorDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) throws NoSuchAlgorithmException {
		
		log.info("Buscando morador: {}", id);
		Response<CadastroMoradorResponseDto> response = new Response<CadastroMoradorResponseDto>();
		
		Optional<Morador> morador = this.moradorService.buscarPorId(id);
		if (!morador.isPresent()) {
			log.info("Morador não encontrada para o ID: {}", id);
			response.getErrors().add("Morador não encontrada para o ID " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		List<Morador> list = new ArrayList<Morador>();
		list.add(morador.get()); 
		Long residenciaId = vinculoResidenciaService.buscarPorMoradorId(morador.get().getId()).size() > 0 ? vinculoResidenciaService.buscarPorMoradorId(morador.get().getId()) .get(0).getResidencia().getId() : 0;
		list.forEach(p -> p.setResidenciaId(residenciaId));
		response.setData(this.converterCadastroMoradorResponseDto(list.get(0), residenciaId));
		return new ResponseEntity<>(morador.get(), HttpStatus.OK);
		
	}
	
	/**
	 * Busca um morador pelo id.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<CadastroMoradorResponseDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarMoradoresFiltro(
			@RequestParam(value = "id", defaultValue = "0") Long id,
			@RequestParam(value = "cpf", defaultValue = "null") String cpf,
			@RequestParam(value = "rg", defaultValue = "null") String rg,
			@RequestParam(value = "nome", defaultValue = "null") String nome,
			@RequestParam(value = "email", defaultValue = "null") String email,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
			@RequestParam(value = "size", defaultValue = "10") int size) throws NoSuchAlgorithmException {
		
		log.info("Buscando moradores...");
		PageRequest pageRequest = new PageRequest(pag, size, Direction.valueOf(dir), ord);
		
		Page<Morador> moradores = null;
		
		if(id == 0 && cpf.equals("null") && rg.equals("null") && email.equals("null") && nome.equals("null") )
			moradores = this.moradorService.bucarTodos(pageRequest);
		else 			
			moradores = this.moradorService.buscarPorIdOrCpfOrRgOrNomeOrEmail(id, cpf, rg, nome, email, pageRequest);
		
		if (moradores.getSize() == 0) {
			log.info("A consulta não retornou dados");
			return ResponseEntity.badRequest().body("A consulta não retornou dados!");
		}
		
		return new ResponseEntity<>(moradores.getContent(), HttpStatus.OK);
		
	}
	
	@GetMapping()
	public ResponseEntity<?> getAll(
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "size", defaultValue = "10") int qtdPorPagina,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
			@RequestParam(value = "ord", defaultValue = "nome") String ord) throws NoSuchAlgorithmException {
		
		PageRequest pageRequest = new PageRequest(pag, qtdPorPagina, Direction.valueOf(dir), ord);
		
		log.info("Buscando moradores...");
		Page<Morador> moradores = this.moradorService.bucarTodos(pageRequest);
		
		if (moradores.getSize() == 0) {
			return ResponseEntity.badRequest().body("A consulta não retornou dados!");
		}
		
		return new ResponseEntity<>(moradores.getContent(), HttpStatus.OK);
	}
	
	private void validarDadosExistentes(CadastroMoradorDto cadastroMoradorDto, BindingResult result) {
		
		Optional<Long> residenciaId = cadastroMoradorDto.getMoradores().get(0).getResidenciaId();
		
		if(cadastroMoradorDto.getMoradores().size() == 0) {
			result.addError(new ObjectError("morador", " Você deve informar ao menos um morador"));
		}
		
		if(!this.residenciaService.buscarPorId(residenciaId.get()).isPresent())
			result.addError(new ObjectError("morador", " Residencia ID " + residenciaId.get() + " não existente"));
		
		for(Morador morador : cadastroMoradorDto.getMoradores()) {
			if(morador.getNome().isEmpty())
				result.addError(new ObjectError("morador", " O campo Nome é obrigatório"));
			
			if(morador.getCpf().isEmpty())
				result.addError(new ObjectError("morador", " O campo CPF é obrigatório"));
			
			if(!ValidaCPF.isCPF(morador.getCpf()))
				result.addError(new ObjectError("morador", " CPF inválido"));
			
			if(morador.getRg().isEmpty())
				result.addError(new ObjectError("morador", " O campo RG é obrigatório"));
			
			if(morador.getEmail().isEmpty())
				result.addError(new ObjectError("morador", " O campo e-mail é obrigatório"));
			
			if(morador.getTelefone().isEmpty() && morador.getCelular().isEmpty())
				result.addError(new ObjectError("morador", " Você deve informar um número de telefone ou celular"));
			
		}
		
		cadastroMoradorDto.getMoradores().forEach(morador ->{
			this.moradorService.buscarPorCpf(morador.getCpf())
				.ifPresent(res -> result.addError(new ObjectError("morador", " CPF " + morador.getCpf() + " já existe")));	
		});
		
		cadastroMoradorDto.getMoradores().forEach(morador ->{
			this.moradorService.buscarPorRg(morador.getRg())
				.ifPresent(res -> result.addError(new ObjectError("morador", " RG " + morador.getRg() + " já existe")));	
		});
	
		cadastroMoradorDto.getMoradores().forEach(morador ->{
			this.moradorService.buscarPorEmail(morador.getEmail())
				.ifPresent(res -> result.addError(new ObjectError("morador", " E-mail " + morador.getEmail() + " já existe")));	
		});
		
		//Valida se o CPF não está duplicado na requisição.
		cadastroMoradorDto.getMoradores().forEach(morador -> {
			if(cadastroMoradorDto.getMoradores()
					.stream()
					.filter(pessoa -> pessoa.getCpf()
					.equals(morador.getCpf())).count() > 1)
				result.addError(new ObjectError("morador", " CPF " + morador.getCpf() + " está duplicado."));
		});	
		
		//Valida se o RG não está duplicado na requisição.
		cadastroMoradorDto.getMoradores().forEach(morador -> {
			if(cadastroMoradorDto.getMoradores()
					.stream()
					.filter(pessoa -> pessoa.getRg()
					.equals(morador.getRg())).count() > 1)
				result.addError(new ObjectError("morador", "RG " + morador.getRg() + " está duplicado."));
		});
		
		//Valida se o E-mail não está duplicado na requisição.
		cadastroMoradorDto.getMoradores().forEach(morador -> {
			if(cadastroMoradorDto.getMoradores()
					.stream()
					.filter(pessoa -> pessoa.getEmail()
					.equals(morador.getEmail())).count() > 1) {
				result.addError(new ObjectError("morador", "E-mail " + morador.getEmail() + " está duplicado."));				
			}
		});
		
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
			item.setSenha(PasswordUtils.gerarBCrypt(morador.getCpf().substring(0, 6)));
			item.setPerfil(morador.getPerfil() == null ? PerfilEnum.ROLE_USUARIO : morador.getPerfil());
			item.setTelefone(morador.getTelefone());
			item.setCelular(morador.getCelular());
			item.setResidenciaId(morador.getResidenciaId().get());
			item.setPosicao(morador.getPosicao());
			moradores.add(item);
		}
		
		return moradores;
	}
	
	public Morador converterDtoParaMorador(Morador morador){
		
		Morador item = new Morador();
		item.setNome(morador.getNome());
		item.setCpf(morador.getCpf());
		item.setRg(morador.getRg());
		item.setEmail(morador.getEmail());
		item.setSenha(PasswordUtils.gerarBCrypt(morador.getCpf().substring(0, 6)));
		item.setPerfil(morador.getPerfil() == null ? PerfilEnum.ROLE_USUARIO : morador.getPerfil());
		item.setTelefone(morador.getTelefone());
		item.setCelular(morador.getCelular());
		item.setResidenciaId(morador.getResidenciaId().get());
		item.setPosicao(morador.getPosicao());
		
		return item;
	}

	/**
	 * Converter o objeto tipo Residencia para o tipo CadastroProcessoDto.
	 * 
	 * @param List<Morador>
	 * @return CadastroMoradorDto
	 */
	private CadastroMoradorDto converterCadastroMoradorDto(List<Morador> moradores) {
		
		CadastroMoradorDto dto = new CadastroMoradorDto();
		
		dto.setMoradores(moradores);
		return dto;
	}

	/**
	 * Converter o objeto tipo Morador para o tipo CadastroMoradorResponseDto.
	 * 
	 * @param Morador
	 * @return CadastroMoradorResponseDto
	 */
	private CadastroMoradorResponseDto converterCadastroMoradorResponseDto(Morador morador, Long residenciaId) {
		
		CadastroMoradorResponseDto dto = new CadastroMoradorResponseDto();
		dto.setId(morador.getId());
		dto.setNome(morador.getNome());
		dto.setEmail(morador.getEmail());
		dto.setCpf(morador.getCpf());
		dto.setRg(morador.getRg());
		dto.setTelefone(morador.getTelefone());
		dto.setCelular(morador.getCelular());
		dto.setResidenciaId(residenciaId);
		dto.setDataCriacao(Utils.dateFormat(morador.getDataCriacao(), "dd/MM/yyyy"));
		dto.setDataAtualizacao(Utils.dateFormat(morador.getDataAtualizacao(), "dd/MM/yyyy"));
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
			vinculo.setMorador(m);
			Residencia residencia = new Residencia();
			residencia.setId(m.getResidenciaId().get());
			vinculo.setResidencia(residencia);
			vinculos.add(vinculo);
		});
		return vinculos;
	}
	
	/**
	 * Atualiza os dados de morador com base nos dados encontrados no DTO.
	 * 
	 * @param morador
	 * @param moradorDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosMorador(Morador morador, AtualizaMoradorDto moradorDto, BindingResult result)
			throws NoSuchAlgorithmException {
		
		if (!morador.getNome().equals(moradorDto.getNome())) {
			this.moradorService.buscarPorNome(moradorDto.getNome())
					.ifPresent(func -> result.addError(new ObjectError("nome", " O nome '" + func.getNome() + "' já existe")));
			morador.setNome(moradorDto.getNome());
		}
		
		if (!morador.getEmail().equals(moradorDto.getEmail())) {
			this.moradorService.buscarPorEmail(moradorDto.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", " O e-mail '" + func.getEmail() + "' já existe")));
			morador.setEmail(moradorDto.getEmail());
		}
		
		if (!morador.getRg().equals(moradorDto.getRg())) {
			this.moradorService.buscarPorRg(moradorDto.getRg())
					.ifPresent(func -> result.addError(new ObjectError("rg", " RG '" + func.getRg() + "' já existe")));
			morador.setRg(moradorDto.getRg());
		}
		
		morador.setTelefone(moradorDto.getTelefone());
		morador.setCelular(moradorDto.getCelular());
		morador.setPosicao(moradorDto.getPosicao());

	}
}

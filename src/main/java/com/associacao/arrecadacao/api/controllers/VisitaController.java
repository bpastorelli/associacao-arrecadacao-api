package com.associacao.arrecadacao.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.associacao.arrecadacao.api.dtos.EncerraVisitaDto;
import com.associacao.arrecadacao.api.dtos.VisitaDto;
import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.entities.Veiculo;
import com.associacao.arrecadacao.api.entities.VinculoVeiculo;
import com.associacao.arrecadacao.api.entities.Visita;
import com.associacao.arrecadacao.api.entities.VisitaResponse;
import com.associacao.arrecadacao.api.entities.Visitante;
import com.associacao.arrecadacao.api.response.Response;
import com.associacao.arrecadacao.api.services.ResidenciaService;
import com.associacao.arrecadacao.api.services.VeiculoService;
import com.associacao.arrecadacao.api.services.VinculoVeiculoService;
import com.associacao.arrecadacao.api.services.VisitaService;
import com.associacao.arrecadacao.api.services.VisitanteService;
import com.associacao.arrecadacao.api.utils.Utils;

@RestController
@RequestMapping("/associados/visita")
@CrossOrigin(origins = "*")
public class VisitaController {
	
	private static final Logger log = LoggerFactory.getLogger(VisitaController.class);
	
	@Autowired
	private VisitaService visitaService;
	
	@Autowired
	private VisitanteService visitanteService;
	
	@Autowired
	private ResidenciaService residenciaService;
	
	@Autowired
	private VinculoVeiculoService vinculoVeiculoService;
	
	@Autowired
	private VeiculoService veiculoService;
	
	public VisitaController() {
		
	}	
	
	/**
	 * Cadastra uma visita
	 * @param visitaDto
	 * @param result
	 * @return Visita
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("/incluir")
	public ResponseEntity<?> cadastrarVisita(@Valid @RequestBody VisitaDto visitaDto,
												BindingResult result) throws NoSuchAlgorithmException{
		
		log.info("Preparando dados para cadastro de visita", visitaDto);
		Response<VisitaResponse> response = new Response<VisitaResponse>();
		
		Visita visita = new Visita();
		visita = this.converterVisitaDtoParaVisita(visitaDto, result);
		
		//Vincula o veiculo ao visitante
		if(visitaDto.getPlaca() != null && visitaDto.getPlaca() != "") {
			
			Optional<Veiculo> veiculo = veiculoService.buscarPorPlaca(visitaDto.getPlaca().replace("-", ""));
			Optional<Visitante> visitante = visitanteService.buscarPorRg(visitaDto.getRg());
		
			List<VinculoVeiculo> vinculos = new ArrayList<VinculoVeiculo>();
			
			if(veiculo.isPresent() && visitante.isPresent()) {
				if(!vinculoVeiculoService.buscarPorPlacaAndVisitanteId(veiculo.get().getPlaca().replace("-", ""), visitante.get().getId()).isPresent()) {					
					
					VinculoVeiculo vinculo = new VinculoVeiculo();
					
					vinculo.setVeiculo(veiculo.get());
					vinculo.setVisitante(visitante.get());
					vinculos.add(vinculo);
					
					this.vinculoVeiculoService.persistir(vinculos);
				}
			}else if(!veiculo.isPresent() && visitante.isPresent()) {
				
				veiculo = this.converterVisitaDtoParaVeiculo(visitaDto, result);
				
				if(result.hasErrors()) {
					log.error("Erro validando dados para cadastro de visita(s): {}", result.getAllErrors());
					result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
					return ResponseEntity.status(400).body(response.getErrors());
				}
				
				veiculo = veiculoService.persistir(veiculo.get());
				
				VinculoVeiculo vinculo = new VinculoVeiculo();
				vinculo.setVeiculo(veiculo.get());
				vinculo.setVisitante(visitante.get());
				vinculos.add(vinculo);
					
				this.vinculoVeiculoService.persistir(vinculos);
			}
			
		}	
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de visita(s): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		this.visitaService.persistir(visita);
		response.setData(this.converterVisitaParaVisitaResponse(visita));
		return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
		
	}
	
	/**
	 * Encerra o periodo de visita aberto
	 * @param encerraVisitaDto
	 * @param result
	 * @return VisitaResponse
	 */
	@PutMapping("/encerrar")
	public ResponseEntity<?> encerrarVisita(@Valid @RequestBody EncerraVisitaDto encerraVisitaDto,
											BindingResult result) {
		
		log.info("Preparando dados para atualizar a visita");
		Response<VisitaResponse> response = new Response<VisitaResponse>();
		
		Visita visita = new Visita();	
		visita = this.atualizaVisita(encerraVisitaDto.getId(), result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro de visita(s): {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.status(400).body(response.getErrors());
		}
		
		this.visitaService.persistir(visita);
		response.setData(this.converterVisitaParaVisitaResponse(visita));
		return ResponseEntity.status(HttpStatus.OK).body(response.getData());
		
	}
	

	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarVisitasFiltro(
			@RequestParam(value = "nome", defaultValue = "null") String nome,
			@RequestParam(value = "rg", defaultValue = "null") String rg,
			@RequestParam(value = "cpf", defaultValue = "null") String cpf,
			@RequestParam(value = "posicao", defaultValue = "2") Integer posicao,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir,
			@RequestParam(value = "size", defaultValue = "10") int size) throws NoSuchAlgorithmException{
		
		log.info("Buscando visitas...");
		
		PageRequest pageRequest = new PageRequest(pag, size, Direction.valueOf(dir), ord);
		
		Page<Visita> visitas = null;
		List<VisitaResponse> listVisitas = new ArrayList<VisitaResponse>();
		
		if(!nome.equals("null") || !rg.equals("null") || !cpf.equals("null") || posicao != 2)
			visitas = this.visitaService.buscarPorPosicaoOrRgOrCpf(posicao, rg, cpf, nome, pageRequest);
		else
			visitas = this.visitaService.buscarTodos(pageRequest);
		
		visitas.getContent().forEach(v -> {	
			listVisitas.add(this.converterVisitaParaVisitaResponse(v));
		});		
		
		return ResponseEntity.status(HttpStatus.OK).body(listVisitas);
		
	}
	
	public Visita atualizaVisita(Long id, BindingResult result) {
		
		Visita visita = visitaService.buscarPorId(id);
		Date dataSaida = new Date();
		Time horaSaida = new Time(dataSaida.getTime());
		Integer posicao = 1;
		
		if(visita == null)
			result.addError(new ObjectError("visita", " O código de visita " + id + " não existe!"));
		else {
			if(visita.getPosicao() == 0) 
				result.addError(new ObjectError("visita", " Esta visita já foi encerrada em " + Utils.dateFormat(visita.getDataSaida(), "dd/MM/yyyy") + " às " + new Time(visita.getDataSaida().getTime()) + "!"));			
		}
		
		if(!result.hasErrors()) {
			visita.setDataSaida(dataSaida);
			visita.setHoraSaida(horaSaida);
			visita.setPosicao(posicao);
			visita.setResidencia(visita.getResidencia());			
		}
		
		return visita;
		
	}
	
	public Visita converterVisitaDtoParaVisita(VisitaDto visitaDto, BindingResult result) {
		
		Visita visita = new Visita();
		Optional<Visitante> visitante = null;
		Optional<Residencia> residencia = this.residenciaService.buscarPorId(visitaDto.getResidenciaId());
		
		if(!residencia.isPresent())
			result.addError(new ObjectError("visita", " Código de residência " + visitaDto.getResidenciaId() + " inexistente"));
				
		if(visitaDto.getRg().equals("")) {
			result.addError(new ObjectError("visita", " Você deve infomar o RG do visitante" ));
		}else {
			if(!visitaDto.getRg().equals("")) {
				visitante = visitanteService.buscarPorRg(visitaDto.getRg());
				if(!visitante.isPresent())
					result.addError(new ObjectError("visita", " Visitante não encontrado para o RG " + visitaDto.getRg() + "!"));				
			}
			else {
				visitante = visitanteService.buscarPorCpf(visitaDto.getCpf());
				if(!visitante.isPresent())
					result.addError(new ObjectError("visita", " Visitante não encontrado para o CPF " + visitaDto.getCpf() + "!"));				
			}
			
		}
		
		//Valida se já existem visitas não encerradas.
		if(!result.hasErrors()) {
			
			Integer posicao = 1;
			List<Visita> listVisitas = new ArrayList<Visita>();
			listVisitas = visitaService.buscarPorPosicaoOrRgOrCpf(posicao, visitante.get().getRg(), visitante.get().getCpf(), visitante.get().getNome());
			
			if(listVisitas.size() > 0) {
				result.addError(new ObjectError("visita", " Este visitante já possui " + listVisitas.size() + " registro(s) ativo(s) de entrada!" ));	
			}
		}		
		
		if(!result.hasErrors()) {
			visita.setVisitante(visitante.get());
			visita.setResidencia(residencia.get());
			visita.setPlaca(visitaDto.getPlaca().toUpperCase());
		}
		
		return visita;
		
	}
	
	public Optional<Veiculo> converterVisitaDtoParaVeiculo(VisitaDto visitaDto, BindingResult result) {
		
		if(visitaDto.getVeiculo().getMarca().length() < 2)
			result.addError(new ObjectError("veiculo", "Campo Marca é obrigatório"));
		
		if(visitaDto.getVeiculo().getModelo().length() < 2)
			result.addError(new ObjectError("veiculo","Campo Modelo é obrigatório"));
		
		if(visitaDto.getVeiculo().getCor().length() < 2)
			result.addError(new ObjectError("veiculo","Campo Cor é obrigatório"));
		
		Veiculo veiculo = new Veiculo();
		if(!result.hasErrors()) {			
			veiculo.setPlaca(visitaDto.getPlaca().replace("-", ""));
			veiculo.setMarca(visitaDto.getVeiculo().getMarca());
			veiculo.setModelo(visitaDto.getVeiculo().getModelo());
			veiculo.setCor(visitaDto.getVeiculo().getCor());
			veiculo.setAno(visitaDto.getVeiculo().getAno());
		}		
		
		return Optional.ofNullable(veiculo);
		
	}
	
	public VisitaResponse converterVisitaParaVisitaResponse(Visita visita){
		
		
		VisitaResponse visitaResponse = new VisitaResponse();
		visitaResponse.setId(visita.getId());
		visitaResponse.setNome(visita.getVisitante().getNome());
		visitaResponse.setRg(visita.getVisitante().getRg());
		visitaResponse.setCpf(visita.getVisitante().getCpf() != null ? visita.getVisitante().getCpf() : "");
		visitaResponse.setDataEntrada(Utils.dateFormat(visita.getDataEntrada(), "dd/MM/yyyy"));		
		visitaResponse.setHoraEntrada(new Time(visita.getHoraEntrada().getTime()));
		visitaResponse.setDataSaida(visita.getDataSaida() != null ? Utils.dateFormat(visita.getDataSaida(), "dd/MM/yyyy") : null);
		visitaResponse.setHoraSaida(visita.getHoraSaida() != null ? new Time(visita.getHoraSaida().getTime()) : null);
		visitaResponse.setEndereco(visita.getResidencia().getEndereco() != null ? visita.getResidencia().getEndereco() : "");
		visitaResponse.setNumero(visita.getResidencia().getNumero().toString() != null ? visita.getResidencia().getNumero().toString() : "");
		visitaResponse.setBairro(visita.getResidencia().getBairro());
		visitaResponse.setCidade(visita.getResidencia().getCidade());
		visitaResponse.setUf(visita.getResidencia().getUf());
		visitaResponse.setPlaca(visita.getPlaca());
		visitaResponse.setPosicao(visita.getPosicao());
		
		return visitaResponse;
	}
	

}	

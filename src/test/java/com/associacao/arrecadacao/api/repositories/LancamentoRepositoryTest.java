package com.associacao.arrecadacao.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.associacao.arrecadacao.api.entities.Lancamento;
import com.associacao.arrecadacao.api.entities.Residencia;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	private Long residenciaId;
	private String periodo;

	@Before
	public void setUp() throws Exception {
		
		Residencia residencia = incluirResidencia();
		this.residenciaId = residencia.getId();
		Lancamento lancamento = this.lancamentoRepository.save(obterDadosLancamento());
		this.periodo = lancamento.getPeriodo();
	}

	@After
	public void tearDown() throws Exception {
		this.residenciaRepository.deleteAll();
	}
	
	@Test
	public void testBuscarLancamentosPorResidenciaIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		Page<Lancamento> lancamentos = this.lancamentoRepository.findByResidenciaId(residenciaId, page);
		
		assertEquals(1, lancamentos.getTotalElements());
	}

	@Test
	public void testBuscarLancamentoPorPeriodo() {
		
		Optional<Lancamento> lancamento = this.lancamentoRepository.findByPeriodoAndResidenciaId(periodo, residenciaId);
		
		assertNotNull(lancamento);
	}
	
	private Lancamento obterDadosLancamento() {
		
		Lancamento lancamento = new Lancamento();
		lancamento.setPeriodo("04/2020");
		lancamento.setResidenciaId(this.residenciaId);
		lancamento.setValor(new BigDecimal(80.00));
		
		return lancamento;
	}
	
	private Residencia incluirResidencia() {
		
		Residencia residencia = new Residencia();
		residencia.setEndereco(Mockito.anyString());
		residencia.setNumero(Mockito.anyString());
		residencia.setBairro(Mockito.anyString());
		residencia.setCep(Mockito.anyString());
		residencia.setCidade(Mockito.anyString());
		residencia.setUf(Mockito.anyString());
		
		residenciaRepository.save(residencia);
		
		return residencia;
		
	}

}

package com.associacao.arrecadacao.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Optional;

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
	private String periodo = "09/2019";

	@Before
	public void setUp() throws Exception {
		
		Residencia residencia = this.residenciaRepository.save(obterResidencia());
		this.residenciaId = residencia.getId();
		
		Lancamento lancamento = this.lancamentoRepository.save(obterDadosLancamento());
		this.periodo = lancamento.getPeriodo();
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
		lancamento.setPeriodo("09/2019");
		lancamento.setResidenciaId(this.residenciaId);
		lancamento.setValor(new BigDecimal(80.00));
		
		return lancamento;
	}
	
	private Residencia obterResidencia() {
		
		Residencia residencia = new Residencia();
		residencia.setEndereco(Mockito.anyString());
		residencia.setNumero(Mockito.anyLong());
		residencia.setBairro(Mockito.anyString());
		residencia.setCep(Mockito.anyString());
		residencia.setCidade(Mockito.anyString());
		residencia.setUf(Mockito.anyString());
		
		return residencia;
		
	}

}

package com.associacao.arrecadacao.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.associacao.arrecadacao.api.entities.Lancamento;
import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.repositories.LancamentoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	@MockBean
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ResidenciaService residenciaService;
	
	public static Long residenciaId = 1L;
	
	public static String periodo = "09/2019";
	
	@Before
	public void setUp() {
		BDDMockito
			.given(this.lancamentoRepository.findByResidenciaId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
			.willReturn(new PageImpl<Lancamento>(new ArrayList<Lancamento>()));
		BDDMockito.given(this.lancamentoRepository.findOne(Mockito.anyLong())).willReturn(new Lancamento());
		BDDMockito.given(this.lancamentoRepository.save(Mockito.any(Lancamento.class))).willReturn(new Lancamento());
	}
	
	@Test
	public void testBuscarLancamentoPorResidenciaIdPaginado() {
		Page<Lancamento> lancamento = this.lancamentoService.buscarPorResidenciaId(residenciaId, new PageRequest(0, 10));
		
		assertNotNull(lancamento);
	}
	
	@Test
	public void testBuscarLancamentoPorResidenciaId() {
		List<Lancamento> lancamento = this.lancamentoService.buscarPorResidenciaId(residenciaId);
		
		assertNotNull(lancamento);
	}
	
	@Test
	public void testBuscarLancamentoPorId() {
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(residenciaId);
		
		assertTrue(lancamento.isPresent());
	}
	
	@Test
	public void testBuscarLancamentoPorPeriodo() {
		
		//Optional<Lancamento> lancamento = this.lancamentoService.buscarPorPeriodoAndResidenciaId(periodo, residenciaId);
		
		//assertTrue(lancamento.isPresent());
	}
	
	@Test
	public void testPersistirLancamento() {
		
		//List<Lancamento> lancamentos = this.lancamentoService.persistir(obterDadosLancamento());
		
		//assertNotNull(lancamentos);
	}
	
	private List<Lancamento> obterDadosLancamento() {
		
		List<Lancamento> lista = new ArrayList<Lancamento>();
		
		Lancamento lancamento = new Lancamento();
		lancamento.setPeriodo(periodo);
		lancamento.setResidenciaId(residenciaId);
		lancamento.setValor(new BigDecimal(80.00));
		lista.add(lancamento);
		return lista;
	}


}
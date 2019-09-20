package com.associacao.arrecadacao.api.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
	private static final String MATRICULA = "789765";

	@Before
	public void setUp() throws Exception {
		Residencia residencia = this.residenciaRepository.save(obterDadosResidencia());
		this.residenciaId = residencia.getId();
	}

	@After
	public void tearDown() throws Exception {
		this.residenciaRepository.deleteAll();
	}
	
	@Test
	public void testBuscarLancamentosPorResidenciaIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		Page<Lancamento> lancamentos = this.lancamentoRepository.findByResidenciaId(residenciaId, page);
		
		assertEquals(2, lancamentos.getTotalElements());
	}

	private Residencia obterDadosResidencia() {
		Residencia residencia = new Residencia();
		residencia.setMatricula(MATRICULA);
		residencia.setEndereco("Rua Antonio Candido de Oliveira");
		residencia.setNumero("5");
		residencia.setBairro("Chacara Tres Marias");
		residencia.setCep("04475492");
		residencia.setCidade("Sorocaba");
		residencia.setUf("SP");
		return residencia;
	}

}

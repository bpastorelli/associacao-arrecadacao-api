package com.associacao.arrecadacao.api.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.entities.VinculoResidencia;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VinculoResidenciaRepositoryTest {
	
	@Autowired
	private VinculoResidenciaRepository vinculoResidenciaRepository; 
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	private static final String MATRICULA = "789765";
	
	private static final Long RESIDENCIA_ID = 1L;
	
	private static final Long MORADOR_ID = 1L;
	
	@Before
	public void setUp() throws Exception {
		Residencia residencia = new Residencia();
		residencia.setMatricula(MATRICULA);
		residencia.setEndereco("Rua Antonio Candido de Oliveira");
		residencia.setNumero("5");
		residencia.setBairro("Chacara Tres Marias");
		residencia.setCep("04475492");
		residencia.setCidade("Sorocaba");
		residencia.setUf("SP");
		this.residenciaRepository.save(residencia);
	}
	
	@Test
	public void testVincularResidencia() {
		
		VinculoResidencia vinculo = new VinculoResidencia();
		vinculo.setMoradorId(MORADOR_ID);
		vinculo.setResidenciaId(RESIDENCIA_ID);
		this.vinculoResidenciaRepository.save(vinculo);
		
		VinculoResidencia response = this.vinculoResidenciaRepository.findByResidenciaIdAndMoradorId(RESIDENCIA_ID, MORADOR_ID);
		assertEquals(MORADOR_ID, response.getMoradorId());
	}
	
	@After
    public final void tearDown() { 
		this.residenciaRepository.deleteAll();
		this.vinculoResidenciaRepository.deleteAll();
	}

}

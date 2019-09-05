package com.associacao.arrecadacao.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.repositories.ResidenciaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ResidenciaServiceTest {

	@MockBean
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private ResidenciaService residenciaService;
	
	private static final String MATRICULA = "789567";
	
	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.residenciaRepository.findByMatricula(Mockito.anyString())).willReturn(new Residencia());
		BDDMockito.given(this.residenciaRepository.save(Mockito.any(Residencia.class))).willReturn(new Residencia());
	}
	
	@Test
	public void testBuscarResidenciaPorMatricula() {
		Optional<Residencia> residencia = this.residenciaService.bucarPorMatricula(MATRICULA);
		
		assertTrue(residencia.isPresent());
	}
	
	@Test
	public void testPersistirResidencia() {
		Residencia residencia = this.residenciaService.persistir(new Residencia());
		
		assertNotNull(residencia);
	}
}

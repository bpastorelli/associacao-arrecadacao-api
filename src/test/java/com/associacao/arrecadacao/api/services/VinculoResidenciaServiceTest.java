package com.associacao.arrecadacao.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
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

import com.associacao.arrecadacao.api.entities.VinculoResidencia;
import com.associacao.arrecadacao.api.repositories.VinculoResidenciaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VinculoResidenciaServiceTest {
	
	@MockBean
	private VinculoResidenciaRepository vinculoResidenciaRepository;
	
	@Autowired
	private VinculoResidenciaService vinculoResidenciaService;
	
	private static final Long RESIDENCIA_ID = 1L;
	
	private static final Long MORADOR_ID = 1L;
	
	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.vinculoResidenciaRepository.findByResidenciaIdAndMoradorId(Mockito.anyLong(), Mockito.anyLong())).willReturn(new VinculoResidencia());
		BDDMockito.given(this.vinculoResidenciaRepository.save(Mockito.any(VinculoResidencia.class))).willReturn(new VinculoResidencia());
	}
	
	@Test
	public void testBuscarResidenciaIdAndMoradorId() {
		
		Optional<VinculoResidencia> buscar = this.vinculoResidenciaService.buscarPorResidenciaIdAndMoradorId(RESIDENCIA_ID, MORADOR_ID);
		
		assertTrue(buscar.isPresent());
	}
	
	@Test
	public void testBuscarPorMoradorId() {
		
		Optional<VinculoResidencia> vinculo = this.vinculoResidenciaService.buscarPorMoradorId(MORADOR_ID);
		
		assertNotNull(vinculo);
	}
	
	@Test
	public void testPersistirResidencia() {
		List<VinculoResidencia> vinculo = this.vinculoResidenciaService.persistir(new ArrayList<VinculoResidencia>());
		
		assertNotNull(vinculo);
	}

}

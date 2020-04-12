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

import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.repositories.MoradorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MoradorServiceTest {
	
	@MockBean
	private MoradorRepository moradorRepository;
	
	@Autowired
	private MoradorService moradorService;
	
	@Before
	public void setUp() {
		BDDMockito.given(this.moradorRepository.save(Mockito.any(Morador.class))).willReturn(new Morador());
		BDDMockito.given(this.moradorRepository.findByCpf(Mockito.anyString())).willReturn(new Morador());
		//BDDMockito.given(this.moradorRepository.findByRg(Mockito.anyString())).willReturn(new Morador());
		//BDDMockito.given(this.moradorRepository.findByEmail(Mockito.anyString())).willReturn(new  );
		BDDMockito.given(this.moradorRepository.findOne(Mockito.anyLong())).willReturn(new Morador());
	}
	
	@Test
	public void testPersistirMorador() {
		List<Morador> moradores = this.moradorService.persistir(new ArrayList<Morador>());
		
		assertNotNull(moradores);
	}
	
	@Test
	public void testBuscarMoradorPorId() {
		Optional<Morador> morador = this.moradorService.buscarPorId(1L);
		
		assertTrue(morador.isPresent());
	}
	
	@Test
	public void testBuscarMoradorPorEmail() {
		List<Morador> morador = this.moradorService.bucarPorEmail("email@email.com");
		
		assertTrue(morador.size() > 0);
	}

	@Test
	public void testBuscarMoradorPorCpf() {
		Optional<Morador> morador = this.moradorService.buscarPorCpf("24291173474");
		
		assertTrue(morador.isPresent());		
	}
	
	@Test
	public void testBuscarMoradorPorRg() {
		List<Morador> morador = this.moradorService.buscarPorRg("352739980");
		
		assertTrue(morador.size() > 0);		
	}
}

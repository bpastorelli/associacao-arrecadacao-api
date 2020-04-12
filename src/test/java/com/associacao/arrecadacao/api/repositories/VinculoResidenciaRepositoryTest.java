package com.associacao.arrecadacao.api.repositories;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.entities.VinculoResidencia;
import com.associacao.arrecadacao.api.enums.PerfilEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VinculoResidenciaRepositoryTest {
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private ResidenciaRepository residenciaRepository;

	@Autowired
	private VinculoResidenciaRepository vinculoResidenciaRepository; 	
	
	private static Morador morador;
	
	private static Residencia residencia;
	
	private static VinculoResidencia vinculo;
	
	private static final String MATRICULA = "789765";
	
	private static final Long MORADOR_ID = 1L;
	
	private static final String EMAIL = "email@email.com";
	
	private static final String CPF = "24291173474";
	
	private static final String RG = "3426576856";
	
	@Before
	public void setUp() throws Exception {
		
		
		residencia = new Residencia();
		residencia.setMatricula(MATRICULA);
		residencia.setEndereco("Rua Antonio Candido de Oliveira");
		residencia.setNumero("5");
		residencia.setBairro("Chacara Tres Marias");
		residencia.setCep("04475492");
		residencia.setCidade("Sorocaba");
		residencia.setUf("SP");
		this.residenciaRepository.save(residencia);
		
		morador = new Morador();
		morador.setNome("Carlos do Santos");
		morador.setCpf(CPF);
		morador.setEmail(EMAIL);
		morador.setRg(RG);
		morador.setTelefone("1167897856");
		morador.setPerfil(PerfilEnum.ROLE_USUARIO);
		morador.setSenha("123456");
		this.moradorRepository.save(morador);
		
		vinculo = new VinculoResidencia();
		vinculo.setMoradorId(morador.getId());
		vinculo.setResidenciaId(residencia.getId());
		this.vinculoResidenciaRepository.save(vinculo);
	}
	
	@Test
	public void testVincularResidencia() {
		
		VinculoResidencia response = this.vinculoResidenciaRepository.findByResidenciaIdAndMoradorId(residencia.getId(), morador.getId());
		assertEquals(MORADOR_ID, response.getMoradorId());
	}
	
	@Test
	public void testBuscarPorMoradorId() {
		
		List<VinculoResidencia> response = this.vinculoResidenciaRepository.findByMoradorId(morador.getId());
		assertEquals(residencia.getId(), response.get(0).getResidenciaId());
	}
	
	@After
    public final void tearDown() {
		this.vinculoResidenciaRepository.deleteAll();
		this.residenciaRepository.deleteAll();
	}

}

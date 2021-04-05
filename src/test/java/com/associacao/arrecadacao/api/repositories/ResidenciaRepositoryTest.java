package com.associacao.arrecadacao.api.repositories;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.associacao.arrecadacao.api.entities.Residencia;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ResidenciaRepositoryTest {
	
	@Autowired
	private ResidenciaRepository residenciaRepository;

	@Before
	public void setUp() throws Exception {
		Residencia residencia = new Residencia();
		residencia.setEndereco("Rua Antonio Candido de Oliveira");
		residencia.setNumero(5L);
		residencia.setBairro("Chacara Tres Marias");
		residencia.setCep("04475492");
		residencia.setCidade("Sorocaba");
		residencia.setUf("SP");
		this.residenciaRepository.save(residencia);
	}
	
	@After
    public final void tearDown() { 
		//this.residenciaRepository.deleteAll();
	}

}

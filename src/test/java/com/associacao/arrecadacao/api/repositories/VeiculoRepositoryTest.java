package com.associacao.arrecadacao.api.repositories;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.associacao.arrecadacao.api.entities.Veiculo;

import io.jsonwebtoken.lang.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VeiculoRepositoryTest {
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Before
	public void setUp() throws Exception {
		Veiculo veiculo = this.veiculoRepository.save(this.obterDadosVeiculo());
		Assert.isTrue(veiculo.getId() > 0);
	}
	
	private Veiculo obterDadosVeiculo() {
		Veiculo veiculo = new Veiculo();
		veiculo.setId(1l);
		veiculo.setPlaca("DRF-1831");
		veiculo.setMarca("VW");
		veiculo.setModelo("FOX");
		veiculo.setAno(2005L);
		veiculo.setPosicao(1L);
		
		return veiculo;
	}

}

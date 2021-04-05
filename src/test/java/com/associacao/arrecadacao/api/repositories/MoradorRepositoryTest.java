package com.associacao.arrecadacao.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;

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
import com.associacao.arrecadacao.api.enums.PerfilEnum;
import com.associacao.arrecadacao.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MoradorRepositoryTest {

	@Autowired
	private MoradorRepository moradorRepository;

	@Autowired
	private ResidenciaRepository residenciaRepository;

	private static final String EMAIL = "email@email.com";
	private static final String CPF = "24291173474";
	private static final String RG = "342739930";

	@Before
	public void setUp() throws Exception {
		Residencia residencia = this.residenciaRepository.save(obterDadosResidencia());
		this.moradorRepository.save(obterDadosMorador(residencia));
	}

	@After
	public final void tearDown() {
		this.residenciaRepository.deleteAll();
		this.moradorRepository.deleteAll();
	}

	@Test
	public void testBuscarMoradorPorEmail() {
		Morador morador = this.moradorRepository.findByEmail(EMAIL);

		assertEquals(EMAIL, morador.getEmail());
	}

	@Test
	public void testBuscarMoradorPorCpf() {
		Morador morador = this.moradorRepository.findByCpf(CPF);

		assertEquals(CPF, morador.getCpf());
	}
	
	@Test
	public void testBuscarMoradorPorRg() {
		Morador morador = this.moradorRepository.findByRg(RG);

		assertEquals(CPF, morador.getCpf());
	}

	@Test
	public void testBuscarMoradorPorEmailECpf() {
		Morador morador = this.moradorRepository.findByCpfOrEmail(CPF, EMAIL);

		assertNotNull(morador);
	}

	@Test
	public void testBuscarMoradorPorEmailOuCpfParaEmailInvalido() {
		Morador morador = this.moradorRepository.findByCpfOrEmail(CPF, "email@invalido.com");

		assertNotNull(morador);
	}

	@Test
	public void testBuscarMoradorPorEmailECpfParaCpfInvalido() {
		Morador morador = this.moradorRepository.findByCpfOrEmail("12345678901", EMAIL);

		assertNotNull(morador);
	}

	private Morador obterDadosMorador(Residencia residencia) throws NoSuchAlgorithmException {
		Morador morador = new Morador();
		morador.setNome("Fulano de Tal");
		morador.setPerfil(PerfilEnum.ROLE_USUARIO);
		morador.setSenha(PasswordUtils.gerarBCrypt("123456"));
		morador.setCpf(CPF);
		morador.setRg(RG);
		morador.setTelefone("1156789087");
		morador.setCelular("11978653456");
		morador.setEmail(EMAIL);
		return morador;
	}

	private Residencia obterDadosResidencia() {
		Residencia residencia = new Residencia();
		residencia.setEndereco("Rua Antonio Candido de Oliveira");
		residencia.setNumero(5L);
		residencia.setBairro("Chacara Tres Marias");
		residencia.setCep("04475492");
		residencia.setCidade("Sorocaba");
		residencia.setUf("SP");
		return residencia;
	}

}

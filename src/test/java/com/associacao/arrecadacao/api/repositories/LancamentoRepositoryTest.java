package com.associacao.arrecadacao.api.repositories;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

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
import com.associacao.arrecadacao.api.entities.Morador;
import com.associacao.arrecadacao.api.entities.Residencia;
import com.associacao.arrecadacao.api.enums.PerfilEnum;
import com.associacao.arrecadacao.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	private Long moradorId;
	private Long residenciaId;
	private static final String EMAIL = "email@email.com";
	private static final String MATRICULA = "789765";
	private static final String CPF = "24291173474";
	private static final String RG = "342739930";

	@Before
	public void setUp() throws Exception {
		Residencia residencia = this.residenciaRepository.save(obterDadosResidencia());
		this.residenciaId = residencia.getId();
		
		Morador morador = this.moradorRepository.save(obterDadosMorador(residencia));
		this.moradorId = morador.getId();
		
		this.lancamentoRepository.save(obterDadosLancamentos(morador));
		this.lancamentoRepository.save(obterDadosLancamentos(morador));
	}

	@After
	public void tearDown() throws Exception {
		this.residenciaRepository.deleteAll();
	}

	@Test
	public void testBuscarLancamentosPorMoradorId() {
		List<Lancamento> lancamentos = this.lancamentoRepository.findByMoradorId(moradorId);
		
		assertEquals(2, lancamentos.size());
	}
	
	@Test
	public void testBuscarLancamentosPorMoradorIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		Page<Lancamento> lancamentos = this.lancamentoRepository.findByMoradorId(moradorId, page);
		
		assertEquals(2, lancamentos.getTotalElements());
	}
	
	@Test
	public void testBuscarLancamentosPorResidenciaIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		Page<Lancamento> lancamentos = this.lancamentoRepository.findByResidenciaId(residenciaId, page);
		
		assertEquals(2, lancamentos.getTotalElements());
	}
	
	@SuppressWarnings("deprecation")
	private Lancamento obterDadosLancamentos(Morador morador) {
		
		BigDecimal valor =  new BigDecimal(87.56); 
		
		Lancamento lancamento = new Lancamento();
		lancamento.setDataPagamento(new Date());
		lancamento.setMesReferencia(new Date().getMonth());
		lancamento.setValor(valor);
		lancamento.setMorador(morador);
		lancamento.setUsuarioRecebimento(1L);
		lancamento.setResidenciaId(1L);
		return lancamento;
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
		morador.setResidencia(residencia.getId());
		return morador;
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

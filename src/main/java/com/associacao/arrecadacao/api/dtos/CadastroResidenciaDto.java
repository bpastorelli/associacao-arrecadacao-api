package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.associacao.arrecadacao.api.entities.Lancamento;
import com.associacao.arrecadacao.api.entities.Morador;

public class CadastroResidenciaDto {
	
	private Long   id;
	private String matricula;
	private String endereco;
	private String numero;
	private String bairro;
	private String cep;
	private String cidade;
	private String uf;
	private List<Morador> moradores;
	private List<Lancamento> lancamentos;
	
	public CadastroResidenciaDto() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@NotEmpty(message = "Matrícula não pode ser nulo.")
	@Length(min = 3, max = 10, message = "Matricula deve conter entre 3 e 10 caracteres.")
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@NotEmpty(message = "Endereço não pode ser nulo.")
	@Length(min = 10, max = 200, message = "Endereço deve conter entre 10 e 200 caracteres.")
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@NotEmpty(message = "Número não pode ser nulo.")
	@Length(min = 1, max = 5, message = "Número deve conter entre 1 e 5 caracteres.")
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	@NotEmpty(message = "Bairro não pode ser nulo.")
	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}	
	
	@NotEmpty(message = "CEP não pode ser nulo.")
	@Length(min = 8, max = 8, message = "Campo CEP deve conter 8 caracteres sem traço.")
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}	
	
	@NotEmpty(message = "Cidade não pode ser nulo.")
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}	
	
	@NotEmpty(message = "UF não pode ser nulo.")
	@Length(min = 2, max = 2, message = "Campo UF deve conter 2 caracteres.")
	public String getUf() {
		return uf;
	}
	
	public void setUf(String uf) {
		this.uf = uf;
	}	

	@NotEmpty(message = "O campo moradores deve conter ao menos um morador.")
	public List<Morador> getMoradores(){
		return moradores;
	}
	
	public void setMoradores(List<Morador> moradores) {
		this.moradores = moradores;
	}
	
	@NotEmpty(message = "O campo moradores deve conter ao menos um morador.")
	public List<Lancamento> getLancamentos(){
		return lancamentos;
	}
	
	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}	
}

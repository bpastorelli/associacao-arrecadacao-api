package com.associacao.arrecadacao.api.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class CadastroResidenciaDto {
	
	private Long   id;
	private String endereco;
	private Long   numero;
	private String complemento;
	private String bairro;
	private String cep;
	private String cidade;
	private String uf;
	
	public CadastroResidenciaDto() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty(message = "Endereço não pode ser nulo.")
	@Length(min = 10, max = 200, message = "Endereço deve conter entre 10 e 200 caracteres.")
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@NotNull(message = "Número não pode ser nulo.")
	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}
	
	@Length(min = 0, max = 50, message = "Campo Complemento deve conter no máximo 50 caracteres.")
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
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


}

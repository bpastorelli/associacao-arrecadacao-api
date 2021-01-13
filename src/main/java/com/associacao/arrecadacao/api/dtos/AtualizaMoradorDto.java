package com.associacao.arrecadacao.api.dtos;

import org.hibernate.validator.constraints.NotEmpty;

public class AtualizaMoradorDto {

	private String nome;
	private String email;
	private String rg;
	private String telefone;
	private String celular;
	private Long posicao;
	private String[] errors;
	
	public AtualizaMoradorDto() {
		
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@NotEmpty(message = " Nome não pode ser nulo")
	public String getNome() {
		return nome;
	}
	
	@NotEmpty(message = " RG não pode ser nulo")
	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}
	
	@NotEmpty(message = " E-mail não pode ser nulo")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@NotEmpty(message = " Celular não pode ser nulo")
	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Long getPosicao() {
		return posicao;
	}

	public void setPosicao(Long posicao) {
		this.posicao = posicao;
	}

	public String[] getErrors() {
		return errors;
	}

	public void setErrors(String[] errors) {
		this.errors = errors;
	}
	
}

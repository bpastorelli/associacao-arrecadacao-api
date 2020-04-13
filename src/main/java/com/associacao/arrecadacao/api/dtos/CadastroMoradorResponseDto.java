package com.associacao.arrecadacao.api.dtos;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.associacao.arrecadacao.api.enums.PerfilEnum;

public class CadastroMoradorResponseDto {

	private Long id;
	private String nome;
	private String email;
	private String cpf;
	private String rg;
	private String senha;
	private String telefone;
	private String celular;
	private PerfilEnum perfil;
	private String dataCriacao;
	private String dataAtualizacao;
	private Long residenciaId;
	
	public CadastroMoradorResponseDto() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@NotEmpty(message = "Nome não pode ser nulo.")
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getCpf() {
		return cpf;
	}

	@NotEmpty(message = "CPF não pode ser nulo.")
	@Length(min = 11, max = 11, message = "CPF deve conter 11 caracteres.")
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getRg() {
		return rg;
	}

	@NotEmpty(message = "RG não pode ser nulo.")
	public void setRg(String rg) {
		this.rg = rg;
	}
	
	public String getEmail() {
		return email;
	}

	@NotEmpty(message = "E-mail não pode ser nulo.")
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public PerfilEnum getPerfil() {
		return perfil;
	}

	@NotEmpty(message = "Perfil não pode ser nulo.")
	public void setPerfil(PerfilEnum perfil) {
		this.perfil = perfil;
	}

	public String getSenha() {
		return senha;
	}

	@NotEmpty(message = "Senha não pode ser nulo.")
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(String dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Long getResidenciaId() {
		return residenciaId;
	}

	@NotEmpty(message = "residenciaId não pode ser nulo.")
	public void setResidenciaId(Long residenciaId) {
		this.residenciaId = residenciaId;
	}
	
}

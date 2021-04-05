package com.associacao.arrecadacao.api.entities;

import java.io.Serializable;
import java.sql.Time;


public class VisitaResponse implements Serializable {
	
	private static final long serialVersionUID = -5754246207015712520L;
	
	private Long    id;
	private String  nome;
	private String  rg;
	private String  cpf;
	private String  dataEntrada;
	private Time    horaEntrada;
	private String  dataSaida;
	private Time    horaSaida;
	private String  endereco;
	private String  numero;
	private String  bairro;
	private String  cidade;
	private String  uf;
	private String  placa;
	private Integer posicao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}	
	
	public String getRg() {
		return rg;
	}
	
	public void setRg(String rg) {
		this.rg = rg;
	}
	
	public String getDataEntrada() {
		return dataEntrada;
	}
	
	public void setDataEntrada(String dataEntrada) {
		this.dataEntrada = dataEntrada;
	}
	
	public Time getHoraEntrada() {
		return horaEntrada;
	}
	
	public String getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(String dataSaida) {
		this.dataSaida = dataSaida == null ? "" : dataSaida;
	}
	
	public void setHoraEntrada(Time horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	
	public Time getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(Time horaSaida) {
		this.horaSaida = horaSaida;
	}
	
	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getBairro() {
		return bairro;
	}
	
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	public String getCidade() {
		return cidade;
	}
	
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	public String getUf() {
		return uf;
	}
	
	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}


}

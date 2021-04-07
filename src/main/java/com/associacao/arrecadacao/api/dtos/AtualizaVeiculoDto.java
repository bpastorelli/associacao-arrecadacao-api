package com.associacao.arrecadacao.api.dtos;

public class AtualizaVeiculoDto {
	
	private String marca;
	private String modelo;
	private Long   ano;
	private Long   posicao;
	
	public AtualizaVeiculoDto() {
		
	}
	
	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public Long getAno() {
		return ano;
	}

	public void setAno(Long ano) {
		this.ano = ano;
	}

	public Long getPosicao() {
		return posicao;
	}

	public void setPosicao(Long posicao) {
		this.posicao = posicao;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

}

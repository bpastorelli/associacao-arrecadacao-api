package com.associacao.arrecadacao.api.dtos;

import org.hibernate.validator.constraints.NotEmpty;

public class VeiculoDto {
	
	private String placa;
	private String marca;
	private String modelo;
	private String cor;
	private Long   ano;
	private Long   visitanteId;
	
	public VeiculoDto() {
		
	}

	@NotEmpty(message = "Campo Placa é obrigatório!")
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	@NotEmpty(message = "O campo Modelo é obrigatório!")
	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	@NotEmpty(message = "O campo Cor é obrigatório!")
	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}
	
	public Long getAno() {
		return ano;
	}

	public void setAno(Long ano) {
		this.ano = ano;
	}

	public Long getVisitanteId() {
		return visitanteId;
	}

	public void setVisitanteId(Long visitanteId) {
		this.visitanteId = visitanteId;
	}

}

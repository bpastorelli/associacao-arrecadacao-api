package com.associacao.arrecadacao.api.access.dtos;

import org.hibernate.validator.constraints.NotEmpty;

public class AtualizaModuloDto {
	
	private String descricao;
	private String pathModulo;
	private Long posicao;
	
	public AtualizaModuloDto() {
		
	}

	@NotEmpty(message = "O campo decrição do módulo é obrigatório")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@NotEmpty(message = "O campo path do módulo é obrigatório")
	public String getPathModulo() {
		return pathModulo;
	}

	public void setPathModulo(String pathModulo) {
		this.pathModulo = pathModulo;
	}

	public Long getPosicao() {
		return posicao;
	}

	public void setPosicao(Long posicao) {
		this.posicao = posicao;
	}

}

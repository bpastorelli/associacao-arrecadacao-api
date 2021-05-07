package com.associacao.arrecadacao.api.access.dtos;

import org.hibernate.validator.constraints.NotEmpty;

public class CadastroModuloDto {
	
	private String descricao;
	private String pathModulo;
	
	public CadastroModuloDto() {
		
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

}

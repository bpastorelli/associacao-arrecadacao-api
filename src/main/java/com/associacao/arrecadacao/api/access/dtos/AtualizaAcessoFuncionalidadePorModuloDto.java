package com.associacao.arrecadacao.api.access.dtos;

import org.hibernate.validator.constraints.NotEmpty;

public class AtualizaAcessoFuncionalidadePorModuloDto {
	
	private Long idFuncionalidade;
	private boolean acesso;
	
	public AtualizaAcessoFuncionalidadePorModuloDto() {
		
	}

	@NotEmpty(message = "O campo id da funcionalidade é obrigatório")
	public Long getIdFuncionalidade() {
		return idFuncionalidade;
	}

	public void setIdFuncionalidade(Long idFuncionalidade) {
		this.idFuncionalidade = idFuncionalidade;
	}
	
	public boolean isAcesso() {
		return acesso;
	}

	public void setAcesso(boolean acesso) {
		this.acesso = acesso;
	}

}

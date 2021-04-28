package com.associacao.arrecadacao.api.access.dtos;

import org.hibernate.validator.constraints.NotEmpty;

public class AtualizaAcessoDto {
	
	
	private Long idModulo;
	private Long idFuncionalidade;
	private boolean acesso;
	
	public AtualizaAcessoDto() {
		
	}

	@NotEmpty(message = "O campo id do módulo é obrigatório")
	public Long getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
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

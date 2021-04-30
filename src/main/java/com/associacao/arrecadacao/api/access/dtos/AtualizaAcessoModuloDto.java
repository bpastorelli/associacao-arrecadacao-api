package com.associacao.arrecadacao.api.access.dtos;

import org.hibernate.validator.constraints.NotEmpty;

public class AtualizaAcessoModuloDto {
	
	
	private Long idModulo;
	private boolean acesso;
	
	public AtualizaAcessoModuloDto() {
		
	}

	@NotEmpty(message = "O campo id do módulo é obrigatório")
	public Long getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	
	public boolean isAcesso() {
		return acesso;
	}

	public void setAcesso(boolean acesso) {
		this.acesso = acesso;
	}

}

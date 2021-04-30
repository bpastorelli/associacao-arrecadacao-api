package com.associacao.arrecadacao.api.access.dtos;

import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class CadastroAcessoModuloDto {
	
	private Long idUsuario;
	private Long idModulo;
	private boolean acesso;
	private List<CadastroAcessoFuncionalidadeDto> funcionalidades;
	
	public CadastroAcessoModuloDto() {
		
	}

	@NotEmpty(message = "O campo id do usuário é obrigatório")
	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
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
	
	@Size(min = 1, message = "Você deve incluir ao menos uma funcionalidade.")
	public List<CadastroAcessoFuncionalidadeDto> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<CadastroAcessoFuncionalidadeDto> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

}

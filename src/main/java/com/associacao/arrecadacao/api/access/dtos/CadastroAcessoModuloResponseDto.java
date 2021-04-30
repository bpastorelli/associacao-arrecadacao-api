package com.associacao.arrecadacao.api.access.dtos;

import java.util.List;

public class CadastroAcessoModuloResponseDto {
	
	private List<CadastroAcessoModuloDto> modulos;
	private List<CadastroAcessoFuncionalidadeDto> funcionalidades;
	
	public CadastroAcessoModuloResponseDto() {
			
	}

	public List<CadastroAcessoModuloDto> getModulos() {
		return modulos;
	}

	public void setModulos(List<CadastroAcessoModuloDto> modulos) {
		this.modulos = modulos;
	}

	public List<CadastroAcessoFuncionalidadeDto> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<CadastroAcessoFuncionalidadeDto> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}	

}

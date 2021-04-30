package com.associacao.arrecadacao.api.access.dtos;

import java.util.List;

import com.associacao.arrecadacao.api.access.entities.AcessoFuncionalidade;
import com.associacao.arrecadacao.api.access.entities.AcessoModulo;

public class CadastroAcessoModuloResponseDto {
	
	private List<AcessoModulo> modulos;
	private List<AcessoFuncionalidade> funcionalidades;
	
	public CadastroAcessoModuloResponseDto() {
			
	}

	public List<AcessoModulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<AcessoModulo> modulos) {
		this.modulos = modulos;
	}

	public List<AcessoFuncionalidade> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<AcessoFuncionalidade> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}	

}

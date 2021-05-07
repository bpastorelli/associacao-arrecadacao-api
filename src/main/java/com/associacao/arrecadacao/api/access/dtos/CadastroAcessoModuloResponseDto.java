package com.associacao.arrecadacao.api.access.dtos;

import java.util.List;

import com.associacao.arrecadacao.api.access.entities.AcessoFuncionalidade;
import com.associacao.arrecadacao.api.access.entities.Modulo;

public class CadastroAcessoModuloResponseDto {
	
	private List<Modulo> modulos;
	private List<AcessoFuncionalidade> funcionalidades;
	
	public CadastroAcessoModuloResponseDto() {
			
	}

	public List<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	public List<AcessoFuncionalidade> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<AcessoFuncionalidade> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}	

}

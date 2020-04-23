package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import javax.validation.constraints.Size;


import com.associacao.arrecadacao.api.entities.Lancamento;

public class CadastroLancamentoDto {
	
	private List<Lancamento> lancamentos;
	
	public CadastroLancamentoDto() {
		
	}
	
	@Size(min = 1, message = "Você deve incluir ao menos um lançamento.")
	public List<Lancamento> getLancamentos(){
		return lancamentos;
	}
	
	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

}

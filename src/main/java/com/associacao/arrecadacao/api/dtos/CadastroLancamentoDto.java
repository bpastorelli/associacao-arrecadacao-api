package com.associacao.arrecadacao.api.dtos;

import java.util.List;

import com.associacao.arrecadacao.api.entities.Lancamento;

public class CadastroLancamentoDto {
	
	private List<Lancamento> lancamentos;
	
	public CadastroLancamentoDto() {
		
	}
	
	public List<Lancamento> getLancamentos(){
		return lancamentos;
	}
	
	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

}

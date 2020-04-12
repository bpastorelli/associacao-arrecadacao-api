package com.associacao.arrecadacao.api.entities;

import java.util.List;

public class ProcessoCadastro {

	private Residencia residencia;
	private List<Morador> moradores;
	private List<Lancamento> lancamentos;
	
	public Residencia getResidencia() {
		
		return residencia;
	}
	
	public void setResidencia(Residencia residencia) {
		
		this.residencia = residencia;
	}
	
	public List<Morador> getMoradores(){
		
		return moradores;
	}
	
	public void setMoradores(List<Morador> moradores) {
		
		this.moradores = moradores;
	}
	
	public List<Lancamento> getLancamentos() {
		
		return lancamentos;
	}
	
	public void setLancamentos(List<Lancamento> lancamentos) {
		
		this.lancamentos = lancamentos;
	}
	
}

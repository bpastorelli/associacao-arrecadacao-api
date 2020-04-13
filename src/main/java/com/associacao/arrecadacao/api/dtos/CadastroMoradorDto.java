package com.associacao.arrecadacao.api.dtos;

import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;
import com.associacao.arrecadacao.api.entities.Morador;

public class CadastroMoradorDto {
	
	private List<Morador> moradores;
	
	public CadastroMoradorDto() {
		
	}
	
	@NotEmpty(message = "O campo moradores deve conter ao menos um morador.")
	public List<Morador> getMoradores(){
		return moradores;
	}
	
	public void setMoradores(List<Morador> moradores) {
		this.moradores = moradores;
	}
	
}

package com.associacao.arrecadacao.api.access.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "funcionalidade")
public class Funcionalidade implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;
	
	private Long id;
	private String descricao;
	private String pathFuncionalidade;
	private Long posicao;
	
	public Funcionalidade() {
		
		
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "descricao", nullable = false)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "path_funcionalidade", nullable = false)
	public String getPathFuncionalidade() {
		return pathFuncionalidade;
	}

	public void setPathFuncionalidade(String pathFuncionalidade) {
		this.pathFuncionalidade = pathFuncionalidade;
	}

	@Column(name = "posicao", nullable = false)
	public Long getPosicao() {
		return posicao;
	}

	public void setPosicao(Long posicao) {
		this.posicao = posicao;
	}
	
    @PrePersist
    public void prePersist() {
        final long status = 1;
        posicao = status;
    }


}

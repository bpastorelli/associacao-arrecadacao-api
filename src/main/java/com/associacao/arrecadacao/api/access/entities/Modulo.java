package com.associacao.arrecadacao.api.access.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "modulo")
public class Modulo implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;
	
	private Long id;
	private String descricao;
	private String pathModulo;
	private Long posicao;
	
	public Modulo() {
		
		
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

	@Column(name = "path_modulo", nullable = false)
	public String getPathModulo() {
		return pathModulo;
	}

	public void setPathModulo(String pathModulo) {
		this.pathModulo = pathModulo;
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

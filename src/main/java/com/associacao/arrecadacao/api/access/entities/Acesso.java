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
@Table(name = "acesso")
public class Acesso implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;
	
	private Long id;
	private Long idUsuario;
	private Long idModulo;
	private Long idFuncionalidade;
	private Date dataCadastro;
	private Long posicao;
	
	public Acesso() {
		
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "id_usuario", nullable = false)
	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long id_usuario) {
		this.idUsuario = id_usuario;
	}

    @Column(name = "id_modulo", nullable = false)
	public Long getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	
	@Column(name = "id_funcionalidade", nullable = false)
	public Long getIdFuncionalidade() {
		return idFuncionalidade;
	}

	public void setIdFuncionalidade(Long id_funcionalidade) {
		this.idFuncionalidade = id_funcionalidade;
	}

	@Column(name = "data_cadastro", nullable = false)
	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date data_cadastro) {
		this.dataCadastro = data_cadastro;
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
        final Date atual = new Date();
        final long status = 1;
        dataCadastro = atual;
        posicao = status;
    }

}

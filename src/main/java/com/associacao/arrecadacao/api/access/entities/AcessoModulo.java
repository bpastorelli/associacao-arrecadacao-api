package com.associacao.arrecadacao.api.access.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "acesso_modulo")
public class AcessoModulo implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;
	
	private Long id;
	private Long idUsuario;
	private Modulo modulo;
	private boolean acesso;
	private Date dataCadastro;
	private Long posicao;
	
	public AcessoModulo() {
		
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
	
    @Column(name = "acesso", nullable = false)
	public boolean isAcesso() {
		return acesso;
	}

	public void setAcesso(boolean acesso) {
		this.acesso = acesso;
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

	@ManyToOne
	@JoinColumn(name="id_modulo")
	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

}

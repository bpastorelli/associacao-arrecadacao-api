package com.associacao.arrecadacao.api.entities;

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
@Table(name = "vinculo_veiculo")
public class VinculoVeiculo implements Serializable {
	
	private static final long serialVersionUID = 3960436649365666214L;
	
	private Long      id;
	private Veiculo   veiculo;
	private Visitante visitante;
	private Date      dataVinculo;
	private Long      posicao;

	public VinculoVeiculo() {
		
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="veiculo_id")
	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

    @ManyToOne
    @JoinColumn(name = "visitante_id")
	public Visitante getVisitante() {
		return visitante;
	}

	public void setVisitante(Visitante visitante) {
		this.visitante = visitante;
	}

	@Column(name = "data_vinculo", nullable = false)
	public Date getDataVinculo() {
		return dataVinculo;
	}

	public void setDataVinculo(Date dataVinculo) {
		this.dataVinculo = dataVinculo;
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
        dataVinculo = atual;
        posicao = 1L;
    }
    
	@Override
	public String toString() {
		return "Vinculo [id=" + id + ", visitante =" + visitante + ", veiculo = " + veiculo + ", dataVinculo=" + dataVinculo + "]";
	}	
	
}

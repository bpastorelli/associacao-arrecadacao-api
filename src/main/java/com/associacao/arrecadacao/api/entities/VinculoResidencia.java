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
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "vinculo_residencia")
public class VinculoResidencia implements Serializable {
	
	private static final long serialVersionUID = 3960436649365666214L;
	
	private Long id;
	private Date dataVinculo;
	private Morador morador;
	private Residencia residencia;
	
	public VinculoResidencia() {
		
	}
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		
		return id;
	}
	
	public void setId(Long id) {
		
		this.id = id;
	}
	
	@Column(name = "data_vinculo", nullable = false)
	public Date getDataVinculo() {
		return dataVinculo;
	}
	
	public void setDataVinculo(Date dataVinculo) {
		this.dataVinculo = dataVinculo;
	}
	
    @ManyToOne
    @JoinColumn(name = "morador_id")
	public Morador getMorador() {
		return morador;
	}
	
	public void setMorador(Morador morador) {
		this.morador = morador;
	}
	
	@ManyToOne
	@JoinColumn(name="residencia_id")
	public Residencia getResidencia() {
		return residencia;
	}
	
	public void setResidencia(Residencia residencia) {
		this.residencia = residencia;
	}
	
	@PreUpdate
    public void preUpdate() {
		dataVinculo = new Date();
    }
     
    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        dataVinculo = atual;
    }
    
	@Override
	public String toString() {
		return "Vinculo [id=" + id + ", dataVinculo=" + dataVinculo + "]";
	}

}

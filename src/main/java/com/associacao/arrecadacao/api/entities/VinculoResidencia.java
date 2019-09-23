package com.associacao.arrecadacao.api.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "vinculo_residencia")
public class VinculoResidencia implements Serializable {
	
	private static final long serialVersionUID = 3960436649365666214L;
	
	private Long id;
	private Long moradorId;
	private Long residenciaId;
	private Date dataVinculo;
	
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
	
	@Column(name = "", nullable = false)
	public Long getMoradorId() {
		
		return moradorId;
	}
	
	public void setMoradorId(Long moradorId) {
		
		this.moradorId = moradorId;
	}
	
	@Column(name = "residencia_id", nullable = false)
	public Long getResidenciaId() {
		
		return residenciaId;
	}

	public void setResidenciaId(Long residenciaId) {
		
		this.residenciaId = residenciaId;
	}
	
	@Column(name = "data_vinculo", nullable = false)
	public Date getDataVinculo() {
		return dataVinculo;
	}
	
	public void setDataVinculo(Date dataVinculo) {
		this.dataVinculo = dataVinculo;
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
		return "Vinculo [id=" + id + ", moradorId=" + moradorId + ", residenciaId=" + residenciaId + ", dataVinculo=" + dataVinculo + "]";
	}

}

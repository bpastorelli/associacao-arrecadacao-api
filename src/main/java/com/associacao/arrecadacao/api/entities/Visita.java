package com.associacao.arrecadacao.api.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "visita")
public class Visita implements Serializable {

	private static final long serialVersionUID = -5754246207015712520L;
	
	private Long id;
	private Long visitanteId;
	private Long residenciaId;
	private Date dataEntrada;
	private Long horaEntrada;
	private Date dataSaida;
	private Long horaSaida;
	private Long posicao;
	
	public Visita(){
		
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@Column(name = "visitante_id", nullable = false)
	public Long getVisitanteId() {
		return visitanteId;
	}

	public void setVisitanteId(Long visitante_id) {
		this.visitanteId = visitante_id;
	}

	
	@Column(name = "residente_id", nullable = false)
	public Long getResidenciaId() {
		return residenciaId;
	}

	public void setResidenciaId(Long residencia_id) {
		this.residenciaId = residencia_id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_entrada", nullable = false)
	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date data_entrada) {
		this.dataEntrada = data_entrada;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "hora_entrada", nullable = false)
	public Long getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(Long horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_saida", nullable = false)
	public Date getData_saida() {
		return dataSaida;
	}

	public void setData_saida(Date data_saida) {
		this.dataSaida = data_saida;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "hora_saida", nullable = false)
	public Long getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(Long horaSaida) {
		this.horaSaida = horaSaida;
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
		
        final Date dataAtual = new Date();
        final Long horaAtual = new Date().getTime();
        final long status = 1;
        dataEntrada = dataAtual;
        horaEntrada = horaAtual;
        posicao = status;
        
	}
	
	
	
}

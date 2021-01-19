package com.associacao.arrecadacao.api.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "visita")
public class Visita implements Serializable {

	private static final long serialVersionUID = -5754246207015712520L;
	
	private Long id;
	private Long residenciaId;
	private Visitante visitante;
	private Date dataEntrada;
	private Date horaEntrada;
	private Date dataSaida;
	private Date horaSaida;
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
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_entrada", nullable = false)
	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date data_entrada) {
		this.dataEntrada = data_entrada;
	}
	
	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "hora_entrada", nullable = false)
	public Date getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(Date horaEntrada) {
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

	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "hora_saida", nullable = false)
	public Date getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(Date horaSaida) {
		this.horaSaida = horaSaida;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Visitante getVisitante() {
		return visitante;
	}

	public void setVisitante(Visitante visitante) {
		this.visitante = visitante;
	}
	
	@Column(name = "residencia_id", nullable = false)
	public Long getResidenciaId() {
		return residenciaId;
	}

	public void setResidenciaId(Long residenciaId) {
		this.residenciaId = residenciaId;
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
        final Date horaAtual = new Date();
        final long status = 1;
        dataEntrada = dataAtual;
        horaEntrada = horaAtual;
        posicao = status;
        
	}
	
}

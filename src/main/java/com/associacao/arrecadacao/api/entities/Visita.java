package com.associacao.arrecadacao.api.entities;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "visita")
public class Visita implements Serializable {

	private static final long serialVersionUID = -5754246207015712520L;
	
	private Long       id;
	private Visitante  visitante;
	private Residencia residencia;
	private Date       dataEntrada;
	private Date       horaEntrada;
	private Date       dataSaida;
	private Date       horaSaida;
	private String     placa;
	private Integer    posicao;
	
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
	@Column(name = "hora_entrada", nullable = false)
	public Date getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(Date horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_saida", nullable = false)
	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
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
	
	@Column(name = "posicao", nullable = false)
	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}
	
	@Column(name = "placa", nullable = true)
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	public Visitante getVisitante() {
		return visitante;
	}

	public void setVisitante(Visitante visitante) {
		this.visitante = visitante;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	public Residencia getResidencia() {
		return residencia;
	}

	public void setResidencia(Residencia residencia) {
		this.residencia = residencia;
	}
	
	@PrePersist
	public void prePersist() {
		
		final Date dataAtual = new Date();
        final Time time = new Time(dataAtual.getTime());
        final int status = 1;
        
        dataEntrada = dataAtual;
        horaEntrada = time;
        
        posicao = status;
        
	}
	
	@PreUpdate
	public void preUpdate() {
		
		final Date dataAtual = new Date();
        final Time time = new Time(dataAtual.getTime());
        final int status = 0;
        
        dataSaida = dataAtual;
        horaSaida = time;
        
        posicao = status;
        
	}
	
}

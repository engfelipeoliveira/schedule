package br.com.system.schedule.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

@Entity
public class Agenda implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull(message="Destinatário é obrigatório")
	@ManyToOne(targetEntity=Destinatario.class, cascade=CascadeType.ALL, fetch=FetchType.EAGER, optional=false)
	private Destinatario destinatario;
	
	@NotNull(message="Data/Hora é obrigatório")
	@Future(message="Data/Hora devem estar no futuro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEvento;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInclusao;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltimaAlteracao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Destinatario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Destinatario destinatario) {
		this.destinatario = destinatario;
	}

	public Date getDataEvento() {
		return dataEvento;
	}

	public void setDataEvento(Date dataEvento) {
		this.dataEvento = dataEvento;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	
}

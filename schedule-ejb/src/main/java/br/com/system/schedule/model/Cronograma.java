package br.com.system.schedule.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
public class Cronograma implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(targetEntity=Usuario.class, fetch=FetchType.EAGER)
	private Usuario usuario;
	
	@NotNull(message="Nome é obrigatório")
	@Length(max=100, message="Nome muito grande")
	private String nome;
	
	@NotNull(message="Texto é obrigatório")
	@Length(max=160, message="Texto muito grande")
	private String texto;

	@NotNull(message="Hora Antes do Evento é obrigatório")
	@Min(value=1, message="O valor deve ser maior que 1")
	private Long horaAntesEvento;
	
	@NotNull(message="Data de Inclusão é obrigatória")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInclusao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Long getHoraAntesEvento() {
		return horaAntesEvento;
	}

	public void setHoraAntesEvento(Long horaAntesEvento) {
		this.horaAntesEvento = horaAntesEvento;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

}

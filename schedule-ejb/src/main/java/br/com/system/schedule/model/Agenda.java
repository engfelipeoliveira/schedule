package br.com.system.schedule.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
public class Agenda implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
	@ManyToOne(targetEntity=Usuario.class)
	private Usuario usuario;
	
	@ManyToOne(targetEntity=ImportaAgenda.class)
	private ImportaAgenda importaAgenda;

	@NotNull(message="Nome é obigatório")
	@Length(max=100, message="Nome muito grande")
	@Column(length=100)
	private String nome;
	
	@NotNull(message="Celular é obrigatório")
	@Column(length=11)
	@Min(value=11, message="Celular deve conter 11 dígitos (ddd + celular)")
	private Long celular;

	@NotNull(message="Data/Hora é obrigatório")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEvento;
	
	@NotNull(message="Data de Inclusão é obrigatória")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInclusao;

	// M - Manual / A - Arquivo / W - Webservice
	private String tipoCadastro;
	
	// A - Agendado / E - Enviado / F - Falha / R - Respondido
	@Column(length=1)
	private String situacao;
	
	@Column(length=100)
	private String retornoSms;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getCelular() {
		return celular;
	}

	public void setCelular(Long celular) {
		this.celular = celular;
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

	public String getTipoCadastro() {
		return tipoCadastro;
	}

	public void setTipoCadastro(String tipoCadastro) {
		this.tipoCadastro = tipoCadastro;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getRetornoSms() {
		return retornoSms;
	}

	public void setRetornoSms(String retornoSms) {
		this.retornoSms = retornoSms;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public ImportaAgenda getImportaAgenda() {
		return importaAgenda;
	}

	public void setImportaAgenda(ImportaAgenda importaAgenda) {
		this.importaAgenda = importaAgenda;
	}	
}

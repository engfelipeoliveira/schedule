package br.com.system.schedule.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	private String nome;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
	private String senha;
	
	private Long telefone;

	@OneToMany(mappedBy="usuario", orphanRemoval=true, cascade=CascadeType.REMOVE)
	private List<Cronograma> cronograma;

	@OneToMany(mappedBy="usuario", orphanRemoval=true, cascade=CascadeType.REMOVE)
	private List<Agenda> agenda;
	
	// A - Ativo / I - Inativo
	@NotNull
	private String situacao;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInclusao;
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Long getTelefone() {
		return telefone;
	}

	public void setTelefone(Long telefone) {
		this.telefone = telefone;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public List<Cronograma> getCronograma() {
		return cronograma;
	}

	public void setCronograma(List<Cronograma> cronograma) {
		this.cronograma = cronograma;
	}

	public List<Agenda> getAgenda() {
		return agenda;
	}

	public void setAgenda(List<Agenda> agenda) {
		this.agenda = agenda;
	}

}

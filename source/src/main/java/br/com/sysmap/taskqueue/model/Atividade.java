package br.com.sysmap.taskqueue.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import br.com.sysmap.taskqueue.model.tipos.StatusAtividade;

@Entity
public class Atividade extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String descricao;

	@NotNull
	private Integer esforco;

	@ManyToOne(cascade = CascadeType.ALL)
	private Pessoa pessoa;

	@Enumerated(EnumType.ORDINAL)
	private StatusAtividade status = StatusAtividade.PENDENTE;

	private Integer tempo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inicio;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getEsforco() {
		return esforco;
	}

	public void setEsforco(Integer esforco) {
		this.esforco = esforco;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public StatusAtividade getStatus() {
		return status;
	}

	public void setStatus(StatusAtividade status) {
		this.status = status;
	}

	public Integer getTempo() {
		return tempo;
	}

	public void setTempo(Integer tempo) {
		this.tempo = tempo;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

}

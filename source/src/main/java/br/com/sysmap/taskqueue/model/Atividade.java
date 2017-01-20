package br.com.sysmap.taskqueue.model;

import java.util.Date;

import br.com.sysmap.taskqueue.model.tipos.StatusAtividade;

public class Atividade {
	
	private String descricao;
	
	private Integer esforco;
	
	private Pessoa pessoa;
	
	private StatusAtividade status = StatusAtividade.PENDENTE;
	
	private Integer tempo;
	
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
	
}

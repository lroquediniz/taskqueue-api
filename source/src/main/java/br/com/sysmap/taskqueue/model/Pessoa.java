/*
 * Sysmap Solutions.
 * Este é um software proprietário; não é permitida a distribuição total ou parcial deste código sem a autorização.
 * Se você recebeu uma cópia, informe-nos através dos contatos abaixo.
 * Site: www.sysmap.com.br
 * E-mail: contato@sysmap.com.br
 */
package br.com.sysmap.taskqueue.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

/**
 * Classe de mapeamento objeto relacional Pessoa.
 * 
 * @author Luan Roque
 */
@Entity
public class Pessoa extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String nome;

	@NotNull
	private Integer produtividade;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getProdutividade() {
		return produtividade;
	}

	public void setProdutividade(Integer produtividade) {
		this.produtividade = produtividade;
	}

}

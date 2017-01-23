package br.com.sysmap.taskqueue.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

/**
 * Classe de mapeamento objeto relacional Pessoa.
 * 
 * @author Luan Roque.
 */
@NamedQueries({ 
	@NamedQuery(name = Pessoa.ConstantePessoa.BUSCAR_TODAS_KEY, query = Pessoa.ConstantePessoa.BUSCAR_TODAS_QUERY),
	@NamedQuery(name = Pessoa.ConstantePessoa.BUSCAR_PESSOA_POR_ID_KEY, query = Pessoa.ConstantePessoa.BUSCAR_PESSOA_POR_ID_QUERY)
})
@Entity
public class Pessoa extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;
	/**
	 * Nome.
	 */
	@NotNull
	private String nome;

	/**
	 * Produtividade.
	 */
	@NotNull
	private Integer produtividade;

	/*
	 * Getters and Setters.
	 */
	
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

	/**
	 * Interface de constantes de consultas de {@link Pessoa}.
	 * @author Luan Roque.
	 *
	 */
	public interface ConstantePessoa {
		
		String ID_FIELD = "id";
		
		String BUSCAR_PESSOA_POR_ID_KEY = "Pessoa.buscarPessoaPorId";
		
		String BUSCAR_PESSOA_POR_ID_QUERY = "SELECT DISTINCT p FROM Pessoa p WHERE p.id = :id ORDER BY p.id";
		
		String BUSCAR_TODAS_KEY = "Pessoa.buscarTodos";
		
		String BUSCAR_TODAS_QUERY = "SELECT DISTINCT p FROM Pessoa p ORDER BY p.id";
		
	}
	
}

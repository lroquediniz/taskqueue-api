package br.com.sysmap.taskqueue.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.sysmap.taskqueue.model.tipos.StatusProcessamento;
/**
 * Classe de entidade de mapeamento objeto relacional.
 * @author Luan Roque
 *
 * @param <ID>
 */
@NamedQueries({ 
	@NamedQuery(name = Atividade.ConstanteAtividade.BUSCAR_ATIVIDADES_POR_STATUS_KEY, query = Atividade.ConstanteAtividade.BUSCAR_ATIVIDADES_POR_STATUS_QUERY),
	@NamedQuery(name = Atividade.ConstanteAtividade.BUSCAR_TODOS_KEY, query = Atividade.ConstanteAtividade.BUSCAR_TODOS_QUERY),
	@NamedQuery(name = Atividade.ConstanteAtividade.BUSCAR_ATIVIDADE_POR_ID_KEY, query = Atividade.ConstanteAtividade.BUSCAR_ATIVIDADE_POR_ID_QUERY)
})
@Entity
public class Atividade extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Descrição.
	 */
	@NotNull
	private String descricao;

	/**
	 * Esforço.
	 */
	@NotNull
	private Integer esforco;

	/**
	 * Pessoa.
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private Pessoa pessoa;

	/**
	 * Status.
	 */
	@Enumerated(EnumType.ORDINAL)
	private StatusProcessamento status;

	/**
	 * Tempo de Execução
	 * 
	 */
	private Integer tempoExecucao;

	/**
	 * Data de cadastro.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	
	/**
	 * Lote de processamento.
	 */
	@ManyToOne
	@JsonBackReference(value="atividades")
	private LoteProcessamento loteProcessamento;

	/*
	 * Getters and Setters.
	 */
	
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

	public StatusProcessamento getStatus() {
		return status;
	}

	public void setStatus(StatusProcessamento status) {
		this.status = status;
	}

	/**
	 * @return Tempo de execucao da tarefa em minutos
	 */
	public Integer getTempoExecucao() {
		return tempoExecucao;
	}

	/**
	 * @param tempoExecucao the tempoExecucao to set
	 */
	public void setTempoExecucao(Integer tempoExecucao) {
		this.tempoExecucao = tempoExecucao;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	/**
	 * Interface de constantes de consultas de {@link Atividade}.
	 * @author luan
	 *
	 */
	public interface ConstanteAtividade {
		
		String STATUS_FIELD = "status";
		
		String ID_FIELD = "id";
		
		String BUSCAR_ATIVIDADES_POR_STATUS_KEY = "Atividade.buscarAtividadePorStatus";
		
		String BUSCAR_ATIVIDADES_POR_STATUS_QUERY = "select a from Atividade a where a.status = :status";
		
		String BUSCAR_ATIVIDADE_POR_ID_KEY = "Atividade.buscarAtividadePorId";
		
		String BUSCAR_ATIVIDADE_POR_ID_QUERY = "SELECT DISTINCT a FROM Atividade a LEFT JOIN FETCH a.pessoa WHERE a.id = :id ORDER BY a.id";
		
		String BUSCAR_TODOS_KEY = "Atividade.buscarTodos";
		
		String BUSCAR_TODOS_QUERY = "SELECT DISTINCT a FROM Atividade a LEFT JOIN FETCH a.pessoa ORDER BY a.id";
		
		
	}

}

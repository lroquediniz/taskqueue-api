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

@NamedQueries({ 
	@NamedQuery(name = Atividade.ConstanteAtividade.BUSCAR_ATIVIDADES_POR_STATUS_KEY, query = Atividade.ConstanteAtividade.BUSCAR_ATIVIDADES_POR_STATUS_QUERY)
})
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
	private StatusProcessamento status;

	private Integer tempoExecucao;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	
	@ManyToOne
	@JsonBackReference(value="atividades")
	private LoteProcessamento loteProcessamento;

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
	
	public interface ConstanteAtividade {
		
		String STATUS_FIELD = "status";
		
		String BUSCAR_ATIVIDADES_POR_STATUS_KEY = "Atividade.buscarAtividadePorStatus";
		
		String BUSCAR_ATIVIDADES_POR_STATUS_QUERY = "select a from Atividade a where a.status = :status";
		
		
		
	}

}

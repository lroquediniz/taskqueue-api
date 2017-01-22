package br.com.sysmap.taskqueue.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.sysmap.taskqueue.model.tipos.StatusProcessamento;

/**
 * Classe de entidade de mapeamento objeto relacional.
 * 
 * @author Luan Roque.
 *
 * @param <ID>
 */
@NamedQueries({
		@NamedQuery(name = LoteProcessamento.ConstanteLoteProcessamento.BUSCAR_TODOS_KEY, query = LoteProcessamento.ConstanteLoteProcessamento.BUSCAR_TODOS_QUERY),
		@NamedQuery(name = LoteProcessamento.ConstanteLoteProcessamento.BUSCAR_LOTE_POR_STATUS_KEY, query = LoteProcessamento.ConstanteLoteProcessamento.BUSCAR_LOTE_POR_STATUS_QUERY),
		@NamedQuery(name = LoteProcessamento.ConstanteLoteProcessamento.VERIFICAR_LOTE_POR_STATUS_KEY, query = LoteProcessamento.ConstanteLoteProcessamento.VERIFICAR_LOTE_POR_STATUS_QUERY) })
@Entity
public class LoteProcessamento extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	/**
	 * Atividades
	 */
	@OneToMany(mappedBy = "loteProcessamento")
	@JsonManagedReference(value = "atividades")
	private List<Atividade> atividades;

	/**
	 * Data inicio.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;

	/**
	 * Data Conclusao
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataConclusao;

	/**
	 * Status do processamento.
	 */
	@Enumerated(EnumType.ORDINAL)
	private StatusProcessamento status;

	/*
	 * Getters and Setters.
	 */
	
	public List<Atividade> getAtividades() {
		return atividades;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	public StatusProcessamento getStatus() {
		return status;
	}

	public void setStatus(StatusProcessamento status) {
		this.status = status;
	}

	public void setAtividades(List<Atividade> atividades) {
		this.atividades = atividades;
	}

	public interface ConstanteLoteProcessamento {

		String STATUS_FIELD = "status";
		
		String BUSCAR_TODOS_KEY = "LoteProcessamento.buscarTodos";

		String BUSCAR_TODOS_QUERY = "SELECT DISTINCT l FROM LoteProcessamento l LEFT JOIN FETCH l.atividades ORDER BY l.id";

		String BUSCAR_LOTE_POR_STATUS_KEY = "LoteProcessamento.buscarLotesPorStatus";

		String BUSCAR_LOTE_POR_STATUS_QUERY = "select l from LoteProcessamento l where l.status = :status";

		String VERIFICAR_LOTE_POR_STATUS_KEY = "LoteProcessamento.verificarLotesPorStatus";

		String VERIFICAR_LOTE_POR_STATUS_QUERY = "select count(l) from LoteProcessamento l where l.status = :status";

	}

}

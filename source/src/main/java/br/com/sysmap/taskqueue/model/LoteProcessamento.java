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

@NamedQueries({ 
	@NamedQuery(name = LoteProcessamento.ConstanteLoteProcessamento.BUSCAR_LOTE_POR_STATUS_KEY, query = LoteProcessamento.ConstanteLoteProcessamento.BUSCAR_LOTE_POR_STATUS_QUERY), 
	@NamedQuery(name = LoteProcessamento.ConstanteLoteProcessamento.VERIFICAR_LOTE_POR_STATUS_KEY, query = LoteProcessamento.ConstanteLoteProcessamento.VERIFICAR_LOTE_POR_STATUS_QUERY)
})
@Entity
public class LoteProcessamento extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy="loteProcessamento")
	@JsonManagedReference(value="atividades")
	private List<Atividade> atividades;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataConclusao;
	
	@Enumerated(EnumType.ORDINAL)
	private StatusProcessamento status;

	/**
	 * @return the atividades
	 */
	public List<Atividade> getAtividades() {
		return atividades;
	}

	/**
	 * @param atividades the atividades to set
	 */
	public void setAtividades(List<Atividade> atividades) {
		this.atividades = atividades;
	}

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * @param dataInicio the dataInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataConclusao
	 */
	public Date getDataConclusao() {
		return dataConclusao;
	}

	/**
	 * @param dataConclusao the dataConclusao to set
	 */
	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	/**
	 * @return the status
	 */
	public StatusProcessamento getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusProcessamento status) {
		this.status = status;
	}
	
	public interface ConstanteLoteProcessamento {
		
		String STATUS_FIELD = "status";
		
		String BUSCAR_LOTE_POR_STATUS_KEY = "LoteProcessamento.buscarLotesPorStatus";
		
		String BUSCAR_LOTE_POR_STATUS_QUERY = "select l from LoteProcessamento l where l.status = :status";
		
		String VERIFICAR_LOTE_POR_STATUS_KEY = "LoteProcessamento.verificarLotesPorStatus";
		
		String VERIFICAR_LOTE_POR_STATUS_QUERY = "select count(l) from LoteProcessamento l where l.status = :status";
		
	}

}

package br.com.sysmap.taskqueue.business;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.sysmap.taskqueue.dto.AtualizacaoAtividade;
import br.com.sysmap.taskqueue.dto.Execucao;
import br.com.sysmap.taskqueue.exception.ProcessamentoException;
import br.com.sysmap.taskqueue.model.Atividade;
import br.com.sysmap.taskqueue.model.Atividade.ConstanteAtividade;
import br.com.sysmap.taskqueue.model.LoteProcessamento;
import br.com.sysmap.taskqueue.model.LoteProcessamento.ConstanteLoteProcessamento;
import br.com.sysmap.taskqueue.model.tipos.StatusProcessamento;
import br.com.sysmap.taskqueue.util.Constantes;

@Singleton
@Startup
public class LoteProcessamentoService {

	@PersistenceContext(unitName = Constantes.DataSource.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	private LoteProcessamento lote;

	private Execucao execucao;
	
	public void validarLoteExecucao() throws ProcessamentoException {
		TypedQuery<Long> q = em.createNamedQuery(ConstanteLoteProcessamento.VERIFICAR_LOTE_POR_STATUS_KEY, Long.class);
		q.setParameter(ConstanteLoteProcessamento.STATUS_FIELD, StatusProcessamento.EM_EXECUCAO);
		Long quantidade = q.getSingleResult();
		if (quantidade > 0) {
			throw new ProcessamentoException();
		}
	}

	public LoteProcessamento iniciarProcessamento() {
		this.lote = criarLoteProcessamento();
		this.lote.getAtividades().stream().forEach(a -> a.setStatus(StatusProcessamento.EM_EXECUCAO));
		em.persist(this.lote);
		return lote;
	}

	private LoteProcessamento criarLoteProcessamento() {
		LoteProcessamento lote = new LoteProcessamento();
		lote.setDataInicio(new Date());
		lote.setAtividades(this.recuperaListaAtividades());
		lote.setStatus(StatusProcessamento.EM_EXECUCAO);
		return lote;
	}

	private List<Atividade> recuperaListaAtividades() {
		TypedQuery<Atividade> q = em.createNamedQuery(ConstanteAtividade.BUSCAR_ATIVIDADES_POR_STATUS_KEY,
				Atividade.class);
		q.setParameter(ConstanteLoteProcessamento.STATUS_FIELD, StatusProcessamento.PENDENTE);
		return q.getResultList();
	}
	
	public void atualizarStatusAtividade (Atividade atividade) {
		em.persist(atividade);
	}
	
	@Schedule(second="*/1")
	public void atualizarPorcentagens() {
		if (this.lote != null) {
			
			LocalDateTime horaInicioProcessLote = LocalDateTime.ofInstant(this.lote.getDataInicio().toInstant(), ZoneId.systemDefault());
			List<Atividade> atividades = this.lote.getAtividades();
			Execucao execucao = new Execucao();
			execucao.setPorcentagem(0);
			execucao.setQtdTarefasAndamento(atividades.size());
			
			execucao.setQtdTarefasPendentes(0);
			
			execucao.setAtualizacaoAtividades(new ArrayList<>());
			AtualizacaoAtividade atualizacaoAtividade = null;
			
			BigDecimal minutosExecutados = null;
			BigDecimal minutosAtividade = null;
			Integer percentualExecucao = null;
			BigDecimal porcentagem = new BigDecimal(Constantes.Params.BASE_PORCENTAGEM);
			
			for (Atividade atividade : atividades) {
				minutosAtividade = new BigDecimal(atividade.getTempoExecucao().longValue());
				minutosExecutados = new BigDecimal(ChronoUnit.MINUTES.between(horaInicioProcessLote, LocalDateTime.now()));
				percentualExecucao = minutosExecutados.divide(minutosAtividade.multiply(porcentagem)).intValue();
				
				if (percentualExecucao.intValue() >= porcentagem.intValue()) {
					execucao.setQtdTarefasPendentes(execucao.getQtdTarefasPendentes() - 1);
					execucao.setQtdTarefasAndamento(execucao.getQtdTarefasAndamento() + 1);
					if (!atividade.getStatus().equals(StatusProcessamento.CONCLUIDO)) {
						atividade.setStatus(StatusProcessamento.CONCLUIDO);
						this.atualizarStatusAtividade(atividade);
					}
				}
				atualizacaoAtividade = new AtualizacaoAtividade();
				atualizacaoAtividade.setPercentualExecucao(percentualExecucao);
				atualizacaoAtividade.setIdAtividade(atividade.getId());
				execucao.getAtualizacaoAtividades().add(atualizacaoAtividade);
				if (atividade.getTempoExecucao() > execucao.getPorcentagem()) {
					execucao.setPorcentagem(percentualExecucao);
				}
			}
			this.execucao = execucao;
		}
	}

	/**
	 * @return the lote
	 */
	public LoteProcessamento getLote() {
		return lote;
	}

	/**
	 * @param lote
	 * the lote to set
	 */
	public void setLote(LoteProcessamento lote) {
		this.lote = lote;
	}

	public Execucao getExecucao() {
		return execucao;
	}

	public void setExecucao(Execucao execucao) {
		this.execucao = execucao;
	}
	
	

}

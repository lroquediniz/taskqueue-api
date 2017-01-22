package br.com.sysmap.taskqueue.business;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

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
import br.com.sysmap.taskqueue.model.tipos.OperacaoTempo;
import br.com.sysmap.taskqueue.model.tipos.StatusProcessamento;
import br.com.sysmap.taskqueue.util.Constantes;

@Singleton
@Startup
public class LoteProcessamentoService {

	@PersistenceContext(unitName = Constantes.DataSource.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	private LoteProcessamento lote;

	private Execucao execucao;
	
	private Atividade atividadeMaiorTempo;
	
	private Long alteracaoTempo = 0L;
	
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
		return lote;
	}

	private LoteProcessamento criarLoteProcessamento() {
		LoteProcessamento lote = new LoteProcessamento();
		lote.setDataInicio(new Date());
		List<Atividade> listaAtividades = this.recuperaListaAtividadePendentes();
		if (listaAtividades.isEmpty()) {
			throw new IllegalArgumentException();
		}
		Comparator<Atividade> comp = (a1, a2) -> Integer.compare( a1.getTempoExecucao(), a2.getTempoExecucao());
		this.atividadeMaiorTempo = listaAtividades.stream().max(comp).get();
		
		lote.setAtividades(listaAtividades);
		lote.setStatus(StatusProcessamento.EM_EXECUCAO);
		return lote;
	}

	public List<Atividade> recuperaListaAtividadePendentes() {
		TypedQuery<Atividade> q = em.createNamedQuery(ConstanteAtividade.BUSCAR_ATIVIDADES_POR_STATUS_KEY,
				Atividade.class);
		q.setParameter(ConstanteLoteProcessamento.STATUS_FIELD, StatusProcessamento.PENDENTE);
		return q.getResultList();
	}
	
	public void atualizarStatusAtividade (Atividade atividade) {
		em.persist(atividade);
	}
	
	@Schedule(hour="*", minute="*" , second="*/1")
	public void atualizarPorcentagens() {
		if (this.lote != null && this.lote.getStatus().equals(StatusProcessamento.EM_EXECUCAO)) {

			LocalDateTime horaInicioProcessLote = LocalDateTime.ofInstant(this.lote.getDataInicio().toInstant(), ZoneId.systemDefault());
			List<Atividade> atividades = this.lote.getAtividades();
			Execucao execucao = new Execucao();
			execucao.setPorcentagem(0);
			execucao.setQtdTarefasConcluidas(0);
			execucao.setQtdTarefasPendentes(this.lote.getAtividades().size());
			execucao.setAtualizacaoAtividades(new ArrayList<>());
			AtualizacaoAtividade atualizacaoAtividade = null;
			
			BigDecimal minutosExecutados = null;
			BigDecimal minutosAtividade = null;
			Integer percentualExecucao = null;
			BigDecimal porcentagem = new BigDecimal(Constantes.Params.BASE_PORCENTAGEM);
			
			for (Atividade atividade : atividades) {
				minutosAtividade = new BigDecimal(atividade.getTempoExecucao().longValue() * 60);
				
				LocalDateTime horaAtual = LocalDateTime.now();
				horaAtual.plusMinutes(this.alteracaoTempo);
				minutosExecutados = new BigDecimal(ChronoUnit.SECONDS.between(horaInicioProcessLote, horaAtual));
				
				percentualExecucao = Float.valueOf((minutosExecutados.floatValue() / minutosAtividade.floatValue()) * porcentagem.floatValue()).intValue();
				System.out.println(percentualExecucao);
				if (percentualExecucao.intValue() >= porcentagem.intValue()) {
					execucao.setQtdTarefasPendentes(execucao.getQtdTarefasPendentes() - 1);
					execucao.setQtdTarefasConcluidas(execucao.getQtdTarefasConcluidas() + 1);
					if (!atividade.getStatus().equals(StatusProcessamento.CONCLUIDO)) {
						atividade.setStatus(StatusProcessamento.CONCLUIDO);
						//this.atualizarStatusAtividade(atividade);
					}
				}
				atualizacaoAtividade = new AtualizacaoAtividade();
				atualizacaoAtividade.setPercentualExecucao(percentualExecucao < porcentagem.intValue() ? percentualExecucao : porcentagem.intValue());
				atualizacaoAtividade.setDescricao(new StringBuilder(atividade.getDescricao()).append(" ").append(atividade.getPessoa().getNome()).toString());
				execucao.getAtualizacaoAtividades().add(atualizacaoAtividade);
				if (atividade.getTempoExecucao().equals(this.atividadeMaiorTempo.getTempoExecucao())) {
					execucao.setPorcentagem(percentualExecucao);
				}
			}
			finalizarLote();
			this.execucao = execucao;
		}
	}
	
	public void atualizarTempo(OperacaoTempo operacaoTempo) {
		this.alteracaoTempo = operacaoTempo.calcular(this.alteracaoTempo);
	}

	private void finalizarLote() {
		Predicate<Atividade> predicate = a -> a.getStatus().equals(StatusProcessamento.CONCLUIDO);
		if (this.lote.getAtividades().stream().allMatch(predicate)) {
			this.lote.setStatus(StatusProcessamento.CONCLUIDO);
			
		} ;
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

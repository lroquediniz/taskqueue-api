package br.com.sysmap.taskqueue.business;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.sysmap.taskqueue.dto.AtualizacaoAtividade;
import br.com.sysmap.taskqueue.dto.Execucao;
import br.com.sysmap.taskqueue.exception.NenhumaAtividadeException;
import br.com.sysmap.taskqueue.exception.ProcessamentoException;
import br.com.sysmap.taskqueue.model.Atividade;
import br.com.sysmap.taskqueue.model.Atividade.ConstanteAtividade;
import br.com.sysmap.taskqueue.model.LoteProcessamento;
import br.com.sysmap.taskqueue.model.LoteProcessamento.ConstanteLoteProcessamento;
import br.com.sysmap.taskqueue.model.tipos.OperacaoTempo;
import br.com.sysmap.taskqueue.model.tipos.StatusProcessamento;
import br.com.sysmap.taskqueue.util.Constantes;

/**
 * Singleton EJB Startup para processamento de Requisicoes
 * @author Luan Roque
 */
@Singleton
@Startup
public class LoteProcessamentoService {

	/**
	 * Gerenciador de entidades JPA.
	 */
	@PersistenceContext(unitName = Constantes.DataSource.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	/**
	 * Lote de processamento.
	 */
	private LoteProcessamento lote;

	/**
	 * Execucao de processamento de tarefas.
	 */
	private Execucao execucao;

	/**
	 * Atividade de maior tempo.
	 */
	private Atividade atividadeMaiorTempo;

	/**
	 * Marcador de tempo.
	 */
	private Long alteracaoTempo = 0L;
	
	/**
	 * 
	 */
	private final static Logger LOGGER = Logger.getLogger(LoteProcessamentoService.class.getName());

	/**
	 * Valida existencia de lote em execução.
	 * @throws ProcessamentoException
	 */
	public void validarLoteExecucao() throws ProcessamentoException {
		if (this.lote != null && this.lote.getStatus().equals(StatusProcessamento.EM_EXECUCAO)) {
			throw new ProcessamentoException();
		}
	}
	
	/**
	 * Inicia processamento em lote de atividades.
	 * @return {@link LoteProcessamento}
	 * @throws NenhumaAtividadeException 
	 */
	public LoteProcessamento iniciarProcessamento() throws NenhumaAtividadeException {
		this.execucao = new Execucao();
		this.lote = criarLoteProcessamento();
		return lote;
	}
	
	/**
	 * Cria um novo lote de processamento.
	 * @return {@link LoteProcessamento}
	 * @throws {@link NenhumaAtividadeException} NenhumaAtividadeException 
	 */
	private LoteProcessamento criarLoteProcessamento() throws NenhumaAtividadeException {
		LoteProcessamento lote = new LoteProcessamento();
		this.alteracaoTempo = 0L;
		lote.setDataInicio(new Date());
		List<Atividade> listaAtividades = this.recuperaListaAtividadePendentes();
		if (listaAtividades.isEmpty()) {
			throw new NenhumaAtividadeException();
		}
		LOGGER.log(Level.INFO, "Inicio de processamento de Lote de Atividades");
		Comparator<Atividade> comp = (a1, a2) -> Integer.compare(a1.getTempoExecucao(), a2.getTempoExecucao());
		this.atividadeMaiorTempo = listaAtividades.stream().max(comp).get();

		lote.setAtividades(listaAtividades);
		lote.setStatus(StatusProcessamento.EM_EXECUCAO);
		return lote;
	}

	/**
	 * Recupera a lista de atividades com status pendente.
	 * @return List<{@link Atividade}>
	 */
	public List<Atividade> recuperaListaAtividadePendentes() {
		TypedQuery<Atividade> q = em.createNamedQuery(ConstanteAtividade.BUSCAR_ATIVIDADES_POR_STATUS_KEY,
				Atividade.class);
		q.setParameter(ConstanteLoteProcessamento.STATUS_FIELD, StatusProcessamento.PENDENTE);
		return q.getResultList();
	}
	
	/**
	 * Recupera a lista de atividades com status concluido.
	 * @return List<{@link Atividade}>
	 */
	public List<Atividade> recuperaListaAtividadeProcessadas() {
		TypedQuery<Atividade> q = em.createNamedQuery(ConstanteAtividade.BUSCAR_ATIVIDADES_POR_STATUS_KEY,
				Atividade.class);
		q.setParameter(ConstanteLoteProcessamento.STATUS_FIELD, StatusProcessamento.CONCLUIDO);
		return q.getResultList();
	}
	
	/**
	 * Atualiza status de uma atividade.
	 * @param atividade {@Link Atividade} - Atividade a ser atualizada.
	 */
	public void atualizarStatusAtividade(Atividade atividade) {
		em.merge(atividade);
	}
	

	/**
	 *	Metodo de schedule de tempo de execução de 1 segundo para processamento de atividades em lote. 	
	 */
	@Schedule(hour = "*", minute = "*", second = "*/1")
	public void atualizarPorcentagens() {
		if (this.lote != null && this.lote.getStatus().equals(StatusProcessamento.EM_EXECUCAO)) {

			LocalDateTime horaInicioProcessLote = LocalDateTime.ofInstant(this.lote.getDataInicio().toInstant(),
					ZoneId.systemDefault());
			List<Atividade> atividades = this.lote.getAtividades();
			this.execucao.setPorcentagem(0);
			this.execucao.setQtdTarefasConcluidas(0);
			this.execucao.setQtdTarefasPendentes(this.lote.getAtividades().size());
			this.execucao.setAtualizacaoAtividades(new ArrayList<>());
			AtualizacaoAtividade atualizacaoAtividade = null;

			BigDecimal minutosExecutados = null;
			BigDecimal minutosAtividade = null;
			Integer percentualExecucao = null;
			BigDecimal porcentagem = new BigDecimal(Constantes.Params.BASE_PORCENTAGEM);

			for (Atividade atividade : atividades) {
				minutosAtividade = new BigDecimal(atividade.getTempoExecucao().longValue() * 60);
				LocalDateTime horaAtual = LocalDateTime.now();
				horaAtual = horaAtual.minusMinutes(this.alteracaoTempo);
				minutosExecutados = new BigDecimal(ChronoUnit.SECONDS.between(horaInicioProcessLote, horaAtual));
				percentualExecucao = Float.valueOf(
						(minutosExecutados.floatValue() / minutosAtividade.floatValue()) * porcentagem.floatValue())
						.intValue();
				if (percentualExecucao.intValue() >= porcentagem.intValue()) {
					this.execucao.setQtdTarefasPendentes(this.execucao.getQtdTarefasPendentes() - 1);
					this.execucao.setQtdTarefasConcluidas(this.execucao.getQtdTarefasConcluidas() + 1);
					if (!atividade.getStatus().equals(StatusProcessamento.CONCLUIDO)) {
						atividade.setStatus(StatusProcessamento.CONCLUIDO);
						this.atualizarStatusAtividade(atividade);
					}
				}
				atualizacaoAtividade = new AtualizacaoAtividade();
				if (percentualExecucao < porcentagem.intValue()) {
					atualizacaoAtividade.setPercentualExecucao(percentualExecucao);
				} else {
					atualizacaoAtividade.setPercentualExecucao(porcentagem.intValue());
				}
				atualizacaoAtividade.setDescricao(new StringBuilder(atividade.getDescricao()).append(" ")
						.append(atividade.getPessoa().getNome()).toString());
				this.execucao.getAtualizacaoAtividades().add(atualizacaoAtividade);
				if (atividade.getTempoExecucao().equals(this.atividadeMaiorTempo.getTempoExecucao())) {
					if (percentualExecucao < porcentagem.intValue()) {
						this.execucao.setPorcentagem(percentualExecucao);
					} else {
						this.execucao.setPorcentagem(porcentagem.intValue());
						this.finalizarLoteExecucao();
					}
				}
			}
		}
	}

	/**
	 * Faz alteração no tempo de processamento das atividades.
	 * @param operacaoTempo
	 */
	public void atualizarTempo(OperacaoTempo operacaoTempo) {
		this.alteracaoTempo = operacaoTempo.calcular(this.alteracaoTempo);
		LOGGER.log(Level.INFO, new StringBuilder("Alteracao de tempo: ").append(operacaoTempo.toString())
				.append(" Tempo: ").append(this.alteracaoTempo).append(" minutos.").toString());
	}

	/**
	 * Finaliza uma execução de um lote de processamento.
	 */
	private void finalizarLoteExecucao() {
		this.lote.setStatus(StatusProcessamento.CONCLUIDO);
		atualizarLoteExecucao();
	}
	/**
	 * Atualiza um lote de executado.
	 */
	public void atualizarLoteExecucao() {
		this.em.merge(this.lote);
		LOGGER.log(Level.INFO, "Lote processado com sucesso.");
	}
	/**
	 * Retorna a excucao atual de processamento.
	 * @return {@Link Execucao} - 
	 */
	public Execucao getExecucao() {
		return this.execucao;
	}

}

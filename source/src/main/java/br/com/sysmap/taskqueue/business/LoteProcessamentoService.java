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
	
	private final static Logger LOGGER = Logger.getLogger(LoteProcessamentoService.class.getName());
	
	public void validarLoteExecucao() throws ProcessamentoException {
		if (this.lote != null && this.lote.getStatus().equals(StatusProcessamento.EM_EXECUCAO)) {
			throw new ProcessamentoException();
		}
	}

	public LoteProcessamento iniciarProcessamento() {
		LOGGER.log(Level.INFO, "Inicio de processamento de Lote de Atividades");
		this.execucao = new Execucao();
		this.lote = criarLoteProcessamento();
		return lote;
	}

	private LoteProcessamento criarLoteProcessamento() {
		LoteProcessamento lote = new LoteProcessamento();
		this.alteracaoTempo = 0L;
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
		em.merge(atividade);
	}
	
	@Schedule(hour="*", minute="*" , second="*/1")
	public void atualizarPorcentagens() {
		if (this.lote != null && this.lote.getStatus().equals(StatusProcessamento.EM_EXECUCAO)) {
			
			LocalDateTime horaInicioProcessLote = LocalDateTime.ofInstant(this.lote.getDataInicio().toInstant(), ZoneId.systemDefault());
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
				percentualExecucao = Float.valueOf((minutosExecutados.floatValue() / minutosAtividade.floatValue()) * porcentagem.floatValue()).intValue();
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
				atualizacaoAtividade.setDescricao(new StringBuilder(atividade.getDescricao()).append(" ").append(atividade.getPessoa().getNome()).toString());
				this.execucao.getAtualizacaoAtividades().add(atualizacaoAtividade);
				if (atividade.getTempoExecucao().equals(this.atividadeMaiorTempo.getTempoExecucao())) {
					if (percentualExecucao < porcentagem.intValue()) {
						this.execucao.setPorcentagem(percentualExecucao);
						LOGGER.log(Level.INFO, new StringBuilder("Processando: ").append(percentualExecucao).append("%").toString());
					} else {
						this.execucao.setPorcentagem(porcentagem.intValue());
						this.finalizarLoteExecucao();
					}					
				}
			}
		}
	}
	
	public void atualizarTempo(OperacaoTempo operacaoTempo) {
		this.alteracaoTempo = operacaoTempo.calcular(this.alteracaoTempo);
		LOGGER.log(Level.INFO, new StringBuilder("Alteracao de tempo: " ).append(operacaoTempo.toString()).append("tempo: ").append(this.alteracaoTempo).append("minutos").toString());
	}

	private void finalizarLoteExecucao() {
		this.lote.setStatus(StatusProcessamento.CONCLUIDO);
		atualizarLoteExecucao();
	}
	
	public void atualizarLoteExecucao(){
		this.em.merge(this.lote);
		LOGGER.log(Level.INFO, "Lote processado com sucesso.");
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

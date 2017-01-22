package br.com.sysmap.taskqueue.dto;

import java.util.List;
/**
 * Classe de padão DTO (Data Tranfer object) para transferencia de informação para tela de processamento.
 * @author Luan Roque.
 *
 */
public class Execucao {
	
	/**
	 * Quantidade de tarefas Pendentes.
	 */
	private Integer qtdTarefasPendentes;

	/**
	 * Quantidade de tarefas concluidas.
	 */
	private Integer qtdTarefasConcluidas;

	/**
	 * Porcentagem de execução do lote de processamento.
	 */
	private Integer porcentagem;

	/**
	 * Lista de atividades do lote.
	 */
	private List<AtualizacaoAtividade> atualizacaoAtividades;

	/*
	 * Getters and Setters.
	 */
	
	public Integer getQtdTarefasPendentes() {
		return qtdTarefasPendentes;
	}

	public void setQtdTarefasPendentes(Integer qtdTarefasPendentes) {
		this.qtdTarefasPendentes = qtdTarefasPendentes;
	}

	public Integer getQtdTarefasConcluidas() {
		return qtdTarefasConcluidas;
	}

	public void setQtdTarefasConcluidas(Integer qtdTarefasConcluidas) {
		this.qtdTarefasConcluidas = qtdTarefasConcluidas;
	}

	public Integer getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Integer porcentagem) {
		this.porcentagem = porcentagem;
	}

	public List<AtualizacaoAtividade> getAtualizacaoAtividades() {
		return atualizacaoAtividades;
	}

	public void setAtualizacaoAtividades(List<AtualizacaoAtividade> atualizacaoAtividades) {
		this.atualizacaoAtividades = atualizacaoAtividades;
	}

}

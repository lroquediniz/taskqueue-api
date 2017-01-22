package br.com.sysmap.taskqueue.dto;

/**
 * Classe de padão DTO (Data Tranfer object) para transferencia de informação para tela de processamento.
 * @author Luan Roque.
 *
 */
public class AtualizacaoAtividade {

	/**
	 * Descricão da atividade.
	 */
	private String descricao;

	/**
	 * Percentual de execução.
	 */
	private Integer percentualExecucao;
	
	/*
	 * Getters and Setters.
	 */

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getPercentualExecucao() {
		return percentualExecucao;
	}

	public void setPercentualExecucao(Integer percentualExecucao) {
		this.percentualExecucao = percentualExecucao;
	}

}

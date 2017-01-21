package br.com.sysmap.taskqueue.dto;

import java.util.List;

public class Execucao {

	private Integer qtdTarefasPendentes;

	private Integer qtdTarefasAndamento;

	private Integer porcentagem;

	private List<AtualizacaoAtividade> atualizacaoAtividades;

	public Integer getQtdTarefasPendentes() {
		return qtdTarefasPendentes;
	}

	public void setQtdTarefasPendentes(Integer qtdTarefasPendentes) {
		this.qtdTarefasPendentes = qtdTarefasPendentes;
	}

	public Integer getQtdTarefasAndamento() {
		return qtdTarefasAndamento;
	}

	public void setQtdTarefasAndamento(Integer qtdTarefasAndamento) {
		this.qtdTarefasAndamento = qtdTarefasAndamento;
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

package br.com.sysmap.taskqueue.dto;

public class AtualizacaoAtividade {

	private String descricao;

	private Integer percentualExecucao;
	

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

package br.com.sysmap.taskqueue.dto;

public class AtualizacaoAtividade {

	private Long idAtividade;

	private Integer percentualExecucao;

	public Long getIdAtividade() {
		return idAtividade;
	}

	public void setIdAtividade(Long idAtividade) {
		this.idAtividade = idAtividade;
	}

	public Integer getPercentualExecucao() {
		return percentualExecucao;
	}

	public void setPercentualExecucao(Integer percentualExecucao) {
		this.percentualExecucao = percentualExecucao;
	}

}

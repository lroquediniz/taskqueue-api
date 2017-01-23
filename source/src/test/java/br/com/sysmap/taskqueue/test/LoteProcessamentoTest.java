package br.com.sysmap.taskqueue.test;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.sysmap.taskqueue.business.LoteProcessamentoService;
import br.com.sysmap.taskqueue.dto.Execucao;
import br.com.sysmap.taskqueue.model.Atividade;
import br.com.sysmap.taskqueue.model.LoteProcessamento;
import br.com.sysmap.taskqueue.model.Pessoa;
import br.com.sysmap.taskqueue.model.tipos.StatusProcessamento;

public class LoteProcessamentoTest {

	private LoteProcessamentoService service;

	@Before
	public void init() {
		service = new LoteProcessamentoService();
	}
	
	@After
	public void finish(){
		service = null;
	}
	
	@Test
	public void testeCalculoTempoExecucao() {

		LoteProcessamento loteProcessamento = new LoteProcessamento();
		loteProcessamento.setDataInicio(new Date());
		loteProcessamento.setAtividades(new ArrayList<>());
		
		Atividade atividade = new Atividade();
		atividade.setPessoa(new Pessoa());
		atividade.getPessoa().setProdutividade(10);
		atividade.setEsforco(1);
		atividade.setTempoExecucao(54);
		atividade.setStatus(StatusProcessamento.PENDENTE);
		atividade.setDescricao("Atividade de Teste");
		atividade.getPessoa().setNome("Pessoa Teste");
		
		loteProcessamento.getAtividades().add(atividade);
		Execucao execucao = new Execucao();
		Long operacaoTempo = -54L; 
		
		service.processaLote(loteProcessamento, execucao, atividade, operacaoTempo);
		
		Assert.assertEquals(execucao.getAtualizacaoAtividades().get(0).getPercentualExecucao(), Integer.valueOf(100));
	}
	
}

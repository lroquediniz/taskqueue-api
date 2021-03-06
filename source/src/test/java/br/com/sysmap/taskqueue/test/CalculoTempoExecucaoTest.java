package br.com.sysmap.taskqueue.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.sysmap.taskqueue.model.Atividade;
import br.com.sysmap.taskqueue.model.Pessoa;
import br.com.sysmap.taskqueue.rest.AtividadeEndpoint;
/**
 * Teste Junit em Enpoint Atividade.
 * @author Luan Roque.
 *
 */
public class CalculoTempoExecucaoTest {

	private AtividadeEndpoint endpoint;

	@Before
	public void init() {
		endpoint = new AtividadeEndpoint();
	}
	
	@After
	public void finish(){
		endpoint = null;
	}

	@Test
	public void testeCalculoTempoExecucao() {

		Atividade atividade = new Atividade();

		atividade.setPessoa(new Pessoa());
		
		atividade.setEsforco(1);
		atividade.getPessoa().setProdutividade(10);
		this.endpoint.calcularTempoExecucao(atividade);
		Assert.assertEquals(atividade.getTempoExecucao(), Integer.valueOf(54));
		
		atividade.getPessoa().setProdutividade(29);
		atividade.setEsforco(50);
		this.endpoint.calcularTempoExecucao(atividade);
		Assert.assertEquals(atividade.getTempoExecucao(), Integer.valueOf(2130));
		
		atividade.getPessoa().setProdutividade(25);
		atividade.setEsforco(60);
		this.endpoint.calcularTempoExecucao(atividade);
		Assert.assertEquals(atividade.getTempoExecucao(), Integer.valueOf(2700));
		
		atividade.getPessoa().setProdutividade(25);
		atividade.setEsforco(30);
		this.endpoint.calcularTempoExecucao(atividade);
		Assert.assertEquals(atividade.getTempoExecucao(), Integer.valueOf(1350));

	}
}

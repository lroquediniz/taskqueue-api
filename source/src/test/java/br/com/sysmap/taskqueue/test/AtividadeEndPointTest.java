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
public class AtividadeEndPointTest {

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
		atividade.getPessoa().setProdutividade(10);
		atividade.setEsforco(1);
		
		
		this.endpoint.calcularTempoExecucao(atividade);
		Assert.assertEquals(atividade.getTempoExecucao(), Integer.valueOf(54));
		
		atividade.getPessoa().setProdutividade(20);
		this.endpoint.calcularTempoExecucao(atividade);
		Assert.assertEquals(atividade.getTempoExecucao(), Integer.valueOf(48));
		
		atividade.getPessoa().setProdutividade(30);
		this.endpoint.calcularTempoExecucao(atividade);
		Assert.assertEquals(atividade.getTempoExecucao(), Integer.valueOf(42));
		
		atividade.setEsforco(100);
		atividade.getPessoa().setProdutividade(30);
		this.endpoint.calcularTempoExecucao(atividade);
		Assert.assertEquals(atividade.getTempoExecucao(), Integer.valueOf(4200));
		
	}
}

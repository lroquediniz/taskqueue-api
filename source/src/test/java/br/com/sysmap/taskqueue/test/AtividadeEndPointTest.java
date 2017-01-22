package br.com.sysmap.taskqueue.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.sysmap.taskqueue.model.Atividade;
import br.com.sysmap.taskqueue.model.Pessoa;
import br.com.sysmap.taskqueue.rest.AtividadeEndpoint;

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
		
//		insert into Pessoa (nome, produtividade) values ('Chuck Norris', 30);
//		insert into Pessoa (nome, produtividade) values ('Steven Seagal', 29);
//		insert into Pessoa (nome, produtividade) values ('Jean Claude Van Damme', 25);
//		insert into Pessoa (nome, produtividade) values ('Jack Chan', 25);
//		insert into Pessoa (nome, produtividade) values ('Anderson Silva', 5);
//		insert into Pessoa (nome, produtividade) values ('Ronda Rousey', 1);
//
//		insert into Atividade (descricao, esforco, pessoa_id, status) values ( 'Tarefa 1', 100, 1, 0);
//		insert into Atividade (descricao, esforco, pessoa_id, status) values ( 'Tarefa 2', 90, 2, 0);
//		insert into Atividade (descricao, esforco, pessoa_id, status) values ( 'Tarefa 3', 80, 3, 0);
//		insert into Atividade (descricao, esforco, pessoa_id, status) values ( 'Tarefa 4', 70, 4, 0);
//		insert into Atividade (descricao, esforco, pessoa_id, status) values ( 'Tarefa 5', 30, 5, 0);
//		insert into Atividade (descricao, esforco, pessoa_id, status) values ( 'Tarefa 6', 10, 6, 0);
		
		Atividade atividade = new Atividade();
		atividade.setPessoa(new Pessoa());
		atividade.getPessoa().setProdutividade(10);
		atividade.setEsforco(1);
		
		atividade.setEsforco(5);
		atividade.getPessoa().setProdutividade(95);
		this.endpoint.calcularTempoExecucao(atividade);
		System.out.println(atividade.getTempoExecucao());
		
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

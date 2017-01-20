package br.com.sysmap.taskqueue.model;

public class Main {

	public static void main(String[] args) {
		Pessoa p = new Pessoa();
		Pessoa p2 = new Pessoa();
		
		p.setNome("Luan Roque");
		p.setProdutividade(10);
		
		p2.setNome("Edson");
		p2.setProdutividade(20);
		
		
		Atividade atividade = new Atividade();
		
		atividade.setDescricao("Tarefa 1");
		atividade.setEsforco(1);
		atividade.setPessoa(p);
	}

}

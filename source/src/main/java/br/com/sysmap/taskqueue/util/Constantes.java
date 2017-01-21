/*
 * GEARCODE
 * Copyright (c) 2016 GEARCODE.
 * Este é um software proprietário; não é permitida a distribuição total ou parcial deste código sem a autorização.
 * Se você recebeu uma cópia, informe-nos através dos contatos abaixo.
 * Site: www.gearcode.com.br
 * E-mail: contato@gearcode.com.br
 */
package br.com.sysmap.taskqueue.util;

/**
 * Interface de costantes do sistema.
 * Constantes como interface garantindo unicidade dos dados.
 * @author luan
 *
 */
public interface Constantes {

	/**
	 * Constantes de configuração de acesso a banco de dados
	 * @author luan
	 *
	 */
	public interface DataSource {
		String PERSISTENCE_UNIT_NAME = "taskqueue-api-persistence-unit";
	}
	
	/**
	 * Parametros internos da aplicação.
	 */
	public interface Params {
		Integer BASE_MINUTOS = 60;
		Integer BASE_PORCENTAGEM = 100;
		
		
	}

}

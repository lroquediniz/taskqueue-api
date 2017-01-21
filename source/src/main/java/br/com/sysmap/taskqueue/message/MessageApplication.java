/*
 * GEARCODE
 * Copyright (c) 2016 GEARCODE.
 * Este é um software proprietário; não é permitida a distribuição total ou parcial deste código sem a autorização.
 * Se você recebeu uma cópia, informe-nos através dos contatos abaixo.
 * Site: www.gearcode.com.br
 * E-mail: contato@gearcode.com.br
 */
package br.com.sysmap.taskqueue.message;

public class MessageApplication {

	private String message;

	public MessageApplication(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

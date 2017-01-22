package br.com.sysmap.taskqueue.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe de padão DTO (Data Tranfer object) para transferencia de informação
 * para tela de processamento.
 * 
 * @author Luan Roque.
 *
 */
@XmlRootElement
public class MessageApplication {
	/**
	 * Messagem.
	 */
	private String message;

	
	/*
	 * Construtores
	 */
	
	public MessageApplication(String message) {
		super();
		this.message = message;
	}

	/*
	 * Getters and Setters
	 */
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

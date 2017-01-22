package br.com.sysmap.taskqueue.model.tipos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

/**
 * Enum de status de processamento.
 * @author Luan Roque.
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusProcessamento {
	
	PENDENTE("label.pendente"),
	EM_EXECUCAO("label.em.execucao"),
	CONCLUIDO("label.concluido");
	
	private String descricao;
	

	private StatusProcessamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getType() {
		return this.toString();
	}
	/**
	 * Método que transforma um objeto JSON em Enumeration java.
	 * @param node
	 * @return
	 */
	@JsonCreator
	public static StatusProcessamento fromObject(JsonNode node) {
		String type = null;
		if (node.getNodeType().equals(JsonNodeType.STRING)) {
			type = node.asText();
		} else {
			if (!node.has("type")) {
				throw new IllegalArgumentException();
			}
			type = node.get("type").asText();
		}
		return valueOf(type);
	}
	
}

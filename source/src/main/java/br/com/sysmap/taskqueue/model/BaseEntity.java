/*
 * Sysmap Solutions.
 * Este é um software proprietário; não é permitida a distribuição total ou parcial deste código sem a autorização.
 * Se você recebeu uma cópia, informe-nos através dos contatos abaixo.
 * Site: www.sysmap.com.br
 * E-mail: contato@sysmap.com.br
 */
package br.com.sysmap.taskqueue.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Classe de entidade base de mapeamento objeto relacional.
 * @author Luan Roque
 *
 * @param <ID>
 */
@MappedSuperclass
public abstract class BaseEntity<ID> implements Serializable {

	private static final long serialVersionUID = 1l;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private ID id;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Boolean equal = Boolean.TRUE;
		if (this == obj) {
			equal = Boolean.TRUE;
		} else {
			if (obj == null) {
				equal = Boolean.FALSE;
			} else if (getClass() != obj.getClass()) {
				equal = Boolean.FALSE;
			}
			BaseEntity<?> other = (BaseEntity<?>) obj;
			if (getId() == null) {
				if (other.getId() != null) {
					equal = Boolean.FALSE;
				}
			} else if (!getId().equals(other.getId())) {
				equal = Boolean.FALSE;
			}
		}
		return equal;
	}

	@Override
	public String toString() {
		String simpleName = getClass().getSimpleName();
		return new StringBuilder(simpleName).append("[").append(getId()).append("]").toString();
	}

	public interface BaseEntityConstant {
		String FIELD_ID = "id";
	}

}

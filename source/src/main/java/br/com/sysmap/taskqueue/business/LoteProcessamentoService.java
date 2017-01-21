package br.com.sysmap.taskqueue.business;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.sysmap.taskqueue.exception.ProcessamentoException;
import br.com.sysmap.taskqueue.model.LoteProcessamento.ConstanteLoteProcessamento;
import br.com.sysmap.taskqueue.model.tipos.StatusProcessamento;
import br.com.sysmap.taskqueue.util.Constantes;

@Stateless
public class LoteProcessamentoService {
	
	@PersistenceContext(unitName = Constantes.DataSource.PERSISTENCE_UNIT_NAME)
	private EntityManager em;
	
	public void validarLoteExecucao() throws ProcessamentoException {
		TypedQuery<Long> q = em.createNamedQuery(ConstanteLoteProcessamento.VERIFICAR_LOTE_POR_STATUS_KEY, Long.class);
		q.setParameter(ConstanteLoteProcessamento.STATUS_FIELD, StatusProcessamento.EM_EXECUCAO);
		Long quantidade = q.getSingleResult();
		if(quantidade > 0){
			throw new ProcessamentoException();
		}
	}
	
}

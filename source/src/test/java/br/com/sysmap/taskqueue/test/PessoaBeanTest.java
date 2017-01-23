package br.com.sysmap.taskqueue.test;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

import br.com.sysmap.taskqueue.business.LoteProcessamentoService;
import br.com.sysmap.taskqueue.model.Pessoa;

/**
 * @author luan
 */
//@RunWith(Arquillian.class)
public class PessoaBeanTest {

	@Inject
	private LoteProcessamentoService pessoaBean;

	@Deployment
	public static Archive<?> createDeployment() {
		return new DeploymentBuilder().build();
	}

	/**
	 * Testa o método {@link IPessoaService#save(Pessoa)} salvando um registro
	 * de pessoa com todos os campos <code>NOT NULL</code> preenchidos.
	 * 
	 * @throws Exception
	 */
	//@Test
	public void injecaoServicoEJB() throws Exception {
		assertNotNull(pessoaBean);
	}

}

package br.com.sysmap.taskqueue.test;

import static org.junit.Assert.assertNotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.sysmap.taskqueue.model.Pessoa;

/**
 * @author luan
 */
//@RunWith(Arquillian.class)
public class PessoaBeanTest {

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
		Pessoa p = new Pessoa();
		assertNotNull(p);
	}

}

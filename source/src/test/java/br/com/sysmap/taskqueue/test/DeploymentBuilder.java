package br.com.sysmap.taskqueue.test;

import javax.persistence.EntityManager;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import br.com.sysmap.taskqueue.model.BaseEntity;
import br.com.sysmap.taskqueue.model.tipos.OperacaoTempo;

public class DeploymentBuilder {

	private static final String WEB_ACRCHIVE_NAME = "test.war";
	private static final String PERSISTENCE_NAME = "test-persistence.xml";
	private static final String PERSISTENCE_FOLDER_NAME = "META-INF/persistence.xml";
	private static final String DATA_SOURCE_NAME = "wildfly-ds.xml";
	private static final String BEANS_NAME = "beans.xml";

	public final Archive<?> build() {
		WebArchive webArchive = ShrinkWrap.create(WebArchive.class, WEB_ACRCHIVE_NAME).addClass(EntityManager.class)
				.addPackage(BaseEntity.class.getPackage()).addPackage(OperacaoTempo.class.getPackage()).addAsResource(PERSISTENCE_NAME, PERSISTENCE_FOLDER_NAME)
				.addAsWebInfResource(DATA_SOURCE_NAME).addAsWebInfResource(EmptyAsset.INSTANCE, BEANS_NAME);
		addSpecificClasses(webArchive);
		return webArchive;
	}

	protected void addSpecificClasses(WebArchive webArchive) {

	}
}
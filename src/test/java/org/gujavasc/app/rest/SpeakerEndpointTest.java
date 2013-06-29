package org.gujavasc.app.rest;

import org.gujavasc.app.model.Speaker;
import org.gujavasc.app.rest.SpeakerEndpoint;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

@RunWith(Arquillian.class)
public class SpeakerEndpointTest {
	@Inject
	private SpeakerEndpoint speakerendpoint;

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addClass(SpeakerEndpoint.class)
				.addPackages(true, Speaker.class.getPackage())
				.addAsManifestResource("META-INF/persistence.xml",
						"persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void testIsDeployed() {
		Assert.assertNotNull(speakerendpoint);
	}

	@Test
	public void testCreation() {
		Speaker sp = new Speaker();
		sp.setName("Teste");
		sp.setCompany("Company");
		Assert.assertNull(sp.getId());
		speakerendpoint.create(sp);
		Assert.assertNotNull(sp.getId());
	}

}

package com.vmware.loginsightapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestConfiguration {

	@Test
	public void testConfigurationConstructor() {
		Configuration config = new Configuration();
		assertNull(config.getHost());
		assertNull(config.getUser());
		assertNull(config.getPassword());
		assertEquals("Mismatch in port", Integer.toString(Configuration.DEFAULT_PORT), config.getPort());
		assertEquals("Mismatch in ingestion port", Integer.toString(Configuration.DEFAULT_INGESTION_PORT),
				config.getIngestionPort());
		assertEquals("Mismatch in scheme", Configuration.DEFAULT_SCHEME, config.getScheme());
	}
	
	@Test
	public void testConfigurationWithConnectionInfo() {
		Configuration config = new Configuration("hostname", "user", "password");
		assertEquals("Mismatch in hostname", "hostname", config.getHost());
		assertEquals("Mismatch in user", "user", config.getUser());
		assertEquals("Mismatch in password", "password", config.getPassword());
		assertEquals("Mismatch in port", Integer.toString(Configuration.DEFAULT_PORT), config.getPort());
		assertEquals("Mismatch in ingestion port", Integer.toString(Configuration.DEFAULT_INGESTION_PORT),
				config.getIngestionPort());
		assertEquals("Mismatch in scheme", Configuration.DEFAULT_SCHEME, config.getScheme());
	}

	@Test
	public void testBuildConfig() {
		Map<String, String> configData = new HashMap<String, String>();
		configData.put(Configuration.KEY_LI_HOST, "hostname");
		configData.put(Configuration.KEY_LI_USER, "username");
		configData.put(Configuration.KEY_LI_PASSWORD, "password");
		configData.put(Configuration.KEY_LI_PORT, Integer.toString(Configuration.DEFAULT_PORT));
		configData.put(Configuration.KEY_LI_INGESTION_PORT, Integer.toString(Configuration.DEFAULT_INGESTION_PORT));
		configData.put(Configuration.KEY_CONNECTION_SCHEME, Configuration.DEFAULT_SCHEME);
		Configuration config = Configuration.buildConfig(configData);
		assertEquals("Mismatch in hostname", "hostname", config.getHost());
		assertEquals("Mismatch in user", "username", config.getUser());
		assertEquals("Mismatch in password", "password", config.getPassword());
		assertEquals("Mismatch in port", Integer.toString(Configuration.DEFAULT_PORT), config.getPort());
		assertEquals("Mismatch in ingestion port", Integer.toString(Configuration.DEFAULT_INGESTION_PORT),
				config.getIngestionPort());
		assertEquals("Mismatch in scheme", Configuration.DEFAULT_SCHEME, config.getScheme());
		
	}

	@Test
	public void testBuildFromConfig() {
		Configuration config = Configuration.buildFromConfig("config.properties");
		assertEquals("Mismatch in hostname", "hostname", config.getHost());
		assertEquals("Mismatch in user", "username", config.getUser());
		assertEquals("Mismatch in password", "password", config.getPassword());
		assertEquals("Mismatch in port", "port-number", config.getPort());
		assertEquals("Mismatch in ingestion port", "ingestion-port-number",
				config.getIngestionPort());
		assertEquals("Mismatch in password", "https", config.getScheme());
	}
	
	@Test
	public void testBuildFromConfigWithMissing() {
		Configuration config = Configuration.buildFromConfig("config-with-missing.properties");
		assertEquals("Mismatch in hostname", "hostname", config.getHost());
		assertEquals("Mismatch in user", "username", config.getUser());
		assertEquals("Mismatch in password", "password", config.getPassword());
		assertEquals("Mismatch in port", Integer.toString(Configuration.DEFAULT_PORT), config.getPort());
		assertEquals("Mismatch in ingestion port", Integer.toString(Configuration.DEFAULT_INGESTION_PORT),
				config.getIngestionPort());
		assertEquals("Mismatch in password", Configuration.DEFAULT_SCHEME, config.getScheme());
	}

}

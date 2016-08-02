/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

/**
 * A custom SSLSocketFactory which ignores the hostname verification and builds
 * a dummy SSL context.
 * 
 */
public class NonValidatingSSLSocketFactory extends SSLConnectionSocketFactory {

	/**
	 * Default constructor
	 */
	public NonValidatingSSLSocketFactory() {
		// super(getSSLContext(),
		// SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		super(getSSLContext(), new NoopHostnameVerifier());
	}

	/**
	 * Initializes the SSLContext with dummy values and return
	 * 
	 * @return SSLContext
	 */
	public static SSLContext getSSLContext() {
		SSLContext context;
		try {
			context = SSLContext.getInstance(TLS);
			context.init(null, new TrustManager[] { new DummyX509TrustManager() }, null);
			return context;
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new RuntimeException(e);
		}
	}

	private static class DummyX509TrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// TODO Auto-generated method stub

		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// TODO Auto-generated method stub

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}

	}
}

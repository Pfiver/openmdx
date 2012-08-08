/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: TestAuthenticatedPrincipals.java,v 1.7 2006/08/11 09:45:21 hburger Exp $
 * Description: Signed Token
 * Revision:    $Revision: 1.7 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2006/08/11 09:45:21 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2004, OMEX AG, Switzerland
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 * 
 * * Neither the name of the openMDX team nor the names of its
 *   contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * ------------------
 * 
 * This product includes software developed by the Apache Software
 * Foundation (http://www.apache.org/).
 */
package org.openmdx.test.security.auth;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.openmdx.base.text.conversion.Base64;
import org.openmdx.kernel.security.authentication.spi.GenericPrincipal;
import org.openmdx.kernel.security.token.TokenException;
import org.openmdx.kernel.security.token.TokenFactory;
import org.openmdx.kernel.security.token.TokenValidator;
import org.openmdx.kernel.text.StringBuilders;

public class TestAuthenticatedPrincipals extends TestCase {
	
    /**
     * Constructor
     */
    public TestAuthenticatedPrincipals() {
        super();
    }

    /**
     * Constructor
     *
     * @param name
     */
    public TestAuthenticatedPrincipals(String name) {
        super(name);
    }

    /**
     * 
     */
    protected TokenFactory tokenFactory;
	
    /**
     * 
     */
	protected TokenValidator tokenValidator;

    /**
     * The batch TestRunner can be given a class to run directly.
     * To start the batch runner from your main you can write: 
     */
    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * A test runner either expects a static method suite as the
     * entry point to get a test to run or it will extract the 
     * suite automatically. 
     */
    public static Test suite(
    ) {
        return new TestSuite(TestAuthenticatedPrincipals.class);
    }

	protected String getAlias(){
		return "DemoIdentity";
	}
	
	protected String getKeyStorePassPhrase(
	){
		return "DemoIdentityKeyStorePassPhrase";
	}
	
	protected String getPrivateKeyPassPhrase(
	){
		return "DemoIdentityPassPhrase";
	}
	
	protected String getAlgorithm(){
		return "SHA1withRSA";		
	}
	
	protected String getKeyStoreType(
	){
		return "JKS";
	}
	
	protected String getKeyStoreFileName(){
		return "C:\\opt\\BEA\\weblogic81\\server\\lib\\DemoIdentity.jks";
	}
	
	protected void setUp() throws Exception {		
		KeyStore keyStore = getKeyStore(
		    getKeyStoreType(),
		    getKeyStoreFileName(),
		    getKeyStorePassPhrase()				
		);
		this.tokenValidator = new TokenValidator(
			keyStore.getCertificate(getAlias()).getPublicKey()
		);
		this.tokenFactory = new TokenFactory(
			getAlgorithm(), 
			(PrivateKey) getKey(
				keyStore,
				getAlias(), 
				getPrivateKeyPassPhrase()
			)
		);
		super.setUp();
	}

	/**
	 * Get a specific Key Store
	 * 
	 * @param keyStoreType
	 * @param keyStoreFileName
	 * @param keyStorePassPhrase
	 * 
	 * @return the initialized key store
	 * 
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
	protected static KeyStore getKeyStore(
		String keyStoreType,
		String keyStoreFileName,
		String keyStorePassPhrase
	) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
		KeyStore keyStore = KeyStore.getInstance(keyStoreType);   
		keyStore.load(
			new FileInputStream(keyStoreFileName),
			keyStorePassPhrase.toCharArray()
		);
		return keyStore;
	}

	/**
	 * Get a spoecific key
	 * 
	 * @param keyStore
	 * @param alias
	 * @param passphrase
	 * @return the requested key
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws UnrecoverableKeyException
	 */
	protected Key getKey(
		KeyStore keyStore,
		String alias,
		String passphrase
	) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException{
		return keyStore.getKey(alias, passphrase.toCharArray());
	}

	/**
	 * Coookie test
	 * 
	 * @throws Throwable
	 */
	public void testCookie() throws Throwable {
		consume(produce());
	}

	public String produce(		
	) throws TokenException {
		byte[] token = this.tokenFactory.create(1000L, PRINCIPAL_SET);
		String cookie = Base64.encode(token);
		System.out.println("Binary size: " + token.length);				
		return cookie;
	}

	public void consume(
		String cookie
	) throws TokenException {
		System.out.println("Base64 size: " + cookie.length());				
		byte[] token = Base64.decode(cookie);
		Principal[] principals = (Principal[]) this.tokenValidator.getValue(token);
		CharSequence text = StringBuilders.newStringBuilder();
		for(int i=0; i < principals.length; i++) {
			Principal p = principals[i];
            StringBuilders.asStringBuilder(
                text
            ).append(
                "\n{"
            );
			if(p instanceof GenericPrincipal) StringBuilders.asStringBuilder(
                text
            ).append(
                ((GenericPrincipal)p).getType()
            ).append(
                ','
            );
            StringBuilders.asStringBuilder(
                text
            ).append(
                p
            ).append(
                ','
            ).append(
                p.getName()
            ).append(
                '}'
            );
		}
		System.out.println("Text size: " + text.length() + " " + text);
	}

	//------------------------------------------------------------------------
	// Test Data
	//------------------------------------------------------------------------

	/**
     * A generic principal
     */
    public static final String PRINCIPAL_TYPE = "org:openmdx:security:identity1:Principal";
    
    /**
     * A group principal
     */
    public static final String GROUP_TYPE = "org:openmdx:security:identity1:Group";

	protected static final Principal[] PRINCIPAL_SET = new Principal[]{
		new GenericPrincipal(
			PRINCIPAL_TYPE,
			"ch::omex::mdx::compatibility::security1/provider/ch::omex/segment/ch::omex::mdx::test/subject/bdb32db0-1043-11d9-8761-31bbe4eeae10/role/SingleSignOn",	
			"myName"
		),
		new GenericPrincipal(
			GROUP_TYPE,
			"ch::omex::mdx::compatibility::security1/provider/ch::omex/segment/ch::omex::mdx::test/subject/bdb32db0-1043-11d9-8761-31bbe4eeae11/role/SingleSignOn", 
			"myFirstRole"
		),
		new GenericPrincipal(
			GROUP_TYPE,
			"ch::omex::mdx::compatibility::security1/provider/ch::omex/segment/ch::omex::mdx::test/subject/bdb32db0-1043-11d9-8761-31bbe4eeae12/role/SingleSignOn", 
			"mySecondRole"
		),
		new GenericPrincipal(
			GROUP_TYPE,
			"ch::omex::mdx::compatibility::security1/provider/ch::omex/segment/ch::omex::mdx::test/subject/bdb32db0-1043-11d9-8761-31bbe4eeae13/role/SingleSignOn", 
			"myThirdRole"
		),
		new GenericPrincipal(
			GROUP_TYPE,
			"ch::omex::mdx::compatibility::security1/provider/ch::omex/segment/ch::omex::mdx::test/subject/bdb32db0-1043-11d9-8761-31bbe4eeae14/role/SingleSignOn", 
			"myFourthRole"
		),
		new GenericPrincipal(
			GROUP_TYPE,
			"ch::omex::mdx::compatibility::security1/provider/ch::omex/segment/ch::omex::mdx::test/subject/bdb32db0-1043-11d9-8761-31bbe4eeae15/role/SingleSignOn", 
			"myFifthRole"
		),
		new GenericPrincipal(
			GROUP_TYPE,
			"ch::omex::mdx::compatibility::security1/provider/ch::omex/segment/ch::omex::mdx::test/subject/bdb32db0-1043-11d9-8761-31bbe4eeae16/role/SingleSignOn", 
			"mySixthRole"
		),
		new GenericPrincipal(
			GROUP_TYPE,
			"ch::omex::mdx::compatibility::security1/provider/ch::omex/segment/ch::omex::mdx::test/subject/bdb32db0-1043-11d9-8761-31bbe4eeae17/role/SingleSignOn", 
			"mySeventhRole"
		),
		new GenericPrincipal(
			GROUP_TYPE,
			"ch::omex::mdx::compatibility::security1/provider/ch::omex/segment/ch::omex::mdx::test/subject/bdb32db0-1043-11d9-8761-31bbe4eeae18/role/SingleSignOn", 
			"myEighthRole"
		),
		new GenericPrincipal(
			GROUP_TYPE,
			"ch::omex::mdx::compatibility::security1/provider/ch::omex/segment/ch::omex::mdx::test/subject/bdb32db0-1043-11d9-8761-31bbe4eeae19/role/SingleSignOn", 
			"myNinethRole"
		)
	};
	
}


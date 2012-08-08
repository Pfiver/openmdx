/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: ApplicationContextFactory.java,v 1.1 2008/01/12 11:28:30 hburger Exp $
 * Description: Application Context Factory
 * Revision:    $Revision: 1.1 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/01/12 11:28:30 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2008, OMEX AG, Switzerland
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
 * This product includes software developed by other organizations as
 * listed in the NOTICE file.
 */
package org.openmdx.tomcat.application.container;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;

import org.openmdx.kernel.naming.container.openmdx.ContainerContextFactory;

/**
 * Application Context Factory
 */
class ApplicationContextFactory extends ContainerContextFactory {

	/**
	 * Constructor
	 */
	ApplicationContextFactory(
	) {
	}

	/* (non-Javadoc)
	 * @see org.openmdx.kernel.naming.container.openmdx.ContainerContextFactory#getInitialContext(java.lang.String, java.lang.ClassLoader, java.lang.String)
	 */
	@Override
	protected Context getSubContext(
		String name, 
		ClassLoader classLoader,
		String uri
	) {
		this.classLoaders.put(uri, classLoader);
		return super.getSubContext(name, classLoader, uri);
	}

	/**
	 * Retrieve an enterprise application's class loader 
	 * @param enterpriseApplication
	 * 
	 * @return the enterprise application's class loader
	 */
	protected ClassLoader getClassLoader(
		String enterpriseApplication
	){
		return this.classLoaders.get(enterpriseApplication);
	}
	
	/**
	 * Enterprise Application Class Loader Registry
	 */
	private final Map<String,ClassLoader> classLoaders = new HashMap<String,ClassLoader>();

}

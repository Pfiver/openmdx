/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: MethodDeploymentDescriptor.java,v 1.2 2005/04/04 12:38:21 hburger Exp $
 * Description: MethodDeploymentDescriptor
 * Revision:    $Revision: 1.2 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2005/04/04 12:38:21 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2005, OMEX AG, Switzerland
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
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
package org.openmdx.kernel.application.deploy.enterprise;

import java.util.Collections;
import java.util.List;

import org.openmdx.kernel.application.deploy.spi.Deployment.Method;


/**
 * MethodDeploymentDescriptor
 */
public class MethodDeploymentDescriptor implements Method {

    /**
     * Constructor
     */
    public MethodDeploymentDescriptor(
        String methodIntf,
        String methodName,
        List methodParams        
    ) {
        this.methodIntf = methodIntf;
        this.methodName = methodName;
        this.methodParams = methodParams;
    }
    
    /**
     * 
     */
    private final String methodIntf;
    
    /**
     * 
     */
    private final String methodName;
    
    /**
     * 
     */
    private final List methodParams;
    
    /* (non-Javadoc)
     * @see org.openmdx.kernel.application.deploy.spi.Deployment.Method#getMethodIntf()
     */
    public String getMethodIntf() {
        return this.methodIntf;
    }

    /* (non-Javadoc)
     * @see org.openmdx.kernel.application.deploy.spi.Deployment.Method#getMethodName()
     */
    public String getMethodName() {
        return this.methodName;
    }

    /* (non-Javadoc)
     * @see org.openmdx.kernel.application.deploy.spi.Deployment.Method#getMethodParams()
     */
    public List getMethodParams() {
        return this.methodParams == null ? 
            null :
            Collections.unmodifiableList(this.methodParams);
    }

}

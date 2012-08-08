/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: XriProtocols.java,v 1.10 2008/08/20 23:37:22 hburger Exp $
 * Description: XRI Protocol Constants
 * Revision:    $Revision: 1.10 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/08/20 23:37:22 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2004, OMEX AG, Switzerland
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
 * ___________________________________________________________________________ 
 *
 * This class should log as it has to be loaded by the system class loader. 
 */
package org.openmdx.kernel.url.protocol;

/**
 * @deprecated use XRI1Protocols or XRI2Protocols
 */
public class XriProtocols extends XriAuthorities {

    /**
     * Constructor 
     */
    protected XriProtocols() {
        // Avoid instantiation
    }

    /**
     * @deprecated use XRI2Protocols.RESOURCE_PREFIX
     */
    public final static String RESOURCE_PREFIX = XRI_2Protocols.RESOURCE_PREFIX;

    /**
     * @deprecated use XRI1Protocols.OPENMDX_PREFIX
     */
    public final static String OPENMDX_PREFIX = XRI_1Protocols.OPENMDX_PREFIX;
    
    /**
     * @deprecated use XRI2Protocols.ZIP_PREFIX
     */
    public final static String ZIP_PREFIX = XRI_2Protocols.ZIP_PREFIX;

    /**
     * @deprecated use XRI2Protocols.ZIP_SUFFIX
     */
    public final static String ZIP_SEPARATOR = XRI_2Protocols.ZIP_SEPARATOR;

}

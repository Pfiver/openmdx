/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: ExtensionTarget.java,v 1.2 2007/05/02 14:15:03 hburger Exp $
 * Description: Extension Target
 * Revision:    $Revision: 1.2 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2007/05/02 14:15:03 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2007, OMEX AG, Switzerland
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
package org.openmdx.model1.importer.metadata;

/**
 * Extension Target
 */
public enum ExtensionTarget {

    NONE("none"),
    JDO("jdo"),
    ORM("orm");
    
    /**
     * Constructor 
     *
     * @param xmlFormat
     */
    private ExtensionTarget(final String xmlFormat) {
        this.xmlFormat = xmlFormat;
    }

    /**
     * The XML value
     * 
     * @return the XML value for literal
     */
    public final String toXMLFormat(){
        return this.xmlFormat;
    }
    
    /**
     * 
     */
    private final String xmlFormat;

    /**
     * Retrieve a <code>ExtensionTarget</code> from its XML representation-
     * 
     * @param xmlFormat
     * 
     * @return the corresponding <code>ExtensionTarget</code> value
     * 
     * @throws IllegalArgumentException if <code>xmlFormat</code> is
     * neither <code>null</code> nor representing any of the 
     * <code>ExtensionTarget</code> values. 
     */
    public static ExtensionTarget fromXMLFormat(
        String xmlFormat
    ){
        if(xmlFormat == null) {
            return null;
        };
        for(ExtensionTarget value : values()) {
            if(value.xmlFormat.equals(xmlFormat)) {
                return value;
            }
        }
        throw new IllegalArgumentException(
            "There is no " + ExtensionTarget.class.getName() + 
            " enum represented by " + xmlFormat
        );
    }
        
}

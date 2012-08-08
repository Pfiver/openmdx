/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: IriMarshaller.java,v 1.3 2008/08/21 20:15:57 hburger Exp $
 * Description: Path/ObjectId Marshaller 
 * Revision:    $Revision: 1.3 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/08/21 20:15:57 $
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
 * This product includes software developed by other organizations as
 * listed in the NOTICE file.
 */
package org.openmdx.compatibility.base.naming;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.compatibility.base.marshalling.Marshaller;
import org.openmdx.kernel.url.protocol.XriAuthorities;


/**
 * Path/ObjectId Marshaller
 */
public final class IriMarshaller
    implements Marshaller
{

    private IriMarshaller(
    ){
        // Avoid external instantiation
    }
    
    /**
     * Memorize the singleton
     */ 
    final static private Marshaller instance = new IriMarshaller();
    
    /**
     * Memorize the delegate's singleton
     */ 
    final private Marshaller delegate = XRI_2Marshaller.getInstance();

    /**
     * Return the singleton
     */ 
    static public Marshaller getInstance(
    ){
        return IriMarshaller.instance;
    }

    
    //------------------------------------------------------------------------
    // Implements Marshaller
    //------------------------------------------------------------------------

    /**
     * Marshal a CharSequence[] into a CharSequence
     * 
     * @param     charSequences
     *            The array of CharSequences to be marshalled.
     * 
     * @return      A CharSequence containing the marshalled objects.
   */
    public Object marshal (
        Object charSequences
    ) throws ServiceException {
        throw new UnsupportedOperationException(
            "Direct IRI marshalling not supported yet"
        );
    }

    /**
     * Unmarshal a CharSequence into a CharSequence[].
     * 
     * @param         marshalledObjects
     *            A string containing a marshalled sequence of objects
     * 
     * @return    A String array containing the unmarshaled sequence
     *                  of objects.
     * @exception ServiceException ILLEGAL_ARGUMENT
    */
    public Object unmarshal (
        Object charSequence
    ) throws ServiceException {
        return this.delegate.unmarshal(
            charSequence.toString(
            ).replaceAll(
                "@\\*openmdx",
                XriAuthorities.OPENMDX_AUTHORITY
            ).replaceAll(
                "/\\*",
                "/"
            ).replaceAll(
                "%2F",
                "/"
            ).replaceAll(
                "%25",
                "%"
            )
        );
    }
        
}

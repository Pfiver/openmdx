/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: UUIDFormatter.java,v 1.7 2008/01/08 16:16:31 hburger Exp $
 * Description: UUID Formatter
 * Revision:    $Revision: 1.7 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/01/08 16:16:31 $
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
package org.openmdx.base.text.format;

import org.openmdx.kernel.collection.ArraysExtension;
import org.openmdx.kernel.text.format.IndentingFormatter;


import java.util.UUID;




/**
 * UUID Formatter
 */
public class UUIDFormatter {

    /**
     * 
     */
    private final UUID uuid;

    /**
     * Constructor
     * 
     * @param uuid
     */
    public UUIDFormatter(
        UUID uuid
    ){
        this.uuid = uuid;
    }

    /**
     * Returns a string representation of the byte buffer
     *
     * @return  a String
     */
    public String toString()
    {
        if(this.uuid == null) return null;
        boolean timeBased = this.uuid.version() == 1;
        return this.uuid.getClass().getName() + " (" + this.uuid + "): " + IndentingFormatter.toString(
                timeBased ? ArraysExtension.asMap(
                new String[]{
                    "variant",
                    "version",
                    "node",
                    "clockSequence",
                    "timestamp"
                 },
                 new Object[]{
                    new Integer(this.uuid.variant()),
                    new Integer(this.uuid.version()),
                    new Long(this.uuid.node()),
                    new Integer(this.uuid.clockSequence()),
                    new Long(this.uuid.timestamp())
                 }
            ) : ArraysExtension.asMap(
                new String[]{
                    "variant",
                    "version"
                },
                                new Object[]{
                    new Integer(this.uuid.variant()),
                    new Integer(this.uuid.version())
                }
                )
        );
    }

}

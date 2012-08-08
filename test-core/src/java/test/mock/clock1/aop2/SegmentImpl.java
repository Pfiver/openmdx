/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: SegmentImpl.java,v 1.1 2010/09/22 09:04:29 hburger Exp $
 * Description: SegmentImpl 
 * Revision:    $Revision: 1.1 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2010/09/22 09:04:29 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2010, OMEX AG, Switzerland
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
package test.mock.clock1.aop2;

import java.lang.reflect.UndeclaredThrowableException;
import java.text.ParseException;

import org.openmdx.base.aop2.AbstractObject;
import org.openmdx.base.naming.Path;
import org.w3c.format.DateTimeFormat;

import test.openmdx.clock1.jmi1.Clock1Package;

/**
 * SegmentImpl
 */
public class SegmentImpl extends AbstractObject<test.openmdx.clock1.jmi1.Segment,test.openmdx.clock1.cci2.Segment,Void> {

    /**
     * Constructor 
     *
     * @param same
     * @param next
     */
    public SegmentImpl(
        test.openmdx.clock1.jmi1.Segment same,
        test.openmdx.clock1.cci2.Segment next
    ) {
        super(same, next);
    }
        
    public test.openmdx.clock1.jmi1.Time currentDateAndTime(
    ){
        Clock1Package clock1Package = (Clock1Package) this.sameObject().refImmediatePackage();
        try {
            return clock1Package.createTime(DateTimeFormat.BASIC_UTC_FORMAT.parse("20000401T120000.000Z"));
        } catch (ParseException exception) {
            throw new UndeclaredThrowableException(exception);
        }
    }
    
    public java.lang.String getDescription(
    ){
        throw new ArrayIndexOutOfBoundsException("Mocked behaviour");
    }
    
    public org.openmdx.base.cci2.Provider getProvider(
    ){
        return this.nextManager().getObjectById(
            org.openmdx.base.cci2.Provider.class,
            "xri://@openmdx*test.openmdx.clock1/provider/Mocked"
        );
    }
    
}

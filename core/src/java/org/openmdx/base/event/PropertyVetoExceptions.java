/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: PropertyVetoExceptions.java,v 1.6 2008/09/10 08:55:29 hburger Exp $
 * Description: openMDX Events: Runtime Exceptions
 * Revision:    $Revision: 1.6 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/10 08:55:29 $
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
 * notice, this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 * 
 * * Neither the name of the openMDX team nor the names of its
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
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
package org.openmdx.base.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import org.openmdx.base.exception.BadParameterException;
import org.openmdx.kernel.exception.BasicException;

/**
 * openMDX Events
 * Runtime Exceptions
 */
public class PropertyVetoExceptions {
    
   /**
    * Avoid instantiation
    */ 
   private PropertyVetoExceptions(
   ){
       super();
   }

   /**
    * Wrap the PropertyVetoException into an IllegalArgumentException
    * 
    * @param cause a PropertyVetoException
    * 
    * @return the corresponding IllegalArgumentException
    */
   public static IllegalArgumentException toIllegalArgumentException(
       PropertyVetoException cause
   ){
       if(cause == null) return null;
       PropertyChangeEvent event = cause.getPropertyChangeEvent();
       return new BadParameterException(
           cause,
           "Property change veto",
           event == null ? null : new BasicException.Parameter[]{
               new BasicException.Parameter("source", event.getSource()),
               new BasicException.Parameter("propertyName", event.getPropertyName()),
               new BasicException.Parameter("oldValue", event.getOldValue()),
               new BasicException.Parameter("newValue", event.getNewValue()),
               new BasicException.Parameter("propagationId",event.getPropagationId())
           }
       );
   }
    
}

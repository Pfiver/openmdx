/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: DecimalMarshaller.java,v 1.8 2008/02/08 16:51:25 hburger Exp $
 * Description: DecimalMarshaller class
 * Revision:    $Revision: 1.8 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/02/08 16:51:25 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2004-2008, OMEX AG, Switzerland
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
package org.openmdx.base.accessor.jmi.spi;

import java.math.BigDecimal;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.compatibility.base.marshalling.Marshaller;
import org.openmdx.kernel.exception.BasicException;


//---------------------------------------------------------------------------
/**
 * Number <-> BigDecimal marshaller. Marshals objects which are instance of
 * Number to the specific type BigDecimal.
 */
public class DecimalMarshaller
  implements Marshaller {

  //-------------------------------------------------------------------------
  private DecimalMarshaller(
  ) {
      super();
  }
  
  //-------------------------------------------------------------------------
  public static DecimalMarshaller getInstance(
    boolean forward
  ) {
    return instance;
  }

  //-------------------------------------------------------------------------
  @SuppressWarnings("unchecked")
  public Object marshal(
    Object source
  ) throws ServiceException {
    try {
        if(source == null) return null;
      if(source instanceof BigDecimal) return source;
      return new BigDecimal(((Number)source).toString());
    } 
    catch (RuntimeException e) {
        throw new ServiceException(
            e,
            BasicException.Code.DEFAULT_DOMAIN, 
            BasicException.Code.TRANSFORMATION_FAILURE, 
            new BasicException.Parameter [] {
              new BasicException.Parameter("source", source),
              new BasicException.Parameter("source class", source.getClass().getName()),
            },
            "Could not marshal source to BigDecimal"
        );
    }
  }
  
  //-------------------------------------------------------------------------
  @SuppressWarnings("unchecked")
  public Object unmarshal (
    Object source
  ) throws ServiceException {
    try {
        return source == null
          ? null
          : (BigDecimal)source;
    } 
    catch (RuntimeException e) {
        throw new ServiceException(
            e,
            BasicException.Code.DEFAULT_DOMAIN, 
            BasicException.Code.TRANSFORMATION_FAILURE, 
            new BasicException.Parameter [] {
              new BasicException.Parameter("source", source),
              new BasicException.Parameter("source class", source.getClass().getName()),
            },
            "Could not unmarshal BigDecimal"
        );
    }
  }

  //-------------------------------------------------------------------------
  // Variables
  //-------------------------------------------------------------------------
  static private DecimalMarshaller instance = new DecimalMarshaller();

}
  
//--- End of File -----------------------------------------------------------

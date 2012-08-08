/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.opencrx.org/
 * Name:        $Id: NullValue.java,v 1.12 2008/08/12 16:38:06 wfro Exp $
 * Description: openCRX application plugin
 * Revision:    $Revision: 1.12 $
 * Owner:       CRIXP AG, Switzerland, http://www.crixp.com
 * Date:        $Date: 2008/08/12 16:38:06 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2004-2007, OMEX AG, Switzerland
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
 *
 * This product includes yui, the Yahoo! UI Library
 * (License - based on BSD).
 *
 */
package org.openmdx.portal.servlet.attribute;

import java.io.Serializable;

import org.openmdx.base.accessor.jmi.cci.RefObject_1_0;
import org.openmdx.portal.servlet.Autocompleter_1_0;
import org.openmdx.portal.servlet.HtmlPage;

public final class NullValue 
    extends AttributeValue
    implements Serializable {
  
    //-------------------------------------------------------------------------
    public static AttributeValue createNullValue(
    ) {
        return new NullValue();
    }
    
    //-------------------------------------------------------------------------
    protected NullValue(
    ) {
        super(null, null, null);
    }

    //-------------------------------------------------------------------------
    public Object getDefaultValue(
    ) {
        return null;
    }

    //-------------------------------------------------------------------------
    public Object getValue(
        boolean forGrid
    ) {
        return null;
    }

    //-------------------------------------------------------------------------
    public String getStringifiedValue(
        HtmlPage p, 
        boolean multiLine, 
        boolean forEditing,
        boolean shortFormat
    ) {
        return "";
    }
  
    //-------------------------------------------------------------------------
    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.attribute.AttributeValue#getBackColor()
     */
    public String getBackColor(
    ) {
        // TODO Auto-generated method stub
        return null;
    }

    //-------------------------------------------------------------------------
    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.attribute.AttributeValue#getColor()
     */
    public String getColor(
    ) {
        // TODO Auto-generated method stub
        return null;
    }

    //-------------------------------------------------------------------------
    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.attribute.AttributeValue#getIconKey()
     */
    public String getIconKey(
    ) {
        // TODO Auto-generated method stub
        return null;
    }
    
    //-------------------------------------------------------------------------
    public boolean isChangeable(
    ) {
      return false;
    }
  
    //-------------------------------------------------------------------------
    public Autocompleter_1_0 getAutocompleter(
        RefObject_1_0 target
    ) {
        return null;
    }
        
    //-------------------------------------------------------------------------
    private static final long serialVersionUID = 3906366047639647799L;

}

//--- End of File -----------------------------------------------------------

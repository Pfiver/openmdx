/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.opencrx.org/
 * Name:        $Id: FieldGroup.java,v 1.8 2008/08/12 16:38:07 wfro Exp $
 * Description: CompositeGrid
 * Revision:    $Revision: 1.8 $
 * Owner:       CRIXP AG, Switzerland, http://www.crixp.com
 * Date:        $Date: 2008/08/12 16:38:07 $
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
package org.openmdx.portal.servlet.view;

import java.io.Serializable;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.portal.servlet.attribute.Attribute;
import org.openmdx.portal.servlet.control.FieldGroupControl;

public class FieldGroup 
    extends ControlState
    implements Serializable {
    
    //-------------------------------------------------------------------------
    /**
     * Creates a field group based on field group control. The feature values
     * of the field group are read from object. If object is null the
     * values are read from view.getObjectReference().getObject().
     */
    public FieldGroup(
        FieldGroupControl control,
        ObjectView view,
        Object object
    ) {
        super(
            control,
            view
        );
        this.object = object;
    }
 
    //-------------------------------------------------------------------------
    public FieldGroupControl getFieldGroupControl(
    ) {
        return (FieldGroupControl)this.control;
    }
    
    //-------------------------------------------------------------------------
    public Attribute[][] getAttribute(
    ) {
        return this.getFieldGroupControl().getAttribute(
            this.getObject(),
            view.getApplicationContext()
        );
    }
    
    //-------------------------------------------------------------------------
    public void refresh(
        boolean refreshData
    ) throws ServiceException {
        // nothing to refresh
    }
    
    //-------------------------------------------------------------------------
    public Object getObject(
    ) {
        return this.object == null
            ? this.view.getObjectReference().getObject()
            : this.object;
    }
    
    //-------------------------------------------------------------------------
    private static final long serialVersionUID = 1911685908400089427L;

    protected final Object object;
}

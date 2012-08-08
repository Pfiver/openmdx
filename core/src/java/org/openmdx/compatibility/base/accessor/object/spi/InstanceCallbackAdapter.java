/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: InstanceCallbackAdapter.java,v 1.4 2004/04/02 16:59:01 wfro Exp $
 * Description: SPICE Object Layer: JDO Instance Callback Adapter
 * Revision:    $Revision: 1.4 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2004/04/02 16:59:01 $
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
package org.openmdx.compatibility.base.accessor.object.spi;

import org.openmdx.base.event.InstanceCallbackEvent;
import org.openmdx.base.event.InstanceCallbackListener;
import org.openmdx.base.exception.ServiceException;


/**
 * @deprecated
 */
public class InstanceCallbackAdapter implements InstanceCallbackListener {

    public InstanceCallbackAdapter(
        org.openmdx.compatibility.base.accessor.object.cci.InstanceCallbacks_1_0 synchronization
    ){
        this.synchronization = synchronization;
    }
    
    private final org.openmdx.compatibility.base.accessor.object.cci.InstanceCallbacks_1_0 synchronization;
    
    /* (non-Javadoc)
     * @see org.openmdx.base.event.InstanceCallbackListener#postLoad(org.openmdx.base.event.InstanceCallbackEvent)
     */
    public void postLoad(InstanceCallbackEvent instanceCallback) throws ServiceException {
        if(this.synchronization!=null)this.synchronization.objPostLoad();
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.event.InstanceCallbackListener#preStore(org.openmdx.base.event.InstanceCallbackEvent)
     */
    public void preStore(InstanceCallbackEvent instanceCallback) throws ServiceException {
        if(this.synchronization!=null)this.synchronization.objPreStore();
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.event.InstanceCallbackListener#preClear(org.openmdx.base.event.InstanceCallbackEvent)
     */
    public void preClear(InstanceCallbackEvent instanceCallback) throws ServiceException {
        if(this.synchronization!=null)this.synchronization.objPreClear();
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.event.InstanceCallbackListener#preDelete(org.openmdx.base.event.InstanceCallbackEvent)
     */
    public void preDelete(InstanceCallbackEvent instanceCallback) throws ServiceException {
        if(this.synchronization!=null)this.synchronization.objPreDelete();
    }

}

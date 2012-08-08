/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: ClassLoaderAwareMDCAdapter.java,v 1.1 2007/12/19 15:48:12 hburger Exp $
 * Description: Class Loader Aware Mapped Diagnostic Context Adapter 
 * Revision:    $Revision: 1.1 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2007/12/19 15:48:12 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
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

package org.slf4j.lenient;

import org.slf4j.helpers.NOPMakerAdapter;
import org.slf4j.spi.MDCAdapter;

/**
 * Class Loader Aware Mapped Diagnostic Context Adapter 
 */
public class ClassLoaderAwareMDCAdapter
    extends ClassLoaderAwareDelegator
    implements MDCAdapter
{

    /* (non-Javadoc)
     * @see org.slf4j.lenient.ClassLoaderAwareDelegator#getFallbackDelegate()
     */
    protected Object getFallbackDelegate() {
        return new NOPMakerAdapter();
    }

    /* (non-Javadoc)
     * @see org.slf4j.lenient.ClassLoaderAwareDelegator#getStandardDelegate(java.lang.Class, java.lang.Object)
     */
    protected Object getStandardDelegate(
        Class binderClass,
        Object binderInstance
    ) throws Exception {
        return binderClass.getMethod(
            "getMDCA",
            new Class[]{}
        ).invoke(
            binderInstance,
            new Object[]{}
        );
    }

    /* (non-Javadoc)
     * @see org.slf4j.lenient.ClassLoaderAwareDelegator#getStaticBinderName()
     */
    protected String getStaticBinderName() {
        return "org.slf4j.impl.StaticMDCBinder";
    }

    /* (non-Javadoc)
     * @see org.slf4j.spi.MDCAdapter#clear()
     */
    public void clear() {
        ((MDCAdapter)getDelegate()).clear();
    }

    /* (non-Javadoc)
     * @see org.slf4j.spi.MDCAdapter#get(java.lang.String)
     */
    public String get(String key) {
        return ((MDCAdapter)getDelegate()).get(key);
    }

    /* (non-Javadoc)
     * @see org.slf4j.spi.MDCAdapter#put(java.lang.String, java.lang.String)
     */
    public void put(String key, String val) {
        ((MDCAdapter)getDelegate()).put(key, val);
    }

    /* (non-Javadoc)
     * @see org.slf4j.spi.MDCAdapter#remove(java.lang.String)
     */
    public void remove(String key) {
        ((MDCAdapter)getDelegate()).remove(key);
    }

}

/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: EmbeddedContainer_1.java,v 1.1 2008/09/11 18:01:06 hburger Exp $
 * Description: Embedded Container
 * Revision:    $Revision: 1.1 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/11 18:01:06 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2008, OMEX AG, Switzerland
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
package org.openmdx.base.accessor.generic.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.openmdx.base.accessor.generic.cci.Object_1_0;
import org.openmdx.base.collection.FilterableMap;

/**
 * Embedded Container
 */
class EmbeddedContainer_1 
    extends TreeMap<String, Object_1_0> 
    implements FilterableMap<String, Object_1_0>
{

    /**
     * Constructor for a modifiable container
     */
    EmbeddedContainer_1(
    ){
        this.readonly = false;
    }

    /**
     * Constructor for an unmodifiable
     *
     * @param source
     */
    EmbeddedContainer_1(
        Map<? extends String, ? extends Object_1_0> source
     ) {
        super(source);
        this.readonly = true;
    }

    /**
     * Tells whether the container is modifiable or not
     */
    private final boolean readonly;
    
    /**
     * Implements <code>Serializable</code>
     */
    private static final long serialVersionUID = -4273080923573939660L;

    /* (non-Javadoc)
     * @see org.openmdx.base.collection.FilterableMap#subMap(java.lang.Object)
     */
    public FilterableMap<String, Object_1_0> subMap(Object filter) {
        throw new UnsupportedOperationException(
            "Filtering of embedded objects not yet implemented"
        );
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.collection.FilterableMap#values(java.lang.Object)
     */
    public List<Object_1_0> values(Object criteria) {
        if(criteria != null) {
            throw new UnsupportedOperationException(
                "Re-Ordering not supported"
            );
        }
        return new ArrayList<Object_1_0>(values());
    }

    /**
     * A read-only container is unmodifiable...
     */
    protected void assertModifiability(){
        if(this.readonly) {
            throw new UnsupportedOperationException(
                "This is a read-only container"
            );
        }
    }
    
    /* (non-Javadoc)
     * @see java.util.TreeMap#clear()
     */
    @Override
    public void clear() {
        assertModifiability();
        super.clear();
    }

    /* (non-Javadoc)
     * @see java.util.TreeMap#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public Object_1_0 put(String key, Object_1_0 value) {
        assertModifiability();
        return super.put(key, value);
    }

    /* (non-Javadoc)
     * @see java.util.TreeMap#remove(java.lang.Object)
     */
    @Override
    public Object_1_0 remove(Object key) {
        assertModifiability();
        return super.remove(key);
    }

    /* (non-Javadoc)
     * @see java.util.TreeMap#entrySet()
     */
    @Override
    public Set<java.util.Map.Entry<String, Object_1_0>> entrySet() {
        return this.readonly ? Collections.unmodifiableSet(super.entrySet()) : super.entrySet();
    }

    /* (non-Javadoc)
     * @see java.util.TreeMap#keySet()
     */
    @Override
    public Set<String> keySet() {
        return this.readonly ? Collections.unmodifiableSet(super.keySet()) : super.keySet();
    }

    /* (non-Javadoc)
     * @see java.util.TreeMap#values()
     */
    @Override
    public Collection<Object_1_0> values() {
        return this.readonly ? Collections.unmodifiableCollection(super.values()) : super.values();
    }
    
}
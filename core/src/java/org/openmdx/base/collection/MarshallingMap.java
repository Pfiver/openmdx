/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: MarshallingMap.java,v 1.18 2008/09/12 17:34:19 hburger Exp $
 * Description: Marshalling Map
 * Revision:    $Revision: 1.18 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/12 17:34:19 $
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
 * This product includes software developed by other organizations as
 * listed in the NOTICE file.
 */
package org.openmdx.base.collection;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.compatibility.base.marshalling.CollectionMarshallerAdapter;
import org.openmdx.compatibility.base.marshalling.Marshaller;
import org.openmdx.compatibility.base.marshalling.ReluctantUnmarshalling;
import org.openmdx.kernel.exception.BasicException;

/**
 * A Marshalling Map
 * <p>
 * The marshaller is applied to the delegate's values, but not to its keys.
 */
public class MarshallingMap<K,V,M extends Map<K,?>>
  extends AbstractMap<K,V>
  implements Serializable 
{

    /**
     * Constructor
     * 
     * @param marshaller
     * @param map
     * @param unmarshalling 
     */
    @SuppressWarnings("unchecked")
    public MarshallingMap(
        org.openmdx.base.persistence.spi.Marshaller marshaller,
        M map, 
        Unmarshalling unmarshalling 
    ) {
        this.marshaller = marshaller;
        this.map = (Map<K, Object>) map;
        this.unmarshalling = unmarshalling;
    }
  
    /**
     * Constructor
     * 
     * @param marshaller
     * @param map
     */
    @SuppressWarnings("unchecked")
    public MarshallingMap(
        org.openmdx.base.persistence.spi.Marshaller marshaller,
        M map 
    ) {
        this(marshaller, map, Unmarshalling.EAGER);
    }

    /**
     * Constructor
     * 
     * @param marshaller
     * @param map
     */
    public MarshallingMap(
        Marshaller marshaller,
        M map 
    ) {
        this(
            new CollectionMarshallerAdapter(marshaller), 
            map, 
            marshaller instanceof ReluctantUnmarshalling ? Unmarshalling.RELUCTANT : Unmarshalling.EAGER
        );
    }
    
        
    /**
     * Implements <code>Serializable</code>
     */
    private static final long serialVersionUID = 3834309540194956341L;

    /**
     * 
     */
    protected final Unmarshalling unmarshalling;    

    /**
     * 
     */    
    private final Map<K,Object> map;

    /**
     * 
     */
    protected final org.openmdx.base.persistence.spi.Marshaller marshaller;
    
    
    @SuppressWarnings("unchecked")
    protected final M getDelegate(){
        return (M) this.map;
    }
    
    
    //------------------------------------------------------------------------
    // Implements Map
    //------------------------------------------------------------------------
    
    /* (non-Javadoc)
     * @see java.util.Map#clear()
     */
    public void clear() {
        this.map.clear();
    }
      
    /* (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value) {
        switch(this.unmarshalling) {
            case RELUCTANT:
                return super.containsValue(value);
            case EAGER: default: 
                return this.map.containsValue(
                    this.marshaller.unmarshal(value)
                );
        }
    }

    /**
     * 
     */
    public Set<Map.Entry<K, V>> entrySet(
    ) {
        return new MarshallingSet<Map.Entry<K, V>>(
            new MapEntryMarshaller<K,V>(this.marshaller),
            this.map.entrySet(),
            this.unmarshalling
        );
    }

    /* (non-Javadoc)
     * @see java.util.Map#get(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        Object value = this.map.get(key); 
        if(this.marshaller instanceof CollectionMarshallerAdapter) {
            try {
                return (V) ((CollectionMarshallerAdapter)this.marshaller).getDelegate().marshal(value);
            } catch (ServiceException exception) {
                if(exception.getExceptionCode() == BasicException.Code.NOT_FOUND) {
                    return null;
                }
            }
        }
        return (V) this.marshaller.marshal(value);
    }

    /* (non-Javadoc)
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.Map#keySet()
     */
    public Set<K> keySet() {
        return map.keySet();
    }
  
    /* (non-Javadoc)
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public V put(
        K key, 
        V value
    ) {
        return (V) this.marshaller.marshal(
            this.map.put(key, this.marshaller.unmarshal(value))
        );
    }

    /* (non-Javadoc)
     * @see java.util.Map#remove(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public V remove(
        Object key
    ) {
        return (V) this.marshaller.marshal(
            this.map.remove(key)
        );
    }

    /* (non-Javadoc)
     * @see java.util.Map#size()
     */
    public int size() {
        return this.map.size();
    }

    /* (non-Javadoc)
     * @see java.util.Map#values()
     */
    public Collection<V> values() {
        return new MarshallingCollection<V>(
            this.marshaller,
            this.map.values(), 
            this.unmarshalling
        );
    }

    
    //------------------------------------------------------------------------
    // Class MapEntryMarshaller
    //------------------------------------------------------------------------

    /**
     * Map Entry Marshaller
     */
    static class MapEntryMarshaller<K,V>
        implements org.openmdx.base.persistence.spi.Marshaller 
     {

        /**
         * Constructor
         * @param marshaller
         */      
        public MapEntryMarshaller(
            org.openmdx.base.persistence.spi.Marshaller marshaller
        ) {
            this.marshaller = marshaller;
        }
    
        /**
         * 
         */
        @SuppressWarnings("unchecked")
        public Object marshal(
            Object source
        ) {
            return source instanceof Map.Entry ? new MarshallingMapEntry<K,V>(
                this.marshaller,
                source
            ) : source;
        }
    
        /**
         * 
         */
        @SuppressWarnings("unchecked")
        public Object unmarshal(
            Object source
        ) {
            return source instanceof MarshallingMapEntry ?
                ((MarshallingMapEntry<K,V>)source).getEntry() :
                source;
        }
            
        /**
         * 
         */
        private final org.openmdx.base.persistence.spi.Marshaller marshaller;
  
    }


    //------------------------------------------------------------------------
    // Class MarshallingMapEntry
    //------------------------------------------------------------------------

    /**
     * Marshalling Map Entry
     */
    static class MarshallingMapEntry<K,V> 
        implements Map.Entry<K,V> 
    {
    
        /**
         * Constructor
         * 
         * @param marshaller
         * @param entry
         */
        @SuppressWarnings("unchecked")
        public MarshallingMapEntry(
            org.openmdx.base.persistence.spi.Marshaller marshaller,
            Object entry
        ) {
            this.marshaller = marshaller;
            this.entry = (Entry<K, Object>) entry;
        }

        /**
         * 
         * @return
         */
        public Map.Entry<K,?> getEntry(
        ) {
            return this.entry;
        }
    
        /**
         * 
         */
        public K getKey(
        ) {
            return this.entry.getKey();      
        }

        /**
         * 
         */    
        @SuppressWarnings("unchecked")
        public V getValue(
        ) {
            return (V) this.marshaller.marshal(
                this.entry.getValue()
            );
        }

        /**
         * 
         */    
        public V setValue(
            V value
        ) {
            V oldValue = this.getValue();
            this.entry.setValue(
                this.marshaller.unmarshal(value)
            );
            return oldValue;
        }
        
        /**
         * 
         */
        private final Map.Entry<K,Object> entry;

        /**
         * 
         */    
        private final org.openmdx.base.persistence.spi.Marshaller marshaller;

    }

}

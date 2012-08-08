/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: Jmi1ContainerInvocationHandler.java,v 1.5 2008/09/18 12:12:48 hburger Exp $
 * Description: ContainerInvocationHandler 
 * Revision:    $Revision: 1.5 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/18 12:12:48 $
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
package org.openmdx.base.accessor.jmi.spi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.oasisopen.jmi1.RefContainer;
import org.openmdx.base.accessor.jmi.cci.JmiServiceException;
import org.openmdx.base.exception.RuntimeServiceException;
import org.openmdx.base.persistence.spi.Marshaller;
import org.slf4j.LoggerFactory;
import org.w3c.cci2.AnyTypePredicate;
import org.w3c.cci2.Container;

/**
 * ContainerInvocationHandler
 */
@SuppressWarnings("unchecked")
public class Jmi1ContainerInvocationHandler
    implements InvocationHandler
{

    /**
     * Constructor 
     * @param delegate
     */
    public Jmi1ContainerInvocationHandler(
        RefContainer delegate
    ) {
        this.marshaller = IdentityMarshaller.INSTANCE;
        this.refDelegate = delegate;
        this.cciDelegate = null;
    }

    /**
     * Constructor 
     *
     * @param delegate
     */
    public Jmi1ContainerInvocationHandler(
        Marshaller marshaller,
        Container<?> delegate
    ) {
        this.marshaller = marshaller == null ? IdentityMarshaller.INSTANCE : marshaller;
        this.cciDelegate = delegate;
        this.refDelegate = null;
    }
    
    /**
     * 
     */
    private final Marshaller marshaller;

    /**
     * 
     */
    private final RefContainer refDelegate;

    /**
     * 
     */
    private final Container<?> cciDelegate;
    
            
    //------------------------------------------------------------------------
    // Implements InvocationHandler
    //------------------------------------------------------------------------
    
    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @SuppressWarnings("deprecation")
    public Object invoke(
        Object proxy, 
        Method method, 
        Object[] args
    ) throws Throwable {        
        String methodName = method.getName().intern();
        Class<?> declaringClass = method.getDeclaringClass();
        try {
            if(this.cciDelegate == null) {
                if(declaringClass == proxy.getClass().getInterfaces()[0]) {
                    // 
                    // This typed association end interface has been prepended 
                    // by the Jmi1ObjectInvocationHandler
                    //
                    if("add" == methodName) {
                        this.refDelegate.refAdd(args);
                        return null;
                    } else if("get" == methodName) {
                        return this.refDelegate.refGet(args);
                    } else if("remove" == methodName) {
                        this.refDelegate.refRemove(args);
                        return null;
                    }
                } else if (declaringClass == Container.class) {
                    // 
                    // This interfaces is extended by the typed association end 
                    // interface which has been prepended 
                    // by the Jmi1ObjectInvocationHandler
                    //
                    if("getAll" == methodName && args.length == 1) {
                        return this.refDelegate.refGetAll(args[0]);
                    } else if("removeAll" == methodName && args.length == 1) {
                        this.refDelegate.refRemoveAll(args[0]);
                        return null;
                    }
                }
                return method.invoke(this.refDelegate, args);
            } else {
                if(declaringClass == RefContainer.class) {
                    if("refAdd" == methodName) {
                        ReferenceDef.getInstance(proxy.getClass()).add.invoke(
                            this.cciDelegate, 
                            (Object[]) this.marshaller.unmarshal(args[0])
                        );
                        return null;
                    } else if("refGet" == methodName) {
                        return this.marshaller.marshal(
                            ReferenceDef.getInstance(proxy.getClass()).get.invoke(
                                this.cciDelegate, 
                                (Object[]) this.marshaller.unmarshal(args[0])
                            )
                        );
                    } else if("refRemove" == methodName) {
                        ReferenceDef.getInstance(proxy.getClass()).remove.invoke(
                            this.cciDelegate, 
                            (Object[]) this.marshaller.unmarshal(args[0])
                        );
                        return null;
                    } else if("refGetAll" == methodName) {
                        Object predicate = this.marshaller.unmarshal(args[0]);
                        return this.marshaller.marshal(
                            predicate instanceof AnyTypePredicate ? ReferenceDef.getAll.invoke(
                                this.cciDelegate, 
                                predicate
                            ) : ((RefContainer)this.cciDelegate).refGetAll(
                                predicate
                            )
                        );
                    } else if("refRemoveAll" == methodName) {
                        Object predicate = this.marshaller.unmarshal(args[0]);
                        if(predicate instanceof AnyTypePredicate) {
                            ReferenceDef.removeAll.invoke(
                                this.cciDelegate, 
                                predicate
                            );
                        } else {
                            ((RefContainer)this.cciDelegate).refRemoveAll(
                                predicate
                            );
                        }
                        return null;
                    } else throw new UnsupportedOperationException(
                        declaringClass + ": " + methodName
                     );
                } else {
                    return this.marshaller.marshal(
                        method.invoke(
                            this.cciDelegate, 
                            (Object[]) this.marshaller.unmarshal(args)
                        )
                    );
                } 
            }
        } catch (InvocationTargetException exception) {
            Throwable throwable = exception.getTargetException();
            throw throwable instanceof RuntimeServiceException ?
                new JmiServiceException(throwable.getCause()) :
                throwable;
        }
    }
    
    
    //------------------------------------------------------------------------
    // Class ReferenceDef
    //------------------------------------------------------------------------
    
    /**
     * ReferenceDef
     */
    static class ReferenceDef {
    
        /**
         * Constructor 
         *
         * @param nativeInterface
         */
        private ReferenceDef(
            Class<?> nativeInterface
        ){  
            Method add = null;
            Method get = null;
            Method remove = null;
            for(Method method : nativeInterface.getDeclaredMethods()) {
                String name = method.getName().intern();
                if("add" == name) {
                    add = method;
                } else if ("get" == name) {
                    get = method;
                } else if ("remove" == name) {
                    remove = method;
                }
            }
            this.add = add;
            this.get = get;
            this.remove = remove;
        }
        
        final Method add;
        final Method get;
        final Method remove;
        final static Method getAll = getContainerMethod("getAll");
        final static Method removeAll = getContainerMethod("removeAll");
        
        private final static ConcurrentMap<Class<?>,ReferenceDef> instances = 
            new ConcurrentHashMap<Class<?>,ReferenceDef>();

        /**
         * Retrieve a proxy class' instance
         * 
         * @param referenceClass
         * 
         * @return the corresponding instance
         */
        static ReferenceDef getInstance(
            Class<?> referenceClass
        ){
            ReferenceDef instance = instances.get(referenceClass);
            if(instance == null) {
                instances.putIfAbsent(
                    referenceClass, 
                    instance = new ReferenceDef(referenceClass.getInterfaces()[0])
                );
            }
            return instance;
        };
                
        /**
         * Retrieve a bulk method
         * 
         * @param methodName
         * 
         * @return the requested method
         */
        private static Method getContainerMethod(
            String methodName
        ){
            try {
                return Container.class.getMethod(methodName, AnyTypePredicate.class);
            } catch (NoSuchMethodException exception) {
                LoggerFactory.getLogger(
                    Jmi1ContainerInvocationHandler.class
                ).error(
                    "Expected getAll() and removeAll() being a member of " + Container.class.getName(),
                    exception
                );
                return null;
            }
        }
        
    }

    
    //------------------------------------------------------------------------
    // Class IdentityMarshaller
    //------------------------------------------------------------------------
    
    /**
     * 
     * IdentityMarshaller
     *
     */
    final static class IdentityMarshaller implements Marshaller {

        /**
         * Constructor 
         */
        private IdentityMarshaller() {
            super();
        }

        /* (non-Javadoc)
         * @see org.openmdx.base.persistence.spi.Marshaller#marshal(java.lang.Object)
         */
        public final Object marshal(Object source) {
            return source;
        }

        /* (non-Javadoc)
         * @see org.openmdx.base.persistence.spi.Marshaller#unmarshal(java.lang.Object)
         */
        public final Object unmarshal(Object source) {
            return source;
        } 
        
        /**
         * The singleton
         */
        final static Marshaller INSTANCE = new IdentityMarshaller();
        
    }
    
}

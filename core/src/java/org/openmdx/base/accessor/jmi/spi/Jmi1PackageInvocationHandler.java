/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Name:        $Id: Jmi1PackageInvocationHandler.java,v 1.14 2008/09/10 08:55:23 hburger Exp $
 * Description: Jmi1PackageInvocationHandler 
 * Revision:    $Revision: 1.14 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/10 08:55:23 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2007-2008, OMEX AG, Switzerland
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
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openmdx.base.accessor.jmi.cci.RefPackage_1_0;
import org.openmdx.base.accessor.jmi.cci.RefPackage_1_4;
import org.openmdx.base.accessor.jmi.cci.RefStruct_1_0;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.kernel.exception.BasicException;
import org.openmdx.model1.accessor.basic.cci.ModelElement_1_0;
import org.openmdx.model1.accessor.basic.cci.Model_1_0;
import org.openmdx.model1.mapping.java.Identifier;
import org.w3c.cci2.AnyTypePredicate;
import org.w3c.cci2.ComparableTypePredicate;
import org.w3c.cci2.MatchableTypePredicate;
import org.w3c.cci2.MultivaluedFeaturePredicate;
import org.w3c.cci2.PartiallyOrderedTypePredicate;
import org.w3c.cci2.ResourceIdentifierTypePredicate;
import org.w3c.cci2.StringTypePredicate;

/**
 * Jmi1PackageInvocationHandler
 *
 */
public class Jmi1PackageInvocationHandler implements InvocationHandler {
    
    //-----------------------------------------------------------------------
    public static class RefPackage_1Proxy extends RefPackage_1 {
        
        public RefPackage_1Proxy(
            String qualifiedPackageName,
            javax.jmi.reflect.RefPackage outermostPackage,
            javax.jmi.reflect.RefPackage immediatePackage        
        ) {
            super(
                outermostPackage, 
                immediatePackage
            );
            this.qualifiedPackageName = qualifiedPackageName;
        }

        private static final long serialVersionUID = 5484412877112463153L;
        private final String qualifiedPackageName;
        
        public String refMofId(
        ) {
            return this.qualifiedPackageName; 
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            return 
                obj instanceof RefPackage_1Proxy && 
                this.qualifiedPackageName.equals(
                    ((RefPackage_1Proxy)obj).qualifiedPackageName
                );
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return this.qualifiedPackageName.hashCode();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "RefPackage " + this.qualifiedPackageName;
        }
        
    }
    
    //-----------------------------------------------------------------------
    public Jmi1PackageInvocationHandler(
        String qualifiedPackageName,
        javax.jmi.reflect.RefPackage outermostPackage,
        javax.jmi.reflect.RefPackage immediatePackage
    ) {
        this.delegation = new RefPackage_1Proxy(
            qualifiedPackageName,
            outermostPackage, 
            immediatePackage
        );
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public String getQualifiedMofName(
        String qualifiedPackageName,
        String cciName
    ) throws ServiceException {
        String qualifiedCciName = qualifiedPackageName + ":" + cciName;
        String qualifiedMofName = qualifiedMofNames.get(qualifiedCciName);
        if(qualifiedMofName == null) {
            Model_1_0 model = this.delegation.refModel();
            for(
                Iterator<ModelElement_1_0> i = model.getContent().iterator(); 
                i.hasNext(); 
            ) {
                ModelElement_1_0 e = i.next();
                String elementName = Identifier.CLASS_PROXY_NAME.toIdentifier((String)e.values("name").get(0));
                String qualifiedElementName = (String)e.values("qualifiedName").get(0);
                if(
                    elementName.equals(cciName) &&
                    qualifiedElementName.substring(0, qualifiedElementName.lastIndexOf(":")).equals(qualifiedPackageName)
                ) {
                    qualifiedMofNames.put(
                        qualifiedCciName, 
                        qualifiedMofName = qualifiedElementName
                    );
                    break;
                }
            }
        }
        if(qualifiedMofName == null) {
            throw new ServiceException (
                BasicException.Code.DEFAULT_DOMAIN, 
                BasicException.Code.NOT_FOUND, 
                "model element not found with qualified cci name.",
                new BasicException.Parameter [] {
                  new BasicException.Parameter("package.name", qualifiedPackageName),
                  new BasicException.Parameter("cci.name", cciName)
                }
            );
        }
        return qualifiedMofName;
    }
    
    //-----------------------------------------------------------------------
    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(
        Object proxy, 
        Method method, 
        Object[] args
    ) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        String methodName = method.getName().intern();
        if(Object.class == declaringClass) {
            //
            // Object methods
            //
            if("toString" == methodName) {
                return proxy.getClass().getName() + " delegating to " + this.delegation;
            } else if ("hashCode" == methodName) {
                return this.delegation.hashCode();
            } else if ("equals" == methodName) {
                if(Proxy.isProxyClass(args[0].getClass())) {
                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(args[0]);
                    if(invocationHandler instanceof Jmi1PackageInvocationHandler) {
                        return this.delegation.equals(
                            ((Jmi1PackageInvocationHandler)invocationHandler).delegation
                        );
                    }
                }
                return false;
            }
        } else if("refClass" == methodName && args.length == 1) {
            //
            // RefClass
            //
            return this.delegation.refClass(
                (String)args[0], // qualifiedClassName
                (RefPackage_1_0)proxy
            );
        } else if(
            declaringClass == RefPackage_1_4.class ||    
            methodName.startsWith("ref") && 
            (method.getName().length() > 3) &&
            Character.isUpperCase(method.getName().charAt(3))
        ) {
            return "refOutermostPackage" == methodName
                ? this.delegation.refOutermostPackage()
                : method.invoke(this.delegation, args);
        } else if(method.getName().startsWith("get")) {
            //
            // Getters
            //
            String cciName = method.getName().substring(3);
            String qualifiedPackageName = this.delegation.refMofId();
            return this.delegation.refClass(
                this.getQualifiedMofName(
                    qualifiedPackageName.substring(0, qualifiedPackageName.lastIndexOf(":")), 
                    cciName
                ),
                (RefPackage_1_0)proxy
            );            
        } else if(method.getName().startsWith("create")) {
            // 
            // Creators
            //
            Class<?> returnType = method.getReturnType();
            String cciName = method.getName().substring(6);
            String qualifiedPackageName = this.delegation.refMofId();   
            if(RefStruct_1_0.class.isAssignableFrom(returnType)) {
                //
                // Structs
                //
                List<Object> iargs = new ArrayList<Object>();
                if(args != null) {
                    for(int i = 0; i < args.length; i++) {
                        iargs.add(args[i]);
                    }
                }
                return this.delegation.refCreateStruct(
                    this.getQualifiedMofName(
                        qualifiedPackageName.substring(0, qualifiedPackageName.lastIndexOf(":")),
                        cciName
                    ), 
                    iargs
                );
            } else if(
                //
                // Queries
                //
                AnyTypePredicate.class.isAssignableFrom(returnType) ||
                ComparableTypePredicate.class.isAssignableFrom(returnType) ||
                MatchableTypePredicate.class.isAssignableFrom(returnType) ||
                MultivaluedFeaturePredicate.class.isAssignableFrom(returnType) ||
                PartiallyOrderedTypePredicate.class.isAssignableFrom(returnType) ||
                ResourceIdentifierTypePredicate.class.isAssignableFrom(returnType) ||
                StringTypePredicate.class.isAssignableFrom(returnType)
            ) {
                return this.delegation.refCreateFilter(
                    this.getQualifiedMofName(
                        qualifiedPackageName.substring(0, qualifiedPackageName.lastIndexOf(":")),
                        cciName.endsWith("Query") 
                            ? cciName.substring(0, cciName.indexOf("Query")) 
                            : cciName
                        ), 
                    null, 
                    null
                );
            }
        }        
        throw new UnsupportedOperationException(methodName);
    }
    
    //-----------------------------------------------------------------------
    // Members
    //-----------------------------------------------------------------------
    protected final RefPackage_1 delegation;
    protected final static Map<String,String> qualifiedMofNames = new HashMap<String,String>();
    
}

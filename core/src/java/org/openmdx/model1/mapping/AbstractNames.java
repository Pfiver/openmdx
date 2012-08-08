/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: AbstractNames.java,v 1.2 2008/02/15 17:24:06 hburger Exp $
 * Description: Abstract Names 
 * Revision:    $Revision: 1.2 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/02/15 17:24:06 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2006-2007, OMEX AG, Switzerland
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
package org.openmdx.model1.mapping;


import org.openmdx.model1.mapping.java.Identifier;


/**
 * Names
 */
abstract class AbstractNames {

    /**
     * Avoid instantiation 
     */
    protected AbstractNames(
    ){
        super();
    }

    /**
     * Evaluate accesor name
     */
    public static String openmdx2AccessorName (
        String featureName,
        boolean forQuery,
        boolean forBoolean,
        boolean singleValued
    ){

        boolean flag = forBoolean & singleValued;
        return Identifier.OPERATION_NAME.toIdentifier(
            featureName,
            flag ? "is" : null, // removablePrefix
            forQuery ? (flag ? "is" : "get") : "set", // prependablePrefix
            null
, null // appendableSuffix
        );
    }

    /**
     * Append a namespace element
     * 
     * @param target
     * @param source
     */
    public static StringBuffer openmdx2PackageName(
        StringBuffer target,
        String source
    ){

        return target.append(
                Identifier.CLASS_PROXY_NAME.toIdentifier(
                        source,
                        null, // removablePrefix, 
                        null, // prependablePrefix, 
                        null
, "package" //appendableSuffix
                )
        );
    }

    /**
     * Append a namespace element
     * 
     * @param target
     * @param source
     */
    public static StringBuffer openmdx2NamespaceElement(
        StringBuffer target,
        String source
    ){

        return target.append(
                Identifier.PACKAGE_NAME.toIdentifier(
                        source,
                        null, // removablePrefix, 
                        null, // prependablePrefix, 
                        null
, null //appendableSuffix
                )
        );
    }

    /**
     * Capitalize the first character
     */
    public static String capitalize(
        String name
    ){
        char start = name.charAt(0);
        return Character.isLowerCase(start) ?
           Character.toUpperCase(start) + name.substring(1) :
           name;
    }

    /**
     * Uncapitalize the first character
     */
    public static String uncapitalize(
        String name
    ){
        char start = name.charAt(0);
        return Character.isUpperCase(start) ?
           Character.toLowerCase(start) + name.substring(1) :
           name;
    }

    /**
     * Tells whether a character is not significant for the MOF to Java name
     * mapping
     * 
     * @param character the character to be tested
     * 
     * @return <code>true</code> if the character is not significant
     */
    public static boolean isNotSignificant(
        char character
    ){
        return
            character == '-' || // hyphen
            character == '_' || // underscore
            character == ' ' || //
            character == '\r' || // CR
            character == '\n' || // LF
            character == '\t' || // HT
            character == '\u000b'; // VT                         
    }

}

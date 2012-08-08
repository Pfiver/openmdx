/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: Throwables.java,v 1.5 2008/09/10 08:55:22 hburger Exp $
 * Description: Throwables
 * Revision:    $Revision: 1.5 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/10 08:55:22 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2004-2006, OMEX AG, Switzerland
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
 * This product includes or is based on software developed by other
 * organizations as listed in the NOTICE file.
 */
package org.openmdx.kernel.exception;

import org.openmdx.kernel.exception.BasicException.Parameter;
import org.openmdx.kernel.log.SysLog;

/**
 * JRE dependent exception handling methods. 
 */
public class Throwables {

    private Throwables() {
        // Avoid instantiation 
    }

    /**
     * Log the throwable at warning level.
     * 
     * @param throwable
     * 
     * @return th throwable
     */
    public static Throwable log(
        Throwable throwable
    ){
        Exception stackedException = BasicException.toStackedException(throwable);
        SysLog.warning(stackedException.getMessage(), stackedException.getCause());
        return throwable;
    }

    /**
     * Set a <code>Throwable</code>'s exception stack.
     *
     * @param throwable A throwable from which the backtrace and other exception
     * information is used. 
     * @param cause An embedded exception
     * @param exceptionDomain An exception domain. A null objects references
     * the default exception domain with negative exception codes only.
     * @param exceptionCode  An exception code. Negative codes describe common
     * exceptions codes. Positive exception codes are specific for a given
     * exception domain.
     * @param parameters  Any exception parameters, maybe <code>null</code>
     * @param description The description, maybe <code>null</code> in which
     * case the throwable's message is used
     * @deprecated Use {@link #initCause(Throwable,Throwable,String,int,String,BasicException.Parameter[])} instead
     */
    public static Throwable initCause (
        Throwable throwable,
        Throwable cause,
        String exceptionDomain,
        int exceptionCode,
        BasicException.Parameter[] parameters,
        String description
    ) {
        return initCause(
            throwable,
            cause,
            exceptionDomain,
            exceptionCode,
            description,
            parameters);
    }

    /**
     * Set a <code>Throwable</code>'s exception stack.
     *
     * @param throwable A throwable from which the backtrace and other exception
     * information is used. 
     * @param cause An embedded exception
     * @param exceptionDomain An exception domain. A null objects references
     * the default exception domain with negative exception codes only.
     * @param exceptionCode  An exception code. Negative codes describe common
     * exceptions codes. Positive exception codes are specific for a given
     * exception domain.
     * @param description The description, maybe <code>null</code> in which
     * case the throwable's message is used
     * @param parameters  Any exception parameters, maybe <code>null</code>
     */
    public static Throwable initCause (
        Throwable throwable,
        Throwable cause,
        String exceptionDomain,
        int exceptionCode,
        String description,
        Parameter... parameters
    ) {
        return throwable.initCause(
            new BasicException(
                cause,
                exceptionDomain,
                exceptionCode,
                parameters,
                description == null ? throwable.getMessage() : description,
                throwable
            )
        );
    }

    /**
     * Returns the cause belonging to a specific exception domain.
     * 
     * @param   exceptionDomain
     *          the desired exception domain,
     *          or <code>null</code> to retrieve the initial cause.
     *
     * @return  Either the cause belonging to a specific exception domain
     *          or the initial cause if <code>exceptionDomain</code> is
     *          <code>null</code>.  
     */
    @SuppressWarnings("deprecation")
    public static BasicException getCause(
        Throwable throwable,
        String exceptionDomain
    ){
        BasicException exceptionStack = null;
        Cause: for(
            Throwable cursor = throwable;
            cursor != null;
            cursor = cursor.getCause()
        ){
            if(cursor instanceof BasicException) {
                exceptionStack = (BasicException) cursor;
            } else if(cursor instanceof BasicException.Wrapper) {
                exceptionStack = ((BasicException.Wrapper)cursor).getExceptionStack();
            } else {
                continue Cause; 
            }
            if(
                exceptionDomain != null && (
                    exceptionDomain.equals(exceptionStack.getExceptionDomain()) || ( 
                        exceptionDomain.equals(BasicException.Code.DEFAULT_DOMAIN) &&
                        exceptionStack.getExceptionCode() <= 0
                    )
                )
            ) return exceptionStack;
        }
        return exceptionDomain == null ? exceptionStack : null;
    }

}

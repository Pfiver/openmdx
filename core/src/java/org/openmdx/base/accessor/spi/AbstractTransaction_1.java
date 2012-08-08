/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Name:        $Id: AbstractTransaction_1.java,v 1.10 2011/11/25 14:51:58 hburger Exp $
 * Description: AbstractTransaction_1 
 * Revision:    $Revision: 1.10 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2011/11/25 14:51:58 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2009-2011, OMEX AG, Switzerland
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
package org.openmdx.base.accessor.spi;

import javax.jdo.JDOException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOHelper;
import javax.jdo.Transaction;
import javax.jdo.spi.PersistenceCapable;
import javax.transaction.Synchronization;

import org.openmdx.base.accessor.rest.spi.Synchronization_2_0;
import org.openmdx.base.persistence.cci.UnitOfWork;
import org.openmdx.kernel.exception.BasicException;

/**
 * AbstractTransaction_1
 */
public abstract class AbstractTransaction_1
    implements Transaction, UnitOfWork, Synchronization_2_0 {

    /**
     * Provide the delegate
     * 
     * @return the delegate
     */
    protected abstract Transaction getDelegate();
    
    /**
     * Marshal a JDOException's failedObject instances
     * 
     * @param source a JDOException
     * 
     * @return the marshalled JDOException
     */
    protected JDOException marshal(
        JDOException source
    ){
        if(source instanceof JDOFatalInternalException) {
            return source;
        } else {
            JDOException target = source;
            Object failedObject = source.getFailedObject();
            if(failedObject instanceof PersistenceCapable) {
                Object objectId = JDOHelper.getObjectId(failedObject);
                try {
                    target = source.getClass().getConstructor(
                        String.class,
                        Throwable[].class,
                        Object.class
                    ).newInstance(
                        source.getMessage(),
                        source.getNestedExceptions(),
                        getPersistenceManager().getObjectById(objectId)
                    );
                } catch (Exception exception) {
                    try {
                        target = source.getClass().getConstructor(
                            String.class,
                            Throwable[].class
                        ).newInstance(
                            source.getMessage() + " (" + objectId + ")",
                            source.getNestedExceptions()
                        );
                    } catch (Exception retry) {
                        throw BasicException.initHolder(
                            new JDOFatalInternalException(
                                "Could not marshal JDOException with failed object",
                                BasicException.newEmbeddedExceptionStack(
                                    source,
                                    BasicException.Code.DEFAULT_DOMAIN,
                                    BasicException.Code.TRANSFORMATION_FAILURE,
                                    new BasicException.Parameter("objectId", objectId)
                                )
                           )
                        );
                    }
                }
            }
            Throwable[] nestedExceptions = target.getNestedExceptions();
            if(nestedExceptions != null) {
                for(int i = 0; i < nestedExceptions.length; i++) {
                    Throwable nestedSource = nestedExceptions[i];
                    if(nestedSource instanceof JDOException) {
                        try {
                            JDOException nestedTarget = marshal((JDOException)nestedSource);
                            if(nestedTarget != nestedSource) {
                                nestedExceptions[i] = nestedTarget;
                            }
                        } catch (Exception forget) {
                            marshal((JDOException)nestedSource);
                        }
                    }
                }
            }
            return target;
        }
    }
    
    public void begin() {
        this.getDelegate().begin();
    }

    public void commit() {
        try {
            this.getDelegate().commit();
        } catch (JDOException exception) {
            throw this.marshal(exception);
        }
    }

    public String getIsolationLevel(
    ) {
        return this.getDelegate().getIsolationLevel();
    }

    public boolean getNontransactionalRead(
    ) {
        return this.getDelegate().getNontransactionalRead();
    }

    public boolean getNontransactionalWrite(
    ) {
        return this.getDelegate().getNontransactionalWrite();
    }

    public boolean getOptimistic(
    ) {
        return this.getDelegate().getOptimistic();
    }

    public boolean getRestoreValues(
    ) {
        return this.getDelegate().getRestoreValues();
    }

    public boolean getRetainValues(
    ) {
        return this.getDelegate().getRetainValues();
    }

    public boolean getRollbackOnly(
    ) {
        return this.getDelegate().getRollbackOnly();
    }

    public Synchronization getSynchronization(
    ) {
        return this.getDelegate().getSynchronization();
    }

    public boolean isActive(
    ) {
        return this.getDelegate().isActive();
    }

    public void rollback(
    ) {
        this.getDelegate().rollback();
    }

    public void setIsolationLevel(
        String level
    ) {
        this.getDelegate().setIsolationLevel(level);
    }

    public void setNontransactionalRead(
        boolean nontransactionalRead
    ) {
        this.getDelegate().setNontransactionalRead(nontransactionalRead);
    }

    public void setNontransactionalWrite(
        boolean nontransactionalWrite
    ) {
        this.getDelegate().setNontransactionalWrite(nontransactionalWrite);
    }

    public void setOptimistic(
        boolean optimistic
    ) {
        this.getDelegate().setOptimistic(optimistic);
    }

    public void setRestoreValues(
        boolean restoreValues
    ) {
        this.getDelegate().setRestoreValues(restoreValues);
    }

    public void setRetainValues(
        boolean retainValues
    ) {
        this.getDelegate().setRetainValues(retainValues);
    }

    public void setRollbackOnly(
    ) {
        this.getDelegate().setRollbackOnly();
    }

    public void setSynchronization(
        Synchronization sync
    ) {
        this.getDelegate().setSynchronization(sync);
    }

    
    //-----------------------------------------------------------------------
    // Implements Synchronization
    //-----------------------------------------------------------------------
    
//  @Override
    public void afterCompletion(
        int status
    ) {
        ((Synchronization)this.getDelegate()).afterCompletion(status);        
    }

//  @Override
    public void beforeCompletion(
    ) {
        ((Synchronization)this.getDelegate()).beforeCompletion();                
    }

    
    //-----------------------------------------------------------------------
    // Implements UnitOfWork
    //-----------------------------------------------------------------------

    /* (non-Javadoc)
	 * @see org.openmdx.base.persistence.cci.UnitOfWork#setForgetOnly()
	 */
	public void setForgetOnly() {
        ((UnitOfWork)this.getDelegate()).setForgetOnly();                
	}

	/* (non-Javadoc)
	 * @see org.openmdx.base.persistence.cci.UnitOfWork#isForgetOnly()
	 */
	public boolean isForgetOnly() {
        return ((UnitOfWork)this.getDelegate()).isForgetOnly();                
	}

	
    //-----------------------------------------------------------------------
    // Implements Synchronization_2_0
    //-----------------------------------------------------------------------

	/* (non-Javadoc)
     * @see org.openmdx.base.accessor.rest.spi.Synchronization_2_0#afterBegin()
     */
//  @Override
    public void afterBegin() {
        ((Synchronization_2_0)this.getDelegate()).afterBegin();                
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.accessor.rest.spi.Synchronization_2_0#clear()
     */
//  @Override
    public void clear() {
        ((Synchronization_2_0)this.getDelegate()).clear();                
    }
   
    /**
     * Retrieve a delegate of the given type
     * 
     * @param type
     * @param transaction
     * 
     * @return the delegate of the given type
     * 
     * @exception NullPointerException if either argument is <code>null</code>
     * @exception IllegalArgumentException if the transaction has no delegate of the given type
     */
    public static <T> T getDelegate(
    	Class<T> type,
    	Transaction transaction
    ){
    	Transaction current = transaction;
    	while(!type.isInstance(current)) {
    		if(current instanceof AbstractTransaction_1) {
        		current = ((AbstractTransaction_1)current).getDelegate();
    		} else {
    	    	throw new IllegalArgumentException(
	        		"The given transaction has no delegate of type " + type.getName() + ": " +  transaction.getClass().getName()
	        	);
    		}
    	}
		return type.cast(current);
    }
    
}

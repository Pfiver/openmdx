/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: LightweightXAConnection.java,v 1.7 2008/09/10 08:55:24 hburger Exp $
 * Description: Lightweight XAConnection
 * Revision:    $Revision: 1.7 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/10 08:55:24 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2005, OMEX AG, Switzerland
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
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
 * This product includes software developed by the Apache Software
 * Foundation (http://www.apache.org/).
 */
package org.openmdx.kernel.application.container.spi.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.openmdx.kernel.callback.CloseCallback;
import org.openmdx.kernel.exception.BasicException;
import org.openmdx.kernel.exception.Throwables;





/**
 * Lightweight XAConnection
 */
@SuppressWarnings("unchecked")
public class LightweightXAConnection
implements XAConnection, CloseCallback
{

    /**
     * Constructor
     * 
     * @param connection
     */
    public LightweightXAConnection(
        Connection connection
    ){
        this.managedConnection = connection;
    }

    /**
     * The connection to be re-used.
     */
    Connection managedConnection;

    /**
     * The lightweight resource supports one-phase commit only.
     */
    private final XAResource lightweightResource = new LightweightResource();


    //------------------------------------------------------------------------
    // Implements XAConnection
    //------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see javax.sql.XAConnection#getXAResource()
     */
    public XAResource getXAResource(
    ) throws SQLException {
        return this.lightweightResource;
    }


    //------------------------------------------------------------------------
    // Implements PooledConnection
    //------------------------------------------------------------------------

    /**
     * The registered connection event listeners
     */
    private Set connectionEventListeners = null;

    /* (non-Javadoc)
     * @see javax.sql.PooledConnection#addConnectionEventListener(javax.sql.ConnectionEventListener)
     */
    public synchronized void addConnectionEventListener(ConnectionEventListener listener) {
        (
                this.connectionEventListeners == null ?
                    this.connectionEventListeners = new HashSet() :
                        this.connectionEventListeners
        ).add(listener);
    }

    /* (non-Javadoc)
     * @see javax.sql.PooledConnection#close()
     */
    public void close() throws SQLException {
        if(this.managedConnection != null) try {
            this.managedConnection.close();
        } finally {
            this.managedConnection = null;
        }
    }

    /* (non-Javadoc)
     * @see javax.sql.PooledConnection#getConnection()
     */
    public Connection getConnection() throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        connection.setDelegate(this, this.managedConnection);
        return connection;
    }

    /* (non-Javadoc)
     * @see javax.sql.PooledConnection#removeConnectionEventListener(javax.sql.ConnectionEventListener)
     */
    public synchronized void removeConnectionEventListener(
        ConnectionEventListener listener
    ) {
        if(this.connectionEventListeners != null) {
            this.connectionEventListeners.remove(listener);
        }
    }


    //------------------------------------------------------------------------
    // Implements CloseCallback
    //------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see org.openmdx.kernel.callback.CloseCallback#postClose(java.lang.Object)
     */
    public void postClose(
        Object closed
    ) {
        if(this.connectionEventListeners != null) {
            ConnectionEvent event = new ConnectionEvent(this);
            for(
                    Iterator i = this.connectionEventListeners.iterator();
                    i.hasNext();
            ) ((ConnectionEventListener)i.next()).connectionClosed(event);
        }
    }


    //------------------------------------------------------------------------
    // Class Resource Manager
    //------------------------------------------------------------------------

    /**
     * The LightweightResource class accepts onePhase commits only
     * because it delegates to the managed connection's LocalTransaction
     * interface.
     */
    class LightweightResource implements XAResource {

        public void commit(
            Xid xid,
            boolean onePhase
        ) throws XAException {
            if(onePhase) try {
                managedConnection.commit();
            } catch (SQLException exception) {
                throw (XAException) Throwables.initCause(
                    new XAException(XAException.XAER_RMERR),
                    exception,
                    BasicException.Code.DEFAULT_DOMAIN,
                    BasicException.Code.TRANSACTION_FAILURE,
                    "Local transaction commit failed",
                    new BasicException.Parameter("xid", xid),
                    new BasicException.Parameter("onePhase", onePhase)
                );
            } else throw (XAException) Throwables.log(
                Throwables.initCause(
                    new XAException(XAException.XAER_PROTO),
                    null,
                    BasicException.Code.DEFAULT_DOMAIN,
                    BasicException.Code.TRANSACTION_FAILURE,
                    "Two-phase commit not supported by " + LightweightResource.class.getName(),
                    new BasicException.Parameter("xid", xid),
                    new BasicException.Parameter("onePhase", onePhase)
                )
            );

        }

        public void end(
            Xid xid,
            int flag
        ) throws XAException {
            switch(flag) {
                case XAResource.TMFAIL:
                    try {
                        managedConnection.rollback();
                    } catch (SQLException exception) {
                        // ignore
                    }
                case XAResource.TMSUCCESS:
                case XAResource.TMSUSPEND:
            }
        }

        public void forget(
            Xid xid
        ) throws XAException {
            //
        }

        public int getTransactionTimeout() throws XAException {
            return 0;
        }

        public boolean isSameRM(XAResource that) throws XAException {
            return this == that;
        }

        public int prepare(Xid xid) throws XAException {
            throw (XAException) Throwables.log(
                Throwables.initCause(
                    new XAException(XAException.XAER_PROTO),
                    null,
                    BasicException.Code.DEFAULT_DOMAIN,
                    BasicException.Code.TRANSACTION_FAILURE,
                    "Two-phase commit not supported by " + LightweightResource.class.getName(),
                    new BasicException.Parameter("xid", xid)
                )
            );
        }

        public Xid[] recover(int arg0) throws XAException {
            return new Xid[]{};
        }

        public void rollback(Xid xid) throws XAException {
            try {
                managedConnection.rollback();
            } catch (SQLException exception) {
                throw (XAException) Throwables.log(
                    Throwables.initCause(
                        new XAException(XAException.XAER_RMERR),
                        exception,
                        BasicException.Code.DEFAULT_DOMAIN,
                        BasicException.Code.TRANSACTION_FAILURE,
                        "Local transaction rollback failed",
                        new BasicException.Parameter("xid", xid)
                    )
                );
            }
        }

        public boolean setTransactionTimeout(
            int timeout
        ) throws XAException {
            return false;
        }

        public void start(
            Xid xid,
            int flags
        ) throws XAException {
            switch (flags) {
                case XAResource.TMNOFLAGS:
                    try {
                        managedConnection.setAutoCommit(false);
                    } catch (SQLException exception) {
                        throw (XAException) Throwables.log(
                            Throwables.initCause(
                                new XAException(XAException.XAER_RMERR),
                                exception,
                                BasicException.Code.DEFAULT_DOMAIN,
                                BasicException.Code.TRANSACTION_FAILURE,
                                "Local transaction start failed",
                                new BasicException.Parameter("xid", xid)
                            )
                        );

                    }
                case XAResource.TMJOIN:
                case XAResource.TMRESUME:
            }
        }

    }
}

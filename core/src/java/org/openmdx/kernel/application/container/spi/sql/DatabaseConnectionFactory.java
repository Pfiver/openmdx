/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: DatabaseConnectionFactory.java,v 1.8 2008/09/10 08:55:24 hburger Exp $
 * Description: Database Connection Factory
 * Revision:    $Revision: 1.8 $
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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.security.PasswordCredential;
import javax.sql.DataSource;

import org.openmdx.kernel.application.container.lightweight.ShareableConnectionManager;
import org.openmdx.kernel.exception.BasicException;
import org.openmdx.kernel.exception.Throwables;


/**
 * Database Connection Factory
 */
@SuppressWarnings("unchecked")
public class DatabaseConnectionFactory
implements DataSource
{

    /**
     * Constructor
     * 
     * @param connectionManager
     * @param managedConnectionFactory
     * @param connectionRequestInfo
     * 
     * @throws ResourceException
     */
    DatabaseConnectionFactory(
        ConnectionManager connectionManager,
        ManagedConnectionFactory managedConnectionFactory,
        ConnectionRequestInfo connectionRequestInfo
    ){
        this.connectionManager = connectionManager == null ? new ShareableConnectionManager(
            Collections.EMPTY_SET,
            DatabaseConnection.class
        ) : connectionManager;
            this.managedConnectionFactory = managedConnectionFactory;
            this.connectionRequestInfo = connectionRequestInfo;
    }


    /**
     * The database connection factory's connection manager
     */
    private final ConnectionManager connectionManager;

    /**
     * The database connection factory's managed connection factory
     */
    private final ManagedConnectionFactory managedConnectionFactory;

    /**
     * The database connection factory's connection request info
     */
    private final ConnectionRequestInfo connectionRequestInfo;

    /**
     * The database connection factory's log writer
     */
    private PrintWriter logWriter = null;


    //------------------------------------------------------------------------
    // Extends Object
    //------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(
    ) {
        return getClass().getName() + ": " + this.connectionRequestInfo;
    }


    //------------------------------------------------------------------------
    // Implements DataSource
    //------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection()
     */
    public Connection getConnection() throws SQLException {
        try {
            return (Connection) this.connectionManager.allocateConnection(
                this.managedConnectionFactory,
                this.connectionRequestInfo
            );
        } catch (ResourceException exception) {
            throw log (
                Throwables.initCause(
                    new SQLException(
                        exception.getMessage()
                    ),
                    exception,
                    BasicException.Code.DEFAULT_DOMAIN,
                    BasicException.Code.GENERIC,
                    null
                )
            );
        }
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
     */
    public synchronized Connection getConnection(
        String username,
        String password
    ) throws SQLException {
        try {
            if(this.connectionManager instanceof ShareableConnectionManager){
                return (Connection) new ShareableConnectionManager(
                    Collections.singleton(
                        new PasswordCredential(
                            username,
                            password.toCharArray()
                        )
                    ),
                    DatabaseConnection.class
                ).allocateConnection(
                    this.managedConnectionFactory,
                    this.connectionRequestInfo
                );
            } else {
                throw new NotSupportedException("Re-authentication not supported");
            }
        } catch (ResourceException exception) {
            throw log (
                Throwables.initCause(
                    new SQLException(
                        exception.getMessage()
                    ),
                    exception,
                    BasicException.Code.DEFAULT_DOMAIN,
                    BasicException.Code.GENERIC,
                    null
                )
            );
        }
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getLogWriter()
     */
    public PrintWriter getLogWriter() throws SQLException {
        return this.logWriter;
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getLoginTimeout()
     */
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
     */
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        this.logWriter = logWriter;
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#setLoginTimeout(int)
     */
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    private SQLException log(
        Throwable sqlException
    ){
        if(this.logWriter != null) ((BasicException)sqlException.getCause()).printStack(
            sqlException,
            this.logWriter,
            true
        );
        return (SQLException) sqlException;
    }



    //------------------------------------------------------------------------
    // Since JRE 6
    //------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    public boolean isWrapperFor(
        Class<?> iface
    ) throws SQLException {
        return
        iface.isAssignableFrom(ConnectionManager.class) ||
        iface.isAssignableFrom(ManagedConnectionFactory.class) ||
        iface.isAssignableFrom(ConnectionRequestInfo.class);
    }

    /* (non-Javadoc)
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    public <T> T unwrap(
        Class<T> iface
    ) throws SQLException {
        return (T) (
                iface.isAssignableFrom(ConnectionManager.class) ? this.connectionManager :
                    iface.isAssignableFrom(ManagedConnectionFactory.class) ? this.managedConnectionFactory :
                        iface.isAssignableFrom(ConnectionRequestInfo.class) ? this.connectionRequestInfo :
                            null
        );
    }



}

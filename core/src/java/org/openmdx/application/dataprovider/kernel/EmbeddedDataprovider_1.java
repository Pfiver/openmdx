/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: Embedded Data Provider
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2009-2013, OMEX AG, Switzerland
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
package org.openmdx.application.dataprovider.kernel;

import javax.resource.ResourceException;
import javax.resource.cci.Interaction;
import javax.resource.spi.ResourceAllocationException;
import javax.sql.DataSource;

import org.openmdx.application.cci.ConfigurationProvider_1_0;
import org.openmdx.application.configuration.Configuration;
import org.openmdx.application.dataprovider.cci.SharedConfigurationEntries;
import org.openmdx.application.dataprovider.spi.Layer_1;
import org.openmdx.application.spi.StandardConfigurationProvider;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.resource.cci.ConnectionFactory;
import org.openmdx.base.resource.spi.Port;
import org.openmdx.base.resource.spi.ResourceExceptions;
import org.openmdx.base.rest.cci.RestConnection;
import org.openmdx.kernel.exception.BasicException;

/**
 * Embedded Dataprovider Bean
 */
public class EmbeddedDataprovider_1 implements Port<RestConnection> {

    /**
     * The configuration URL
     */
    private String configuration;

    /**
     * The shareable data provider
     */
    private Layer_1 delegate;
    
    /**
     * The data source URL
     */
    private String datasourceName;
    
    /**
     * The data source
     * 
	 * @deprecated will not be supported by the dataprovider 2 stack
	 */
	 @Deprecated
    private DataSource datasource;
    
    /**
     * The connections' factory
     * 
	 * @deprecated will not be supported by the dataprovider 2 stack
	 */
	 @Deprecated
    private ConnectionFactory dataprovider;

    /**
     * Retrieve the data source name.
     *
     * @return Returns the data source name.
     */
    public String getDatasourceName() {
        return this.datasourceName;
    }

    /**
     * Set the data source name.
     * 
     * @param datasourceName The data source name to set.
     */
    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

	 /**
	  * Set the configuration URL
	  * 
	  * @param configuration The configuration URL to set.
	  */
	 public void setConfiguration(String configuration) {
		 this.configuration = configuration;
	 }
	 
	 /**
	  * Retrieve the configuration URL
	  *
	  * @return Returns the configuration URL
	  */
	 public String getConfiguration() {
		 return this.configuration;
	 }
	 
    /**
     * Set delegate.
     * 
     * @param delegate The delegate to set.
     */
    public void setDelegate(
        Layer_1 delegate
    ) {
        this.delegate = delegate;
    }

    /**
     * Set the delegate up
     * 
     * @return the delegate
     * 
     * @throws ResourceException
     */
    protected Port<RestConnection> getDelegate(
    ) throws ResourceException {
        if(this.delegate == null) try {
            ConfigurationProvider_1_0 configurationProvider = new StandardConfigurationProvider(
            	getConfiguration()
            );
            Configuration configuration = new Configuration();
            propagateDataprovider(configuration);
            if(getDatasourceName() != null) {
                configuration.values(
                    SharedConfigurationEntries.DATABASE_CONNECTION_FACTORY_NAME
                ).put(
                    Integer.valueOf(0),
                    getDatasourceName()
                );
            }
            this.delegate = new Dataprovider_1(
                configuration,
                configurationProvider
            ).getDelegate();
        } catch (ServiceException exception) {
            throw ResourceExceptions.initHolder(
                new ResourceAllocationException(
                    "Dataprovider provider could not be set up",
                    BasicException.newEmbeddedExceptionStack(
                        exception,
                        BasicException.Code.DEFAULT_DOMAIN,
                        BasicException.Code.ACTIVATION_FAILURE
                    )
                )
            );
        }
        return this.delegate;
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.rest.spi.RestPlugIn#getInteraction(javax.resource.cci.Connection)
     */
    @Override
    public Interaction getInteraction(
		RestConnection connection
    ) throws ResourceException {
    	return this.getDelegate().getInteraction(connection);
    }
    
	/**
	 * @deprecated will not be supported by the dataprovider 2 stack
	 */
	 @Deprecated
	private void propagateDataprovider(Configuration configuration) {
		configuration.values(
		    SharedConfigurationEntries.DATAPROVIDER_CONNECTION_FACTORY
		).put(
		    Integer.valueOf(0),
		    getDataprovider()
		);
	}
    
    /**
     * Retrieve the data source.
     *
	 * @return the data source
     * 
	 * @deprecated will not be supported by the dataprovider 2 stack
	 */
	 @Deprecated
	public DataSource getDatasource() {
		return datasource;
	}

	/**
	 * Set the data source
	 * 
	 * @param datasource the datasource to set
     * 
	 * @deprecated will not be supported by the dataprovider 2 stack
	 */
	 @Deprecated
	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

    /**
     * Retrieve dataprovider.
     *
     * @return Returns the dataprovider.
     * 
	 * @deprecated will not be supported by the dataprovider 2 stack
	 */
	 @Deprecated
    public ConnectionFactory getDataprovider() {
        return this.dataprovider;
    }
    
    /**
     * Set dataprovider.
     * 
     * @param dataprovider The dataprovider to set.
     * 
	 * @deprecated will not be supported by the dataprovider 2 stack
	 */
	 @Deprecated
    public void setDataprovider(ConnectionFactory dataprovider) {
        this.dataprovider = dataprovider;
    }

}

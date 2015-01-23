/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: Stream Operation Aware Layer_1_0 Implementation
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2005-2013, OMEX AG, Switzerland
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
package org.openmdx.application.dataprovider.spi;

import javax.resource.ResourceException;
import javax.resource.cci.Interaction;
import javax.resource.cci.MappedRecord;

import org.openmdx.application.configuration.Configuration;
import org.openmdx.application.dataprovider.cci.DataproviderRequest;
import org.openmdx.application.dataprovider.cci.ServiceHeader;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.naming.Path;
import org.openmdx.base.resource.spi.RestInteractionSpec;
import org.openmdx.base.rest.cci.MessageRecord;
import org.openmdx.base.rest.cci.RestConnection;
import org.openmdx.base.rest.spi.Facades;
import org.openmdx.base.rest.spi.Object_2Facade;
import org.openmdx.kernel.exception.BasicException;
import org.w3c.cci2.SparseArray;


/**
 * Stream operation aware Layer_1_0 implementation.
 * <p>
 * This class dispatches operation requests to<ul>
 * <li>getStreamOperation
 * <li>otherOperation
 * </ul>
 */
public abstract class OperationAwareLayer_1 extends Layer_1 {

    /**
     * Constructor 
     */
    protected OperationAwareLayer_1(
    ) {
        super();
    }
    
    //-----------------------------------------------------------------------
    @Override
    public Interaction getInteraction(
        RestConnection connection
    ) throws ResourceException {
        return new LayerInteraction(connection);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Retrieves a configuration value
     * 
     * @param source
     * @param key
     * @param defaultValue
     * 
     * @return the configuration value or its default
     */
    protected String getConfigurationValue(
        String key,
        String defaultValue
    ){
        Configuration configuration = getConfiguration();
        return configuration.containsEntry(key) && !configuration.values(key).isEmpty() ?
            ((String)configuration.values(key).get(Integer.valueOf(0))) :
                defaultValue;
    }

    //-----------------------------------------------------------------------
    /**
     * Retrieves a configuration value
     * 
     * @param source
     * @param key
     * @param defaultValue
     * 
     * @return the configuration value or its default
     */
    protected int getConfigurationValue(
        String key,
        int defaultValue
    ){
        Configuration configuration = getConfiguration();
        if(configuration.containsEntry(key)) {
            SparseArray<Object> values = configuration.values(key);
            if(!values.isEmpty()) {
                Object value = values.get(Integer.valueOf(0));
                if(value instanceof Number) {
                    return ((Number)value).intValue();
                } else if (value instanceof String){
                    return Integer.parseInt((String)value);
                } else if (value != null) {
                    throw BasicException.initHolder(
                        new IllegalArgumentException(
                            "Inappropriate configuration value",
                            BasicException.newEmbeddedExceptionStack(
                                BasicException.Code.DEFAULT_DOMAIN,
                                BasicException.Code.BAD_PARAMETER,
                                new BasicException.Parameter("value", value),
                                new BasicException.Parameter("supported", String.class.getName(), Integer.class.getName()),
                                new BasicException.Parameter("value", value.getClass().getName())
                            )
                        )
                    );
                }
            }
        }
        return  defaultValue;
    }

    //-----------------------------------------------------------------------
    public class LayerInteraction extends Layer_1.LayerInteraction {
        
        /**
         * Constructor 
         *
         * @param connection
         * @throws ResourceException
         */
        protected LayerInteraction(
            RestConnection connection
        ) throws ResourceException {
            super(connection);
        }
        
        /* (non-Javadoc)
         * @see org.openmdx.compatibility.base.dataprovider.spi.Operation_1_0#operation(org.openmdx.compatibility.base.dataprovider.cci.ServiceHeader, org.openmdx.compatibility.base.dataprovider.cci.DataproviderRequest)
         */
        @Override
        public boolean invoke(
            RestInteractionSpec ispec, 
            MessageRecord input, 
            MessageRecord output
        ) throws ServiceException {
            ServiceHeader header = this.getServiceHeader();
            DataproviderRequest request = this.newDataproviderRequest(ispec, input);
            String operationName = request.path().getSegment(request.path().size() - 2).toClassicRepresentation();
            MappedRecord response = this.otherOperation(
                header, 
                request, 
                operationName, 
                newResponseId(input.getResourceIdentifier())
            ); 
            if(response == null) {
                super.invoke(
                    ispec, 
                    input, 
                    output
                );
            } else {
                Object_2Facade responseFacade = Facades.asObject(response);
                output.setResourceIdentifier(responseFacade.getPath());
                output.setBody(responseFacade.getValue());
            }
            return true;
        }

        /**
         * This method is overridden by a subclass if it supports otherOperation().
         * 
         * @param header
         * @param request
         * @param operation
         * @param replyPath
         * 
         * @return the reply object; or null if the request should be delegated
         * 
         * @throws ServiceException
         */
        protected MappedRecord otherOperation(
            ServiceHeader header,
            DataproviderRequest request,
            String operation, 
            Path replyPath
        ) throws ServiceException {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.NOT_SUPPORTED,
                "Operation not supported",
                new BasicException.Parameter("path", request.path()),
                new BasicException.Parameter("operation", operation)
            );
        }
        
    }

}

/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: Database_1Jdbc2.java,v 1.28 2008/09/10 08:55:19 hburger Exp $
 * Description: Database_1Jdbc2 plugin
 * Revision:    $Revision: 1.28 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/10 08:55:19 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2004, OMEX AG, Switzerland
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 * 
 * * Neither the name of the openMDX team nor the names of its
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
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
package org.openmdx.compatibility.base.dataprovider.layer.persistence.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.stream.cci.Source_1_0;
import org.openmdx.compatibility.base.exception.StackedException;
import org.openmdx.kernel.application.container.spi.rmi.ByteString;
import org.openmdx.kernel.application.container.spi.rmi.ByteStringInputStream;
import org.openmdx.kernel.exception.BasicException;
import org.openmdx.kernel.log.SysLog;
import org.openmdx.model1.accessor.basic.cci.ModelElement_1_0;
import org.openmdx.model1.code.Multiplicities;
import org.w3c.cci2.BinaryLargeObject;

/*
 * Concrete implementation of the AbstractDatabase_1 plugin using Jdbc2 driver
 * features. 
 */
//---------------------------------------------------------------------------
public class Database_1Jdbc2
extends AbstractDatabase_1 {

    //---------------------------------------------------------------------------
    PreparedStatement prepareStatement(
        Connection conn,
        String statement,
        boolean updatable
    ) throws SQLException {
        if(updatable) {
            return conn.prepareStatement(
                statement,
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE
            );
        }
        else {
            String databaseProductName = "N/A";
            try {
                DatabaseMetaData dbm = conn.getMetaData();
                databaseProductName = dbm.getDatabaseProductName();
            } catch(Exception e) {
                // ignore
            }
            boolean allowScrollSensitiveResultSet = Boolean.valueOf(this.jdbcDriverSqlProperties.getProperty(databaseProductName + ".ALLOW.SCROLLSENSITIVE.RESULTSET")).booleanValue();            
            return conn.prepareStatement(
                statement,
                allowScrollSensitiveResultSet
                ? this.resultSetType
                    : ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY
            );
        }
    }

    //---------------------------------------------------------------------------
    boolean isBlobColumnValue(
        Object val
    ) {
        return
        val instanceof byte[] ||
        val instanceof Blob;
    }

    //---------------------------------------------------------------------------
    boolean isClobColumnValue(
        Object val
    ) {
        return
        val instanceof String ||
        val instanceof Clob;
    }

    //---------------------------------------------------------------------------
    Object getBlobColumnValue(
        Object val,
        String attributeName,
        ModelElement_1_0 attributeDef
    ) throws SQLException {

        boolean isStream = attributeDef == null
        ? false
            : attributeDef.values("multiplicity").contains(Multiplicities.STREAM);

        // Blob
        if(val instanceof Blob) {
            Blob blob = (Blob)val;
            if(isStream) {
                return blob.getBinaryStream();
            }
            else {
                return blob.getBytes(1L, (int)blob.length());
            }
        }

        // byte[]
        else if(val instanceof byte[]) {
            byte[] bytes = (byte[])val;
            if(isStream) {
                return new ByteStringInputStream(
                    new ByteString(bytes)
                );
            }
            else {
                return bytes;
            }
        }
        else {
            return null;
        }
    }

    //---------------------------------------------------------------------------
    Object getClobColumnValue(
        Object val,
        String attributeName,
        ModelElement_1_0 attributeDef
    ) throws SQLException {

        boolean isStream = attributeDef == null
        ? false
            : attributeDef.values("multiplicity").contains(Multiplicities.STREAM);

        // Clob
        if(val instanceof Clob) {
            Clob clob = (Clob)val;
            if(isStream) {
                return clob.getCharacterStream();
            }
            else {
                return clob.getSubString(1L, (int)clob.length());
            }
        }    

        // String
        else if(val instanceof String) {
            String chars = (String)val;
            if(isStream) {
                return new StringReader(chars);
            }
            else {
                return chars;
            }
        }

        else {
            return null;
        }
    }

    //---------------------------------------------------------------------------
    void setClobColumnValue(
        PreparedStatement ps,
        int column,
        Object val
    ) throws SQLException, ServiceException {

        // String
        if(val instanceof String) {
            try {
                Clob clob = this.createClob(
                    new StringReader((String)val),
                    ((String)val).length()
                );
                ps.setClob(
                    column,
                    clob
                );
            }
            catch(IOException e) {
                throw new ServiceException(
                    StackedException.DEFAULT_DOMAIN,
                    StackedException.SYSTEM_EXCEPTION,
                    "Can not create Clob from value",
                    new BasicException.Parameter("value", val)
                );            
            }
        }

        // Reader
        else if(val instanceof Reader) {
            try {
                Clob clob = this.createClob(
                    (Reader)val,
                    -1L
                );
                ps.setClob(
                    column,
                    clob
                );
            }
            catch(IOException e) {
                throw new ServiceException(e);
            }
        } 

        // Clob
        else if(val instanceof Clob) {
            ps.setClob(
                column,
                (Clob)val
            );
        }

        // Not supported
        else {
            throw new ServiceException(
                StackedException.DEFAULT_DOMAIN,
                StackedException.NOT_SUPPORTED,
                "String type not supported. Supported are [String|Reader|Clob]",
                new BasicException.Parameter("type", val == null ? null : val.getClass().getName())
            );
        }
    }

    //---------------------------------------------------------------------------
    void setBlobColumnValue(
        PreparedStatement ps,
        int column,
        Object val
    ) throws SQLException, ServiceException {

        // byte[]
        if(val instanceof byte[]) {
            ps.setBytes(
                column,
                (byte[])val
            );
        }

        // InputStream
        else if(val instanceof InputStream) {
            try {
                Blob blob = this.createBlob(
                    (InputStream)val,
                    val instanceof Source_1_0 ?
                        ((Source_1_0)val).length() :
                            -1L
                );
                ps.setBinaryStream(
                    column,
                    blob.getBinaryStream(),
                    (int)blob.length()
                );
            }
            catch(IOException e) {
                throw new ServiceException(e);
            }
        } 

        // BinaryLargeObject
        else if(val instanceof BinaryLargeObject) {
            try {
                BinaryLargeObject lob = (BinaryLargeObject)val;
                Blob blob = this.createBlob(
                    lob.getContent(),
                    lob.getLength() == null ? -1L : lob.getLength()
                );
                ps.setBinaryStream(
                    column,
                    blob.getBinaryStream(),
                    (int)blob.length()
                );
            }
            catch(IOException e) {
                throw new ServiceException(e);
            }
        } 

        // Blob
        else if(val instanceof Blob) {
            ps.setBlob(
                column,
                (Blob)val
            );
        }

        // Not supported
        else {
            throw new ServiceException(
                StackedException.DEFAULT_DOMAIN,
                StackedException.NOT_SUPPORTED,
                "binary type not supported. Supported are [byte[]|InputStream|InputStream_1_0|Blob]",
                new BasicException.Parameter("type", val == null ? null : val.getClass().getName())
            );
        }
    }

    //---------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    FastResultSet setPosition(
        ResultSet rs,
        int position,
        int lastPosition,
        int lastRowCount,
        boolean isIndexed
    ) throws ServiceException, SQLException {

        boolean hasMore = rs.next();
        // do not touch rs with hasMore==false
        // DB2 reports 'Invalid operation: result set closed'
        if(!hasMore) {
            return null;
        }

        // align
        FastResultSet frs = new FastResultSet(
            this,
            rs
        );

        // forward
        if(position > 0) {
            boolean positioned = false;
            // Move forward to position by ResultSet.absolute()
            if(!isIndexed && this.supportsAbsolutePositioning) {
                try {
                    hasMore = frs.absolute(position+1);
                    positioned = true;
                } catch(SQLException e) {
                    this.supportsAbsolutePositioning = false;
                    SysLog.warning("Absolute positioning not supported. Fallback to positioning by iteration");
                }            
            }
            // Move forward to position by iterating the result set
            if(!positioned) {
                // PK format: id
                if(frs.getColumnNames().contains(OBJECT_ID)) {
                    int count = 0;  
                    String previousId = (String)frs.getObject(OBJECT_ID);
                    while(hasMore) {
                        String id = (String)frs.getObject(OBJECT_ID);
                        if(!id.equals(previousId)) {
                            count++;
                            previousId = id;
                        }
                        if(count >= position) break;
                        hasMore = frs.next();
                    }            
                }
                // PK format: rid/id
                else {
                    int count = 0;    
                    Object previousOid = frs.getObject(OBJECT_OID);
                    Object previousRid = frs.getObject(OBJECT_RID);        
                    while(hasMore) {
                        Object oid = frs.getObject(OBJECT_OID);
                        Object rid = frs.getObject(OBJECT_RID);          
                        if(
                                !oid.equals(previousOid) ||
                                (rid instanceof Comparable ? ((Comparable)rid).compareTo(previousRid) != 0 : !rid.equals(previousRid)) 
                        ) {
                            count++;
                            previousOid = oid;
                            previousRid = rid;
                        }
                        if(count >= position) break;
                        hasMore = frs.next();
                    }
                }
            }
        }    
        if(hasMore) {
            return frs;
        }
        else {
            return null;
        }
    }

    //---------------------------------------------------------------------------
    void resultSetUpdateLong(
        ResultSet rs,
        String columnName,
        long value
    ) throws SQLException {
        rs.updateLong(
            columnName,
            value
        );
    }

    //---------------------------------------------------------------------------
    void resultSetUpdateInt(
        ResultSet rs,
        String columnName,
        int value
    ) throws SQLException {
        rs.updateInt(
            columnName,
            value
        );
    }

    //---------------------------------------------------------------------------
    void resultSetUpdateString(
        ResultSet rs,
        String columnName,
        String value
    ) throws SQLException {
        rs.updateString(
            columnName,
            value
        );
    }

    //---------------------------------------------------------------------------
    void resultSetUpdateRow(
        ResultSet rs
    ) throws SQLException {
        rs.updateRow();
    }

    //---------------------------------------------------------------------------
    int resultSetGetRow(
        ResultSet rs
    ) throws SQLException {
        // not implemented yet
        return -1;
    }

    //---------------------------------------------------------------------------
    // Members
    //---------------------------------------------------------------------------
    private boolean supportsAbsolutePositioning = true;
}

//---End of File -------------------------------------------------------------

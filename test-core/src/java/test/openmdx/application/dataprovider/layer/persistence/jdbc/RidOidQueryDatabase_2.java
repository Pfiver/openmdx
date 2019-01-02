/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: RidOidQueryDatabase_2 
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2012-2014, OMEX AG, Switzerland
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
package test.openmdx.application.dataprovider.layer.persistence.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RidOidQueryDatabase_2
    extends
    org.openmdx.base.dataprovider.layer.persistence.jdbc.RidOidQueryDatabase_2
{

    /**
     * Query count for unit tests
     */
    private static volatile int queryCount = 0;
    
    /**
     * Query count for unit tests
     */
    private static volatile int updateCount = 0;
    
    
    /**
     * Retrieve queryCount.
     *
     * @return Returns the queryCount.
     */
    public static int getQueryCount() {
        return queryCount;
    }

    /**
     * Retrieve updateCount.
     *
     * @return Returns the updateCount.
     */
    public static int getUpdateCount() {
        return updateCount;
    }

    @Override
    public ResultSet executeQuery(
        PreparedStatement ps,
        String statement,
        List<?> statementParameters,
        int maxRows
    ) throws SQLException {
        queryCount++;
        return super.executeQuery(
            ps, 
            statement, 
            statementParameters,
            maxRows
        );
    }

    @Override
    public int executeUpdate(
        PreparedStatement ps,
        String statement,
        List<?> statementParameters
    ) throws SQLException {
        updateCount++;
        return super.executeUpdate(ps, statement, statementParameters);
    }

}

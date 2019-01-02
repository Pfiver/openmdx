/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Description: Test Time Zones 
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2018, OMEX AG, Switzerland
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

package org.w3c.time;

import java.util.TimeZone;

import javax.xml.datatype.DatatypeConstants;

import org.junit.Assert;
import org.junit.Test;


/**
 * Test Time Zones
 */
public class TestTimeZones {
    
    @Test
    public void whenFieldUndefinedThenDefaultTimeZone(){
        // Arrange
        final int zoneOffset = DatatypeConstants.FIELD_UNDEFINED;
        // Act
        final TimeZone timeZone = TimeZones.toTimeZone(zoneOffset);
        // Assert
        Assert.assertNotNull(timeZone);
        Assert.assertEquals(TimeZone.getDefault(), timeZone);
    }

    
    @Test
    public void when0ThenUTC(){
        // Arrange
        final int zoneOffset = 0;
        // Act
        final TimeZone timeZone = TimeZones.toTimeZone(zoneOffset);
        // Assert
        Assert.assertEquals(TimeZone.getTimeZone("UTC").getRawOffset(), timeZone.getRawOffset());
    }
    
    @Test
    public void whenPlus60ThenUTCPlus1(){
        // Arrange
        final int zoneOffset = 60;
        // Act
        final TimeZone timeZone = TimeZones.toTimeZone(zoneOffset);
        // Assert
        Assert.assertEquals("GMT+01:00", timeZone.getID());
    }

    @Test
    public void whenMinus120ThenUTCMinus2(){
        // Arrange
        final int zoneOffset = -120;
        // Act
        final TimeZone timeZone = TimeZones.toTimeZone(zoneOffset);
        // Assert
        Assert.assertEquals("GMT-02:00", timeZone.getID());
    }

}

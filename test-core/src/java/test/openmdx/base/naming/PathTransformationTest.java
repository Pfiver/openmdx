/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: Path Transformation Test
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2004-2014, OMEX AG, Switzerland
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
package test.openmdx.base.naming;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Before;
import org.junit.Test;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.naming.Path;
import org.openmdx.base.naming.PathComponent;

public class PathTransformationTest {
    
    /**
     * Add an instance variable for each part of the fixture 
     */
    protected String[] xri1;

    /**
     * Add an instance variable for each part of the fixture 
     */
    protected String[] xri2;

    /**
     * Add an instance variable for each part of the fixture 
     */
    protected String[] iri2;
    
    private static final boolean LEGACY_STRING_REPRESENTATION = Boolean.TRUE.booleanValue(); // to avoid dead code warning
    
    @Before
    public void setUp() {
        xri2 = new String[]{
            null,
            "xri://(ejb/e.jar)",
            "xri://(../../somewhere/else/e.jar)",
            "xri://www.openmdx.org/dtd/openmdx-ejb-jar_1_0.dtd",
            "xri://@openmdx*(+example)/provider/java:properties/segment/(java:comp/env)",
            "xri://@openmdx*(+example)/provider/java:properties*($(+class)/org::openmdx::base::Provider)"
        };
        xri1 = new String[]{
            null,
            "xri:(ejb/e.jar)",
            "xri:(../../somewhere/else/e.jar)",
            "xri://www.openmdx.org/dtd/openmdx-ejb-jar_1_0.dtd"
        };
        iri2 = new String[]{
            null,
            "xri://(ejb%2Fe.jar)",
            "xri://(..%2F..%2Fsomewhere%2Felse%2Fe.jar)",
            "xri://www.openmdx.org/dtd/openmdx-ejb-jar_1_0.dtd",
            "xri://@openmdx*(+example)/provider/java:properties/segment/(java:comp%2Fenv)",
            "xri://@openmdx*(+example)/provider/java:properties*($(+class)%2Forg::openmdx::base::Provider)"
        };
    }

    @Test
    public void testI2I(
    ) throws ServiceException {
        for (
            int i = 1;
            i < iri2.length;
            i++
        ) assertEquals(
            "[" + i + "]: " + iri2[i],
            iri2 [i],
            URI.create(iri2[i]).toString()
        );
    }

    @Test
    public void testCodec(
    ) throws UnsupportedEncodingException{
        for(
            int i = 1;
            i < this.xri2.length;
            i++
        ){
            assertEquals(
                xri2[i], 
                URLDecoder.decode(
                    URLEncoder.encode(xri2[i], "UTF-8"), 
                    "UTF-8"
                )
            );
        }
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testCR0003329(
    ){
        Path p = new Path("xri:@openmdx:a/b/c").getChild(new Path("xri:@openmdx:1/2/3")); 
        Path p1 = new Path(p.toXri());
        assertEquals(
            "p.toString()", 
            LEGACY_STRING_REPRESENTATION ? "a/b/c/1//2//3" : "xri://@openmdx*a/b/c/(@openmdx*1/2/3)", 
            p.toString()
        );
        assertEquals("p.toXri()", "xri:@openmdx:a/b/c/(@openmdx:1/2/3)", p.toXri());
        assertEquals("p.toUri()", "spice://a/b/c/1%2f2%2f3", p.toUri());
        assertEquals("1/2/3", p.getLastSegment().toClassicRepresentation(), p1.getLastSegment().toClassicRepresentation());
        Path p2 = new Path(new String[]{"x", "y", "a/b/c/1//2//3"});
        assertEquals(
            "p2.toString()", 
            LEGACY_STRING_REPRESENTATION ? "x/y/a//b//c//1////2////3" : "xri://@openmdx*x/y/(@openmdx*a/b/c/(@openmdx*1/2/3))", 
            p2.toString()
        );
        assertEquals("p2.toXri()", "xri:@openmdx:x/y/(@openmdx:a/b/c/(@openmdx:1/2/3))", p2.toXri());
        assertEquals("p2.toUri()", "spice://x/y/a%2fb%2fc%2f1%2f%2f2%2f%2f3", p2.toUri());
//        XRI xref = new XRI("@openmdx:1/2/3");
//        assertTrue("isAbsolute()", xref.isAbsolute());
//        assertFalse("isRelative()", xref.isRelative());
//        assertEquals("xref aurhority path", "@openmdx:1", xref.getAuthorityPath().toString());
//        assertEquals("xref xri path", "/2/3", xref.getXRIPath().toString());
    }

    @Test
    public void testCR20006216(
    ){
        PathComponent stateId = new PathComponent("sins:0:");
        assertEquals("Object Id", "sins", stateId.get(0));
        assertEquals("State Number", "0", stateId.get(1));
        assertFalse("PlaceHolder", stateId.isPlaceHolder());
        assertTrue("Private", stateId.isPrivate());        
    }
    
}

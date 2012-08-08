/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: PathTransformationTest.java,v 1.2 2008/08/21 20:17:01 hburger Exp $
 * Description: Path Transformation Test
 * Revision:    $Revision: 1.2 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/08/21 20:17:01 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2004-2008, OMEX AG, Switzerland
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
package org.openmdx.test.base.text.conversion;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.compatibility.base.naming.Path;
import org.openmdx.compatibility.base.naming.PathComponent;
import org.openmdx.compatibility.base.naming.XRI_2Marshaller;
import org.openxri.XRI;

public class PathTransformationTest extends TestCase {
    
    /**
     * Constructs a test case with the given name.
     */
    public PathTransformationTest(String name) {
        super(name);
    }
    
    /**
     * The batch TestRunner can be given a class to run directly.
     * To start the batch runner from your main you can write: 
     */
    public static void main (String[] args) {
        junit.textui.TestRunner.run (suite());
    }
    
    /**
     * A test runner either expects a static method suite as the
     * entry point to get a test to run or it will extract the 
     * suite automatically. 
     */
    public static Test suite() {
        return new TestSuite(PathTransformationTest.class);
    }

    /**
     * Add an instance variable for each part of the fixture 
     */
    protected String[] uri1;

    /**
     * Add an instance variable for each part of the fixture 
     */
    private String[] uri2;

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

    /**
     * Add an instance variable for each part of the fixture 
     */
    protected String xri1compatibility_15;
    
    /**
     * Add an instance variable for each part of the fixture 
     */
    protected String[] paths;

    /**
     * Sets up the fixture, for example, open a network connection.
     * This method is called before a test is executed.
     * 
     * See http://www.wachob.com/xriescape/index.html
     */
    protected void setUp() {
        paths = new String[]{
            null, // Must be the first entry
            "org::openmdx::preferences1/provider/Java::Properties/segment/(java::comp//env)",
            "org::openmdx::preferences1/provider/Java::Properties/segment/(java::comp)",
            "",
            "::*",
            "A::B",
            "A::B/::*",
            "org::openmdx::preferences1/provider/Java::Properties/segment/System",
            "org::openmdx::preferences1/provider/Java::Properties/segment/(+resource//application-preferences.xml)",
            "A::B/Fran\u00e7ois", // "A::B/Fran�ois"
            "A::B/provider/P::Q/segment/S.T/object/RR_1;state=0",
            "A::B/provider/P::Q/segment/S.T/object/012345;transient",
            "A::B/B::::B0//B1/C",
            "A::B/B//B0%3AB1/C",
            "A::B/provider/${PROVIDER}/segment/${SEGMENT}",
//          "A::B/ !\"$%00&'*+,-.0123456789::;<=>@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", // '(', ')', '#' & '?' require escape!
            "::*/provider/::*/segment/%",
            "A::B/provider/::org*/segment/org%",
            "A::B/provider/::org::*/segment/org::%"
        };
        uri1 = new String[]{
            null,
            "spice://org:openmdx:preferences1/provider/Java:Properties/segment/(java:comp%2fenv)",
            "spice://org:openmdx:preferences1/provider/Java:Properties/segment/(java:comp)",
            "spice:/",
            "spice://:*",
            "spice://A:B",
            "spice://A:B/:*",
            "spice://org:openmdx:preferences1/provider/Java:Properties/segment/System",
            "spice://org:openmdx:preferences1/provider/Java:Properties/segment/(+resource%2fapplication-preferences.xml)",
            "spice://A:B/Fran%c3%a7ois",
            "spice://A:B/provider/P:Q/segment/S.T/object/RR_1;state=0",
            "spice://A:B/provider/P:Q/segment/S.T/object/012345;transient",
            "spice://A:B/B%3a%3aB0%2fB1/C",
            "spice://A:B/B%2fB0%253AB1/C",
            "spice://A:B/provider/$%7bPROVIDER%7d/segment/$%7bSEGMENT%7d",
//          "spice://A:B/%20!%22$%2500&'*+,-.0123456789:;%3c=%3e@ABCDEFGHIJKLMNOPQRSTUVWXYZ%5b%5c%5d%5e_%60abcdefghijklmnopqrstuvwxyz%7b%7c%7d~",
            "spice://:*/provider/:*/segment/%",
            "spice://A:B/provider/:org*/segment/org%",
            "spice://A:B/provider/:org:*/segment/org:%"
        };
        xri1 = new String[]{
            null,
            "xri:@openmdx:org.openmdx.preferences1/provider/Java:Properties/segment/(java:comp/env)",
            "xri:@openmdx:org.openmdx.preferences1/provider/Java:Properties/segment/(java:comp)",
            "xri:@openmdx",
            "xri:@openmdx:**",
            "xri:@openmdx:A.B",
            "xri:@openmdx:A.B/**",
            "xri:@openmdx:org.openmdx.preferences1/provider/Java:Properties/segment/System",
            "xri:@openmdx:org.openmdx.preferences1/provider/Java:Properties/segment/(+resource/application-preferences.xml)",
            "xri:@openmdx:A.B/Fran\u00e7ois", // "xri:@openmdx:A.B/Fran�ois"
            "xri:@openmdx:A.B/provider/P:Q/segment/S.T/object/RR_1;state=0",
            "xri:@openmdx:A.B/provider/P:Q/segment/S.T/object/012345;transient",
            "xri:@openmdx:A.B/(@openmdx:B.B0/B1)/C",
            "xri:@openmdx:A.B/(@openmdx:B/B0%253AB1)/C",
            "xri:@openmdx:A.B/provider/$%7BPROVIDER%7D/segment/$%7BSEGMENT%7D",
//          "xri:@openmdx:A.B/%20!%22$%2500&'*+,-.0123456789:;%3C=%3E@ABCDEFGHIJKLMNOPQRSTUVWXYZ%5B%5C%5D%5E_%60abcdefghijklmnopqrstuvwxyz%7B%7C%7D~",
            "xri:@openmdx:**/provider/**/segment/***",
            "xri:@openmdx:A.B/provider/org**/segment/org***",
            "xri:@openmdx:A.B/provider/org:**/segment/org:***",
            "xri:@openmdx:A.B/(@openmdx:B/B0%3AB1)/C",
            "xri:(ejb/e.jar)",
            "xri:(../../somewhere/else/e.jar)",
            "xri://www.openmdx.org/dtd/openmdx-ejb-jar_1_0.dtd"
        };
        xri1compatibility_15 = "xri:@openmdx:*/provider/:*/segment/%";
        xri2 = new String[]{
            null,
            "xri://@openmdx*org.openmdx.preferences1/provider/Java:Properties/segment/(java:comp/env)",
            "xri://@openmdx*org.openmdx.preferences1/provider/Java:Properties/segment/(java:comp)",
            "xri://@openmdx",
            "xri://@openmdx*($..)",
            "xri://@openmdx*A.B",
            "xri://@openmdx*A.B/($..)",
            "xri://@openmdx*org.openmdx.preferences1/provider/Java:Properties/segment/System",
            "xri://@openmdx*org.openmdx.preferences1/provider/Java:Properties/segment/(+resource/application-preferences.xml)",
            "xri://@openmdx*A.B/Fran\u00e7ois", // "xri://@openmdx*A.B/Fran�ois"
            "xri://@openmdx*A.B/provider/P:Q/segment/S.T/object/RR_1;state:0",
            "xri://@openmdx*A.B/provider/P:Q/segment/S.T/object/012345;transient",
            "xri://@openmdx*A.B/(@openmdx*B.B0/B1)/C",
            "xri://@openmdx*A.B/(@openmdx*B/B0%3AB1)/C",
            "xri://@openmdx*A.B/provider/($t*ces*%24%7BPROVIDER%7D)/segment/($t*ces*%24%7BSEGMENT%7D)",
//          "xri://@openmdx*A.B/%20%21%22%24%2500&'%2A%2B,-.0123456789*;%3C:%3E%40ABCDEFGHIJKLMNOPQRSTUVWXYZ%5B%5C%5D%5E_%60abcdefghijklmnopqrstuvwxyz%7B%7C%7D~",
            "xri://@openmdx*($..)/provider/($..)/segment/($...)",
            "xri://@openmdx*A.B/provider/($.*org)*($..)/segment/($.*org)*($..)/($...)",
            "xri://@openmdx*A.B/provider/($.*org:)*($..)/segment/($.*org:)*($..)/($...)", // A::B/provider/::org::*/segment/org::%
            "xri://@openmdx*A.B/(@openmdx*B/B0%3AB1)/C",
            "xri://(ejb/e.jar)",
            "xri://(../../somewhere/else/e.jar)",
            "xri://www.openmdx.org/dtd/openmdx-ejb-jar_1_0.dtd",
            "xri://@openmdx*(+example)/provider/java:properties/segment/(java:comp/env)",
            "xri://@openmdx*(+example)/provider/java:properties*($(+class)/org::openmdx::base::Provider)"
        };
        iri2 = new String[]{
            null,
            "xri://@openmdx*org.openmdx.preferences1/provider/Java:Properties/segment/(java:comp%2Fenv)",
            "xri://@openmdx*org.openmdx.preferences1/provider/Java:Properties/segment/(java:comp)",
            "xri://@openmdx",
            "xri://@openmdx*($..)",
            "xri://@openmdx*A.B",
            "xri://@openmdx*A.B/($..)",
            "xri://@openmdx*org.openmdx.preferences1/provider/Java:Properties/segment/System",
            "xri://@openmdx*org.openmdx.preferences1/provider/Java:Properties/segment/(+resource%2Fapplication-preferences.xml)",
            "xri://@openmdx*A.B/Fran\u00e7ois", // "xri:@openmdx:A.B/Fran�ois"
            "xri://@openmdx*A.B/provider/P:Q/segment/S.T/object/RR_1;state:0",
            "xri://@openmdx*A.B/provider/P:Q/segment/S.T/object/012345;transient",
            "xri://@openmdx*A.B/(@openmdx*B.B0%2FB1)/C",
            "xri://@openmdx*A.B/(@openmdx*B%2FB0%253AB1)/C",
            "xri://@openmdx*A.B/provider/($t*ces*%2524%257BPROVIDER%257D)/segment/($t*ces*%2524%257BSEGMENT%257D)",            
//          "xri://@openmdx*A.B/%2520%2521%2522%2524%252500&'%252A%252B,-.0123456789*;%253C;%253E%2540ABCDEFGHIJKLMNOPQRSTUVWXYZ%255B%255C%255D%255E_%2560abcdefghijklmnopqrstuvwxyz%257B%257C%257D~",
            "xri://@openmdx*($..)/provider/($..)/segment/($...)",
            "xri://@openmdx*A.B/provider/($.*org)*($..)/segment/($.*org)*($..)/($...)",
            "xri://@openmdx*A.B/provider/($.*org:)*($..)/segment/($.*org:)*($..)/($...)",
            "xri://@openmdx*A.B/(@openmdx*B%2FB0%253AB1)/C",
            "xri://(ejb%2Fe.jar)",
            "xri://(..%2F..%2Fsomewhere%2Felse%2Fe.jar)",
            "xri://www.openmdx.org/dtd/openmdx-ejb-jar_1_0.dtd",
            "xri://@openmdx*(+example)/provider/java:properties/segment/(java:comp%2Fenv)",
            "xri://@openmdx*(+example)/provider/java:properties*($(+class)%2Forg::openmdx::base::Provider)"
        };
        uri2 = new String[]{
            iri2[0],
            iri2[1],
            iri2[2],
            iri2[3],
            iri2[4],
            iri2[5],
            iri2[6],
            iri2[7],
            iri2[8],
            "xri://@openmdx*A.B/Fran%C3%A7ois", // "xri:@openmdx:A.B/Fran�ois"
            iri2[10],
            iri2[11],
            iri2[12],
            iri2[13],
            iri2[14],
            iri2[15],
            iri2[16],
            iri2[17],
            iri2[18],
            iri2[19],
            iri2[20],
            iri2[21],
            iri2[22],
            iri2[23]
        };
    }

    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     */
    public void testX2P(
    ) throws ServiceException {
        for (
            int i = 1;
            i < paths.length;
            i++
        ) assertEquals(
            "[" + i + "]: " + paths[i],
            paths[i],
            new Path(xri1[i]).toString()
        );
    }

    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     */
    public void testU2P(
    ) throws ServiceException {
        for (
            int i = 1;
            i < uri1.length;
            i++
        ) assertEquals(
            "[" + i + "]: " + paths[i],
            paths[i],
            new Path(uri1[i]).toString()
        );
    }

    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     */
    public void testP2P(
    ) throws ServiceException {
        for (
            int i = 1;
            i < paths.length;
            i++
        ) assertEquals(
            "[" + i + "]: " + paths[i],
            paths[i],
            new Path(paths[i]).toString()
        );
    }
    
    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     */
    public void testO2P(
    ) throws ServiceException {
        for (
            int i = 1;
            i < paths.length;
            i++
        ) assertEquals(
            "[" + i + "]: " + paths[i],
            new Path(paths[i]),
            new Path(xri2[i])
        );
    }

    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     */
    public void testO2I(
    ) throws ServiceException {
        for (
            int i = 1;
            i < paths.length;
            i++
        ) assertEquals(
            "[" + i + "]: " + paths[i],
            iri2[i],
            new Path(paths[i]).toIRI().toString()
        );
    }

    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     * @throws URISyntaxException 
     */
    public void testI2P(
    ) throws ServiceException, URISyntaxException {
        for (
            int i = 1;
            i < paths.length;
            i++
        ) assertEquals (
            "[" + i + "]: " + paths[i],
            new Path(paths[i]),
            new Path(new URI(iri2[i]))
        );
    }
    
    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     */
    public void testP2O(
    ) throws ServiceException {
        for (
            int i = 1;
            i < paths.length;
            i++
        ) assertEquals(
            "[" + i + "]: " + paths[i],
            xri2[i],
            XRI_2Marshaller.getInstance().marshal(
                new Path(paths[i]).getSuffix(0)
            ).toString()
        );
    }

    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     */
    public void testP2I(
    ) throws ServiceException {
        for (
            int i = 1;
            i < xri2.length;
            i++
        ){
            String xriValue = xri2[i];
            String iriValue = iri2[i];
            XRI xri = new XRI(xriValue);
            assertEquals("XRI [" + i + "]: " + xriValue, xriValue, xri.toString());
            assertEquals("IRI [" + i + "]: " + iriValue, iriValue, xri.toIRINormalForm());
        }
    }
    
    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     */
    public void testI2X(
    ) throws ServiceException {
        for (
            int i = 1;
            i < iri2.length;
            i++
        ) assertEquals(
            "[" + i + "]: " + iri2[i],
            xri2[i],
            XRI.fromIRINormalForm(iri2[i]).toString()
        );
    }

    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     */
    public void testP2U(
    ) throws ServiceException {
        for (
            int i = 1;
            i < paths.length;
            i++
        ) assertEquals(
            "[" + i + "]: " + paths[i],
            uri1[i],
            new Path(paths[i]).toUri().toString()
        );
    }

    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     */
    public void testP2X(
    ) throws ServiceException {
        for (
            int i = 1;
            i < paths.length;
            i++
        ) assertEquals(
            "[" + i + "]: " + paths[i],
            xri1[i],
            new Path(paths[i]).toXri().toString()
        );
        assertEquals(
            xri1compatibility_15,
            xri1[15],
            new Path(xri1compatibility_15).toXri().toString()
        );
    }


    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection. 
     */
    public void testI2I(
    ) throws ServiceException {
        for (
            int i = 1;
            i < iri2.length;
            i++
        ) assertEquals(
            "[" + i + "]: " + iri2[i],
            iri2 [i],
            XRI.fromIRINormalForm(iri2[i]).toIRINormalForm()
        );
    }

    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection.
     */
    public void testX2I(
    ) throws ServiceException {
        for (
            int i = 1;
            i < xri2.length;
            i++
        ) {
        	XRI xri = new XRI(xri2[i]);
            assertEquals(
	            "iri2["+i+"] ("+xri2[i]+")",
	            iri2[i],
	            xri.toIRINormalForm()
	        );
            assertEquals(
                "uri2["+i+"] ("+xri2[i]+")",
	            uri2[i],
	            xri.toURINormalForm()
	        );
        }
    }

    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection.
     */
    public void testCR0003329(
    ){
        Path p = new Path("xri:@openmdx:a/b/c").add(new Path("xri:@openmdx:1/2/3").toString()); 
        Path p1 = new Path(p.toXri());
        assertEquals("p.toString()", "a/b/c/1//2//3", p.toString());
        assertEquals("p.toXri()", "xri:@openmdx:a/b/c/(@openmdx:1/2/3)", p.toXri());
        assertEquals("p.toUri()", "spice://a/b/c/1%2f2%2f3", p.toUri());
        assertEquals("1/2/3", p.getBase(), p1.getBase());
        Path p2 = new Path(new String[]{"x", "y", "a/b/c/1//2//3"});
        assertEquals("p2.toString()", "x/y/a//b//c//1////2////3", p2.toString());
        assertEquals("p2.toXri()", "xri:@openmdx:x/y/(@openmdx:a/b/c/(@openmdx:1/2/3))", p2.toXri());
        assertEquals("p2.toUri()", "spice://x/y/a%2fb%2fc%2f1%2f%2f2%2f%2f3", p2.toUri());
        XRI xref = new XRI("@openmdx:1/2/3");
        assertTrue("isAbsolute()", xref.isAbsolute());
        assertFalse("isRelative()", xref.isRelative());
        assertEquals("xref aurhority path", "@openmdx:1", xref.getAuthorityPath().toString());
        assertEquals("xref xri path", "/2/3", xref.getXRIPath().toString());
    }

    /**
     * Write the test case method in the fixture class.
     * Be sure to make it public, or it can't be invoked through reflection.
     */
    public void testCR20006216(
    ){
        assertEquals(paths[8], "(+resource/application-preferences.xml)", new Path(paths[8]).getComponent(4).get(0));
        PathComponent stateId = new PathComponent("sins:0:");
        assertEquals("Object Id", "sins", stateId.get(0));
        assertEquals("State Number", "0", stateId.get(1));
        assertFalse("PlaceHolder", stateId.isPlaceHolder());
        assertTrue("Private", stateId.isPrivate());        
    }
    
}

/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: TestNonManaged.java,v 1.1 2011/06/29 06:23:05 hburger Exp $
 * Description: Test Non-Managed Naming Contexts
 * Revision:    $Revision: 1.1 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2011/06/29 06:23:05 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2008-2011, OMEX AG, Switzerland
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
package test.openmdx.kernel.lightweight.naming;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openmdx.kernel.lightweight.naming.NonManagedInitialContextFactoryBuilder;

public class TestNonManaged {
	
	@BeforeClass
	public static void initialize() throws NamingException{
		NonManagedInitialContextFactoryBuilder.install(Collections.emptyMap());
	}

	@Test
	public void start() throws NamingException{
		Context root = new InitialContext();
		for(
			NamingEnumeration<Binding> bindings = root.listBindings("");
			bindings.hasMoreElements();
		){
			fail("Initially there should be no bindings");
		}
		assertEquals("The initial contex's name", "", root.getNameInNamespace());
	}

	@Test
	public void populateInitialContext() throws NamingException{
		Context root = new InitialContext();
		root.createSubcontext("org");
		root.createSubcontext("ch");
		Set<String> expected = new HashSet<String>(
			Arrays.asList("ch", "org")
		);
		for(
			NamingEnumeration<Binding> bindings = root.listBindings("");
			bindings.hasMoreElements();
		){
			Binding binding = bindings.nextElement();
			if(expected.remove(binding.getName())) {
				assertTrue(binding.getName() + " is expected to be a Context", binding.getObject() instanceof Context);
			}
		}
		assertTrue("Top level contexts", expected.isEmpty());
	}

	@Test
	public void populateTopLevelContexts() throws NamingException{
		Context root = new InitialContext();
		Context org = (Context) root.lookup("org");
		org.createSubcontext("openmdx");
		Context ch = (Context) root.lookup("ch");
		ch.createSubcontext("omex");
		Set<String> expected = new HashSet<String>(
			Arrays.asList("org/openmdx")
		);
		for(
			NamingEnumeration<Binding> bindings = root.listBindings("org");
			bindings.hasMoreElements();
		){
			Binding binding = bindings.nextElement();
			if(expected.remove(binding.getNameInNamespace())) {
				assertTrue(binding.getName() + " is expected to be a Context", binding.getObject() instanceof Context);
			}
		}
		assertTrue("Second level contexts", expected.isEmpty());
	}

	@Test
	public void populateThirdLevelContexts(
    ) throws NamingException{
		Context root = new InitialContext();
		Context openmdx = (Context) root.lookup("org/openmdx");
		openmdx.bind("value", "2011-04-01");
		Set<String> expected = new HashSet<String>(
			Arrays.asList("2011-04-01")
		);
		for(
			NamingEnumeration<Binding> bindings = root.listBindings("org/openmdx");
			bindings.hasMoreElements();
		){
			Binding binding = bindings.nextElement();
			if(expected.remove(binding.getObject())) {
				assertEquals(binding.getName() + " is expected to be a String", String.class.getName(), binding.getClassName());
			}
		}
		assertTrue("Thrid level bindings", expected.isEmpty());
	}

}

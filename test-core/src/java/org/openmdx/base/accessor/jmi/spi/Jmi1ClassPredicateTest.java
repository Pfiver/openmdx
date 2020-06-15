/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: Jmi1ClassPredicateTest 
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2020, OMEX AG, Switzerland
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
package org.openmdx.base.accessor.jmi.spi;

import java.util.function.Predicate;

import javax.resource.ResourceException;

import org.junit.Assert;
import org.junit.Test;
import org.openmdx.base.accessor.rest.spi.ObjectRecords;
import org.openmdx.base.rest.cci.ObjectRecord;

/**
 * Jmi1ClassPredicate Test
 */
public class Jmi1ClassPredicateTest {

    @Test
    public void sameClass() throws ResourceException {
        //
        // Arrange
        //
        final String modelClass = "org:openmdx:base:Creatable";
        final Predicate<ObjectRecord> testee = Jmi1ClassPredicate.newInstance(false, modelClass);
        final ObjectRecord candidate = ObjectRecords.createObjectRecord(null, modelClass);
        //
        // Act
        //
        final boolean matches = testee.test(candidate);
        //
        // Assert
        //
        Assert.assertTrue(matches);
    }

    @Test
    public void subClass() throws ResourceException {
        //
        // Arrange
        //
        final String modelClass = "org:openmdx:base:Creatable";
        final Predicate<ObjectRecord> testee = Jmi1ClassPredicate.newInstance(false, modelClass);
        final ObjectRecord candidate = ObjectRecords.createObjectRecord(null, "org:openmdx:base:BasicObject");
        //
        // Act
        //
        final boolean matches = testee.test(candidate);
        //
        // Assert
        //
        Assert.assertFalse(matches);
    }

    @Test
    public void superClass() throws ResourceException {
        //
        // Arrange
        //
        final String modelClass = "org:omg:model1:Element";
        final Predicate<ObjectRecord> testee = Jmi1ClassPredicate.newInstance(false, modelClass);
        final ObjectRecord candidate = ObjectRecords.createObjectRecord(null, "org:openmdx:base:BasicObject");
        //
        // Act
        //
        final boolean matches = testee.test(candidate);
        //
        // Assert
        //
        Assert.assertFalse(matches);
    }

    @Test
    public void equalPredicate() throws ResourceException {
        final Predicate<ObjectRecord> left = Jmi1ClassPredicate.newInstance(false, "org:omg:model1:Element");
        final Predicate<ObjectRecord> right = Jmi1ClassPredicate.newInstance(false, "org:omg:model1:Element");
        //
        // Act
        //
        final boolean ler = left.equals(right);
        final boolean rel = right.equals(left);
        //
        // Assert
        //
        Assert.assertTrue("left equals right", ler);
        Assert.assertTrue("right equals left", rel);
        Assert.assertEquals("hashCode", left.hashCode(), right.hashCode());
    }
    
    @Test
    public void differentClass() throws ResourceException {
        final Predicate<ObjectRecord> creatable = Jmi1ClassPredicate.newInstance(false, "org:openmdx:base:Creatable");
        final Predicate<ObjectRecord> removable = Jmi1ClassPredicate.newInstance(false, "org:openmdx:base:Removable");
        //
        // Actomg:model1:Element
        //
        final boolean cer = creatable.equals(removable);
        final boolean rec = removable.equals(creatable);
        //
        // Assert
        //
        Assert.assertFalse("creatable predicate equals removable predicate", cer);
        Assert.assertFalse("withSubclasses equals withoutSubclasses predicate", rec);
        Assert.assertNotEquals("hashCode", creatable.hashCode(), removable.hashCode());
    }

}

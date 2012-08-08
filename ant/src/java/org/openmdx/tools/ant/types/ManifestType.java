/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: ManifestType.java,v 1.1 2005/12/06 17:42:53 hburger Exp $
 * Description: Manifest
 * Revision:    $Revision: 1.1 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2005/12/06 17:42:53 $
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
package org.openmdx.tools.ant.types;

import org.apache.tools.ant.taskdefs.Manifest;
import org.apache.tools.ant.taskdefs.ManifestException;
import org.apache.tools.ant.types.DataType;

/**
 * Manifest
 */
public class ManifestType extends DataType {

    /**
     * Holds the real data.
     */
    private Manifest nestedManifest = new Manifest();

    /**
     * Add a section to the manifest
     *
     * @param section the manifest section to be added
     *
     * @exception ManifestException if the section is not valid.
     */
    public void addConfiguredSection(
    	Manifest.Section section
    ) throws ManifestException {
    	checkChildrenAllowed();
    	this.nestedManifest.addConfiguredSection(section);
    }

    /**
     * Add an attribute to the manifest - it is added to the main section.
     *
     * @param attribute the attribute to be added.
     *
     * @exception ManifestException if the attribute is not valid.
     */
    public void addConfiguredAttribute(
    	Manifest.Attribute attribute
    ) throws ManifestException {
    	checkChildrenAllowed();
    	this.nestedManifest.addConfiguredAttribute(attribute);
    }
	
    /**
     * Retrieves this manifesttype's content.
     */
    public Manifest getManifest() {
        return isReference() ?
            getRef().getManifest() : 
            this.nestedManifest;
    }
    
    /**
     * Performs the check for circular references and returns the
     * referenced Selector.
     */
    private ManifestType getRef(){
        return (ManifestType) getCheckedRef(
        	ManifestType.class, 
        	"manifest"
        );
    }

}

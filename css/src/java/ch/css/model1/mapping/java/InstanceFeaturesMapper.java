/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: InstanceFeaturesMapper.java,v 1.1 2007/01/06 17:25:27 hburger Exp $
 * Description: Class Features Mapper
 * Revision:    $Revision: 1.1 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2007/01/06 17:25:27 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2006, OMEX AG, Switzerland
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
 * This product includes or is based on software developed by other 
 * organizations as listed in the NOTICE file.
 */
package ch.css.model1.mapping.java;

import java.io.Writer;
import java.util.Iterator;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.model1.accessor.basic.cci.ModelElement_1_0;
import org.openmdx.model1.accessor.basic.cci.Model_1_0;
import org.openmdx.model1.mapping.AttributeDef;
import org.openmdx.model1.mapping.ClassDef;
import org.openmdx.model1.mapping.MapperUtils;
import org.openmdx.model1.mapping.OperationDef;
import org.openmdx.model1.mapping.ReferenceDef;

/**
 * Class Features Mapper
 */
public class InstanceFeaturesMapper
    extends FeaturesMapper 
{

    /**
     * Constructor 
     *
     * @param classDef
     * @param writer
     * @param model
     * @param format
     * @param packageSuffix
     * @throws ServiceException
     */
    public InstanceFeaturesMapper(
        ModelElement_1_0 classDef,
        Writer writer,
        Model_1_0 model,
        String format, 
        String packageSuffix
    ) throws ServiceException {
        super(
            writer,
            model,
            format, 
            packageSuffix
        );
        this.classDef = new ClassDef(classDef, model);
    }

    /* (non-Javadoc)
     * @see org.openmdx.compatibility.model1.mapping.java.JMIAbstractMapper#getId()
     */
    protected String mapperId() {
        return "$Id: InstanceFeaturesMapper.java,v 1.1 2007/01/06 17:25:27 hburger Exp $";
    }

    /**
     * Map reference name
     * 
     * @param referenceDef
     * 
     * @throws ServiceException
     */
    public void mapReference(
        ReferenceDef referenceDef
    ) throws ServiceException {
//      this.trace("ClassFeatures/Reference");
        this.pw.println("   /**");
        this.pw.println(MapperUtils.wrapText(
            "    * ",
            "Reference feature <code>" + referenceDef.getName() + "</code>."));
        this.pw.println("    */");
        this.pw.print("    java.lang.String ");
        this.pw.print(getConstantName(referenceDef.getName()));
        this.pw.print(" = \"");
        this.pw.print(referenceDef.getName());
        this.pw.println("\";");
        this.pw.println();
    }

    /**
     * Map operation name
     * 
     * @param operationDef
     * 
     * @throws ServiceException
     */
    public void mapOperation(
        OperationDef operationDef
    ) throws ServiceException {
//      this.trace("ClassFeatures/Operation");
        this.pw.println("   /**");
        this.pw.println(MapperUtils.wrapText(
            "    * ",
            "Behavioural feature <code>" + operationDef.getName() + "</code>."));
        this.pw.println("    */");
        this.pw.print("    java.lang.String ");
        this.pw.print(getConstantName(operationDef.getName()));
        this.pw.print(" = \"");
        this.pw.print(operationDef.getName());
        this.pw.println("\";");
        this.pw.println();
    }

    /**
     * 
     *
     */
    public void mapEnd() {
//      this.trace("ClassFeatures/End");
        this.pw.println("}");
    }

    /**
     * 
     * @throws ServiceException
     */
    public void mapBegin()
        throws ServiceException {
//      this.trace("ClassFeatures/Begin");
        this.fileHeader();
        this.pw.println("package "
            + this.getNamespace(MapperUtils.getNameComponents(MapperUtils
                .getPackageName(this.classDef.getQualifiedName()))) + ";");
        this.pw.println();
        this.pw.println("/**");
        this.pw.println(MapperUtils.wrapText(" * ", "Features of class " + this.classDef.getName()));
        this.pw.println(" */");
        this.pw.print("public interface " + this.classDef.getName() + FEATURES_INTERFACCE_SUFFIX);
        if (!this.classDef.getSupertypes().isEmpty()) {
            String separator = " extends "; 
            for (
                Iterator i = this.classDef.getSupertypes().iterator(); 
                i.hasNext(); 
                separator = ", "
            ) {
                ClassDef supertype = (ClassDef) i.next();
                this.pw.print(separator);
                this.pw.print(this.getType(supertype.getQualifiedName()) + FEATURES_INTERFACCE_SUFFIX);
            }
        }
        this.pw.println(" {");
        this.pw.println();
    }

    /**
     * Map operation name
     * 
     * @param attributeDef
     * @throws ServiceException
     */
    public void mapAttribute(
        AttributeDef attributeDef
    ) throws ServiceException {
//      this.trace("ClassFeatures/Attribute");
        this.pw.println("   /**");
        this.pw.println(MapperUtils.wrapText(
            "    * ",
            "Structural feature <code>" + attributeDef.getName() + "</code>."));
        this.pw.println("    */");
        this.pw.print("    java.lang.String ");
        this.pw.print(getConstantName(attributeDef.getName()));
        this.pw.print(" = \"");
        this.pw.print(attributeDef.getName());
        this.pw.println("\";");
        this.pw.println();
    }

    /**
     * 
     */
    private final ClassDef classDef;
        
}

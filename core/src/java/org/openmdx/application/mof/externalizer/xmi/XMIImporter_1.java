/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: XMIImporter_1.java,v 1.14 2010/09/21 16:32:46 hburger Exp $
 * Description: XMI Model Importer
 * Revision:    $Revision: 1.14 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2010/09/21 16:32:46 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2004-2005, OMEX AG, Switzerland
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
 * This product includes software developed by the Apache Software
 * Foundation (http://www.apache.org/).
 */
package org.openmdx.application.mof.externalizer.xmi;

import java.io.File;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.resource.ResourceException;
import javax.resource.cci.MappedRecord;

import org.omg.mof.cci.DirectionKind;
import org.omg.mof.cci.ScopeKind;
import org.omg.mof.cci.VisibilityKind;
import org.openmdx.application.dataprovider.cci.Dataprovider_1_0;
import org.openmdx.application.dataprovider.cci.ServiceHeader;
import org.openmdx.application.mof.cci.ModelAttributes;
import org.openmdx.application.mof.cci.ModelExceptions;
import org.openmdx.application.mof.externalizer.spi.ModelImporter_1;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1AggregationKind;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1Association;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1AssociationEnd;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1Attribute;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1ChangeableKind;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1Class;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1Consumer;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1DataType;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1Generalization;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1ModelElement;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1MultiplicityRange;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1Operation;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1Package;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1Parameter;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1TaggedValue;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1VisibilityKind;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.mof.cci.AggregationKind;
import org.openmdx.base.mof.cci.Stereotypes;
import org.openmdx.base.naming.Path;
import org.openmdx.base.rest.spi.Object_2Facade;
import org.openmdx.kernel.exception.BasicException;
import org.openmdx.kernel.exception.BasicException.Code;
import org.openmdx.kernel.log.SysLog;

@SuppressWarnings("unchecked")
public class XMIImporter_1
extends ModelImporter_1
implements UML1Consumer {

    //---------------------------------------------------------------------------
    public XMIImporter_1(
        URL modelUrl,
        short xmiFormat,
        Map pathMap
    ) {
        this(
            modelUrl,
            xmiFormat,
            pathMap,
            System.out,
            System.err,
            System.err
        );
    }

    //---------------------------------------------------------------------------
    public XMIImporter_1(
        File file,
        short xmiFormat,
        Map pathMap
    ) throws MalformedURLException {
        this(
            file,
            xmiFormat,
            pathMap,
            System.out,
            System.err,
            System.err
        );
    }

    //---------------------------------------------------------------------------
    public XMIImporter_1(
        File file,
        short xmiFormat,
        Map pathMap,
        PrintStream infos,
        PrintStream warnings,
        PrintStream errors
    ) throws MalformedURLException {
        this(
            file.toURI().toURL(),
            xmiFormat,
            pathMap,
            infos,
            warnings,
            errors
        );
    }

    //---------------------------------------------------------------------------
    public XMIImporter_1(
        URL modelUrl,
        short xmiFormat,
        Map pathMap,
        PrintStream infos,
        PrintStream warnings,
        PrintStream errors
    ) {
        this.modelUrl = modelUrl;
        this.xmiFormat = xmiFormat;
        this.pathMap = pathMap;
        this.infos = infos;
        this.warnings = warnings;
        this.errors = errors;
        this.hasErrors = false;
    }

    //---------------------------------------------------------------------------
    public short getXMIFormat(
    ) {
        return this.xmiFormat;
    }

    //---------------------------------------------------------------------------
    public Map getPathMap(
    ) {
        return this.pathMap;
    }

    //---------------------------------------------------------------------------
    public void process(
        ServiceHeader header,
        Dataprovider_1_0 target,
        String providerName
    ) throws ServiceException {

        this.header = header;
        this.target = target;
        this.providerName = providerName;

        try {
            this.beginImport();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        try {
            this.invokeParser(
                null,
                new Stack()
            );
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        try {
            this.endImport();
        }
        catch(Exception e) {
            e.printStackTrace();
        }        
    }

    //---------------------------------------------------------------------------
    public void processNested(
        XMIImporter_1 importer,
        XMIReferenceResolver resolver,
        Stack scope
    ) throws ServiceException {

        this.header = importer.header;
        this.target = importer.target;
        this.providerName = importer.providerName;
        this.segments = importer.segments;
        this.invokeParser(
            resolver,
            scope
        );
    }

    //---------------------------------------------------------------------------
    private void info(
        String message
    ) {
        this.infos.println("INFO:    " + message);
    }

    //---------------------------------------------------------------------------
    private void invokeParser(
        XMIReferenceResolver _resolver,
        Stack scope
    ) throws ServiceException {
        XMIReferenceResolver resolver = _resolver;
        try {
            XMIParser xmiParser = null;
            this.info("Parsing url=" + this.modelUrl);
            if(XMI_FORMAT_POSEIDON == this.xmiFormat) {
                xmiParser = new XMI1Parser(this.infos, this.warnings, this.errors);
                if(resolver == null) {
                    resolver = new XMI1ReferenceResolver(new HashMap(), this.errors);
                    resolver.parse(this.modelUrl.toString());
                }
            } else if(XMI_FORMAT_MAGICDRAW == this.xmiFormat) {
                xmiParser = new XMI1Parser(this.infos, this.warnings, this.errors);
                if(resolver == null) {
                    resolver = new XMI1ReferenceResolver(new HashMap(), this.errors);
                    resolver.parse(this.modelUrl.toString());
                }
            } else if(XMI_FORMAT_RSM == this.xmiFormat) {
                xmiParser = new XMI20Parser(this.infos, this.warnings, this.errors);
                if(resolver == null) {
                    resolver = new XMI2ReferenceResolver(
                        new HashMap(),
                        new Stack(),
                        this.pathMap,
                        this.infos,
                        this.warnings,
                        this.errors, 
                        new HashMap<String, UML1AssociationEnd>()
                    );
                    resolver.parse(this.modelUrl.toString());
                }
            } else if(XMI_FORMAT_EMF == this.xmiFormat) {
                xmiParser = new XMI2Parser(this.infos, this.warnings, this.errors);
                if(resolver == null) {
                    resolver = new XMI2ReferenceResolver(
                        new HashMap(),
                        new Stack(),
                        this.pathMap,
                        this.infos,
                        this.warnings,
                        this.errors, 
                        null
                    );
                    resolver.parse(this.modelUrl.toString());
                }
            }
            if(resolver.hasErrors()) {
                throw new ServiceException(
                    ModelExceptions.MODEL_DOMAIN,
                    Code.ABORT,
                    "Parsing reported errors"
                );
            }
            else {
                xmiParser.parse(
                    this.modelUrl.toString(),
                    this,
                    resolver,
                    scope
                );
                if(this.hasErrors) {
                    throw new ServiceException(
                        ModelExceptions.MODEL_DOMAIN,
                        Code.ABORT,
                        "Parsing reported errors"
                    );
                }
            }
        }
        catch(Exception ex) {
            throw new ServiceException(ex);
        }
    }

    //---------------------------------------------------------------------------
    public void processUMLPackage(
        UML1Package umlPackage
    ) throws ServiceException {

        SysLog.detail("Processing package", umlPackage.getQualifiedName());
        Object_2Facade modelPackageFacade;
        try {
            modelPackageFacade = Object_2Facade.newInstance(
                toElementPath(
                    nameToPathComponent(umlPackage.getQualifiedName()),
                    umlPackage.getName()
                ),
                ModelAttributes.PACKAGE
            );
        } 
        catch (ResourceException e) {
            throw new ServiceException(e);
        }
        modelPackageFacade.attributeValuesAsList("isAbstract").add(Boolean.FALSE);
        modelPackageFacade.attributeValuesAsList("visibility").add(VisibilityKind.PUBLIC_VIS);

        // annotation
        String annotation = this.getAnnotation(umlPackage);
        if (annotation.length() != 0) {
            modelPackageFacade.attributeValuesAsList("annotation").add(annotation);
        }
        this.createModelElement(
            null,
            modelPackageFacade.getDelegate()
        );
    }

    //---------------------------------------------------------------------------
    public void processUMLAssociation(
        UML1Association umlAssociation
    ) throws Exception {

        this.verifyAssociationName(umlAssociation.getName());

        // ModelAssociation object
        Object_2Facade associationDefFacade = Object_2Facade.newInstance(
            toElementPath(
                nameToPathComponent(getScope(umlAssociation.getQualifiedName())),
                umlAssociation.getName()
            ),
            ModelAttributes.ASSOCIATION
        );
        // container
        associationDefFacade.attributeValuesAsList("container").add(
            toElementPath(
                nameToPathComponent(getScope(umlAssociation.getQualifiedName())),
                getName(getScope(umlAssociation.getQualifiedName()))
            )
        );

        // annotation
        String annotation = this.getAnnotation(umlAssociation);
        if (annotation.length() != 0)
        {
            associationDefFacade.attributeValuesAsList("annotation").add(annotation);
        }

        // stereotype
        associationDefFacade.attributeValuesAsList("stereotype").addAll(umlAssociation.getStereotypes());

        associationDefFacade.attributeValuesAsList("isAbstract").add(Boolean.FALSE);
        associationDefFacade.attributeValuesAsList("visibility").add(VisibilityKind.PUBLIC_VIS);
        associationDefFacade.attributeValuesAsList("isDerived").add(
            Boolean.valueOf(umlAssociation.isDerived())
        );
        this.createModelElement(
            null, 
            associationDefFacade.getDelegate()
        );
        Object_2Facade associationEnd1DefFacade = Object_2Facade.newInstance(
            this.processAssociationEnd(
                (UML1AssociationEnd)umlAssociation.getConnection().get(0),
                associationDefFacade.getDelegate()
            )
        );
        Object_2Facade associationEnd2DefFacade = Object_2Facade.newInstance(
            this.processAssociationEnd(
                (UML1AssociationEnd)umlAssociation.getConnection().get(1),
                associationDefFacade.getDelegate()
            )
        );
        if(XMI_FORMAT_POSEIDON == this.xmiFormat) {
            /**
             * Poseidon XMI/UML format
             * NOTE:
             * To comply with our MOF model implementation we change aggregation and 
             * qualifier assignments. Client aggregation and qualifier attributes 
             * now belong to the supplier side and supplier aggregation and qualifier 
             * attributes now belong to the client side.
             */

            // swap 'aggregation' to comply with our MOF model
            String temp = (String)associationEnd1DefFacade.attributeValuesAsList("aggregation").get(0);
            associationEnd1DefFacade.attributeValuesAsList("aggregation").set(
                0,
                associationEnd2DefFacade.attributeValuesAsList("aggregation").get(0)
            );
            associationEnd2DefFacade.attributeValuesAsList("aggregation").set(0, temp);

            // swap 'qualifierName' to comply with our MOF model
            List tempQualifier = new ArrayList(associationEnd1DefFacade.attributeValuesAsList("qualifierName"));
            associationEnd1DefFacade.attributeValuesAsList("qualifierName").clear();
            associationEnd1DefFacade.attributeValuesAsList("qualifierName").addAll(
                associationEnd2DefFacade.attributeValuesAsList("qualifierName")
            );
            associationEnd2DefFacade.attributeValuesAsList("qualifierName").clear();
            associationEnd2DefFacade.attributeValuesAsList("qualifierName").addAll(tempQualifier);

            // swap 'qualifierType' to comply with our MOF model
            tempQualifier = new ArrayList(associationEnd1DefFacade.attributeValuesAsList("qualifierType"));
            associationEnd1DefFacade.attributeValuesAsList("qualifierType").clear();
            associationEnd1DefFacade.attributeValuesAsList("qualifierType").addAll(
                associationEnd2DefFacade.attributeValuesAsList("qualifierType")
            );
            associationEnd2DefFacade.attributeValuesAsList("qualifierType").clear();
            associationEnd2DefFacade.attributeValuesAsList("qualifierType").addAll(tempQualifier);
        }
        else if (XMI_FORMAT_MAGICDRAW == this.xmiFormat)
        {
            /**
             * MagicDraw XMI/UML format
             * NOTE:
             * To comply with our MOF model implementation we must change the 
             * aggregation assignments for association ends.
             */

            // swap 'aggregation' to comply with our MOF model
            String temp = (String)associationEnd1DefFacade.attributeValuesAsList("aggregation").get(0);
            associationEnd1DefFacade.attributeValuesAsList("aggregation").set(
                0,
                associationEnd2DefFacade.attributeValuesAsList("aggregation").get(0)
            );
            associationEnd2DefFacade.attributeValuesAsList("aggregation").set(0, temp);
        }

        this.verifyAndCompleteAssociationEnds(
            associationEnd1DefFacade.getDelegate(),
            associationEnd2DefFacade.getDelegate()
        );
        this.exportAssociationEndAsReference(
            associationEnd1DefFacade.getDelegate(),
            associationEnd2DefFacade.getDelegate(),
            associationDefFacade.getDelegate(),
            null
        );
        this.exportAssociationEndAsReference(
            associationEnd2DefFacade.getDelegate(),
            associationEnd1DefFacade.getDelegate(),
            associationDefFacade.getDelegate(),
            null
        );
        this.createModelElement(
            null, 
            associationEnd1DefFacade.getDelegate()
        );
        this.createModelElement(
            null, 
            associationEnd2DefFacade.getDelegate()
        );
    }

    //---------------------------------------------------------------------------
    private MappedRecord processAssociationEnd(
        UML1AssociationEnd umlAssociationEnd,
        MappedRecord associationDef
    ) throws Exception {
        this.verifyAssociationEndName(
            associationDef,
            umlAssociationEnd == null ? null : umlAssociationEnd.getName()
        );
        if(XMI_FORMAT_POSEIDON == this.xmiFormat) {
            // Note: 
            // In the Poseidon XMI/UML format qualifiers are entered by using 
            // the association end name. The association end names consist of the
            // association end name and the qualifiers in square brackets (name and
            // qualfiers are separated by a new line). Therefore the real 
            // association end name and the qualifiers must be extracted.

            // extract/parse association end name and qualifiers
            // Note: 
            // Poseidon and MagicDraw use UMLAttributes for their internal qualifier 
            // representation, therefore the internal qualifier representation has 
            // to be mapped
            for(
                Iterator it = this.toAssociationEndQualifiers(umlAssociationEnd.getName()).iterator();
                it.hasNext();
            ) {
                Qualifier qualifier = (Qualifier)it.next();
                UML1Attribute attribute = new UML1Attribute("", qualifier.getName());
                attribute.setType(qualifier.getType());
                umlAssociationEnd.getQualifier().add(attribute);
            }
            umlAssociationEnd.setName(
                this.toAssociationEndName(umlAssociationEnd.getName())
            );
        }
        Object_2Facade associationEndDefFacade = Object_2Facade.newInstance(
            newFeaturePath(
                Object_2Facade.getPath(associationDef),
                umlAssociationEnd.getName()
            ),
            ModelAttributes.ASSOCIATION_END
        );
        // name
        associationEndDefFacade.attributeValuesAsList("name").add(umlAssociationEnd.getName());
        // annotation
        String annotation = this.getAnnotation(umlAssociationEnd);
        if (annotation.length() != 0) {
            associationEndDefFacade.attributeValuesAsList("annotation").add(annotation);
        }
        // type
        if(umlAssociationEnd.getParticipant() == null) {
            throw new ServiceException(
                ModelExceptions.MODEL_DOMAIN,
                ModelExceptions.INVALID_ATTRIBUTE_TYPE,
                "type is null for association",
                new BasicException.Parameter("association", umlAssociationEnd.getQualifiedName()),
        		new BasicException.Parameter("id", umlAssociationEnd.getId()),
        		new BasicException.Parameter("name", umlAssociationEnd.getName()),
        		new BasicException.Parameter("qualifiedName", umlAssociationEnd.getQualifiedName())
            );
        }
        associationEndDefFacade.attributeValuesAsList("type").add(
            toElementPath(
                nameToPathComponent(getScope(umlAssociationEnd.getParticipant())),
                getName(umlAssociationEnd.getParticipant())
            )
        );
        // multiplicity
        associationEndDefFacade.attributeValuesAsList("multiplicity").add(
            this.toMOFMultiplicity(umlAssociationEnd.getMultiplicityRange())
        );
        // container
        associationEndDefFacade.attributeValuesAsList("container").add(
            Object_2Facade.getPath(associationDef)
        );
        // isChangeable
        associationEndDefFacade.attributeValuesAsList("isChangeable").add(
            Boolean.valueOf(
                this.toMOFChangeability(umlAssociationEnd.getChangeability())
            )
        );
        // aggregation
        associationEndDefFacade.attributeValuesAsList("aggregation").add(
            this.toMOFAggregation(umlAssociationEnd.getAggregation())
        );
        // isNavigable
        associationEndDefFacade.attributeValuesAsList("isNavigable").add(
            Boolean.valueOf(umlAssociationEnd.isNavigable())
        );
        // qualifiers
        for (
            Iterator it = umlAssociationEnd.getQualifier().iterator();
            it.hasNext();
        ) {
            UML1Attribute qualifier = (UML1Attribute)it.next();
            associationEndDefFacade.attributeValuesAsList("qualifierName").add(qualifier.getName());
            try {
                associationEndDefFacade.attributeValuesAsList("qualifierType").add(
                    toElementPath(
                        nameToPathComponent(getScope(qualifier.getType())),
                        getName(qualifier.getType())
                    )
                );
            }
            catch(Exception e) {
                throw new ServiceException(
                    e,
                    ModelExceptions.MODEL_DOMAIN,
                    ModelExceptions.INVALID_PARAMETER_DECLARATION,
                    "can not get qualifier type for association end",
                    new BasicException.Parameter("association end", umlAssociationEnd.getQualifiedName()),
                    new BasicException.Parameter("qualifier type", qualifier.getType())
                );
            }
        }

        return associationEndDefFacade.getDelegate();
    }

    //---------------------------------------------------------------------------
    public void processUMLClass(
        UML1Class umlClass
    ) throws Exception {
        // depending on stereotype, the given class must be treated differently
        if (umlClass.getStereotypes().contains(Stereotypes.STRUCT)) {
            this.processStructureType(umlClass);
        }
        else if (umlClass.getStereotypes().contains(Stereotypes.ALIAS)) {
            this.processAliasType(umlClass);
        }
        else {
            this.processClass(umlClass);
        }
    }

    //---------------------------------------------------------------------------
    private void processStructureType(
        UML1Class umlClass
    ) throws Exception {

        SysLog.detail("Processing structure type", umlClass.getQualifiedName());

        // ModelClass object
        Object_2Facade structureTypeDefFacade = Object_2Facade.newInstance(
            toElementPath(
                nameToPathComponent(getScope(umlClass.getQualifiedName())),
                umlClass.getName()
            ),
            ModelAttributes.STRUCTURE_TYPE
        );
        // container
        structureTypeDefFacade.attributeValuesAsList("container").add(
            toElementPath(
                nameToPathComponent(getScope(umlClass.getQualifiedName())),
                getName(getScope(umlClass.getQualifiedName()))
            )
        );

        /**
         * skip stereotype because its value 'Struct'
         * was marked to note the difference between
         * ordinary classes and structure types
         */

        // annotation
        String annotation = this.getAnnotation(umlClass);
        if (annotation.length() != 0) {
            structureTypeDefFacade.attributeValuesAsList("annotation").add(annotation);
        }
        // supertype
        SortedSet superTypePaths = new TreeSet();
        for (
            Iterator it = umlClass.getSuperclasses().iterator();
            it.hasNext();
        ) {
            String superclass = (String)it.next();
            superTypePaths.add(
                toElementPath(
                    nameToPathComponent(getScope(superclass)),
                    getName(superclass)
                )
            );
        }
        // add supertypes in sorted order
        for(
            Iterator it = superTypePaths.iterator();
            it.hasNext();
        ) {
            structureTypeDefFacade.attributeValuesAsList("supertype").add(it.next());
        }

        // isAbstract attribute
        structureTypeDefFacade.attributeValuesAsList("isAbstract").add(
            Boolean.valueOf(umlClass.isAbstract())
        );

        // visibility attribute
        structureTypeDefFacade.attributeValuesAsList("visibility").add(VisibilityKind.PUBLIC_VIS);

        // isSingleton attribute
        structureTypeDefFacade.attributeValuesAsList("isSingleton").add(
            Boolean.FALSE
        );

        this.createModelElement(
            null,
            structureTypeDefFacade.getDelegate()
        );

        for(
            Iterator it = umlClass.getAttributes().iterator();
            it.hasNext();
        ) {
            this.processStructureField(
                (UML1Attribute)it.next(),
                structureTypeDefFacade.getDelegate()
            );
        }
    }

    //---------------------------------------------------------------------------
    private void processStructureField(
        UML1Attribute umlAttribute,
        MappedRecord aContainer
    ) throws Exception {

        SysLog.detail("Processing structure field", umlAttribute.getName());

        Object_2Facade structureFieldDefFacade = Object_2Facade.newInstance(
            newFeaturePath(
                Object_2Facade.getPath(aContainer),
                umlAttribute.getName()
            ),
            ModelAttributes.STRUCTURE_FIELD
        );
        // container
        structureFieldDefFacade.attributeValuesAsList("container").add(
            Object_2Facade.getPath(aContainer)
        );

        // maxLength attribute
        structureFieldDefFacade.attributeValuesAsList("maxLength").add(new Integer(this.getAttributeMaxLength(umlAttribute)));

        // multiplicity attribute
        // openMDX uses attribute stereotype to indicate multiplicity
        // this allows to use multiplicities like set, list, ...
        // if no multiplicity has been modeled, the default multiplicity is taken
        structureFieldDefFacade.attributeValuesAsList("multiplicity").add(
            umlAttribute.getStereotypes().size() > 0
            ? umlAttribute.getStereotypes().iterator().next()
                : umlAttribute.getMultiplicityRange() != null
                ? this.toMOFMultiplicity(umlAttribute.getMultiplicityRange())
                    : DEFAULT_ATTRIBUTE_MULTIPLICITY
        );

        // annotation
        String annotation = this.getAnnotation(umlAttribute);
        if (annotation.length() != 0) {
            structureFieldDefFacade.attributeValuesAsList("annotation").add(annotation);
        }
        if(umlAttribute.getType() == null) {
            this.error("Undefined type for field " + umlAttribute.getQualifiedName());
        }
        else {
            structureFieldDefFacade.attributeValuesAsList("type").add(
                toElementPath(
                    nameToPathComponent(getScope(umlAttribute.getType())),
                    getName(umlAttribute.getType())
                )
            );
            this.createModelElement(
                null,
                structureFieldDefFacade.getDelegate()
            );
        }
    }

    //---------------------------------------------------------------------------
    private void processAliasType(
        UML1Class umlClass
    ) throws Exception {

        SysLog.detail("Processing alias type", umlClass.getQualifiedName());

        // ModelClass object
        Object_2Facade aliasTypeDefFacade = Object_2Facade.newInstance(
            toElementPath(
                nameToPathComponent(getScope(umlClass.getQualifiedName())),
                umlClass.getName()
            ),
            ModelAttributes.ALIAS_TYPE
        );
        // container
        aliasTypeDefFacade.attributeValuesAsList("container").add(
            toElementPath(
                nameToPathComponent(getScope(umlClass.getQualifiedName())),
                getName(getScope(umlClass.getQualifiedName()))
            )
        );


        // annotation
        String annotation = this.getAnnotation(umlClass);
        if (annotation.length() != 0)
        {
            aliasTypeDefFacade.attributeValuesAsList("annotation").add(annotation);
        }

        // isAbstract attribute
        aliasTypeDefFacade.attributeValuesAsList("isAbstract").add(
            Boolean.valueOf(umlClass.isAbstract())
        );

        // visibility attribute
        aliasTypeDefFacade.attributeValuesAsList("visibility").add(VisibilityKind.PUBLIC_VIS);

        // isSingleton attribute
        aliasTypeDefFacade.attributeValuesAsList("isSingleton").add(
            Boolean.FALSE
        );

        // type
        List attributes = umlClass.getAttributes();
        this.verifyAliasAttributeNumber(
            aliasTypeDefFacade.getDelegate(), 
            attributes.size()
        );

        UML1Attribute attribute = (UML1Attribute)umlClass.getAttributes().get(0);
        this.verifyAliasAttributeName(
            aliasTypeDefFacade.getDelegate(), 
            attribute.getName()
        );
        aliasTypeDefFacade.attributeValuesAsList("type").add(
            toElementPath(
                nameToPathComponent(getScope(attribute.getName())),
                getName(attribute.getName())
            )
        );

        // create element
        this.createModelElement(
            null,
            aliasTypeDefFacade.getDelegate()
        );
    }

    //---------------------------------------------------------------------------
    private void processClass(
        UML1Class umlClass
    ) throws Exception {
        SysLog.detail("Processing class", umlClass.getQualifiedName());

        // ModelClass object
        Object_2Facade classDefFacade = Object_2Facade.newInstance(
            toElementPath(
                nameToPathComponent(getScope(umlClass.getQualifiedName())),
                umlClass.getName()
            ),
            ModelAttributes.CLASS
        );

        // container
        classDefFacade.attributeValuesAsList("container").add(
            toElementPath(
                nameToPathComponent(getScope(umlClass.getQualifiedName())),
                getName(getScope(umlClass.getQualifiedName()))
            )
        );

        // stereotype
        classDefFacade.attributeValuesAsList("stereotype").addAll(
            umlClass.getStereotypes()
        );

        // annotation
        String annotation = this.getAnnotation(umlClass);
        if (annotation.length() != 0) {
            classDefFacade.attributeValuesAsList("annotation").add(annotation);
        }

        // supertype
        SortedSet superTypePaths = new TreeSet();
        for (
            Iterator it = umlClass.getSuperclasses().iterator();
            it.hasNext();
        ) {
            String superclass = (String)it.next();
            superTypePaths.add(
                toElementPath(
                    nameToPathComponent(getScope(superclass)),
                    getName(superclass)
                )
            );
        }
        // add supertypes in sorted order
        for(
            Iterator it = superTypePaths.iterator();
            it.hasNext();
        ) {
            classDefFacade.attributeValuesAsList("supertype").add(it.next());
        }

        // isAbstract attribute
        classDefFacade.attributeValuesAsList("isAbstract").add(
            Boolean.valueOf(umlClass.isAbstract())
        );

        // visibility attribute
        classDefFacade.attributeValuesAsList("visibility").add(VisibilityKind.PUBLIC_VIS);

        // isSingleton attribute
        classDefFacade.attributeValuesAsList("isSingleton").add(
            Boolean.FALSE
        );

        SysLog.detail("Class", classDefFacade.getPath());
        this.createModelElement(
            null, 
            classDefFacade.getDelegate()
        );

        // process attributes of this class
        for (
            Iterator it = umlClass.getAttributes().iterator();
            it.hasNext();
        ) {
            this.processAttribute((UML1Attribute)it.next());
        }

        // process operations of this class
        for (
            Iterator it = umlClass.getOperations().iterator();
            it.hasNext();
        ) {
            this.processOperation(
                (UML1Operation)it.next(), 
                classDefFacade.getDelegate()
            );
        }
    }

    //---------------------------------------------------------------------------
    private void processAttribute(
        UML1Attribute umlAttribute
    ) throws Exception {
        SysLog.detail("Processing attribute", umlAttribute.getName());

        Path containerPath = toElementPath(
            nameToPathComponent(getScope(getScope(umlAttribute.getQualifiedName()))),
            getName(getScope(umlAttribute.getQualifiedName()))
        );

        Object_2Facade attributeDefFacade = Object_2Facade.newInstance(
            newFeaturePath(
                containerPath,
                umlAttribute.getName()
            ),
            ModelAttributes.ATTRIBUTE
        );

        attributeDefFacade.attributeValuesAsList("container").add(containerPath);
        attributeDefFacade.attributeValuesAsList("visibility").add(this.toMOFVisibility(umlAttribute.getVisiblity()));
        attributeDefFacade.attributeValuesAsList("uniqueValues").add(Boolean.valueOf(DEFAULT_ATTRIBUTE_IS_UNIQUE));
        attributeDefFacade.attributeValuesAsList("isLanguageNeutral").add(Boolean.valueOf(DEFAULT_ATTRIBUTE_IS_LANGUAGE_NEUTRAL));
        attributeDefFacade.attributeValuesAsList("maxLength").add(new Integer(this.getAttributeMaxLength(umlAttribute)));

        boolean isDerived = this.isAttributeDerived(umlAttribute);
        boolean isChangeable = this.toMOFChangeability(umlAttribute.getChangeability());
        if(isDerived && isChangeable) {
            this.warning("Attribute <" + attributeDefFacade.getPath().toString() + "> is derived AND changeable. Derived attributes MUST NOT be changeable. Continuing with isChangeable=false!");
            isChangeable = false;
        }

        if(umlAttribute.getType() == null) {
            this.error("Undefined type for attribute " + umlAttribute.getQualifiedName());
        }
        else {
            attributeDefFacade.attributeValuesAsList("type").add(
                toElementPath(
                    nameToPathComponent(getScope(umlAttribute.getType())),
                    getName(umlAttribute.getType())
                )
            );
        }

        // openMDX uses attribute stereotype to indicate multiplicity
        // this allows to use multiplicities like set, list, ...
        attributeDefFacade.attributeValuesAsList("multiplicity").add(
            umlAttribute.getStereotypes().size() > 0
            ? umlAttribute.getStereotypes().iterator().next()
                : umlAttribute.getMultiplicityRange() != null
                ? this.toMOFMultiplicity(umlAttribute.getMultiplicityRange())
                    : DEFAULT_ATTRIBUTE_MULTIPLICITY
        );
        attributeDefFacade.attributeValuesAsList("scope").add(ScopeKind.INSTANCE_LEVEL);
        attributeDefFacade.attributeValuesAsList("isDerived").add(Boolean.valueOf(isDerived));
        attributeDefFacade.attributeValuesAsList("isChangeable").add(Boolean.valueOf(isChangeable));

        // annotation
        String annotation = this.getAnnotation(umlAttribute);
        if (annotation.length() != 0)
        {
            attributeDefFacade.attributeValuesAsList("annotation").add(annotation);
        }

        SysLog.detail("Attribute", attributeDefFacade.getPath());
        this.createModelElement(
            null, 
            attributeDefFacade.getDelegate()
        );
    }

    //---------------------------------------------------------------------------
    private void processOperation(
        UML1Operation umlOperation,
        MappedRecord aContainer
    ) throws Exception {
        SysLog.detail("Processing operation", umlOperation.getName());
        String operationName = umlOperation.getName();
        Object_2Facade operationDefFacade = Object_2Facade.newInstance(
            newFeaturePath(
                Object_2Facade.getPath(aContainer),
                umlOperation.getName()
            ),
            ModelAttributes.OPERATION
        );
        // container
        operationDefFacade.attributeValuesAsList("container").add(
            Object_2Facade.getPath(aContainer)
        );

        // stereotype
        boolean isException = false;
        if(umlOperation.getStereotypes().size() > 0) {
            operationDefFacade.attributeValuesAsList("stereotype").addAll(umlOperation.getStereotypes());

            // well-known stereotype <<exception>>
            if(operationDefFacade.attributeValuesAsList("stereotype").contains(Stereotypes.EXCEPTION)) {
                operationDefFacade.getValue().setRecordName(
                    ModelAttributes.EXCEPTION
                );
                operationDefFacade.attributeValuesAsList("stereotype").clear();
                isException = true;
            }
        }

        // annotation
        String annotation = this.getAnnotation(umlOperation);
        if (annotation.length() != 0)
        {
            operationDefFacade.attributeValuesAsList("annotation").add(annotation);
        }

        // visibility
        operationDefFacade.attributeValuesAsList("visibility").add(VisibilityKind.PUBLIC_VIS);

        // scope
        operationDefFacade.attributeValuesAsList("scope").add(ScopeKind.INSTANCE_LEVEL);

        // isQuery
        operationDefFacade.attributeValuesAsList("isQuery").add(
            Boolean.valueOf(umlOperation.isQuery())
        );

        // parameters
        List parameters = umlOperation.getParametersWithoutReturnParameter();

        if(parameters.size() > 0) {

            /**
             * In openMDX all operations have exactly one parameter with name 'in'. The importer
             * supports two forms how parameters may be specified:
             * 1) p0:t0, p1:t1, ..., pn:tn. In this case a class with stereotype <parameter> is created
             *    and p0, ..., pn are added as class attributes. Finally, a parameter with name 'in'
             *    is created with the created parameter type.
             * 2) in:t. In this case the the parameter with name 'in' is created with the specified type.
             */

            /**
             * Create the parameter type class. We need this class only in case 1. Because we only know
             * at the end whether we really need it, create it anyway but do not add it to the repository.
             */
            String capOperationName =
                operationName.substring(0,1).toUpperCase() +
                operationName.substring(1);

            Object_2Facade parameterTypeFacade = Object_2Facade.newInstance(
                new Path(
                    Object_2Facade.getPath(aContainer).toString() + capOperationName + "Params"
                ),
                ModelAttributes.STRUCTURE_TYPE
            );
            parameterTypeFacade.attributeValuesAsList("visibility").add(VisibilityKind.PUBLIC_VIS);
            parameterTypeFacade.attributeValuesAsList("isAbstract").add(Boolean.FALSE);
            parameterTypeFacade.attributeValuesAsList("isSingleton").add(Boolean.FALSE);
            parameterTypeFacade.attributeValuesAsList("container").addAll(
                Object_2Facade.newInstance(aContainer).attributeValuesAsList("container")
            );

            /**
             * Create parameters either as STRUCTURE_FIELD of parameterType (case 1) 
             * or as PARAMETER of modelOperation (case 2)
             */
            boolean createParameterType = true;
            boolean parametersCreated = false;
            for(
                Iterator it = parameters.iterator();
                it.hasNext();
            ) {
                UML1Parameter aParameter = (UML1Parameter)it.next();
                MappedRecord parameterDef = this.processParameter(
                    aParameter,
                    parameterTypeFacade.getDelegate()
                );
                /**
                 * Case 2: Parameter with name 'in'. Create object as PARAMETER.
                 */
                String fullQualifiedParameterName = Object_2Facade.getPath(parameterDef).getBase();
                if("in".equals(fullQualifiedParameterName.substring(fullQualifiedParameterName.lastIndexOf(':') + 1))) {
                    // 'in' is the only allowed parameter
                    if(parametersCreated) {
                        SysLog.error("Parameter format must be [p0:T0...pn:Tn | in:T], where T must be a class with stereotype " + Stereotypes.STRUCT);
                        throw new ServiceException(
                            ModelExceptions.MODEL_DOMAIN,
                            ModelExceptions.INVALID_PARAMETER_DECLARATION,
                            "Parameter format must be [p0:T0...pn:Tn | in:T], where T must be a class with stereotype " + Stereotypes.STRUCT,
                            new BasicException.Parameter("operation", operationDefFacade.getPath())
                        );
                    }
                    parameterTypeFacade = Object_2Facade.newInstance(
                        (Path)Object_2Facade.newInstance(parameterDef).attributeValue("type")
                    );
                    createParameterType = false;
                }
                /**
                 * Case 1: Parameter is attribute of parameter type. Create object as ATTRIBUTE.
                 */
                else {
                    // 'in' is the only allowed parameter
                    if(!createParameterType) {
                        SysLog.error("Parameter format must be [p0:T0, ... ,pn:Tn | in:T], where T must be a class with stereotype " + ModelAttributes.STRUCTURE_TYPE);
                        throw new ServiceException(
                            ModelExceptions.MODEL_DOMAIN,
                            ModelExceptions.INVALID_PARAMETER_DECLARATION,
                            "Parameter format must be [p0:T0, ... ,pn:Tn | in:T], where T must be a class with stereotype " + ModelAttributes.STRUCTURE_TYPE,
                            new BasicException.Parameter("operation", operationDefFacade.getPath())
                        );
                    }
                    this.createModelElement(
                        null,
                        parameterDef
                    );
                    parametersCreated = true;
                }

            }
            /**
             * Case 1: parameter type must be created
             */
            if(createParameterType) {
                this.createModelElement(
                    null,
                    parameterTypeFacade.getDelegate()
                );
            }
            // in-parameter
            Object_2Facade inParameterDefFacade = Object_2Facade.newInstance(
                newFeaturePath(
                    operationDefFacade.getPath(),
                    "in"
                ),
                ModelAttributes.PARAMETER
            );
            inParameterDefFacade.attributeValuesAsList("container").add(
                operationDefFacade.getPath()
            );
            inParameterDefFacade.attributeValuesAsList("direction").add(
                DirectionKind.IN_DIR
            );
            inParameterDefFacade.attributeValuesAsList("multiplicity").add(
                "1..1"
            );
            inParameterDefFacade.attributeValuesAsList("type").add(
                parameterTypeFacade.getPath()
            );
            this.createModelElement(
                null,
                inParameterDefFacade.getDelegate()
            );
        }

        // void in-parameter
        else {
            Object_2Facade inParameterDefFacade = Object_2Facade.newInstance(
                newFeaturePath(
                    operationDefFacade.getPath(),
                    "in"
                ),
                ModelAttributes.PARAMETER
            );
            inParameterDefFacade.attributeValuesAsList("container").add(
                operationDefFacade.getPath()
            );
            inParameterDefFacade.attributeValuesAsList("direction").add(
                DirectionKind.IN_DIR
            );
            inParameterDefFacade.attributeValuesAsList("multiplicity").add(
                "1..1"
            );
            inParameterDefFacade.attributeValuesAsList("type").add(
                toElementPath(
                    nameToPathComponent("org::openmdx::base"),
                    "Void"
                )
            );
            this.createModelElement(
                null,
                inParameterDefFacade.getDelegate()
            );
        }
        // Note:
        // return parameter is ignored for exceptions (operations with stereotype
        // exception)
        if(!isException) {
            Object_2Facade resultDefFacade = Object_2Facade.newInstance(
                newFeaturePath(
                    operationDefFacade.getPath(),
                    "result"
                ),
                ModelAttributes.PARAMETER
            );
            resultDefFacade.attributeValuesAsList("container").add(
                operationDefFacade.getPath()
            );
            resultDefFacade.attributeValuesAsList("direction").add(
                DirectionKind.RETURN_DIR
            );
            resultDefFacade.attributeValuesAsList("multiplicity").add(
                "1..1"
            );

            if(umlOperation.getReturnParameter().getType() == null) {
                this.error("Undefined return type for operation " + umlOperation.getQualifiedName());
            }
            else {
                resultDefFacade.attributeValuesAsList("type").add(
                    toElementPath(
                        nameToPathComponent(getScope(umlOperation.getReturnParameter().getType())),
                        getName(umlOperation.getReturnParameter().getType())
                    )
                );
                this.createModelElement(
                    null,
                    resultDefFacade.getDelegate()                   
                );
            }
        }

        // exceptions
        String allExceptions = this.getOperationExceptions(umlOperation);
        if (allExceptions != null) {
            StringTokenizer exceptions = new StringTokenizer(allExceptions, ",; ");
            while(exceptions.hasMoreTokens()) {
                String qualifiedExceptionName = exceptions.nextToken().trim();
                if (qualifiedExceptionName.indexOf("::") == -1) {
                    this.errors.println("Found invalid exception declaration <" + qualifiedExceptionName + "> for the operation " + umlOperation.getQualifiedName() + "; this exception is ignored unless a valid qualified exception name is specified.");
                }
                else {
                    String qualifiedClassName = qualifiedExceptionName.substring(0, qualifiedExceptionName.lastIndexOf("::"));
                    String scope = getScope(qualifiedClassName);
                    String name = getName(qualifiedClassName);
                    if (scope.length() == 0 || name.length() == 0)
                    {
                        this.errors.println("Found invalid exception declaration <" + qualifiedExceptionName + "> for the operation " + umlOperation.getQualifiedName() + "; this exception is ignored unless a valid qualified exception name is specified.");
                    }
                    else {
                        operationDefFacade.attributeValuesAsList("exception").add(
                            newFeaturePath(
                                toElementPath(nameToPathComponent(scope), name),
                                getName(qualifiedExceptionName)
                            )
                        );
                    }
                }
            }
        }
        this.createModelElement(
            null,
            operationDefFacade.getDelegate()
        );
    }

    //---------------------------------------------------------------------------
    private MappedRecord processParameter(
        UML1Parameter umlParameter,
        MappedRecord parameterType
    ) throws Exception {
        SysLog.detail("Processing parameter", umlParameter.getName());

        Object_2Facade parameterDefFacade = Object_2Facade.newInstance(
            newFeaturePath(
                Object_2Facade.getPath(parameterType),
                umlParameter.getName()
            ),
            ModelAttributes.STRUCTURE_FIELD
        );

        // container
        parameterDefFacade.attributeValuesAsList("container").add(
            Object_2Facade.getPath(parameterType)
        );

        parameterDefFacade.attributeValuesAsList("maxLength").add(new Integer(DEFAULT_PARAMETER_MAX_LENGTH));

        if(umlParameter.getType() == null) {
            this.error("Undefined type for parameter " + umlParameter.getQualifiedName());
        }
        else {
            parameterDefFacade.attributeValuesAsList("type").add(
                toElementPath(
                    nameToPathComponent(getScope(umlParameter.getType())),
                    getName(umlParameter.getType())
                )
            );
            parameterDefFacade.attributeValuesAsList("multiplicity").add(
                umlParameter.getStereotypes().size() > 0 ?
                    umlParameter.getStereotypes().iterator().next() :
                        DEFAULT_PARAMETER_MULTIPLICITY
            );
        }

        return parameterDefFacade.getDelegate();

    }

    //---------------------------------------------------------------------------
    public void processUMLDataType(
        UML1DataType umlDataType
    ) throws Exception {
        SysLog.detail("Processing primitive type", umlDataType.getQualifiedName());

        // ModelPrimitiveType object
        Object_2Facade primitiveTypeDefFacade = Object_2Facade.newInstance(
            toElementPath(
                nameToPathComponent(getScope(umlDataType.getQualifiedName())),
                umlDataType.getName()
            ),
            ModelAttributes.PRIMITIVE_TYPE
        );

        // container
        primitiveTypeDefFacade.attributeValuesAsList("container").add(
            toElementPath(
                nameToPathComponent(getScope(umlDataType.getQualifiedName())),
                getName(getScope(umlDataType.getQualifiedName()))
            )
        );

        /**
         * skip stereotype because its value 'Primitive'
         * was marked to note the difference between
         * ordinary classes and primitive types
         */

        // annotation
        String annotation = this.getAnnotation(umlDataType);
        if (annotation.length() != 0) {
            primitiveTypeDefFacade.attributeValuesAsList("annotation").add(annotation);
        }

        // isAbstract attribute
        primitiveTypeDefFacade.attributeValuesAsList("isAbstract").add(
            Boolean.valueOf(umlDataType.isAbstract())
        );

        // isSingleton attribute
        primitiveTypeDefFacade.attributeValuesAsList("isSingleton").add(
            Boolean.FALSE
        );

        // visibility attribute
        primitiveTypeDefFacade.attributeValuesAsList("visibility").add(VisibilityKind.PUBLIC_VIS);

        SysLog.detail("Primitive type", primitiveTypeDefFacade.getPath());
        createModelElement(
            null, 
            primitiveTypeDefFacade.getDelegate()
        );
    }

    //---------------------------------------------------------------------------
    public void processUMLGeneralization(UML1Generalization genDef) {
        //
    }

    //---------------------------------------------------------------------------
    private String toMOFVisibility(
        UML1VisibilityKind umlVisibility
    ) {
        if(UML1VisibilityKind.PRIVATE.equals(umlVisibility)) {
            return VisibilityKind.PRIVATE_VIS;
        }
        else if(UML1VisibilityKind.PUBLIC.equals(umlVisibility)) {
            return VisibilityKind.PUBLIC_VIS;
        }
        else
        {
            return VisibilityKind.PUBLIC_VIS;
        }
    }

    //---------------------------------------------------------------------------
    private String toMOFAggregation(
        UML1AggregationKind umlAggregation
    ) {
        if(UML1AggregationKind.COMPOSITE.equals(umlAggregation)) {
            return AggregationKind.COMPOSITE;
        }
        else if(UML1AggregationKind.AGGREGATE.equals(umlAggregation)) {
            return AggregationKind.SHARED;
        }
        else
        {
            return AggregationKind.NONE;
        }
    }

    //---------------------------------------------------------------------------
    private String toMOFMultiplicity(
        UML1MultiplicityRange range
    ) {
        return new StringBuilder(
        ).append(
            range.getLower()
        ).append(
            ".."
        ).append(
            "-1".equals(range.getUpper()) || "*".equals(range.getUpper()) ? 
                "n" : 
                range.getUpper()
        ).toString();
    }

    //---------------------------------------------------------------------------
    private boolean toMOFChangeability(
        UML1ChangeableKind umlChangeability
    ) {
        return UML1ChangeableKind.CHANGEABLE.equals(umlChangeability);
    }

    //---------------------------------------------------------------------------
    private boolean isAttributeDerived(
        UML1Attribute attribute
    ) {
        // if feature isDerived is set return it
        if(attribute.isDerived() != null) {
            return attribute.isDerived().booleanValue();
        }
        // otherwise try to retrieve the isDerived information from tagged values
        for(
                Iterator it = attribute.getTaggedValues().iterator();
                it.hasNext();
        ) {
            UML1TaggedValue taggedValue = (UML1TaggedValue)it.next();
            if("derived".equals(taggedValue.getType().getName()) && "true".equals(taggedValue.getDataValue())) {
                return true;
            }
        }
        return false;
    }

    //---------------------------------------------------------------------------
    private String getAnnotation(
        UML1ModelElement modelElement
    ) {
        StringBuilder annotation = new StringBuilder();
        for(String comment: modelElement.getComment()) {
            annotation.append(comment);
        }
        return annotation.toString();
    }

    //---------------------------------------------------------------------------
    private int getAttributeMaxLength(
        UML1Attribute attribute
    ) {
        // the information about the maxLength of an attribute is stored as a
        // tagged value  (openMDX choice)
        for(
                Iterator it = attribute.getTaggedValues().iterator();
                it.hasNext();
        ) {
            UML1TaggedValue taggedValue = (UML1TaggedValue)it.next();
            if ("maxLength".equals(taggedValue.getType().getName()))
            {
                return Integer.parseInt(taggedValue.getDataValue());
            }
        }
        return DEFAULT_ATTRIBUTE_MAX_LENGTH;
    }

    //---------------------------------------------------------------------------
    private String getOperationExceptions(
        UML1Operation operation
    ) {
        for(String comment: operation.getComment()) {
            if(comment.startsWith(THROWS_EXCEPTION_PREFIX)) {
                return comment.substring(THROWS_EXCEPTION_PREFIX.length());
            }
        }
        for(           
                Iterator it = operation.getTaggedValues().iterator();
                it.hasNext();
        ) {
            UML1TaggedValue taggedValue = (UML1TaggedValue)it.next();
            if ("exceptions".equals(taggedValue.getType().getName())) {
                return taggedValue.getDataValue();
            }
        }
        return null;
    }

    //---------------------------------------------------------------------------
    private String toAssociationEndName(
        String assEndNameWithQualifier
    ) {
        if (assEndNameWithQualifier != null && assEndNameWithQualifier.indexOf((char)10) != -1)
        {
            return assEndNameWithQualifier.substring(
                0,
                assEndNameWithQualifier.indexOf((char)10)
            );
        }
        else
        {
            return assEndNameWithQualifier;
        }
    }

    //---------------------------------------------------------------------------
    private List toAssociationEndQualifiers(
        String assEndNameWithQualifier
    ) throws ServiceException {
        List qualifiers = new ArrayList();
        if (assEndNameWithQualifier != null && assEndNameWithQualifier.indexOf('[') != -1)
        {
            String qualifierText = assEndNameWithQualifier.substring(
                assEndNameWithQualifier.indexOf('[') + 1,
                assEndNameWithQualifier.indexOf(']')
            );
            qualifiers = this.parseAssociationEndQualifierAttributes(qualifierText);
        }
        return qualifiers;
    }

    //---------------------------------------------------------------------------
    private void warning(
        String text
    ) {
        SysLog.warning(text);
        this.warnings.println("WARNING: " + text);
    }

    //---------------------------------------------------------------------------
    private void error(
        String text
    ) {
        SysLog.error(text);
        this.errors.println("ERROR:   " + text);
        this.hasErrors = true;
    }

    //---------------------------------------------------------------------------
    // Variables
    //---------------------------------------------------------------------------
    public static final short XMI_FORMAT_POSEIDON = 1;
    public static final short XMI_FORMAT_MAGICDRAW = 2;
    public static final short XMI_FORMAT_RSM = 3;
    public static final short XMI_FORMAT_EMF = 4;
    public static final String THROWS_EXCEPTION_PREFIX = "@throws";

    private PrintStream infos = null;
    private PrintStream errors = null;
    private PrintStream warnings = null;
    private final URL modelUrl;
    private final short xmiFormat;
    private final Map pathMap;


}

//--- End of File -----------------------------------------------------------

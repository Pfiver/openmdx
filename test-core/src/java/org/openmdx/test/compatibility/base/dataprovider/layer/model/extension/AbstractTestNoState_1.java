/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Name:        $Id: AbstractTestNoState_1.java,v 1.6 2007/11/19 17:22:06 hburger Exp $
 * Description: TestState_1 junit class
 * Revision:    $Revision: 1.6 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2007/11/19 17:22:06 $
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
 * 
 * Copyright (c) 2004-2006, OMEX AG, Switzerland
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
package org.openmdx.test.compatibility.base.dataprovider.layer.model.extension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.jdo.JDOException;
import javax.jdo.JDOHelper;
import javax.jdo.Transaction;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.TestCase;

import org.openmdx.application.log.AppLog;
import org.openmdx.base.accessor.generic.cci.ObjectFactory_1_0;
import org.openmdx.base.accessor.generic.cci.Object_1_0;
import org.openmdx.base.accessor.generic.view.Manager_1;
import org.openmdx.base.accessor.jmi.cci.JmiServiceException;
import org.openmdx.base.accessor.jmi.cci.RefObject_1_0;
import org.openmdx.base.accessor.jmi.cci.RefPackage_1_1;
import org.openmdx.base.accessor.jmi.spi.RefRootPackage_1;
import org.openmdx.base.exception.RuntimeServiceException;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.text.format.DateFormat;
import org.openmdx.compatibility.application.dataprovider.transport.ejb.cci.Dataprovider_1ConnectionFactoryImpl;
import org.openmdx.compatibility.base.application.container.LightweightContainer_1;
import org.openmdx.compatibility.base.application.container.SimpleServiceLocator;
import org.openmdx.compatibility.base.collection.SparseList;
import org.openmdx.compatibility.base.dataprovider.cci.AttributeSelectors;
import org.openmdx.compatibility.base.dataprovider.cci.DataproviderObject;
import org.openmdx.compatibility.base.dataprovider.cci.DataproviderObject_1_0;
import org.openmdx.compatibility.base.dataprovider.cci.Dataprovider_1_0;
import org.openmdx.compatibility.base.dataprovider.cci.Directions;
import org.openmdx.compatibility.base.dataprovider.cci.QualityOfService;
import org.openmdx.compatibility.base.dataprovider.cci.RequestCollection;
import org.openmdx.compatibility.base.dataprovider.cci.ServiceHeader;
import org.openmdx.compatibility.base.dataprovider.cci.SystemAttributes;
import org.openmdx.compatibility.base.dataprovider.exporter.XmlExporter;
import org.openmdx.compatibility.base.dataprovider.importer.xml.XmlImporter;
import org.openmdx.compatibility.base.dataprovider.layer.model.State_1_Attributes;
import org.openmdx.compatibility.base.dataprovider.layer.model.StopWatch_1;
import org.openmdx.compatibility.base.dataprovider.transport.adapter.Provider_1;
import org.openmdx.compatibility.base.dataprovider.transport.cci.Dataprovider_1_0Connection;
import org.openmdx.compatibility.base.dataprovider.transport.delegation.Connection_1;
import org.openmdx.compatibility.base.exception.StackedException;
import org.openmdx.compatibility.base.naming.Path;
import org.openmdx.compatibility.base.query.FilterOperators;
import org.openmdx.compatibility.base.query.FilterProperty;
import org.openmdx.compatibility.base.query.Quantors;
import org.openmdx.compatibility.base.time.SetupStopWatch;
import org.openmdx.compatibility.base.time.TreeStopWatch;
import org.openmdx.deployment1.accessor.basic.DeploymentConfiguration_1;
import org.openmdx.kernel.exception.BasicException;
import org.openmdx.model1.accessor.basic.cci.ModelElement_1_0;
import org.openmdx.model1.accessor.basic.cci.Model_1_0;
import org.openmdx.model1.accessor.basic.spi.Model_1;
import org.openmdx.model1.code.Multiplicities;
import org.openmdx.test.compatibility.state1.inclusive.cci.NonStatedInStatedContainer;
import org.openmdx.test.compatibility.state1.inclusive.cci.NonStatedInStatedContainerClass;
import org.openmdx.test.compatibility.state1.inclusive.cci.Segment;
import org.openmdx.test.compatibility.state1.inclusive.cci.SegmentClass;
import org.openmdx.test.compatibility.state1.inclusive.cci.StateA;
import org.openmdx.test.compatibility.state1.inclusive.cci.StateAC;
import org.openmdx.test.compatibility.state1.inclusive.cci.StateAClass;
import org.openmdx.test.compatibility.state1.inclusive.cci.StateC;
import org.openmdx.test.compatibility.state1.inclusive.cci.StateCClass;
import org.openmdx.test.compatibility.state1.inclusive.cci.StateD;
import org.openmdx.test.compatibility.state1.inclusive.cci.StateDClass;
import org.openmdx.test.compatibility.state1.inclusive.cci.StateNoState;
import org.openmdx.test.compatibility.state1.inclusive.cci.StateNoStateClass;
import org.openmdx.test.compatibility.state1.inclusive.cci.StatedAgain;
import org.openmdx.test.compatibility.state1.inclusive.cci.StatedAgainClass;
import org.openmdx.test.compatibility.state1.inclusive.jmi.InclusivePackage;
import org.openmdx.test.compatibility.state1.inclusive.query.StateDQuery;
import org.openmdx.test.compatibility.state1.inclusive.query.StateNoStateQuery;
/**
 * Propositions for next test driver:
 * 
 * executeOperation could take the requestCollection it uses as a (optional) 
 * parameter. This would allow control of UnitOfWork from the outside.
 * 
 * @author anyff
 */
public abstract class AbstractTestNoState_1
  extends TestCase
{

    /** 
     * Class to be used as Exception indication for executeOperation, that the 
     * exception should be ignored.
     *  
     * @author anyff
     */
    static class IgnoreEx extends Exception {

        /**
         * Implements <code>Serializable</code>
         */
        private static final long serialVersionUID = -3263875893283225472L;

        /**
         * Constructs an <code>Exception</code> with no specified detail message. 
         */
        public IgnoreEx() {
            super();
        }


    }
    private DatatypeFactory datatypeFactory;

    //---------------------------------------------------------------------------  
    /** Creates a new instance of AbstractTestNoState_1 */
    protected AbstractTestNoState_1(String name) {
        super(name);
    }

  //---------------------------------------------------------------------------  
  private Path getRootSegment(){
      StringTokenizer tokens = new StringTokenizer(getName(), "_");
      return new Path(
          "xri:@openmdx:org.openmdx.test.compatibility.state1.inclusive/provider/" +
          tokens.nextToken() +
          "/segment/Inclusive/inclusive/" +
          tokens.nextToken()
      );
  }

  //---------------------------------------------------------------------------    

    /**
     * Connector Deployment Unit Callback
     * 
     * @return the URLs of the connector deployment units
     */
    protected Path[] connectorDeploymentUnits(){
        return new Path[]{
            new Path("xri:@openmdx:org.openmdx.deployment1/provider/org:openmdx/segment/org:openmdx:test/configuration/junit/domain/apps/deploymentUnit/extendedconnectors")
        };
    }

    /** 
     * Provider Deployment Unit Callback
     * 
     * @return the URLs of the provider deployment units
     */
    protected Path[] providerDeploymentUnits(){
        return new Path[]{
            new Path("xri:@openmdx:org.openmdx.deployment1/provider/org:openmdx/segment/org:openmdx:test/configuration/junit/domain/apps/deploymentUnit/testnostate")
        };
    }

    /** 
     * Deployment configurations
     * 
     * @return the URLs of the deployment configurations
     */
    protected String[] deploymentConfigurations(){
        return new String[]{
            "xri://+resource/org/openmdx/test/deployment.configuration.xml",
            "xri://+resource/org/openmdx/test/compatibility/base/dataprovider/layer/model/statewithholes/deployment.configuration.xml"
        };
    }

  protected void setUp(
  ) throws Exception {
    try {
        this.datatypeFactory = DatatypeFactory.newInstance();
        if(! deployed){
            System.out.println("Deploying...");
            DeploymentConfiguration_1.createInstance(
                deploymentConfigurations()
            );
            new LightweightContainer_1(
                "TestNoState_1",
                connectorDeploymentUnits(),
                providerDeploymentUnits()
            );
            deployed = true;
      }
      System.out.println(">>>> **** Start Test: " + this.getName());
      AppLog.info("Start Test", this.getName());
      SetupStopWatch.instance().start();

      // get connection
      Dataprovider_1_0Connection remoteConnection = Dataprovider_1ConnectionFactoryImpl.createGenericConnection(
              SimpleServiceLocator.getInitialContext().lookup("org/openmdx/test/managing/explorer")
      );

      // intercept webservice transport for testing
      connection =
        remoteConnection;
//      new Dataprovider_1_0ConnectionImpl("http://localhost:8080/dataproviders/junits");
//      new Dataprovider_1_0ConnectionImpl(remoteConnection);      

      // initial paths      
      AbstractTestNoState_1.rootSegment = getRootSegment();

      _model = new Model_1();
      _model.addModels(
        Arrays.asList(
          new String[]{
            "org:openmdx:base",
            "org:openmdx:datastore1",
            "org:openmdx:compatibility:state1",
            "org:openmdx:compatibility:view1",
            "org:openmdx:compatibility:role1",
            "org:openmdx:test:compatibility:state1",
            "org:openmdx:test:compatibility:state1:inclusive"
          }
        )
      );

    }
    catch(RuntimeServiceException e) {
      AppLog.error("exception", e.getExceptionStack());
      throw e;
    } catch(JmiServiceException e) {
      AppLog.error("exception", e.getExceptionStack());
      throw e;
    } catch(JDOException e) {
      AppLog.error("exception", e);
      throw e;
    }
  }


  //---------------------------------------------------------------------------  
  protected void tearDown(
  ) {
    try {
      SetupStopWatch.instance().stop(this.getName(), SetupStopWatch.EXECUTION);
      System.out.println("<<<< **** End Test: " + this.getName());
      AppLog.info("End Test", getName());
    }
    catch(Exception e) {
      System.out.println("error in deactivating");
    }
  }

  //---------------------------------------------------------------------------
  public void NoneWithoutStates_Standard(
  ) throws Throwable {
    doAll(
        false, // supportsView
        false, // supportsHistory
        false, // readOnly
        false // xmlImport
    );
  }

  //---------------------------------------------------------------------------
  public void JdbcWithoutStates_Standard(
  ) throws Throwable {
      doAll(
          true, // supportsView
          false, // supportsHistory
          false, // xmlExport
          false // xmlImport
      );
  }

  //---------------------------------------------------------------------------
  public void JdbcWithoutStates_Standard_ReadOnly(
  ) throws Throwable {
      doAll(
          true, // supportsView
          false, // supportsHistory
          true, // xmlExport
          false // xmlImport
      );
  }

  //---------------------------------------------------------------------------
  public void JdbcWithoutStates_Standard_ExportImport(
  ) throws Throwable {
      doAll(
          true, // supportsView
          false, // supportsHistory
          true, // xmlExport
          true // xmlImport
      );
  }

  //---------------------------------------------------------------------------
  public void doAll(
      boolean supportsView,
      boolean supportsHistory,
      boolean xmlExport,
      boolean xmlImport
  ) throws Throwable {
      try {
          if(xmlExport){
            File xmlFile = testExport();
              if(xmlImport){
                  testImport(xmlFile);
              }
          } else {
              testIterationNonStated();
              testStatedObjectContainedInNonStated();
              if(supportsView) {
// TODO           testSharedRelationStateD(supportsHistory);
              }
              testEver();
              testReferenceToStatedObject();
              testReferenceBetweenStatedObjects();
              testNonStatedObjectContainedInStated();
              testSetOperation();
              testFind(supportsHistory);
//            testTimeRangeSearch(supportsHistory);
//            testDocu();
//            testRemoveOperation(supportsHistory);
//            testPeriodOperation(supportsHistory);
              testPerformance();
              testExceptions();
              testStateBasicNormalId(supportsHistory);
//            if(!supportsView) {
//                testStateBasicSpecialId(supportsHistory);
//            }
              testOperationParametersInPath(supportsHistory);
          }
      } catch (Exception exception){
          exception.printStackTrace();
          throw exception;
      }
 }

    //-------------------------------------------------------------------------    
    private boolean usesSortablePersistence() {
        return false;
    }

    //-------------------------------------------------------------------------    
    /** 
     * helper method for creating Package.
     * 
     * @param header        header for channel
     * @param dataprovider  dataprovider for channel
     * @return role1Package  initialized package for channel
     * @throws Exception
     */
    protected InclusivePackage createInclusivePackage(
      ServiceHeader header
    ) throws Exception {
        AbstractTestNoState_1.manager = new Manager_1(
          new Connection_1(
            new Provider_1(
              new RequestCollection(
                header,
                AbstractTestNoState_1.connection
              ),
              false
            ),
            false
          )
        );
        RefPackage rootPkg = new RefRootPackage_1(
            AbstractTestNoState_1.manager,
            null,
            null,
            false
        );
        InclusivePackage pkg = (InclusivePackage)rootPkg.refPackage("org:openmdx:test:compatibility:state1:inclusive");
        return pkg;
    }

  //-------------------------------------------------------------------------    
    private void showException(Throwable t) {
        if (t instanceof JmiServiceException) {
            JmiServiceException je = (JmiServiceException) t;
            System.out.println("JmiServiceException: " + je.getMessage());
            showException(je.getExceptionStack());
        }
        else if (t instanceof ServiceException) {
            ServiceException se = (ServiceException) t;
            System.out.println("ServiceException: " + se.getMessage());
            System.out.println(se.toString());
        }
        else if (t instanceof RuntimeServiceException){
            RuntimeServiceException se = (RuntimeServiceException) t;
            System.out.println("RuntimeServiceException: " + se.getMessage());
            showException(se.getExceptionStack());
        }
        else {
            t.getMessage();
            t.printStackTrace();
        }
    }


    //---------------------------------------------------------------------------  
    private void verifyException(
      BasicException e,
      int code,
      String description
    ) throws ServiceException {
      if(!assertException(e, code, description)) {
        AppLog.error("exception", e);
        throw new ServiceException(e);
      }
    }

    //---------------------------------------------------------------------------  
    private boolean assertException(
      BasicException e,
      int code,
      String description
    ) {
      BasicException latest = (BasicException)e.getExceptionStack().get(0);
      /*
      System.out.println(
        e.getStackedException().toString()
      );
      */
      if(latest.getExceptionCode() != code) {
        System.out.println("exception code should be " + code + " but is " + latest.getExceptionCode());
        return false;
      }
      else if(
        (description != null) &&
        !"".equals(description) &&
        !latest.getDescription().equals(description)
      ) {
        System.out.println("description should be " + description + " but is " + latest.getDescription());
        return false;
      }
      else {
        return true;
      }
    }

    //---------------------------------------------------------------------------
    /**
     * from org.openmdx.compatibility.base.dataprovider.transport.dispatching.Plugin_1
     */
    private void setDataproviderObjectValue(
      Object sourceValue,
      SparseList targetValue
    ) {
      if(sourceValue == null) {
      }
      else if(sourceValue instanceof SortedMap) {
        for(
          Iterator j = ((SortedMap)sourceValue).entrySet().iterator();
          j.hasNext();
        ) {
          Map.Entry entry = (Entry)j.next();
          targetValue.set(
            ((Number)entry.getValue()).intValue(),
            entry.getKey()
          );
        }
      }
      else if(sourceValue instanceof Set) {
        targetValue.addAll((Set)sourceValue);
      }
      else if(sourceValue instanceof List) {
        targetValue.addAll((List)sourceValue);
      }
      else {
        targetValue.add(
          sourceValue
        );
      }
    }

    private DataproviderObject toDataproviderObject(
        RefObject source
    ) throws ServiceException {
        if(source instanceof RefObject_1_0) {
            return toDataproviderObject(
                ((RefObject_1_0)source).refDelegate()
            );
        } else throw new UnsupportedOperationException(
            getClass().getName() + ": toDataproviderObject not yet supported for openMDX 2"
        );
    }

    private DataproviderObject toDataproviderObject(
      Object_1_0 source
    ) throws ServiceException {
      DataproviderObject target = new DataproviderObject(
        new Path(source.objGetPath())
      );
      target.values(SystemAttributes.OBJECT_CLASS).add(
        source.objGetClass()
      );
      ModelElement_1_0 classDef = _model.getElement(
        source.objGetClass()
      );

      // first get a value which must be present, to load the object in the cache
      source.objGetValue("stateValidTo");
//SOID      // source.objGetValue(State_1_Attributes.STATED_OBJECT_IDENTITY);
//      Set defaultFetchGroup = source.objDefaultFetchGroup(); 
//      
//      if (classDef.attributeNames().contains(State_1_Attributes.STATE_VALID_FROM)) {
//          defaultFetchGroup.add(State_1_Attributes.STATED_OBJECT_IDENTITY);
//          defaultFetchGroup.add(State_1_Attributes.UNDERLYING_STATE);
//      }
      for(
        Iterator i = source.objDefaultFetchGroup().iterator(); //defaultFetchGroup.iterator(); //source.objDefaultFetchGroup().iterator();
        i.hasNext();
      ) {
        String attribute = (String)i.next();

        // for debugging
        if (attribute.equals("view")) {
          System.out.println("attribute:" + attribute);
        }

        if(!attribute.equals(SystemAttributes.OBJECT_INSTANCE_OF)
         && !attribute.equals("view")
        ) {
          ModelElement_1_0 attributeDef = _model.getFeatureDef(
            classDef,
            attribute,
            true
          );
          assertNotNull("Definition for attribute '" + attribute + "' missing", attributeDef);
          ModelElement_1_0 attributeType = _model.getDereferencedType(
            attributeDef.values("type").get(0)
          );
          // structure types are not supported
          if(_model.isStructureType(attributeType)) {
            throw new ServiceException(
              StackedException.DEFAULT_DOMAIN,
              StackedException.ASSERTION_FAILURE,
              new BasicException.Parameter[]{
                new BasicException.Parameter("object", source),
                new BasicException.Parameter("field", attribute),
                new BasicException.Parameter("type", attributeType)
              },
              "structure types can not be transferred to DataproviderObjects"
            );
          }
          String multiplicity = (String)attributeDef.values("multiplicity").get(0);
          if(Multiplicities.SET.equals(multiplicity)) {
            this.setDataproviderObjectValue(
              source.objGetSet(attribute),
              target.clearValues(attribute)
            );
          }
          else if(Multiplicities.LIST.equals(multiplicity) || Multiplicities.MULTI_VALUE.equals(multiplicity)) {
            this.setDataproviderObjectValue(
              source.objGetList(attribute),
              target.clearValues(attribute)
            );
          }
          else if(Multiplicities.SPARSEARRAY.equals(multiplicity)) {
            this.setDataproviderObjectValue(
              source.objGetSparseArray(attribute),
              target.clearValues(attribute)
            );
          }
          else {
            this.setDataproviderObjectValue(
              source.objGetValue(attribute),
              target.clearValues(attribute)
            );
          }
        }
      }
      return target;
    }


    //---------------------------------------------------------------------------  
    /**
     * Execute the operation and test for exception ex.
     * 
     * Returns DataproviderObject_1_0 except in operation find where it 
     * returns a list of DataproviderObject_1_0. 
     * 
     * The request for find is constructed from the object: 
     * path = object.path
     * filters: each attribute of object is treated as the attribute name in a 
     * filter, it's value showing the desired value.
     *
     */
    private Object executeOperation(
        String tname,
        short operation,
        DataproviderObject object,
        String requestedAt,
        String requestedFor,
        Exception expected
    ) {
        Object result = null;
        if(operation == extentOperation) tname += " via extent";
        System.out.println("test: " + tname);
//      if(requestedFor != null) System.out.println("requestedFor="+ requestedFor);
//      if(requestedAt != null) System.out.println("requestedAt="+ requestedAt);
        ServiceHeader header = new ServiceHeader(
            "anyff",
            null,
            false,
            new QualityOfService(),
            prepareTimestamp(requestedAt),
            prepareDate(requestedFor)
        );

        RequestCollection requests = new RequestCollection(
            header,
            AbstractTestNoState_1.connection
        );

        try {
          requests.beginUnitOfWork(false);
            switch (operation) {
                case createOperation:
                    result = requests.addCreateRequest(object);
                    break;
                case getOperation:
                    result = requests.addGetRequest(new Path(object.path()));
                    break;
                case modifyOperation:
                    result = requests.addModifyRequest(object);
                    break;
                case replaceOperation:
                    result = requests.addReplaceRequest(object);
                    break;
                case setOperation:
                    result = requests.addSetRequest(object);
                    break;
                case removeOperation:
                    short selector = AttributeSelectors.NO_ATTRIBUTES;
                    if (object.getValues("selector") != null) {
                        selector = ((Short)object.getValues("selector").get(0)).shortValue();
                    }

                    result = requests.addRemoveRequest(
                        object.path(),
                        selector,
                        null
                    );

                    break;
                case findOperation: {
                    ArrayList filters = new ArrayList();
                    // all the attributes of the object are treated as 
                    // properties:
                    for (Iterator i = object.attributeNames().iterator();
                        i.hasNext();
                    ) {
                        String attributeName = (String) i.next();
                        // introducing some technical attributes:

                        if (attributeName.equals(HISTORY_REQUEST_START)) {
                            filters.add(
                                new FilterProperty(
                                    Quantors.THERE_EXISTS,
                                    SystemAttributes.MODIFIED_AT,
                                    FilterOperators.IS_GREATER_OR_EQUAL,
                                    object.values(attributeName).toArray()
                                )
                            );
                        }
                        else if (attributeName.equals(HISTORY_REQUEST_END)) {
                            filters.add(
                                new FilterProperty(
                                    Quantors.THERE_EXISTS,
                                    AbstractTestNoState_1.INVALIDATED_AT,
                                    FilterOperators.IS_LESS_OR_EQUAL,
                                    object.values(attributeName).toArray()
                                )
                             );
                        }
                        else if (attributeName.equals(VALID_REQUEST_START)) {

                            filters.add(
                                new FilterProperty(
                                    Quantors.FOR_ALL, // THERE_EXISTS, // WARNING: does not work if valid_to is null!
                                    AbstractTestNoState_1.VALID_TO,
                                    FilterOperators.IS_GREATER_OR_EQUAL,
                                    object.values(attributeName).toArray()
                                )
                            );
                        }
                        else if (attributeName.equals(VALID_REQUEST_END)) {

                            filters.add(
                                new FilterProperty(
                                    Quantors.FOR_ALL, // Quantors.THERE_EXISTS, // WARNING: does not work if validFrom is null!
                                    AbstractTestNoState_1.VALID_FROM,
                                    FilterOperators.IS_LESS_OR_EQUAL,
                                    object.values(attributeName).toArray()
                                )
                            );
                        }
                        else {
                            // it is a normal attribute
                            filters.add(
                                new FilterProperty(
                                    Quantors.THERE_EXISTS,
                                    attributeName,
                                    FilterOperators.IS_IN,
                                    object.values(attributeName).toArray()
                                )
                            );
                        }
                    }

                    result = requests.addFindRequest(
                        object.path(),
                        (FilterProperty[])filters.toArray(new FilterProperty[filters.size()]),
                        AttributeSelectors.ALL_ATTRIBUTES,
                        0,
                        FIND_BATCH_SIZE,
                        Directions.ASCENDING
                    );
                } break;
                case extentOperation: {
                    ArrayList filters = new ArrayList();
                    // all the attributes of the object are treated as 
                    // properties:
                    for (Iterator i = object.attributeNames().iterator();
                        i.hasNext();
                    ) {
                        String attributeName = (String) i.next();
                        // introducing some technical attributes:

                        if (attributeName.equals(HISTORY_REQUEST_START)) {
                            filters.add(
                                new FilterProperty(
                                    Quantors.THERE_EXISTS,
                                    SystemAttributes.MODIFIED_AT,
                                    FilterOperators.IS_GREATER_OR_EQUAL,
                                    object.values(attributeName).toArray()
                                )
                            );
                        }
                        else if (attributeName.equals(HISTORY_REQUEST_END)) {
                            filters.add(
                                new FilterProperty(
                                    Quantors.THERE_EXISTS,
                                    AbstractTestNoState_1.INVALIDATED_AT,
                                    FilterOperators.IS_LESS_OR_EQUAL,
                                    object.values(attributeName).toArray()
                                )
                             );
                        }
                        else if (attributeName.equals(VALID_REQUEST_START)) {
                            //
                            // WARNING: does not work if valid_to is null!
                            //
                            filters.add(
                                new FilterProperty(
                                    Quantors.THERE_EXISTS,
                                    AbstractTestNoState_1.VALID_TO,
                                    FilterOperators.IS_GREATER_OR_EQUAL,
                                    object.values(attributeName).toArray()
                                )
                            );
                        }
                        else if (attributeName.equals(VALID_REQUEST_END)) {
                            //
                            // WARNING: does not work if validFrom is null!
                            //
                            filters.add(
                                new FilterProperty(
                                    Quantors.THERE_EXISTS,
                                    AbstractTestNoState_1.VALID_FROM,
                                    FilterOperators.IS_LESS_OR_EQUAL,
                                    object.values(attributeName).toArray()
                                )
                            );
                        }
                        else {
                            //
                            // it is a normal attribute
                            //
                            filters.add(
                                new FilterProperty(
                                    Quantors.THERE_EXISTS,
                                    attributeName,
                                    FilterOperators.IS_IN,
                                    object.values(attributeName).toArray()
                                )
                            );
                        }
                    }
//                  filters.add(
//                      new FilterProperty(
//                          Quantors.THERE_EXISTS,
//                          SystemAttributes.OBJECT_INSTANCE_OF,
//                          FilterOperators.IS_IN,
//                          new String[]{"org:openmdx:test:compatibility:state1:inclusive:StateA"}
//                      )
//                  );
                    filters.add(
                        SHARED_REFS.contains(object.path().getBase()) ? new FilterProperty(
                            Quantors.THERE_EXISTS,
                            SystemAttributes.OBJECT_IDENTITY,
                            FilterOperators.IS_IN,
                            new String[]{object.path().getParent().toXri()}
                        ) : new FilterProperty(
                            Quantors.THERE_EXISTS,
                            SystemAttributes.OBJECT_IDENTITY,
                            FilterOperators.IS_LIKE,
                            new String[]{object.path().getChild(":*").toXri()}
                        )
                    );
                    result = requests.addFindRequest(
                        object.path().getPrefix(5).add("extent"),
                        (FilterProperty[])filters.toArray(new FilterProperty[filters.size()]),
                        AttributeSelectors.ALL_ATTRIBUTES,
                        0,
                        EXTENT_BATCH_SIZE,
                        Directions.ASCENDING
                    );
                } break;
                default:
                    fail(
                        "test: " + tname +
                        "unsupported operation: " + operation
                    );
            }
          requests.endUnitOfWork();
            if (expected != null
                && !(expected instanceof IgnoreEx)
            ) {
              assertNull(
                "test: " + tname +
                " expected exception " + expected.getClass() +
                "\n but none was thrown ",
                expected
              );
            }


        }
        catch(ServiceException es) {
          if (!(expected instanceof IgnoreEx)) {

              if (expected != null &&
                  expected instanceof ServiceException
              ) {
                  ServiceException expectedSE = (ServiceException)expected;
                  assertTrue(
                    "test: " + tname +
                    " expected exception " + expected.toString() +
                    "\n but caught exception " + es.toString(),
                    this.assertException(
                      es.getExceptionStack(),
                      expectedSE.getExceptionCode(),
                      expectedSE.getExceptionStack().getDescription()
                    )
                  );
              }
              else {
                showException(es);
                fail(
                  "test: " + tname +
                  " expected no exception " +
                  "\n but caught exception " + es.toString()
                );
              }
          }
        }
        catch(Exception e) {
          System.out.println(e.getMessage());
          e.printStackTrace();
          fail(
            "test: " + tname +
            " expected ServiceException, but got " + e.getClass()
          );
          System.out.println(e.getMessage());
          e.printStackTrace();
        }
        return result;
    }

    // --------------------------------------------------------------------------
    /**
     * returns the actual time
     */
    private String timeNow() {
        String timePoint = DateFormat.getInstance().format(new Date());
        return timePoint;
    }

    //---------------------------------------------------------------------------  
    /**
     * Converts a stringified date into a java.util.Date and back to String to
     * ensure correct format. 
     * 
     * Date format to use: dd.mm.yyyy
     */
    private String prepareDate(String date) {
        return date == null ?
            null :
            date.substring(6) + date.substring(3, 5) + date.substring(0, 2);
    }

    /**
     * Converts a stringified date into a java.util.Date and back to String to
     * ensure correct format. 
     * 
     * Date format to use: dd.mm.yyyy or yyyymmddThhmmss.mmm
     */
    private String prepareTimestamp(String date) {
        return
            date == null ? null :
            date.length() == 10 ? prepareDate(date) + "T000000.000Z" :
            date;
    }

    // --------------------------------------------------------------------------
//    /**
//     * returns the actual time as long
//     */
//    private Long timeNowAsLong() {
//        Long timePoint = new Long( new Date().getTime());
//        return timePoint;
//    }

    //---------------------------------------------------------------------------  
    /**
     * Converts a stringified date into a java.util.Date. 
     * 
     * Date format to use: dd.mm.yyyy
     */
    private XMLGregorianCalendar prepareDateAsDate(String dateString) {
        assertNotNull("Null date", dateString);
        return this.datatypeFactory.newXMLGregorianCalendar(
            dateString.substring(6) + '-' + dateString.substring(3, 5) + '-' + dateString.substring(0, 2)
        );
    }

    /**
     * set validity and invalidatedAt
     * for comparing results, its enough to have some value in invalidated field
     */
    private DataproviderObject setInvalid(
      DataproviderObject object,
      String from,
      String to
    ) {
      this.setValidity(object, from, to);
      object.values(AbstractTestNoState_1.INVALIDATED_AT).set(0, INVALIDATION_INDICATOR);
      return object;
    }

    //---------------------------------------------------------------------------  
    private DataproviderObject setValidity(
      DataproviderObject object,
      String from,
      String to
    ) {
        if (from != null) {
            object.values(VALID_FROM).set(0, prepareDate(from));
        }
        else {
            object.attributeNames().remove(VALID_FROM);
        }

        if (to != null) {
            object.values(VALID_TO).set(0, prepareDate(to));
        }
        else {
            object.attributeNames().remove(VALID_TO);
        }

        object.attributeNames().remove(INVALIDATED_AT);
      /*
      try {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy,z");        
        DateFormat df = DateFormat.getInstance();
        String validFrom = from == null ? "ever" : df.format(sdf.parse(from + ",GMT"));
        String validTo = to == null ? "ever" : df.format(sdf.parse(to + ",GMT"));      
        object.path().setTo(
          object.path().getParent().getChild(
            object.path().getBase() + ";validFrom=" + validFrom + ";validTo=" + validTo
          )
        );
        
      }
      catch(ParseException e) {
        throw new RuntimeServiceException(e);
      }
      */
      return object;
    }

    //---------------------------------------------------------------------------  
    /** 
     *set or clear the attribute. Existing values are removed. If the SparseList
     * should contain empty values, those must be represented by a "-" in the
     * values array.
     *
     */
    private void setAttribute(
        String attribute,
        Object[] values,
        DataproviderObject dpo
    ) {
        if (values != null) {

            dpo.clearValues(attribute);

            for (int i = 0;
                i < values.length;
                i++
            ) {
                if (! "-".equals(values[i])) {
                    dpo.values(attribute).set(i, values[i]);
                }
            }
        }
        else {
            dpo.clearValues(attribute);
        }
    }


    //---------------------------------------------------------------------------  
    /**
     * Prepares an object. 
     * 
     * All attributes of the class className are changed. If you need a 
     * more complex object, just call this function several times with the
     * same object.
     * 
     */
    private DataproviderObject prepareObject(
        String className,
        DataproviderObject dpo,
        String[] id, // id of the object (last part of path)
        Object stateEntry, // value for state attributes 
        Object[] valueEntry, // value for value attributes
        String role // role for StateCRole
    ) {
        /*
        if (
            dpo != null && 
            role != null && 
            dpo.path().get(dpo.path().size() - 2).equals("role") 
        ) {
            // remove last part and add new role
            dpo.path().remove(dpo.path().size() - 1);
            dpo.path().add(role);
        }
        else if (
            dpo != null &&
            role != null
        ) {
            dpo.path().add("role").add(role);
        }
        
        // removal of roles is automatic; the only case where this makes 
        // sense is when RoleClass or RoleClassExtension is created. Those
        // create a new object instead of appending to the existing
        */

        Object[] stateEntries = new Object[1];
        stateEntries[0] = stateEntry;
        if (className.equals("StateNoState")) {
            dpo = new DataproviderObject(new Path(rootSegment).addAll(id));
            dpo.clearValues(SystemAttributes.OBJECT_CLASS).
                add(0, "org:openmdx:test:compatibility:state1:inclusive:StateNoState");
            setAttribute("stateAttr", stateEntries, dpo);
            setAttribute("value", valueEntry, dpo);
        }
        else if (className.equals("StateB")) {
            dpo = new DataproviderObject(new Path(rootSegment).addAll(id));
            dpo.clearValues(SystemAttributes.OBJECT_CLASS).
                add(0, "org:openmdx:test:compatibility:state1:inclusive:StateB");
            setAttribute("stateAttr", stateEntries, dpo);
            setAttribute("value", valueEntry, dpo);
        }
        else if (className.equals("StateA")) {
            dpo = new DataproviderObject(new Path(rootSegment).addAll(id));
            dpo.clearValues(SystemAttributes.OBJECT_CLASS).
                add(0, "org:openmdx:test:compatibility:state1:inclusive:StateA");
            setAttribute("stateAttr", stateEntries, dpo);
            setAttribute("value", valueEntry, dpo);
        }
        else if (className.equals("StateADerived")) {
            dpo = new DataproviderObject(new Path(rootSegment).addAll(id));
            dpo.clearValues(SystemAttributes.OBJECT_CLASS).
                add(0, "org:openmdx:test:compatibility:state1:inclusive:StateADerived");
            setAttribute("stateAttr", stateEntries, dpo);
            setAttribute("value", valueEntry, dpo);
            setAttribute("stateADerived", stateEntries, dpo);
            setAttribute("valueADerived", valueEntry, dpo);
        }
        else if (className.equals("StateC")) {
            dpo = new DataproviderObject(new Path(rootSegment).addAll(id));
            dpo.clearValues(SystemAttributes.OBJECT_CLASS).
                add(0, "org:openmdx:test:compatibility:state1:inclusive:StateC");
            setAttribute("stateAttr", stateEntries, dpo);
            setAttribute("value", valueEntry, dpo);

        }
        else if (className.equals("StateCRole")) {
            if (dpo == null) {
                dpo = new DataproviderObject(new Path(rootSegment).addAll(id));
            }

            dpo.clearValues(SystemAttributes.OBJECT_CLASS).
                add("org:openmdx:test:compatibility:state1:inclusive:StateCRole");
            dpo.path().add("role");
            dpo.path().add(role);
            setAttribute("name", stateEntries, dpo);
            setAttribute("num", valueEntry, dpo);

        }
        else {
            fail(
                "prepareObject: unknown class:" + className
            );
        }

        return dpo;
    }

//    //---------------------------------------------------------------------------  
//    /*
//      private boolean isDuplicateException(
//          JmiServiceException se
//      ) {
//          boolean duplicateEx = false;
//          duplicateEx =  
//          ((StackedException)se.getStackedException().getExceptionStack().get(0))
//              .getExceptionCode() == StackedException.DUPLICATE;
//        
//          return duplicateEx;
//      }
//    */
////  //---------------------------------------------------------------------------  
////  private boolean isNotFoundException(BasicException se) {
////    boolean duplicateEx = false;
////    duplicateEx =
////      ((BasicException) se.getExceptionStack().get(0)).getExceptionCode()
////        == StackedException.NOT_FOUND;
////    return duplicateEx;
////  }

  //-------------------------------------------------------------------------    
    private void compareObjects(
        DataproviderObject_1_0 result,
        DataproviderObject_1_0 expect,
        String pathCompletion
    ) {
      // remove empty attributes
      for(
        Iterator i = result.attributeNames().iterator();
        i.hasNext();
      ) {
        String attributeName = (String)i.next();
        if((result.getValues(attributeName) == null) || (result.values(attributeName).size() == 0)) {
          i.remove();
        }
      }
//        if (expect.attributeNames().size() !=  result.attributeNames().size()) {
            ArrayList expectAttrs = new ArrayList();
            for (Iterator i = expect.attributeNames().iterator(); i.hasNext(); ){
                String attribute = (String) i.next();
                // empty value lists are treated like non existing attribute
                if (expect.getValues(attribute).size() > 0) {
                    expectAttrs.add(attribute);
                }
                else {
                    // remove it from the expected object
                    i.remove();
                }
            }


            for (Iterator i = result.attributeNames().iterator(); i.hasNext(); ) {
                String attribute = (String) i.next();

                if (result.getValues(attribute).size() > 0) {
//                    assertTrue(
//                        "attribute only in result " + attribute, 
//                        expectAttrs.contains(attribute)
//                    );
                    expectAttrs.remove(attribute);
                }
                else {
                    i.remove();
                }

            }

//SOID            for (Iterator a = expectAttrs.iterator(); a.hasNext();) {
//                String attribute = (String)a.next();
//                if (attribute.equals(State_1_Attributes.UNDERLYING_STATE)){
//                    a.remove();
//                }
//            }
            //
            // Ignore embedded objects
            //
            for(
                Iterator i = expectAttrs.iterator();
                i.hasNext();
            ){
                String attribute = (String) i.next();
                if(attribute.indexOf(':') > 0) i.remove();
            }
    
            assertTrue("attributes only in expected " + expectAttrs, expectAttrs.isEmpty());

        for (Iterator i = result.attributeNames().iterator();
            i.hasNext();
        ) {
            String attributeName = (String) i.next();
            if (attributeName.equals(AbstractTestNoState_1.INVALIDATED_AT)) {
                if((expect.getValues(attributeName) == null) || (result.getValues(attributeName) == null)) {
                    // just make sure that they both have an entry
                    fail("attribute " + attributeName +
                        " only set in one object. " +
                        " Expected: " + expect.getValues(attributeName) +
                        " Received: " + result.getValues(attributeName)
                    );
                }

            }
            else if (
                !SystemAttributes.MODIFIED_AT.equals(attributeName) &&
                !SystemAttributes.CREATED_AT.equals(attributeName) &&
                !SystemAttributes.CREATED_AT.equals(attributeName) &&
                !SystemAttributes.MODIFIED_AT.equals(attributeName) &&
                !"roleType".equals(attributeName) &&
                !attributeName.startsWith(SystemAttributes.VIEW_PREFIX) &&
                !attributeName.startsWith(SystemAttributes.CONTEXT_PREFIX) &&
                !attributeName.startsWith(SystemAttributes.OBJECT_IDENTITY)
//SOID                 && !attributeName.equals(State_1_Attributes.UNDERLYING_STATE)
            ) {
                if(
                  (result.getValues(attributeName) != null) &&
                  (result.getValues(attributeName).size() == 1) &&
                  (result.getValues(attributeName).get(0) == null)
                ) {
                  fail("attribute with null value " + result.path() + "; " + attributeName);
                }
                SparseList resultValue = result.getValues(attributeName) == null
                  ? null
                  : result.values(attributeName).size() == 0
                    ? null
                    : result.values(attributeName);
                SparseList expectValue = expect.getValues(attributeName) == null
                  ? null
                  : expect.values(attributeName).size() == 0
                    ? null
                    : expect.values(attributeName);

                if (expectValue != null
                    && expectValue.get(0) instanceof Number
                    && !(expectValue.get(0) instanceof BigDecimal)
                    && resultValue.get(0) instanceof BigDecimal
                ) {
                    if (expectValue.get(0) instanceof Integer) {
                        for (int j = 0; j < expectValue.size(); j++) {
                            Integer in = (Integer) expectValue.get(j);
                            if (in != null) {
                                BigDecimal bd = new BigDecimal(in.intValue());
                                expectValue.remove(j);
                                expectValue.set(j, bd);
                            }
                        }
                    }
                    else if (expectValue.get(0) instanceof Long) {
                        for (int j = 0; j < expectValue.size(); j++) {
                            Long in = (Long) expectValue.get(j);
                            if (in != null) {
                                BigDecimal bd = new BigDecimal(in.longValue());
                                expectValue.remove(j);
                                expectValue.set(j, bd);
                            }
                        }
                    }
                    else if (expectValue.get(0) instanceof Short) {
                        for (int j = 0; j < expectValue.size(); j++) {
                            Short in = (Short) expectValue.get(j);
                            if (in != null) {
                                BigDecimal bd = new BigDecimal(in.shortValue());
                                expectValue.remove(j);
                                expectValue.set(j, bd);
                            }
                        }
                    }
                }


                assertEquals("comparing values of attribute " + attributeName, expectValue, resultValue);
            }
        }

        /**
         * CR0001012
         *
        // compare path 
        if (pathCompletion == null) {
            assertTrue(" differing paths : " + 
                "\n Expected: " + expect.path() +
                "\n Received: " + result.getValues(SystemAttributes.OBJECT_IDENTITY).get(0),
                result.getValues(SystemAttributes.OBJECT_IDENTITY).equals(expect.path())
            );
        }
        else {
            Path resultPath = (Path) result.path().clone();
            Path expectPath = (Path) expect.path().clone();
            
            
            assertTrue(" expected path with " + 
                pathCompletion + "/<someNumber>, "+
                "/n but received: "+ result.path(),
                isStateNumber(((String)resultPath.remove(resultPath.size()-1))) &&
                pathCompletion.equals(resultPath.remove(resultPath.size()-1))
            );
                 
            assertTrue(" differing paths : " + 
                "\n Expected: " + expect.path() +
                "\n Received: " + result.path(),
                resultPath.equals(expectPath)
           );
        }
        */
    }

//  //-------------------------------------------------------------------------    
//    /** 
//     * remove entries which were invalidated before the time specified.
//     * <p>
//     */
//    private List removeResultsBefore(
//        List results,
//        String date
//    ) {
//        ArrayList res = new ArrayList();
//        for (Iterator i = results.iterator();
//            i.hasNext();
//        ) {
//            DataproviderObject_1_0 obj = (DataproviderObject_1_0) i.next();
//            if (obj.getValues(AbstractTestNoState_1.INVALIDATED_AT) == null ||
//                obj.getValues(AbstractTestNoState_1.INVALIDATED_AT).isEmpty() ||
//                date.compareTo((String)obj.getValues(AbstractTestNoState_1.INVALIDATED_AT).get(0)) < 0
//            ) {
//                res.add(obj);
//            }
//        }
//        return res;
//    }


    private void printSorted(
        String attribute,
        Collection objects
    ) {
        Object smallestObject = null;
        Comparable smallestValue = null;
        List remaining = new ArrayList();
        for (Iterator o = objects.iterator(); o.hasNext(); ) {
            DataproviderObject_1_0 object = (DataproviderObject_1_0)o.next();

            if (object.getValues(attribute) == null ||
                object.getValues(attribute).size() == 0
            ) {
                System.out.println("" + object);
            }
            else {
                remaining.add(object);
                if (smallestValue == null ||
                    smallestValue.compareTo((Comparable) object.getValues(attribute).get(0)) < 0
                ) {
                    smallestValue = (Comparable) object.getValues(attribute).get(0);
                    smallestObject = object;
                }
            }

        }

        while (smallestObject != null) {
            System.out.println("" + smallestObject);
            remaining.remove(smallestObject);
            smallestObject = null;
            smallestValue = null;

            for (Iterator o = remaining.iterator(); o.hasNext(); ) {
                DataproviderObject_1_0 object = (DataproviderObject_1_0)o.next();
                if (smallestValue == null ||
                    smallestValue.compareTo(object.getValues(attribute).get(0)) <= 0
                ) {
                    smallestValue = (Comparable)object.getValues(attribute).get(0);
                    smallestObject = object;
                }
            }

        }
    }


    /**
     * find out if the shorter path is contained in the longer, beginning
     * from start, regardless of any operation parameters
     * 
     * @param shorter
     * @param longer
     */
    private boolean startsWithNoOP(
        Path longer,
        Path shorter
    ) {
        boolean equal = true;
        for (int i = 0; i < shorter.size() && i < longer.size() && equal; i++) {
            String shortComp = shorter.get(i);

            int pos = shortComp.indexOf(';');
            if (pos >0 ) {
                shortComp = shortComp.substring(0,pos);
            }

            String longComp = longer.get(i);
            pos = longComp.indexOf(';');
            if (pos>0) {
                longComp = longComp.substring(0,pos);
            }

            equal = longComp.equals(shortComp);
        }

        if (equal && shorter.size() <= longer.size() ) {
            return true;
        }

        return false;
    }


  //-------------------------------------------------------------------------    
    /** 
     * do checks for several objects.
     * The objects in the two lists may be at random!
     * 
     * @param results is a list containing the results.
     * @param pathCompletion additional components the path must contain
     * @param ordered if true the objects must be in the same order
     * @param supportsHistory 
     * @param expect  an Array containing the expected results.
     * @return sorted List of results.
     */
    private List checkResultList(
        List results,
        DataproviderObject_1_0[] expectArray,
        String pathCompletion,
        boolean ordered, boolean supportsHistory
    ) {
        // first get entire list:
        ArrayList allResults = new ArrayList();
        for(
            Iterator i = results.iterator();
            i.hasNext();
        ) allResults.add(i.next());

        ArrayList sortedResults = new ArrayList();

        ArrayList expectedList = new ArrayList();
        if(!supportsHistory) expectArray = excludeInvalidatedStates(expectArray);
        for (int i = 0; i < expectArray.length; i++) {
            if (expectArray[i] != null) {
                expectedList.add(expectArray[i]);
            }
        }
        if (SHOW_COMPARE) {
            System.out.println("==================");
            System.out.println("Received: ");
            printSorted("object_validFrom", allResults);
//            for (int i = 0; i < allResults.size(); i++) {
//                System.out.println("Received: " + allResults.get(i));
//            }
            System.out.println("----");

            System.out.println("Expected: ");
            printSorted("object_validFrom", expectedList);
//            for (int i = 0; i < expect.length; i++) {
//                System.out.println("Expected: " + expect[i]);
//            }
            System.out.println("==================");
        }
        assertEquals("result size", expectedList.size(), allResults.size());

        for (int i = 0;
            i < expectedList.size();
            i++
        ) {
            if (ordered) {
                // objects must be at same position
                compareObjects(
                    (DataproviderObject_1_0)allResults.get(i),
                    (DataproviderObject_1_0)expectedList.get(i),
                    pathCompletion
                );
            }
            else {
                boolean found = false;
                Error error = null;

                for (int j = 0;
                    j < expectedList.size() && !found;
                    j++
                ) {
                    // in the presence of states, there may be several states
                    // per object, wich all would match the path (hopefully).
                    // Thus have to try all states to find if one eventually 
                    // matches.
                    try {
                        Path cpath = ((DataproviderObject_1_0)allResults.get(j)).path();
                        // test only those which have a chance to match
                        if (
                            startsWithNoOP(cpath, (((DataproviderObject_1_0)expectedList.get(i)).path()))

                        //cpath.toString().startsWith(
                        //     (((DataproviderObject_1_0)expectedList.get(i)).path()).toString())
                        ) {
                            this.compareObjects(
                                (DataproviderObject_1_0)allResults.get(j),
                                (DataproviderObject_1_0)expectedList.get(i),
                                pathCompletion
                            );

                            found = true;
                            sortedResults.add(allResults.get(j));
                        }
                    }
                    catch (Error ex) {
                        // junit throws errors
                        error = ex;
                    }
                }
                assertTrue("no matching object for: "+ ((DataproviderObject_1_0)expectedList.get(i)) +
                    ". \nLast error: " + (error == null ? null: error.getMessage() ) ,
                    found
                );
            }
        }
        return ordered ? results : sortedResults;
    }

    //-------------------------------------------------------------------------    
    /**
     * Allows to excpude invalidated states if history
     * is not supported
     */
    private DataproviderObject_1_0[] excludeInvalidatedStates(
        DataproviderObject_1_0[] expected
    ){
        List expectedDPO = new ArrayList();
        for(
            int i = 0;
            i < expected.length;
            i++
        ){
            SparseList invalidatedAt = expected[i] == null ? null : expected[i].getValues(AbstractTestNoState_1.INVALIDATED_AT);
            if(invalidatedAt == null || invalidatedAt.isEmpty()) expectedDPO.add(expected[i]);
        }
        return expectedDPO.size() == expected.length ?
            expected :
            (DataproviderObject_1_0[]) expectedDPO.toArray(
                new DataproviderObject_1_0[expectedDPO.size()]
            );
    }

  //-------------------------------------------------------------------------    
    private void checkResult(
        DataproviderObject_1_0 result,
        String stateEntry,
        String[] valueEntry,
        String validFrom,
        String validTo
    ) {
        assertNotNull("result is null!", result);

        assertTrue("state entry is wrong: expected: <" +
                    stateEntry +
                   ">, received: <" +
                    result.values("stateAttr").get(0) +
                   ">. ",
                   stateEntry.equals(result.values("stateAttr").get(0))
        );

        List valueEntryList = Arrays.asList(valueEntry);
        assertTrue("value entry is wrong: expected: <" +
                    valueEntryList +
                   "> received: <" +
                   result.values("value") +
                   ">. ",
                   result.values("value").containsAll(valueEntryList) &&
                   valueEntryList.containsAll(result.values("value"))
        );

        String validFromDate = prepareDate(validFrom);
        assertTrue("validFrom is wrong:  expected: <" +
                    validFromDate +
                    "> received: <"+
                    result.values(AbstractTestNoState_1.VALID_FROM).get(0) +
                    ">.",
                    (
                        validFromDate == null &&
                        result.values(AbstractTestNoState_1.VALID_FROM).size() == 0
                    )
                    ||
                    (
                        validFromDate != null &&
                        validFromDate.equals(result.values(AbstractTestNoState_1.VALID_FROM).get(0))
                    )
        );

        String validToDate = prepareDate(validTo);
        assertTrue("validTo is wrong:  expected: <" +
                    validToDate +
                    "> received: <"+
                    result.values(AbstractTestNoState_1.VALID_TO).get(0) +
                    ">.",
                    (
                        validToDate == null &&
                        result.values(AbstractTestNoState_1.VALID_TO).size() == 0
                    )
                    ||
                    (
                        validToDate != null &&
                        validToDate.equals(result.values(AbstractTestNoState_1.VALID_TO).get(0))
                    )
        );
    }

  //-------------------------------------------------------------------------    
    /**
     * finds the highest number of a certain object in the storage. This 
     * serves as an indicator for the number of tests executed. The number is 
     * used for generating unique names.
     */
    private String findTestID(
    ) {
        ID = "211";
        return ID;
        /*
        if (ID == null) {
            ServiceHeader header = new ServiceHeader(
                "anyff", 
                null,
                false,
                new QualityOfService(),
                null,
                null
            );

            int i = 100;
            boolean foundId = false;
            
            try {
                while (i < 1000 && foundId == false) {
                    ID = "t" + String.valueOf(i);
                    DataproviderObject object = prepareObject("StateA", null, "stateA/testId" + ID, "find available number.", new String[] { "find available number." }, null );
    
                    RequestCollection requests = new RequestCollection(
                        header,
                        this.dataprovider
                    );
                
                    try {
                        foundId = false; 
                        requests.addCreateRequest(object);
                        foundId = true;
                    }
                    catch (ServiceException se) {
                        if (se.getExceptionCode() == StackedException.ASSERTION) {
                            // just try next number
                             
                        }
                        else {
                            System.out.println("Exception in findTestId");
                            se.getDescription();
                            se.printStackTrace();
                            throw se;
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Exception in findTestId");
                        e.getMessage();
                        e.printStackTrace();
                        throw e;
                    }
                    i++;
                }
                if (i== 1000) {
                    System.out.println("findTestID(): there are "+ i+ " entries in DB, clean it first!");
                }
                            }
            catch (Exception e) {
            }
            
            System.out.println("findTestID(): testId = " + ID );
 
        }
        
        return ID;
        */
    }

//  //-------------------------------------------------------------------------    
//    /**
//     * prepare for TimeRangeSearch test
//     * 
//     * returns the times when 
//     * 0) all objects were created, 
//     * 1) all objects were updated for the first time
//     * 2) all objects were updated for the second time
//     * 
//     * @param map containing all the states produced, as 
//     *          DataproviderObject_1_0 objectsthe key is id:<statenumber>
//     */
//    protected Date[] setUpForTimeRangeSearch(
//            InclusivePackage pkg,
//            Segment modelSegment,
//            String id1,
//            String id2,
//            HashMap states
//    ) throws Exception {
//
//        Date times[] = new Date[3];
//        try {
//         first create the objects
//        StateADerivedClass stateADerivedClass = pkg.getStateADerived();
//        Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
//
//        unitOfWork.begin();
//        StateADerived stateA = stateADerivedClass.createStateADerived();
//        stateA.setStateAttr("state_0");
//        stateA.setValue(new String[] {"A", id1 });
//        stateA.setStateADerived("1_derived_0");
//        stateA.setValueADerived(new String[] {"11AA", id1});
//        // need a date !!! (otherwise it is not recognised as a stated request)
//        // stateA.setStateValidFrom(prepareDateAsDate("01.01.1001"));
//        // stateA.setStateValidFrom(null);
//
//        // stateA.setStateValidTo(null);
//        // dont do it like that: all further updates will go from ever to ever 
//        // modelSegment.addStateA(id1 + ";validFrom=ever;validTo=ever", stateA);
//        modelSegment.addStateA(id1, stateA);
//        unitOfWork.commit();
//
//        unitOfWork.begin();
//        DataproviderObject dpoId1 = toDataproviderObject(stateA);
//        unitOfWork.commit();
//
//        dpoId1.values(AbstractTestNoState_1.INVALIDATED_AT).add(0, INVALIDATION_INDICATOR);
//        DataproviderObject dpoId1_initial = (DataproviderObject)dpoId1.clone();
//        states.put(id1+":0", dpoId1.clone());
//
//        unitOfWork.begin();
//        StateADerived stateB = stateADerivedClass.createStateADerived();
//        stateB.setStateValidFrom(prepareDateAsDate("01.02.2002"));
//        // validTo = null
//        stateB.setStateAttr("state_0");
//        stateB.setValue(new String[] {"A", id2 });
//        stateB.setStateADerived("2_derived_0");
//        stateB.setValueADerived(new String[] {"22AA", id2});
//
//        modelSegment.addStateA(id2, stateB);
//        unitOfWork.commit();
//
//        unitOfWork.begin();
//        DataproviderObject dpoId2 = toDataproviderObject(stateB);
//        unitOfWork.commit();
//
//        DataproviderObject dpoId2_initial = (DataproviderObject) dpoId2.clone();
//        dpoId2.values(AbstractTestNoState_1.INVALIDATED_AT).add(0, "just an indicator");
//        states.put(id2+":0", dpoId2.clone());
//
//        Thread.sleep(100); // to ensure gap
//        times[0] = new Date();
//        Thread.sleep(100); // to ensure gap
//
//         // created, start updates
//        unitOfWork.begin();
//        // get for update 
////        stateA = (StateADerived) modelSegment.getStateA(id1 + 
////            ";validFrom="+prepareDate("01.02.2002")
////            +";validTo="+prepareDate("30.09.2002"));
//
//        stateA.setStateValidFrom(prepareDateAsDate("01.02.2002"));
//        stateA.setStateValidTo(prepareDateAsDate("30.09.2002"));
//        stateA.setStateADerived("1_derived_1");
//        stateA.getValueADerived().add("11BB");
//        stateA.getValue().add("BB");
//        unitOfWork.commit();
//
//        // splitted states
//        dpoId1.values(AbstractTestNoState_1.VALID_TO).set(0,prepareDate("31.01.2002"));
//        dpoId1.attributeNames().remove(AbstractTestNoState_1.INVALIDATED_AT);
//        states.put(id1+":1", dpoId1.clone());
//        dpoId1.values(AbstractTestNoState_1.VALID_FROM).set(0,prepareDate("01.10.2002"));
//        dpoId1.values(AbstractTestNoState_1.INVALIDATED_AT).add(0,INVALIDATION_INDICATOR);
//        dpoId1.attributeNames().remove(AbstractTestNoState_1.VALID_TO);
//        states.put(id1+":3", dpoId1.clone());
//        dpoId1.values(AbstractTestNoState_1.VALID_FROM).set(0,prepareDate("01.11.2002"));
//        dpoId1.attributeNames().remove(AbstractTestNoState_1.VALID_TO);
//        dpoId1.attributeNames().remove(AbstractTestNoState_1.INVALIDATED_AT);
//        states.put(id1+":7", dpoId1.clone());
//
//        // new intermediate state
//        dpoId1.values(AbstractTestNoState_1.VALID_FROM).set(0,prepareDate("01.02.2002"));
//        dpoId1.values(AbstractTestNoState_1.VALID_TO).set(0,prepareDate("30.09.2002"));
//        dpoId1.values(AbstractTestNoState_1.INVALIDATED_AT).add(0, INVALIDATION_INDICATOR);
//        dpoId1.values("stateADerived").set(0,"1_derived_1");
//        dpoId1.values("valueADerived").add("11BB");
//        dpoId1.values("value").add("BB");
//        states.put(id1+":2", dpoId1.clone());
//
//
//        unitOfWork.begin();
//        stateB.setStateValidFrom(prepareDateAsDate("01.08.2002"));
//        stateB.setStateADerived("2_derived_1");
//        stateB.getValueADerived().set(0, "22BB");
//        stateB.getValue().add("BB");
//        unitOfWork.commit();
//
//
//        dpoId2.values(AbstractTestNoState_1.VALID_TO).set(0,prepareDate("31.07.2002"));
//        states.put(id2+":1", dpoId2.clone());
//        dpoId2.attributeNames().remove(AbstractTestNoState_1.VALID_TO);
//        dpoId2.values(AbstractTestNoState_1.VALID_FROM).set(0,prepareDate("01.08.2002"));
//        dpoId2.values("stateADerived").set(0,"2_derived_1");
//        dpoId2.values("valueADerived").set(0,"22BB");
//        dpoId2.values("value").add("BB");
//        dpoId2.values(AbstractTestNoState_1.INVALIDATED_AT).add(0, INVALIDATION_INDICATOR);
//        states.put(id2+":2", dpoId2.clone());
//
//        Thread.sleep(100); // to ensure gap
//        times[1] = new Date();
//        Thread.sleep(100); // to ensure gap
//
//        // first update executed, second now:
//        // to get an empty stateA (attributes not set are not present
//        unitOfWork.begin();
//        // get for update 
////        stateA = (StateADerived) modelSegment.getStateA(id1 + 
////            ";validFrom="+prepareDate("01.07.2002")
////            +";validTo="+prepareDate("31.10.2002"));
//
//        stateA = stateADerivedClass.getStateADerived(new Path(rootSegment).add("stateA").add(id1));
//        stateA.setStateValidFrom(prepareDateAsDate("01.07.2002"));
//        stateA.setStateValidTo(prepareDateAsDate("31.10.2002"));
//        stateA.setStateADerived("1_derived_2");
//        stateA.getValueADerived().add("11CC");
//        stateA.getValueADerived().add(id1);
//        unitOfWork.commit();
//
//        // unitOfWork.begin();        
//        dpoId1.attributeNames().remove(AbstractTestNoState_1.INVALIDATED_AT);
//        dpoId1.values(AbstractTestNoState_1.VALID_TO).set(0,prepareDate("30.06.2002"));
//        states.put(id1+":4", dpoId1.clone());
//
//        dpoId1.values(AbstractTestNoState_1.VALID_FROM).set(0,prepareDate("01.07.2002"));
//        dpoId1.values(AbstractTestNoState_1.VALID_TO).set(0,prepareDate("30.09.2002"));
//        dpoId1.values("stateADerived").set(0,"1_derived_2");
//        dpoId1.values("valueADerived").add("11CC");
//        dpoId1.values("valueADerived").add(id1);
//        states.put(id1+":5", dpoId1.clone());
//
//        dpoId1_initial.values(AbstractTestNoState_1.VALID_FROM).set(0,prepareDate("01.10.2002"));
//        dpoId1_initial.values(AbstractTestNoState_1.VALID_TO).set(0,prepareDate("31.10.2002"));
//        dpoId1_initial.clearValues(AbstractTestNoState_1.INVALIDATED_AT);
//        dpoId1_initial.values("stateADerived").set(0,"1_derived_2");
//        dpoId1_initial.clearValues("valueADerived").addAll(dpoId1.values("valueADerived"));
//        states.put(id1+":6", dpoId1_initial.clone());
//
//        // to get an empty stateB (attributes not set are not present
//        unitOfWork.begin();
//        stateB = stateADerivedClass.getStateADerived(new Path(rootSegment).add("stateA").add(id2));
//        stateB.setStateValidFrom(prepareDateAsDate("01.04.2002"));
//        stateB.setStateValidTo(prepareDateAsDate("31.08.2002"));
//        stateB.setStateADerived("2_derived_2");
////        stateB.setValueADerived(0, "22CC");
////        stateB.setValueADerived(1, id2);
//        stateB.setValueADerived(new String[] {"22CC", id2});
//        unitOfWork.commit();
//
//        //unitOfWork.begin();
//        dpoId2.values(AbstractTestNoState_1.VALID_FROM).set(0,prepareDate("01.09.2002"));
//        dpoId2.attributeNames().remove(AbstractTestNoState_1.INVALIDATED_AT);
//        //unitOfWork.commit();
//        states.put(id2+":6", dpoId2.clone());
//
//        dpoId2.values(AbstractTestNoState_1.VALID_FROM).set(0,prepareDate("01.08.2002"));
//        dpoId2.values(AbstractTestNoState_1.VALID_TO).set(0,prepareDate("31.08.2002"));
//        dpoId2.values("stateADerived").set(0,"2_derived_2");
//        dpoId2.values("valueADerived").set(0,"22CC");
//        states.put(id2+":5", dpoId2.clone());
//
//        dpoId2_initial.values(AbstractTestNoState_1.VALID_TO).set(0,prepareDate("31.03.2002"));
//        dpoId2_initial.attributeNames().remove(AbstractTestNoState_1.INVALIDATED_AT);
//        states.put(id2+":3", dpoId2_initial.clone());
//
//        dpoId2_initial.values(AbstractTestNoState_1.VALID_FROM).set(0,prepareDate("01.04.2002"));
//        dpoId2_initial.values(AbstractTestNoState_1.VALID_TO).set(0,prepareDate("31.07.2002"));
//        dpoId2_initial.values("stateADerived").set(0,"2_derived_2");
//        dpoId2_initial.values("valueADerived").set(0,"22CC");
//        dpoId2_initial.values("valueADerived").set(1, id2);
//        states.put(id2+":4", dpoId2_initial.clone());
//
//        dpoId2 = (DataproviderObject)((DataproviderObject)states.get(id2+":1")).clone();
//
//
//        Thread.sleep(100); // to ensure gap
//        times[2] = new Date();
//
//
//        } catch (Throwable t) {
//            showException(t);
//        }
//        return times;
//
//    }

  //-------------------------------------------------------------------------    
    /**
     * clean up after timeRangeSearch test
     */
    protected void tearDownAfterTimeRangeSearch(
        InclusivePackage pkg,
        Segment modelSegment,
        String id1,
        String id2
    ) throws Exception {
        Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
        // just remove the objects


        try {
            unitOfWork.begin();
            modelSegment.removeStateA(id1 + ";validFrom=ever;validTo=ever;state=first");
            unitOfWork.commit();
        }
        catch(JmiServiceException e) {
            if (unitOfWork.isActive()){
                unitOfWork.rollback();
            }
          this.verifyException(
            e.getExceptionStack(),
            StackedException.NOT_FOUND,
            null
          );
        }
        catch(JDOException je) {
            if (unitOfWork.isActive()){
                unitOfWork.rollback();
            }
            ServiceException se = (ServiceException) je.getCause();
            this.verifyException(
              se.getExceptionStack(),
              StackedException.NOT_FOUND,
              null
            );
          }

        try{
            unitOfWork.begin();
            modelSegment.removeStateA(id2 + ";validFrom=ever;validTo=ever;state=first");
            unitOfWork.commit();
        }
        catch(JmiServiceException e) {
            if (unitOfWork.isActive()){
                unitOfWork.rollback();
            }
          this.verifyException(
            e.getExceptionStack(),
            StackedException.NOT_FOUND,
            null
          );
        }
        catch(JDOException je) {
            if (unitOfWork.isActive()){
                unitOfWork.rollback();
            }
            ServiceException se = (ServiceException) je.getCause();
            this.verifyException(
              se.getExceptionStack(),
              StackedException.NOT_FOUND,
              null
            );
          }
    }


    /**
     * Test working on a object which has operation parameters in its path.
     * <p>
     * @param supportsHistory 
     * 
     * @throws Exception
     */
    public void testOperationParametersInPath(boolean supportsHistory) throws Exception {
        try {
            String baseId = "idOPinPath";
            String id1 = baseId + "_1";
//          String id2 = baseId + "_2";

            // for checking correctness; 
            // states collects the an example of every state; without 
            // adjusting the times.
            DataproviderObject states[] = new DataproviderObject[100];

            String opValidFrom = ";" + State_1_Attributes.OP_VALID_FROM + "=";
            String opValidTo = ";" + State_1_Attributes.OP_VALID_TO + "=";
            String opFirst = ";" + State_1_Attributes.OP_STATE + "=" + State_1_Attributes.OP_VAL_FIRST;
//          String opSkip = ";" + State_1_Attributes.OP_SKIP_MISSING_STATES;

            // need to set requestedFor, otherwise the result set changes over time.
            ServiceHeader header =
                new ServiceHeader(
                    "anyff",
                    "some",
                    false,
                    null,
                    null,
                    prepareDate("20.08.2002"));

            InclusivePackage pkg = createInclusivePackage(header);
            Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();

            // get segment singleton
            SegmentClass modelSegmentClass = pkg.getSegment();
            StateAClass stateAClass = pkg.getStateA();
            StateDClass stateDClass = pkg.getStateD();

            unitOfWork.begin();
            Segment modelSegment =
                modelSegmentClass.getSegment(new Path(rootSegment));
            unitOfWork.commit();


            {
                // cleaning any reaminings 
                String validFrom = opValidFrom + State_1_Attributes.OP_VAL_EVER;
                String validTo = opValidTo + State_1_Attributes.OP_VAL_EVER;

                try {
                    unitOfWork.begin();

                    StateA stateA = modelSegment.getStateA(id1+";state=first");
                    if(stateA != null) {
                        stateA.removeStateD(id1 + validFrom + validTo);
                    }
                    unitOfWork.commit();
                }
                catch(JmiServiceException e) {
                    if (unitOfWork.isActive()){
                        unitOfWork.rollback();
                    }
                    this.verifyException(
                        e.getExceptionStack(),
                        StackedException.NOT_FOUND,
                        null
                    );
                }
                        catch(JDOException je) {
                            if (unitOfWork.isActive()){
                                unitOfWork.rollback();
                            }
                            ServiceException se = (ServiceException) je.getCause();
                            this.verifyException(
                              se.getExceptionStack(),
                              StackedException.NOT_FOUND,
                              null
                            );
                          }
                try {
                    unitOfWork.begin();
                    modelSegment.removeStateA(id1 + validFrom + validTo);
                    unitOfWork.commit();
                }
                catch (JmiServiceException e) {
                    if (unitOfWork.isActive()) {
                        unitOfWork.rollback();
                    }
                    this.verifyException(
                        e.getExceptionStack(),
                        StackedException.NOT_FOUND,
                        null);
                }
                        catch(JDOException je) {
                            if (unitOfWork.isActive()){
                                unitOfWork.rollback();
                            }
                            ServiceException se = (ServiceException) je.getCause();
                            this.verifyException(
                              se.getExceptionStack(),
                              StackedException.NOT_FOUND,
                              null
                            );
                          }

            }

            {
                // create an object in the future:
                unitOfWork.begin();
                StateA stateA = stateAClass.createStateA();
                stateA.setStateAttr("state_0");
                stateA.setValue(new String[] {"B", id1 });
//                String validFrom = opValidFrom + prepareDate("01.01.2100");
                String validFrom = opValidFrom + State_1_Attributes.OP_VAL_EVER;
//              String validTo = opValidTo + prepareDate("31.12.2999");
                String validTo = opValidTo + State_1_Attributes.OP_VAL_EVER;
                modelSegment.addStateA(id1+validFrom+validTo, stateA);
                unitOfWork.commit();
                stateA = modelSegment.getStateA(id1 + ";state="+ prepareDate("02.01.2100"));

                // add an object D to A created before:
                unitOfWork.begin();
                StateD stateD = stateDClass.createStateD();
                stateD.setIntegerVal(new Integer(1));
                stateD.setLongVal(new Long(1));
                stateA.addStateD(id1+validFrom+validTo, stateD);
                unitOfWork.commit();
                states[0] = toDataproviderObject(stateD);
                states[0].path().setTo(new Path(stateD.getIdentity()));
                states[0].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2100"));
                states[0].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2999"));
            }

            {
                // get an object in the future (state=first) and change it
                StateA stateA = modelSegment.getStateA(id1+opFirst);

//              String validFrom = opValidFrom + prepareDate("01.01.2150");
                String validFrom = opValidFrom + State_1_Attributes.OP_VAL_EVER;
//              String validTo = opValidTo + prepareDate("31.12.2299");
                String validTo = opValidTo + State_1_Attributes.OP_VAL_EVER;

                // get stateD for changing 
                StateD stateD = stateA.getStateD(id1+validFrom+validTo+opFirst);
                unitOfWork.begin();
                stateD.setIntegerVal(new Integer(2));
                // leave long val
                unitOfWork.commit();
                states[0].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2149"));

                states[1] = new DataproviderObject(states[0]);
                states[1].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2150"));
                states[1].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.2.2999"));
                states[1].values("integerVal").set(0, new Integer(2));

                states[2] = new DataproviderObject(states[0]);
                states[2].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2300"));
                states[2].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2999"));
            }
            {
                // get an object in the future (state=first)
                StateA stateA = modelSegment.getStateA(id1+opFirst);

//              String validFrom = opValidFrom + prepareDate("01.01.2200");
                String validFrom = opValidFrom + State_1_Attributes.OP_VAL_EVER;
//              String validTo = opValidTo + prepareDate("31.12.2399");
                String validTo = opValidTo + State_1_Attributes.OP_VAL_EVER;

                // get stateD for changing 
                StateD stateD = stateA.getStateD(id1+validFrom+validTo+opFirst);
                unitOfWork.begin();
                stateD.setLongVal(new Long(2));
                // leave long val
                unitOfWork.commit();

//                states[1].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2199"));
                states[1].values(State_1_Attributes.STATE_VALID_TO).set(0,null);
//              states[2].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2400"));
                states[2].values(State_1_Attributes.STATE_VALID_FROM).set(0,null);

                states[3] = new DataproviderObject(states[1]);
//              states[3].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2200"));
                states[3].values(State_1_Attributes.STATE_VALID_FROM).set(0,null);
//              states[3].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2299"));
                states[3].values(State_1_Attributes.STATE_VALID_TO).set(0,null);
                states[3].values("longVal").set(0, new Long(2));

                states[4] = new DataproviderObject(states[0]);
//              states[4].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2300"));
                states[4].values(State_1_Attributes.STATE_VALID_FROM).set(0,null);
//              states[4].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2399"));
                states[4].values(State_1_Attributes.STATE_VALID_TO).set(0,null);
                states[4].values("longVal").set(0, new Long(2));
            }
            {
                // find starting from an object in the future (state=first)
                StateA stateA = modelSegment.getStateA(id1+opFirst);

                assertTrue("stateA.refGetPath() ends with state= but is " + refGetPath(stateA), refGetPath(stateA).toString().endsWith("state=first"));
                assertTrue("stateA.identity has no id operators but is " + refGetPath(stateA), stateA.getIdentity().indexOf("state=first") < 0);

                // find stateD within stateA 
                StateDQuery filter = pkg.createStateDQuery();
                filter.thereExistsIntegerVal().equalTo(new Integer(2));
                filter.forAllStateValidFrom().greaterThan(prepareDateAsDate("01.01.1800"));

                List dstates = stateA.getStateD(filter);

//                assertEquals("dstates.size()", 2, dstates.size());
                assertEquals("dstates.size()", 1, dstates.size()); // TODO verify
            }
//            {
//                // find starting from an object in the future which was received through a find (state=first)
//                StateAQuery filter = pkg.createStateAQuery();
//                filter.stateAttr().equalTo("state_0");
////                filter.forAllStateValidFrom().greaterThan(prepareDateAsDate("01.01.1800"));
//
//                List alist = modelSegment.getStateA(filter);
//
//                assertEquals("aList.size()", 1, alist.size());
//                StateA stateA = (StateA)alist.iterator().next();
//
//                assertTrue("stateA.refGetPath() ends with state= but is " + refGetPath(stateA), refGetPath(stateA).toString().indexOf("state=") > (refGetPath(stateA).toString().length() - 10));
//                assertTrue("stateA.identity has no id operators but is " + refGetPath(stateA), stateA.getIdentity().indexOf("state=") < 0);
//
//                // find stateD within stateA 
//                StateDQuery dfilter = pkg.createStateDQuery();
//                dfilter.thereExistsIntegerVal().equalTo(new Integer(2));
//                dfilter.forAllStateValidFrom().greaterThan(prepareDateAsDate("01.01.1800"));
//
//                List dstates = stateA.getStateD(dfilter);
//
////              assertEquals("dstates.size()", 2, dstates.size());
//                assertEquals("dstates.size()", 1, dstates.size()); // TODO verify
//
//            }
//            {
//                // check the states so far
//                // states within defined range
//                // validTo, validFrom with real dates
//                StateDQuery validQuery = pkg.createStateDQuery();
//
////                validQuery.forAllStateValidFrom().greaterThan(prepareDateAsDate("01.01.1800"));
//
//
//                // one of the filter values will match; keep like that 
//                // without the rootSegment to make it explicit.
//                validQuery.identity().elementOf(
//                    new String[]{
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/Jdbc/segment/Inclusive/inclusive/Standard/stateA/"+id1+"/stateD/" + id1,
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/None/segment/Inclusive/inclusive/Standard/stateA/"+id1+"/stateD/" + id1,
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/Jdbc/segment/Inclusive/inclusive/WithoutHistory/stateA/"+id1+"/stateD/" + id1,
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/None/segment/Inclusive/inclusive/WithoutHistory/stateA/"+id1+"/stateD/" + id1
//                    }
//                );
//
//                unitOfWork.begin();
//
//                StateA stateA = modelSegment.getStateA(id1+opFirst);
//
//                List findings = stateA.getStateD(validQuery);
////              Container findings = (Container) stateA.getStateD();
////              findings = findings.subSet(validQuery);
//
//                // set up the states to expect:
//                checkJmiResultList(
//                    findings,
//                    states,
//                    State_1_Attributes.OP_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//
//            }
//            {
//                // remove a hole in stateD 
//                // get an object in the future (state=first) 
//                StateA stateA = modelSegment.getStateA(id1+opFirst);
//
////                String validFrom = opValidFrom + prepareDate("01.01.2500");
//                String validFrom = opValidFrom + State_1_Attributes.OP_VAL_EVER;
////              String validTo = opValidTo + prepareDate("31.12.2599");
//                String validTo = opValidTo + State_1_Attributes.OP_VAL_EVER;
//
//                // get stateD for changing 
//
//                unitOfWork.begin();
//                stateA.removeStateD(id1+validFrom+validTo);
//                // leave long val
//                unitOfWork.commit();
//
////                states[2].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2499"));
//                states[2].values(State_1_Attributes.STATE_VALID_TO).set(0,null);
//                states[5] = new DataproviderObject(states[2]);
////                states[5].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2600"));
//                states[5].values(State_1_Attributes.STATE_VALID_FROM).set(0,null);
////              states[5].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2999"));
//                states[5].values(State_1_Attributes.STATE_VALID_TO).set(0,null);
//            }
//            {
//                // check that it is removed
//                // states within defined range
//                // validTo, validFrom with real dates
//                StateDQuery validQuery = pkg.createStateDQuery();
//
//                validQuery.forAllStateValidFrom().greaterThan(prepareDateAsDate("01.01.1800"));
//
//                // one of the four filter values will match; keep like that 
//                // without the rootSegment to make it explicit.
//                validQuery.identity().elementOf(
//                    new String[] {
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/Jdbc/segment/Inclusive/inclusive/Standard/stateA/"+id1+"/stateD/" + id1,
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/None/segment/Inclusive/inclusive/Standard/stateA/"+id1+"/stateD/" + id1,
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/Jdbc/segment/Inclusive/inclusive/WithoutHistory/stateA/"+id1+"/stateD/" + id1,
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/None/segment/Inclusive/inclusive/WithoutHistory/stateA/"+id1+"/stateD/" + id1
//                   }
//                );
//
//                unitOfWork.begin();
//
//                StateA stateA = modelSegment.getStateA(id1+opFirst);
//
//                List findings = stateA.getStateD(validQuery);
////              Container findings = (Container) stateA.getStateD();
////              findings = findings.subSet(validQuery);
//
//                // set up the states to expect:
//                checkJmiResultList(
//                    findings,
//                    states,
//                    State_1_Attributes.OP_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//            }
            {
                // remove stateD entirely
                // get an object in the future (state=first) 
                StateA stateA = modelSegment.getStateA(id1+opFirst);


                unitOfWork.begin();
                String validFrom = opValidFrom + State_1_Attributes.OP_VAL_EVER;
                String validTo = opValidTo + State_1_Attributes.OP_VAL_EVER;

                stateA.removeStateD(id1 + validFrom + validTo);
                // leave long val
                unitOfWork.commit();
            }
        }
        catch (Exception e) {
            showException(e);
            throw e;
        }
    }

//    public void testRemoveOperation(boolean supportsHistory) throws Exception {
//        try {
//            String baseId = "idRemove";
//            String id1 = baseId + "_1";
//            String id2 = baseId + "_2";
//
//            // for checking correctness; 
//            // states collects the an example of every state; without 
//            // adjusting the times.
//            DataproviderObject states[] = new DataproviderObject[100];
//
//            String opValidFrom = ";" + State_1_Attributes.OP_VALID_FROM + "=";
//            String opValidTo = ";" + State_1_Attributes.OP_VALID_TO + "=";
////          String opSkip = ";" + State_1_Attributes.OP_SKIP_MISSING_STATES;
//
//            // need to set requestedFor, otherwise the result set changes over time.
//            ServiceHeader header =
//                new ServiceHeader(
//                    "anyff",
//                    "some",
//                    false,
//                    null,
//                    null,
//                    prepareDate("20.08.2002"));
//
//            InclusivePackage pkg = createInclusivePackage(header);
//            Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
//
//            // get segment singleton
//            SegmentClass modelSegmentClass = pkg.getSegment();
//            StateAClass stateAClass = pkg.getStateA();
//
//            unitOfWork.begin();
//            Segment modelSegment =
//                modelSegmentClass.getSegment(new Path(rootSegment));
//            unitOfWork.commit();
//
//            // use the same function to clean:
//            tearDownAfterTimeRangeSearch(pkg, modelSegment, id1, id2);
//            {
//                // create an object in the future:
//                unitOfWork.begin();
//                StateA stateA = stateAClass.createStateA();
//                stateA.setStateAttr("testRemoveOperation state_0");
//                stateA.setValue(new String[] {"B", id1 });
//                String validFrom = opValidFrom + prepareDate("01.01.2100");
//                String validTo = opValidTo + prepareDate("31.12.2999");
//                modelSegment.addStateA(id1+validFrom+validTo, stateA);
//                unitOfWork.commit();
//                stateA = modelSegment.getStateA(id1 + ";state="+ prepareDate("02.01.2100"));
//                states[0] = toDataproviderObject(stateA);
//                states[0].path().setTo(new Path(stateA.getIdentity()));
//                states[0].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.3000"));
//                states[0].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2099"));
//            }
//
//            {
//                // put a gap in the validity (in the middle)
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.2200");
//                String validTo = opValidTo + prepareDate("31.12.2299");
//                modelSegment.removeStateA(id1+validFrom+validTo);
//                unitOfWork.commit();
//            }
//
//            {
//                // reduce validity at the start
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.2000");
//                String validTo = opValidTo + prepareDate("31.12.2109");
//                modelSegment.removeStateA(id1+validFrom+validTo);
//                unitOfWork.commit();
//            }
//            {
//                // reduce validity at the end
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.2990");
//                String validTo = opValidTo + prepareDate("31.12.3009");
//                modelSegment.removeStateA(id1+validFrom+validTo);
//                unitOfWork.commit();
//            }
//            {
//                // reduce validity inside the gap (should not throw exception)
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.2230");
//                String validTo = opValidTo + prepareDate("31.12.2259");
//                modelSegment.removeStateA(id1+validFrom+validTo);
//                unitOfWork.commit();
//            }
//            {
//                // delete before gap, into the gap
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.2190");
//                String validTo = opValidTo + prepareDate("31.12.2209");
//                modelSegment.removeStateA(id1+validFrom+validTo);
//                unitOfWork.commit();
//            }
//            {
//                // delete from inside the gap out to the valid part
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.2290");
//                String validTo = opValidTo + prepareDate("31.12.2309");
//                modelSegment.removeStateA(id1+validFrom+validTo);
//                unitOfWork.commit();
//            }
//
//            {
//                // prepare states to expect:
//                states[0].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2110"));
//                states[0].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2189"));
//                states[1] = new DataproviderObject(states[0]);
//                states[1].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2310"));
//                states[1].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2989"));
//
//                // now get the valid states and compare them 
//                unitOfWork.begin();
//
//                // states within defined range
//                // validTo, validFrom with real dates
//                StateAQuery validQuery = pkg.createStateAQuery();
//
//                validQuery.forAllStateValidFrom().greaterThan(prepareDateAsDate("01.01.1800"));
//
//                // one of the four filter values will match; keep like that 
//                // without the rootSegment to make it explicit.
//                validQuery.identity().elementOf(
//                    new String[] {
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/Jdbc/segment/Inclusive/inclusive/Standard/stateA/idRemove_1",
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/None/segment/Inclusive/inclusive/Standard/stateA/idRemove_1",
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/Jdbc/segment/Inclusive/inclusive/WithoutHistory/stateA/idRemove_1",
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/None/segment/Inclusive/inclusive/WithoutHistory/stateA/idRemove_1"
//                   }
//                );
//
//
//                List findings = modelSegment.getStateA(validQuery);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(validQuery);
//
//                // set up the states to expect:
//                checkJmiResultList(
//                    findings,
//                    states,
//                    State_1_Attributes.OP_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//
//            }
//            {
//                // remove exactly the remaining states and try to recreate the object 
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.2110");
//                String validTo = opValidTo + prepareDate("31.12.2189");
//                modelSegment.removeStateA(id1+validFrom+validTo);
//
//                validFrom = opValidFrom + prepareDate("01.01.2310");
//                validTo = opValidTo + prepareDate("31.12.2989");
//                modelSegment.removeStateA(id1+validFrom+validTo);
//                unitOfWork.commit();
//
//                unitOfWork.begin();
//                StateA stateA = stateAClass.createStateA();
//                stateA.setStateAttr("testRemoveOperation recreated");
//                stateA.setValue(new String[] {"C", id1 });
//                validFrom = opValidFrom + prepareDate("01.01.2000");
//                validTo = opValidTo + prepareDate("31.12.2100");
//                modelSegment.addStateA(id1+validFrom+validTo, stateA);
//                unitOfWork.commit();
//            }
//            {
//                // Try create an existing object
//                try {
//                    unitOfWork.begin();
//                    StateA stateA = stateAClass.createStateA();
//                    stateA.setStateAttr("testRemoveOperation recreated again");
//                    stateA.setValue(new String[] {"D", id1 });
//                    String validFrom = opValidFrom + prepareDate("01.01.2000");
//                    String validTo = opValidTo + prepareDate("31.12.2100");
//                    modelSegment.addStateA(id1+validFrom+validTo, stateA);
//                    unitOfWork.commit();
//
//                        fail("Exception sollte geworfen werden");
//                }
//                catch (JmiServiceException jse) {
//                        this.assertException(
//                        jse.getExceptionStack(),
//                        BasicException.Code.DUPLICATE,
//                        "Trying to create object which exists and is still valid."
//                    );
//                }
//                        catch(JDOException je) {
//                            ServiceException se = (ServiceException) je.getCause();
//                        this.assertException(
//                              se.getExceptionStack(),
//                        BasicException.Code.DUPLICATE,
//                        "Trying to create object which exists and is still valid."
//                            );
//                          }
//            }
//        }
//        catch (Exception e) {
//            showException(e);
//            throw e;
//        }
//    }
//
//
//
//    //-------------------------------------------------------------------------    
//    /** 
//     * Test the searches within a certain time period.
//     * @param supportsHistory 
//     */
//    public void testPeriodOperation(boolean supportsHistory) throws Exception {
//        try {
//            String baseId = "idPeriod";
//            String id1 = baseId + "_1";
//            String id2 = baseId + "_2";
//
//            // for checking correctness; 
//            // states collects the an example of every state; without 
//            // adjusting the times.
//            DataproviderObject states[] = new DataproviderObject[100];
//
//            String opValidFrom = ";" + State_1_Attributes.OP_VALID_FROM + "=";
//            String opValidTo = ";" + State_1_Attributes.OP_VALID_TO + "=";
//            String opSkip = ";" + State_1_Attributes.OP_SKIP_MISSING_STATES;
//
//            // need to set requestedFor, otherwise the result set changes over time.
//            ServiceHeader header =
//                new ServiceHeader(
//                    "anyff",
//                    "some",
//                    false,
//                    null,
//                    null,
//                    prepareDate("20.08.2002"));
//
//            InclusivePackage pkg = createInclusivePackage(header);
//            Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
//
//            // get segment singleton
//            SegmentClass modelSegmentClass = pkg.getSegment();
//            StateAClass stateAClass = pkg.getStateA();
//
//            unitOfWork.begin();
//            Segment modelSegment =
//                modelSegmentClass.getSegment(new Path(rootSegment));
//            unitOfWork.commit();
//
//            // use the same function to clean:
//            tearDownAfterTimeRangeSearch(pkg, modelSegment, id1, id2);
//
//            { //  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9
//                //                      000000000000000000000000000000000000000>
//                // create an object 
//                unitOfWork.begin();
//                StateA stateA = stateAClass.createStateA();
//                stateA.setStateAttr("testPeriodOperation state_0");
//                stateA.setValue(new String[] {"A", id1 });
//                stateA.setStateValidFrom(prepareDateAsDate("01.01.2000"));
//                modelSegment.addStateA(id1, stateA);
//                unitOfWork.commit();
//                states[0] = toDataproviderObject(stateA);
//                states[0].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2000"));
//
//            }
//            { //  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 
//                //                      000000000000000000000000000000000000000011111>
//                // get the object at a certain time it exists and update it
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.2100");
//                String validTo = opValidTo + prepareDate("31.12.2199");
//                StateA stateA = modelSegment.getStateA(id1 + validFrom+validTo);
//                stateA.setStateAttr("testPeriodOperation state_1");
//                unitOfWork.commit();
//                states[1] = new DataproviderObject(states[0]);
//                states[1].values("stateAttr").set(0,"testPeriodOperation state_1");
//                states[1].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2100"));
//                states[1].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2199"));
//                states[0].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2099"));
//                states[2] = new DataproviderObject(states[0]);
//                states[2].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2200"));
//                states[2].clearValues(State_1_Attributes.STATE_VALID_TO);
//            }
//
//            { //  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 
//                //  2222                000000000000000000000000000000000000000011111>
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.1900");
//                String validTo = opValidTo + prepareDate("31.12.1919");
//                StateA stateA = modelSegment.getStateA(id1+validFrom+validTo);
//                stateA.setStateAttr("testPeriodOperation state_2");
//                // note: value is optional attribute. Else it would have to be set here
//                unitOfWork.commit();
//                states[3] = new DataproviderObject(states[0]);
//                states[3].clearValues("value");
//                states[3].values("stateAttr").set(0,"testPeriodOperation state_2");
//                states[3].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.1900"));
//                states[3].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.1919"));
//
//            }
//            { //  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 
//                //  223333              000000000000000000000000000000000000000011111>
//
//                // get the object in the first part and update into the hole
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.1910");
//                String validTo = opValidTo + prepareDate("31.12.1929");
//                StateA stateA = modelSegment.getStateA(id1+validFrom+validTo);
//                stateA.setStateAttr("testPeriodOperation state_3");
//                unitOfWork.commit();
//                states[4] = new DataproviderObject(states[3]);
//                states[4].values("stateAttr").set(0,"testPeriodOperation state_3");
//                states[4].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.1910"));
//                states[4].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.1929"));
//                states[3].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.1909"));
//            }
//            { //  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 
//                //  223333            44550000000000000000000000000000000000000011111>
//
//                // get the object and update it out of the hole
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.1990");
//                String validTo = opValidTo + prepareDate("31.12.2009");
//                StateA stateA = modelSegment.getStateA(id1+validFrom+validTo);
//                stateA.setStateAttr("testPeriodOperation state_4");
//                unitOfWork.commit();
//                states[5] = new DataproviderObject(states[3]);
//                states[5].values("stateAttr").set(0,"testPeriodOperation state_4");
//                states[5].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.1990"));
//                states[5].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.1999"));
//                states[6] = new DataproviderObject(states[5]);
//                states[6].values("value").addAll(states[0].values("value"));
//                states[6].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2000"));
//                states[6].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.2009"));
//                states[0].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.2010"));
//            }
//            { //  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 
//                //  223333  6666      44550000000000000000000000000000000000000011111>
//
//                // get the object in the hole and update it
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.1940");
//                String validTo = opValidTo + prepareDate("31.12.1959");
//                StateA stateA = modelSegment.getStateA(id1+validFrom+validTo);
//                stateA.setStateAttr("testPeriodOperation state_5");
//                unitOfWork.commit();
//                states[7] = new DataproviderObject(states[3]);
//                states[7].values("stateAttr").set(0,"testPeriodOperation state_5");
//                states[7].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.1940"));
//                states[7].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.1959"));
//
//            }
//
//            { //  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 
//                //  223377777766      44550000000000000000000000000000000000000011111>
//
//                // get object and fill the hole merging the states
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.1920");
//                String validTo = opValidTo + prepareDate("31.12.1949");
//                StateA stateA = modelSegment.getStateA(id1+validFrom+validTo);
//                stateA.setStateAttr("testPeriodOperation state_6");
//                unitOfWork.commit();
//                states[8] = new DataproviderObject(states[3]);
//                states[8].values("stateAttr").set(0,"testPeriodOperation state_6");
//                states[8].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.1920"));
//                states[8].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.1949"));
//                states[4].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.1919"));
//                states[7].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.1950"));
//
//            }
//            { //  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 
//                //  223377777768      94550000000000000000000000000000000000000011111>
//
//                // test update over the gap, with skip missing state
//                unitOfWork.begin();
//                String validFrom = opValidFrom + prepareDate("01.01.1955");
//                String validTo = opValidTo + prepareDate("31.12.1994");
//                StateA stateA =
//                    modelSegment.getStateA(id1 + validFrom + validTo + opSkip);
//                stateA.setStateAttr("testPeriodOperation state_7");
//                unitOfWork.commit();
//                states[9] = new DataproviderObject(states[3]);
//                states[9].values("stateAttr").set(0,"testPeriodOperation state_7");
//                states[9].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.1955"));
//                states[9].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.1959"));
//                states[10] = new DataproviderObject(states[9]);
//                states[10].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.1990"));
//                states[10].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.1994"));
//                states[5].values(State_1_Attributes.STATE_VALID_FROM).set(0,prepareDate("01.01.1995"));
//                states[7].values(State_1_Attributes.STATE_VALID_TO).set(0,prepareDate("31.12.1954"));
//            }
//
//
//            {
//                // now get the valid states and compare them 
//                unitOfWork.begin();
//
//                // states within defined range
//                // validTo, validFrom with real dates
//                StateAQuery validQuery = pkg.createStateAQuery();
//
//                validQuery.forAllStateValidFrom().greaterThan(prepareDateAsDate("01.01.1800"));
//
//                // one of the four filter values will match; keep like that 
//                // without the rootSegment to make it explicit.
//                validQuery.identity().elementOf(
//                    new String[]{
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/Jdbc/segment/Inclusive/inclusive/Standard/stateA/idPeriod_1",
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/None/segment/Inclusive/inclusive/Standard/stateA/idPeriod_1",
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/Jdbc/segment/Inclusive/inclusive/WithoutHistory/stateA/idPeriod_1",
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/None/segment/Inclusive/inclusive/WithoutHistory/stateA/idPeriod_1"
//                    }
//                );
//
//
//                List findings = modelSegment.getStateA(validQuery);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(validQuery);
//
//                // set up the states to expect:
//                checkJmiResultList(
//                    findings,
//                    states,
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//            }
//        }
//        catch (Exception e) {
//            showException(e);
//            throw e;
//        }
//    }


  //-------------------------------------------------------------------------    
//    /** 
//     * Test the searches within a certain time period.
//     * @param supportsHistory 
//     */
//    public void testTimeRangeSearch(boolean supportsHistory
//    ) throws Exception {
//        try {
//            String baseId = "idRange";
//            String id1 = baseId + "_1";
//            String id2 = baseId + "_2";
//
//            HashMap states = new HashMap();
//            Date[] times = null;
//            Date timeAtStart = new Date();
//
//            // need to set requestedFor, otherwise the result set changes over time.
//            ServiceHeader header =
//                new ServiceHeader("anyff", "some", false, null, null, prepareDate("20.08.2002"));
//
//            InclusivePackage pkg = createInclusivePackage(header);
//            Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
//
//            // get segment singleton
//            SegmentClass modelSegmentClass = pkg.getSegment();
//            unitOfWork.begin();
//            Segment modelSegment = modelSegmentClass.getSegment(
//              new Path(rootSegment)
//            );
//            unitOfWork.commit();
//
//            tearDownAfterTimeRangeSearch(pkg, modelSegment, id1, id2);
//
//            times = setUpForTimeRangeSearch(pkg, modelSegment, id1, id2, states);
//
//            // start searching
//            {
//
//                unitOfWork.begin();
//
//                // states within defined range
//                // validTo, validFrom with real dates
//                StateADerivedQuery filter = pkg.createStateADerivedQuery();
//
//                filter.forAllStateValidTo().greaterThan(prepareDateAsDate("01.05.2002"));
//                filter.forAllStateValidFrom().lessThan(prepareDateAsDate("15.08.2002"));
//
//                List findings = modelSegment.getStateA(filter);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(filter);
//
//                // set up the states to expect
//                checkJmiResultList(
//                    findings,
//                    new DataproviderObject_1_0[] {
//                        (DataproviderObject_1_0)states.get(id1+":4"),
//                        (DataproviderObject_1_0)states.get(id1+":5"),
//                        (DataproviderObject_1_0)states.get(id2+":4"),
//                        (DataproviderObject_1_0)states.get(id2+":5")
//                    },
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//            }
//            {
//                // get the valid states uf just one object in time period
//                unitOfWork.begin();
//
//                // states within defined range
//                // validTo, validFrom with real dates
//                StateADerivedQuery filter = pkg.createStateADerivedQuery();
//
//                filter.forAllStateValidTo().greaterThan(prepareDateAsDate("01.05.2002"));
//                filter.forAllStateValidFrom().lessThan(prepareDateAsDate("15.08.2002"));
//                // one of the four filter values will match; keep like that 
//                // without the rootSegment to make it explicit.
//                filter.identity().elementOf(
//                    new String[] {
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/Jdbc/segment/Inclusive/inclusive/Standard/stateA/idRange_1",
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/None/segment/Inclusive/inclusive/Standard/stateA/idRange_1",
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/Jdbc/segment/Inclusive/inclusive/WithoutHistory/stateA/idRange_1",
//                        "xri:@openmdx:org.openmdx.test.compatibility.state1:inclusive/provider/None/segment/Inclusive/inclusive/WithoutHistory/stateA/idRange_1"
//                   }
//                );
//
//                List findings = modelSegment.getStateA(filter);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(filter);
//
//                // set up the states to expect:
//                checkJmiResultList(
//                    findings,
//                    new DataproviderObject_1_0[] {
//                        (DataproviderObject_1_0)states.get(id1+":4"),
//                        (DataproviderObject_1_0)states.get(id1+":5")
//                    },
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//            }
//            {
//                // all states from 15.08.2002 to ever
//                // validTo, validFrom with null values
//
//                StateADerivedQuery filter = pkg.createStateADerivedQuery();
//
//                // its enough to have one filter to enable states return          
//                filter.forAllStateValidTo().greaterThanOrEqualTo(prepareDateAsDate("15.08.2002"));
//
//                unitOfWork.begin();
//                List findings = modelSegment.getStateA(filter);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(filter);
//
//                // set up the states to expect
//                checkJmiResultList(
//                    findings,
//                    new DataproviderObject_1_0[] {
//                        (DataproviderObject_1_0)states.get(id1+":5"),
//                        (DataproviderObject_1_0)states.get(id1+":6"),
//                        (DataproviderObject_1_0)states.get(id1+":7"),
//                        (DataproviderObject_1_0)states.get(id2+":5"),
//                        (DataproviderObject_1_0)states.get(id2+":6")
//                    },
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//            }
//            {
//                // all states from beginning to end (only valid)
//
//                // validFrom or validTo must be contained in request, otherwise
//                // requestedAt/For are used. 
//
//                StateADerivedQuery filter = pkg.createStateADerivedQuery();
//
//                filter.forAllStateValidTo().notAnElementOf(
//                    Collections.EMPTY_LIST
//                );
//
//                unitOfWork.begin();
//                List findings = modelSegment.getStateA(filter);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(filter);
//
//                // set up the states to expect:
//                checkJmiResultList(
//                    findings,
//                    new DataproviderObject_1_0[] {
//                        (DataproviderObject_1_0)states.get(id1+":1"),
//                        (DataproviderObject_1_0)states.get(id1+":4"),
//                        (DataproviderObject_1_0)states.get(id1+":5"),
//                        (DataproviderObject_1_0)states.get(id1+":6"),
//                        (DataproviderObject_1_0)states.get(id1+":7"),
//                        (DataproviderObject_1_0)states.get(id2+":3"),
//                        (DataproviderObject_1_0)states.get(id2+":4"),
//                        (DataproviderObject_1_0)states.get(id2+":5"),
//                        (DataproviderObject_1_0)states.get(id2+":6")
//                    },
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//            }
//
//            //
//            // now some tests with the history:
//            //
//            {
//                // states which are valid at a certain time point 
//                StateADerivedQuery filter = pkg.createStateADerivedQuery();
//
//                filter.modifiedAt().lessThan(times[1]);
//
//                filter.forAllObject_invalidatedAt().greaterThan(times[1]);
//
//                unitOfWork.begin();
//                List findings = modelSegment.getStateA(filter);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(filter);
//
//                // set up the states to expect:
//                checkJmiResultList(
//                    findings,
//                    new DataproviderObject_1_0[] {
//                        (DataproviderObject_1_0)states.get(id1+":2"),
//                        (DataproviderObject_1_0)states.get(id2+":2"),
//                    },
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//            }
//            {
//                // states which are valid in a period
//                StateADerivedQuery filter = pkg.createStateADerivedQuery();
//
//                filter.modifiedAt().greaterThan(times[0]);
//
//                // don't use forAllObjects, because this includes always the 
//                // still valid ones, which are null. 
//                filter.thereExistsObject_invalidatedAt().lessThan(times[2]);
//
//                unitOfWork.begin();
//                List findings = modelSegment.getStateA(filter);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(filter);
//
//                // set up the states to expect:
//                checkJmiResultList(
//                    findings,
//                    new DataproviderObject_1_0[] {
//                        (DataproviderObject_1_0)states.get(id1+":2"),
//                        (DataproviderObject_1_0)states.get(id2+":2")
//                    },
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//            }
//            {
//                // get all history states, created in this run
//                // here we don't have to find an artificial way to introduce
//                // null as search criteria. All the modifiedAt have a normal,
//                // valid date. So we can just search for all that have been 
//                // created before now. (To avoid problems with differing clocks
//                // on client and server one could also add a day or so)
//                // states which are valid in a period
//                StateADerivedQuery filter = pkg.createStateADerivedQuery();
//
//                filter.modifiedAt().lessThanOrEqualTo(new Date());
//                filter.createdAt().greaterThan(timeAtStart);
//
//
//                unitOfWork.begin();
//                List findings = modelSegment.getStateA(filter);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(filter);
//
//                // set up the states to expect
//                checkJmiResultList(
//                    findings,
//                    new DataproviderObject_1_0[] {
//                        (DataproviderObject_1_0)states.get(id1+":0"),
//                        (DataproviderObject_1_0)states.get(id1+":2"),
//                        (DataproviderObject_1_0)states.get(id1+":5"),
//                        (DataproviderObject_1_0)states.get(id2+":0"),
//                        (DataproviderObject_1_0)states.get(id2+":2"),
//                        (DataproviderObject_1_0)states.get(id2+":5")
//                    },
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//            }
//
//
//            // now span a time window using validFrom, validTo, modifiedAt, invalidatedAt
//            {
//                // states within defined range
//                // validTo, validFrom with real dates
//                StateADerivedQuery filter = pkg.createStateADerivedQuery();
//
//                filter.forAllStateValidTo().greaterThan(prepareDateAsDate("01.05.2002"));
//                filter.forAllStateValidFrom().lessThan(prepareDateAsDate("15.08.2002"));
//
//                filter.modifiedAt().greaterThan(timeAtStart);
//
//                // don't use forAllObjects, because this includes always the 
//                // still valid ones, which are null. 
//                filter.thereExistsObject_invalidatedAt().lessThan(times[2]);
//
//
//                unitOfWork.begin();
//                List findings = modelSegment.getStateA(filter);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(filter);
//
//                // set up the states to expect:
//                checkJmiResultList(
//                    findings,
//                    new DataproviderObject_1_0[] {
//                        (DataproviderObject_1_0)states.get(id1+":0"),
//                        (DataproviderObject_1_0)states.get(id1+":2"),
//                        (DataproviderObject_1_0)states.get(id2+":0"),
//                        (DataproviderObject_1_0)states.get(id2+":1"),
//                        (DataproviderObject_1_0)states.get(id2+":2")
//                    },
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//            }
//
//            {
//                // get all states
//                StateADerivedQuery filter = pkg.createStateADerivedQuery();
//
//                filter.forAllStateValidTo().notAnElementOf(
//                    Collections.EMPTY_LIST
//                );
//
//                filter.modifiedAt().lessThanOrEqualTo(new Date());
//
//                filter.modifiedAt().greaterThan(timeAtStart);
//
//                unitOfWork.begin();
//                List findings = modelSegment.getStateA(filter);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(filter);
//
//                // set up the states to expect
//                checkJmiResultList(
//                    findings,
//                    new DataproviderObject_1_0[] {
//                        (DataproviderObject_1_0)states.get(id1+":0"),
//                        (DataproviderObject_1_0)states.get(id1+":1"),
//                        (DataproviderObject_1_0)states.get(id1+":2"),
//                        (DataproviderObject_1_0)states.get(id1+":3"),
//                        (DataproviderObject_1_0)states.get(id1+":4"),
//                        (DataproviderObject_1_0)states.get(id1+":5"),
//                        (DataproviderObject_1_0)states.get(id1+":6"),
//                        (DataproviderObject_1_0)states.get(id1+":7"),
//                        (DataproviderObject_1_0)states.get(id2+":0"),
//                        (DataproviderObject_1_0)states.get(id2+":1"),
//                        (DataproviderObject_1_0)states.get(id2+":2"),
//                        (DataproviderObject_1_0)states.get(id2+":3"),
//                        (DataproviderObject_1_0)states.get(id2+":4"),
//                        (DataproviderObject_1_0)states.get(id2+":5"),
//                        (DataproviderObject_1_0)states.get(id2+":6")
//                    },
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//            }
//
//            // search with an additional condition
//            {
//                // get all history states
//                // here we don't have to find an artificial way to introduce
//                // null as search criteria. All the modifiedAt have a normal,
//                // valid date. So we can just search for all that have been 
//                // created before now. (To avoid problems with differing clocks
//                // on client and server one could also add a day or so)
//                // states which are valid in a period
//                StateADerivedQuery filter = pkg.createStateADerivedQuery();
//                filter.modifiedAt().lessThanOrEqualTo(new Date());
//                filter.modifiedAt().lessThanOrEqualTo(new Date());
//                filter.modifiedAt().greaterThan(timeAtStart);
//                filter.thereExistsValue().equalTo("BB");
//
//                unitOfWork.begin();
//                List findings = modelSegment.getStateA(filter);
////              Container findings = (Container) modelSegment.getStateA();
////              findings = findings.subSet(filter);
//
//                // set up the states to expect:
//                checkJmiResultList(
//                    findings,
//                    new DataproviderObject_1_0[] {
//                        (DataproviderObject_1_0)states.get(id1+":2"),
//                        (DataproviderObject_1_0)states.get(id1+":5"),
//                        (DataproviderObject_1_0)states.get(id2+":2"),
//                        (DataproviderObject_1_0)states.get(id2+":5")
//                    },
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//
//            }
//
//            // test find access through state reference
//            {
//                // get all valid states of object
//                unitOfWork.begin();
//                StateA stateA = modelSegment.getStateA(id1);
//                unitOfWork.commit();
//
//                StateAQuery filter = pkg.createStateAQuery(); // .createStateFilter();
//
//                /// can't use modifiedAt as filter value because not an attribute of State
//                /// can't use attributes of stated object as filter "   "    "   "   "  
//                filter.object_invalidatedAt().isNull();
//
//                unitOfWork.begin();
//                //
//                // Container findings = (Container)stateA.getState();
//                // findings = findings.subSet(filter);
//                //
////              filter.identity().equalTo(stateA.getIdentity());
////              filter.statedObject().isNonNull();
//                filter.thereExistsStatedObject().equalTo(stateA);
//                List findings = modelSegment.getStateA(filter);
//                //
//                checkJmiResultList(
//                    findings,
//                    new DataproviderObject_1_0[] {
//                        (DataproviderObject_1_0)states.get(id1+":1"),
//                        (DataproviderObject_1_0)states.get(id1+":4"),
//                        (DataproviderObject_1_0)states.get(id1+":5"),
//                        (DataproviderObject_1_0)states.get(id1+":6"),
//                        (DataproviderObject_1_0)states.get(id1+":7")
//                    },
//                    AbstractTestNoState_1.REF_STATE,
//                    usesSortablePersistence(), supportsHistory
//                );
//                unitOfWork.commit();
//
//            }
//
//            // test get access through state reference
////          {
////              StateA stateA = modelSegment.getStateA(id1);
////
////              String atTP = timeNow();
////              String forTP = prepareDate("01.01.2002");
////
////              PathComponent stateQualifier = new PathComponent(atTP);
////              stateQualifier.add(forTP);
////              unitOfWork.begin();
////              stateA = (StateA)stateA.getState(stateQualifier.toString());
////              unitOfWork.commit();
////
////              compareObjects(
////                  //(DataproviderObject)stateA.refDelegate(), 
////                  toDataproviderObject(stateA),
////                  (DataproviderObject_1_0)states.get(id1+":1"),
////                  AbstractTestDateStateExcludingEnd_1.REF_STATE
////              );
////          }
//
//            tearDownAfterTimeRangeSearch(pkg, modelSegment, id1, id2);
//
//
//        }
//        catch (Exception e) {
//            showException(e);
//            throw e;
//        }
//    }
//
//  //-------------------------------------------------------------------------    
//    /**
//     * Remove objects which could interfere wirh other tests (db mostly). 
//     * <p>
//     * Reseting the DB to its original state gets available for other tests 
//     * which would have a conflict otherwise.
//     * 
//     * @throws Exception
//     */
//    protected void removeCriticalObjects(
//        String baseId
//    ) throws Exception {
//        //String baseId = "RR";
//        String id1 = baseId + "_1";
//
//        InclusivePackage pkg = createInclusivePackage(new ServiceHeader());
//        Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
//
//        // get segment singleton
//        SegmentClass modelSegmentClass = pkg.getSegment();
//
//        unitOfWork.begin();
//        Segment modelSegment = modelSegmentClass.getSegment(
//                new Path(rootSegment)
//        );
//        unitOfWork.commit();
//
//        // reset db
//        try {
//            unitOfWork.begin();
//            modelSegment.removeStateC(id1);
//            unitOfWork.commit();
//        }
//        catch(JmiServiceException se) {
//          this.verifyException(
//            se.getExceptionStack(),
//            StackedException.NOT_FOUND,
//            null
//          );
//        }
//        catch(JDOException je) {
//          ServiceException se = (ServiceException) je.getCause();
//          this.verifyException(
//            se.getExceptionStack(),
//            StackedException.NOT_FOUND,
//            null
//          );
//        }
//    }




    //-------------------------------------------------------------------------    
    public void testStatedObjectContainedInNonStated(
    ) throws Exception {
      try {
        String baseId = "sins";
        String nsId1 = baseId + "_1";
        String nsId2 = baseId + "_2";
        String againId = baseId + "_sa";

        ServiceHeader header = new ServiceHeader();
        header.addPrincipal("me");
        InclusivePackage pkg = createInclusivePackage(header);
        Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
        SegmentClass modelSegmentClass = pkg.getSegment();
        StateAClass stateAClass = pkg.getStateA();
        StatedAgainClass statedAgainClass = pkg.getStatedAgain();
        NonStatedInStatedContainerClass nonStatedClass =
            pkg.getNonStatedInStatedContainer();

        Segment segment = modelSegmentClass.getSegment(
            new Path(rootSegment)
        );

        // first remove all the objects used here
//		try {
//		    StateA stateA = null;
//		    try {
//		        stateA = segment.getStateA(baseId);
//		    }
//		    catch (JmiServiceException jse) {
//		        // some of the Tests (eg testSharedRelationsInStateD)
//		        // remove all the stateA instances from segment, thus
//		        // "disconnecting" the objects contained in our stateA.
//		        // Just recreate our stateA and reconnect the contained objects.
//		        
//		        unitOfWork.begin();
//		        stateA = stateAClass.createStateA();
//		        stateA.setStateAttr("state to delete");
//		        stateA.setStateValidFrom(prepareDateAsDate("01.01.2002"));
//		        segment.addStateA(baseId, stateA);
//		        unitOfWork.commit();
//		    }
//		    
//		    try {
//		        unitOfWork.begin();
//		        NonStatedInStatedContainer nonStated1 =
//		            stateA.getNonStated(nsId1);
//		        nonStated1.removeStatedAgain(againId);
//		        unitOfWork.commit();
//		    }
//		    catch (Throwable t) {
//		        if (unitOfWork.isActive()) {
//		            unitOfWork.rollback();
//		        }
//		    }
//		    try {
//		        unitOfWork.begin();
//		        stateA.removeNonStated(nsId1);
//		        unitOfWork.commit();
//		    }
//		    catch (Throwable t) {
//		        if (unitOfWork.isActive()) {
//		            unitOfWork.rollback();
//		        }
//		    }
//		    try {
//		        unitOfWork.begin();
//		        stateA.removeNonStated(nsId2);
//		        unitOfWork.commit();
//		    }
//		    catch (Throwable t) {
//		        if (unitOfWork.isActive()) {
//		            unitOfWork.rollback();
//		        }
//		    }
//		    unitOfWork.begin();
//		    segment.removeStateA(baseId);
//		    unitOfWork.commit();
//		}
//		catch (Throwable e) {
//			showException(e);
//		    if (unitOfWork.isActive()) {
//		        unitOfWork.rollback();
//		    }
//		}

            StateA stateA = segment.getStateA(baseId);
            if(stateA == null) {
                // some of the Tests (eg testSharedRelationsInStateD)
                // remove all the stateA instances from segment, thus
                // "disconnecting" the objects contained in our stateA.
                // Just recreate our stateA and reconnect the contained objects.

                unitOfWork.begin();
                stateA = stateAClass.createStateA();
                stateA.setStateAttr("state to delete");
//              stateA.setStateValidFrom(prepareDateAsDate("01.01.2002"));
                segment.addStateA(baseId, stateA);
                unitOfWork.commit();
            }

            unitOfWork.begin();
        NonStatedInStatedContainer nonStated1 = stateA.getNonStated(nsId1);
        StatedAgain statedAgain = null;
        if(nonStated1 != null) statedAgain = nonStated1.getStatedAgain(againId);
        if(statedAgain != null) statedAgain.refDelete();
        unitOfWork.commit();

        unitOfWork.begin();
        NonStatedInStatedContainer nonStated = stateA.getNonStated(nsId1);
        if(nonStated != null) nonStated.refDelete();
        nonStated = stateA.getNonStated(nsId2);
        if(nonStated != null) nonStated.refDelete();
        unitOfWork.commit();

            unitOfWork.begin();
            stateA.refDelete();
            unitOfWork.commit();

        unitOfWork.begin();
        stateA = stateAClass.createStateA();
        stateA.setStateAttr("first state");
//      stateA.setStateValidFrom(prepareDateAsDate("01.01.2002"));
        segment.addStateA(baseId, stateA);
        unitOfWork.commit();
        // after commit the object is empty, must get any attribute which gets 
        // the entire object
        stateA.getUnderlyingState();
        checkSpecialAttributes(stateA);


        unitOfWork.begin();
        nonStated = nonStatedClass.createNonStatedInStatedContainer();
        nonStated.setName("first non stated");
//        nonStated.setObject_validFrom(prepareDateAsDate("01.01.2002"));
        stateA.addNonStated(nsId1, nonStated);
System.out.println("modfiedBy: " +nonStated.getModifiedBy());
System.out.println("createdAt: " + nonStated.getCreatedAt());

        unitOfWork.commit();

        // test 
System.out.println("modfiedBy: " +nonStated.getModifiedBy());
System.out.println("createdAt: " + nonStated.getCreatedAt());

        unitOfWork.begin();
        NonStatedInStatedContainer nonStated2 = nonStatedClass.createNonStatedInStatedContainer();
        nonStated2.setName("second non stated");
//        nonStated2.setObject_validFrom(prepareDateAsDate("01.01.2001"));
//        nonStated2.setObject_validTo(prepareDateAsDate("01.01.2010"));
        stateA.addNonStated(nsId2, nonStated2);
        unitOfWork.commit();

        unitOfWork.begin();
        stateA.setSpecialNonStated(nonStated);
//        stateA.setStateValidFrom(prepareDateAsDate("01.01.2002"));
        unitOfWork.commit();
        stateA.getStatedObject();
        checkSpecialAttributes(stateA);


        // update of non stated
        unitOfWork.begin();
        nonStated2.setName("second non stated updated");
//        nonStated2.setObject_validFrom(prepareDateAsDate("01.01.2002"));
//        nonStated2.setObject_validTo(prepareDateAsDate("01.01.2010"));
        unitOfWork.commit();


        unitOfWork.begin();
        statedAgain = statedAgainClass.createStatedAgain();
        statedAgain.setName("first state of statedAgain");
//        statedAgain.setStateValidFrom(prepareDateAsDate("01.02.2002"));
        nonStated.addStatedAgain(againId, statedAgain);
        unitOfWork.commit();


        // just some simple test:
        {
            stateA = null;
            nonStated = null;
            statedAgain = null;
            stateA = segment.getStateA(baseId);
            assertNotNull("expected stateA to exist", stateA);

            // test accessing special attributes
            // avoid rountrips, allow access to these attributes
            checkSpecialAttributes(stateA);

            NonStatedInStatedContainer special = stateA.getSpecialNonStated();
            nonStated = stateA.getNonStated(nsId1);

            assertEquals(
                "non stated and special non stated should be same",
                special, nonStated
            );

            statedAgain = nonStated.getStatedAgain(againId);
            assertNotNull("expected statedAgain to exist", statedAgain);
        }

      }
      catch (Exception se) {
          showException(se);
          throw se;
      }
    }


    /**
     * Check that the special attributes are present from start and that they 
     * are accessible.
     * 
     * @param object
     */
    private void checkSpecialAttributes(StateA object) {
        if(object instanceof RefObject_1_0) {
            RefObject_1_0 stateA = (RefObject_1_0) object;
            assertTrue(
                "identity not fetched in first access",
                stateA.refDefaultFetchGroup().contains(SystemAttributes.OBJECT_IDENTITY)
            );

            assertTrue(
                "underlyingState not fetched in first access",
                stateA.refDefaultFetchGroup().contains("underlyingState")
            );

            assertTrue(
                "statedObject not fetched in first access",
                stateA.refDefaultFetchGroup().contains("statedObject")
            );

            assertTrue(
                "object_invalidatedAt not fetched in first access",
                stateA.refDefaultFetchGroup().contains("object_invalidatedAt")
            );
        } else {
            // TODO
        }

        // now try accessing it
        object.getIdentity();
        object.getObject_invalidatedAt();
        object.getUnderlyingState();
        object.getStatedObject();
    }


    /**
     * Try finding out which operations are time consuming.
     *
     */
    public void testPerformance(
    ) throws Exception {
        try {
        String timer = null;
        String baseId = "perf";
        final int num = 10;

        TreeStopWatch TSW = new TreeStopWatch();
        StopWatch_1.setStopWatch(TSW);

        TSW.switchTest("testPerformance");

        InclusivePackage pkg = createInclusivePackage(new ServiceHeader());
        Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
        SegmentClass modelSegmentClass = pkg.getSegment();
        StateCClass stateCClass = pkg.getStateC();

        Segment modelSegment = modelSegmentClass.getSegment(
            new Path(rootSegment)
        );

        // first remove all the objects used here
        for (int i = 0; i < num; i++) {
            try {
                unitOfWork.begin();
                modelSegment.removeStateC(baseId + String.valueOf(i));
                unitOfWork.commit();
            }
            catch(JmiServiceException je) {
                if (unitOfWork.isActive()) {
                    unitOfWork.rollback();
                }
            }
                catch(JDOException je) {
                if (unitOfWork.isActive()) {
                    unitOfWork.rollback();
                }
          }
        }


        // create an object and split it up several times; then replace by new 
        // state
        StateC[] objects = new StateC[num];

        for (int i = 0; i < num; i++) {
            StateC stateC = stateCClass.createStateC();
            StopWatch_1.instance().startTimer(timer = "createObj");

            unitOfWork.begin();

            stateC.setStateAttr("startState");
            stateC.getValue().add("1111");
            stateC.getValue().add(String.valueOf(i));
//          stateC.setStateValidFrom(prepareDateAsDate("01.01.1900"));
            stateC.setStateValidFrom(null);
//          stateC.setStateValidTo(prepareDateAsDate("01.12.2100"));
            stateC.setStateValidTo(null);
            modelSegment.addStateC(baseId + String.valueOf(i), stateC);
            objects[i] = stateC;
            unitOfWork.commit();
            StopWatch_1.instance().stopTimer(timer);
        }

        // update for states
        int startYear = 1900;
        int endYear = 2100;

        for (int u = 0; u < 5; u++) {
            startYear += 10;
            endYear -= 10;
            for (int i = 0; i < num; i++) {
                StateC stateC = objects[i];
                StopWatch_1.instance().startTimer(timer = "smallUpdateObj");

                unitOfWork.begin();

                stateC.setStateAttr("state "+ String.valueOf(u));

//              stateC.setStateValidFrom(prepareDateAsDate("01.01." + String.valueOf(startYear)));
                stateC.setStateValidFrom(null);
//              stateC.setStateValidTo(prepareDateAsDate("01.12." + String.valueOf(endYear)));
                stateC.setStateValidTo(null);
                unitOfWork.commit();
                StopWatch_1.instance().stopTimer(timer);
            }
        }

        for (int i = 0; i < num; i++) {
            StateC stateC = objects[i];
            StopWatch_1.instance().startTimer(timer = "bigUpdateObj");

            unitOfWork.begin();

            stateC.setStateAttr("big update spanning all existing");

//          stateC.setStateValidFrom(prepareDateAsDate("01.01.1899"));
            stateC.setStateValidFrom(null);
//          stateC.setStateValidTo(prepareDateAsDate("01.12.2001"));
            stateC.setStateValidTo(null);
            unitOfWork.commit();
            StopWatch_1.instance().stopTimer(timer);
        }


        // now remove all the objects used here
        for (int i = 0; i < num; i++) {
            StopWatch_1.instance().startTimer(timer = "removeObj");

            try {
                unitOfWork.begin();
                modelSegment.removeStateC(baseId + String.valueOf(i));
                unitOfWork.commit();
            }
            catch(JmiServiceException je) {
                if (unitOfWork.isActive()) {
                    unitOfWork.rollback();
                }
            }
                catch(JDOException je) {
                if (unitOfWork.isActive()) {
                    unitOfWork.rollback();
                }
          }

            StopWatch_1.instance().stopTimer(timer);
        }


        TSW.printOut("testPerformance", new PrintWriter(System.out, true));

        StringWriter buffer = new StringWriter();
        TSW.printOut("testPerformance", new PrintWriter(buffer, true));
        buffer.flush();

        AppLog.statistics("test", buffer.toString());
        AppLog.statistics("test", "test");

        StopWatch_1.setStopWatch(null);
    }
    catch (Exception se) {
        showException(se);
        throw se;
    }
    }


//    /**
//     * test the shared relation to stateD
//     * @param supportsHistory 
//     * @throws Exception
//     */
//    public void testSharedRelationStateD(boolean supportsHistory
//    ) throws Exception {
//        try {
//
//        String baseId = "SR";
//        String stateA1Id = baseId + "A1";
//        String stateA2Id = baseId + "A2";
//        String stateD1Id = baseId + "D1";
//        String stateD2Id = baseId + "D2";
//        // D3 is within A2, so it can be called like an existing D
//        String stateD3Id = baseId + "D1";
//
//        Date timeAtStart = new Date();
//
//        InclusivePackage pkg = createInclusivePackage(new ServiceHeader());
//        Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
//
//        SegmentClass segmentClass = pkg.getSegment();
//
//        StateAClass stateAClass = pkg.getStateA();
//        StateDClass stateDClass = pkg.getStateD();
//
//        unitOfWork.begin();
//        Segment segment = segmentClass.getSegment(
//          new Path(rootSegment)
//        );
//        unitOfWork.commit();
//
//        // first clean all stateD in all stateA and all stateA
//        Collection stateAs = segment.getStateA();
//        for (Iterator a = stateAs.iterator(); a.hasNext();) {
//            StateA stateA = (StateA)a.next();
//            unitOfWork.begin();
//            for (Iterator d = stateA.getStateD().iterator(); d.hasNext();) {
//                d.next();
//                d.remove();
//            }
//            unitOfWork.commit();
//        }
//
//        unitOfWork.begin();
//        stateAs = segment.getStateA();
//        for (Iterator a = stateAs.iterator(); a.hasNext();) {
//            a.next();
//            a.remove();
//        }
//        unitOfWork.commit();
//
//        StateA stateA1 = stateAClass.createStateA();
//        stateA1.setStateAttr("first state");
//        // need a date to be accepted as stated client
//        stateA1.setStateValidFrom(prepareDateAsDate("01.01.1900"));
//        unitOfWork.begin();
//        segment.addStateA(stateA1Id, stateA1);
//        unitOfWork.commit();
//        DataproviderObject a1s0 = toDataproviderObject(stateA1);
//
//
//        StateA stateA2 = stateAClass.createStateA();
//        stateA2.setStateAttr("second state");
//        // need a date to be accepted as stated client
//        stateA2.setStateValidFrom(prepareDateAsDate("01.01.1900"));
//        unitOfWork.begin();
//        segment.addStateA(stateA2Id, stateA2);
//        unitOfWork.commit();
//
//        // now add stateD
//        StateD stateD1 = stateDClass.createStateD();
//        stateD1.setLongVal(new Long(1));
//        stateD1.setIntegerVal(new Integer(1));
//        // need multivalued attribute
//        stateD1.getLongValues().put(
//            new Integer(0),
//            new Long(0)
//        );
//        stateD1.getLongValues().put(
//            new Integer(1),
//            new Long(1)
//        );
//        stateD1.getLongValues().put(
//            new Integer(2),
//            new Long(2)
//        );
//        // need a date to be accepted as stated client
//        stateD1.setStateValidFrom(prepareDateAsDate("01.01.1900"));
//        unitOfWork.begin();
//        stateA1 = segment.getStateA(stateA1Id);
//        stateA1.addStateD(stateD1Id, stateD1);
//        unitOfWork.commit();
//
//        DataproviderObject d1s0 = toDataproviderObject(stateD1);
//        Path basePath = d1s0.path().getPrefix(d1s0.path().size()-4);
//        basePath.add("stateD");
//        // path of direct access:
//        Path directD1Path = new Path(basePath);
//        directD1Path.add(stateA1Id + ":" + stateD1Id);
//        d1s0.path().setTo(directD1Path);
//        d1s0.values(SystemAttributes.OBJECT_IDENTITY).set(0, directD1Path.toUri());
//        //SOID d1s0.values(State_1_Attributes.STATED_OBJECT_IDENTITY).set(0, directD1Path.toUri());
//
//        // now update stateD to have some states 
//        unitOfWork.begin();
//        stateD1.setStateValidFrom(prepareDateAsDate("01.08.2001"));
//        stateD1.setStateValidTo(prepareDateAsDate("30.09.2001"));
//        stateD1.setLongVal(new Long(12));
//        stateD1.setStateAttr("second state");
//        unitOfWork.commit();
//        DataproviderObject d1s1 = (DataproviderObject)d1s0.clone();
//        DataproviderObject d1s2 = (DataproviderObject)d1s0.clone();
//        DataproviderObject d1s3 = (DataproviderObject)d1s0.clone();
//        d1s0.clearValues("object_invalidatedAt").add(INVALIDATION_INDICATOR);
//
//        d1s1.values("stateValidTo").set(0,prepareDate("31.07.2001"));
//
//        d1s2.values("stateValidFrom").set(0, prepareDate("01.08.2001"));
//        d1s2.values("stateValidTo").set(0, prepareDate("30.09.2001"));
//        d1s2.values("longVal").set(0,new BigDecimal(12));
//        d1s2.values("stateAttr").set(0, "second state");
//
//        d1s3.values("stateValidFrom").set(0,prepareDate("01.10.2001"));
//
//
//        // add a second stateD to A1
//        StateD stateD2 = stateDClass.createStateD();
//        stateD2.setLongVal(new Long(2));
//        stateD2.setIntegerVal(new Integer(2));
//        stateD2.getLongValues().put(
//            new Integer(0),
//            new Long(2)
//        );
//        stateD2.getLongValues().put(
//            new Integer(1),
//            new Long(2)
//        );
//        stateD2.getLongValues().put(
//            new Integer(2),
//            new Long(2)
//        );
//        // need a date to be accepted as stated client
//        stateD2.setStateValidFrom(prepareDateAsDate("01.01.1900"));
//        unitOfWork.begin();
//        stateA1.addStateD(stateD2Id, stateD2);
//        unitOfWork.commit();
//        DataproviderObject d2s0 = toDataproviderObject(stateD2);
//        Path directD2Path = new Path(basePath);
//        directD2Path.add(stateA1Id + ":" + stateD2Id);
//        d2s0.path().setTo(directD2Path);
//        d2s0.attributeNames().remove(SystemAttributes.OBJECT_IDENTITY);
//        //SOID d2s0.values(State_1_Attributes.STATED_OBJECT_IDENTITY).set(0, directD2Path.toUri());
//
//        // add third stateD to A2
//        StateD stateD3 = stateDClass.createStateD();
//        stateD3.setLongVal(new Long(3));
//        stateD3.setIntegerVal(new Integer(3));
//        stateD3.getLongValues().put(
//            new Integer(0),
//            new Long(3)
//        );
//        stateD3.getLongValues().put(
//            new Integer(1),
//            new Long(3)
//        );
//        stateD3.getLongValues().put(
//            new Integer(2),
//            new Long(3)
//        );
//        // need a date to be accepted as stated client
//        stateD3.setStateValidFrom(prepareDateAsDate("01.01.1900"));
//        unitOfWork.begin();
//        stateA2 = segment.getStateA(stateA2Id);
//        stateA2.addStateD(stateD3Id, stateD3);
//        unitOfWork.commit();
//        DataproviderObject d3s0 = toDataproviderObject(stateD3);
//        Path directD3Path = new Path(basePath);
//        directD3Path.add(stateA2Id + ":" + stateD3Id);
//        d3s0.path().setTo(directD3Path);
//        d3s0.attributeNames().remove(SystemAttributes.OBJECT_IDENTITY);
//        //SOID d3s0.values(State_1_Attributes.STATED_OBJECT_IDENTITY).set(0, directD3Path.toUri());
//
//
//        {
//            // do the get for all at the end 
//            unitOfWork.begin();
//            // normal access to objects
//            checkJmiResultList(
//                segment.getStateD(),
//                new DataproviderObject[] {
//                    d1s3, d2s0, d3s0
//                },
//                AbstractTestNoState_1.REF_STATE,
//                false, supportsHistory
//            );
//            unitOfWork.commit();
//        }
//
//
//        {
//            unitOfWork.begin();
//            // get all states 
//            StateDQuery filter = pkg.createStateDQuery();
//            filter.forAllStateValidTo().notAnElementOf(Collections.EMPTY_LIST);
//            filter.modifiedAt().lessThanOrEqualTo(new Date());
//            filter.modifiedAt().greaterThan(timeAtStart);
////          Container result = (Container)segment.getStateD();^
//            List findings = segment.getStateD(filter);
//
//            checkJmiResultList(
//                findings, // result.subSet(filter),
//                new DataproviderObject[] {
//                    d1s0, d1s1, d1s2, d1s3, d2s0, d3s0
//                },
//                AbstractTestNoState_1.REF_STATE,
//                false, supportsHistory
//            );
//            unitOfWork.commit();
//        }
//        {
//            unitOfWork.begin();
//            // get all states of one object
//            StateDQuery filter = pkg.createStateDQuery();
//
//            filter.forAllStateValidTo().notAnElementOf(Collections.EMPTY_LIST);
//            filter.modifiedAt().lessThanOrEqualTo(new Date());
//            filter.modifiedAt().greaterThan(timeAtStart);
//            // NOTE: can't use original identity for search; need to construct
//            // new which represents changed access and new constructed ID!
//            Path idPath = new Path(basePath);
//            idPath.add(stateA1Id + ":" + stateD1Id);
//            System.out.println("suche mit idPath:" + idPath.toUri());
//            filter.identity().equalTo(idPath.toUri());
////          Container result = (Container)segment.getStateD();
//            List findings = segment.getStateD(filter);
//            checkJmiResultList(
//                findings, // result.subSet(filter),
//                new DataproviderObject[] {
//                    d1s0, d1s1, d1s2, d1s3
//                },
//                AbstractTestNoState_1.REF_STATE,
//                false, supportsHistory
//            );
//            unitOfWork.commit();
//
//        }
//        {
//            unitOfWork.begin();
//            // get all states of one object (direct, not through a view)
//            StateAQuery filter = pkg.createStateAQuery();
//            filter.forAllStateValidTo().notAnElementOf(Collections.EMPTY_LIST);
//            filter.modifiedAt().lessThanOrEqualTo(new Date());
//            filter.modifiedAt().greaterThan(timeAtStart);
//
//            System.out.println("suche mit Path   (toUri()): " + a1s0.path().toUri());
//            System.out.println("und nicht mit (toString()): " + a1s0.path().toString());
//
//            filter.identity().equalTo(a1s0.path().toUri());
////          Container result = (Container)segment.getStateA();
//            List findings = segment.getStateA(filter);
//            checkJmiResultList(
//                findings, // result.subSet(filter),
//                new DataproviderObject[] {
//                    a1s0
//                },
//                AbstractTestNoState_1.REF_STATE,
//                false, supportsHistory
//            );
//            unitOfWork.commit();
//        }
//
//        }
//        catch (Exception se) {
//            showException(se);
//            throw se;
//        }
//  }
//
//  //-------------------------------------------------------------------------    
//  /** 
//   * this "test" is for writing the documentation. 
//   */
//    public void testDocu() throws Exception {
//      try {
//        ServiceHeader header =
//          new ServiceHeader(
//            (String) null,
//            null,
//            false,
//            new QualityOfService(),
//            null,
//            "20020701T000000.000Z"
//          //sdf.parse("01.07.2002")
//        );
//        header.addPrincipal("anyff");
//
//        InclusivePackage pkg = createInclusivePackage(header);
//        Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
//
//        // get segment singleton
//        SegmentClass modelSegmentClass = pkg.getSegment();
//        unitOfWork.begin();
//        Segment modelSegment =
//          modelSegmentClass.getSegment(new Path(rootSegment));
//        unitOfWork.commit();
//
//        // case the delete did go wrong:
//        try {
//          unitOfWork.begin();
//          modelSegment.removeStateNoState("id_noState");
//          unitOfWork.commit();
//        }
//        catch (Exception se) {
//          if (unitOfWork.isActive())
//            unitOfWork.rollback();
//        }
//
//        StateNoStateClass noStateClass = pkg.getStateNoState();
//        StateNoState noState =
//          noStateClass.createStateNoState("no states here");
//        unitOfWork.begin();
//        modelSegment.addStateNoState("id_noState", noState);
//        unitOfWork.commit();
//
//        System.out.println(" create a  StateDoc object ...");
//
//        StateDocClass stateDocClass = pkg.getStateDoc();
//        StateDoc stateDoc = stateDocClass.createStateDoc();
//        stateDoc.setStateValidFrom(prepareDateAsDate("01.01.2002"));
//        stateDoc.setStateValidTo(prepareDateAsDate("30.11.2002"));
//        stateDoc.setA1("a");
//        stateDoc.setA2(new Integer(1));
//
//        // the code which starts at column 0 is used in examples
//
//        //modelSegment
//        unitOfWork.begin();
//        noState.addStateDoc("id_1", stateDoc);
//        unitOfWork.commit();
//
//        // update with new state (on 1.2.)
//        unitOfWork.begin();
//        stateDoc.setStateValidFrom(prepareDateAsDate("01.06.2002"));
//        stateDoc.setStateValidTo(prepareDateAsDate("31.08.2002"));
//        stateDoc.setA2(new Integer(2));
//        unitOfWork.commit();
//
//        unitOfWork.begin();
//        StateDoc stateS2 = noState.getStateDoc(
//          "id_1"
//          + ";validFrom=" + prepareDate("01.03.2002")
//          + ";validTo=" + prepareDate("31.08.2002")
//        );
//        stateS2.setA1("b");
//        unitOfWork.commit();
//
//        // update on 1.4.
//        unitOfWork.begin();
//        stateS2 = noState.getStateDoc(
//          "id_1"
//          + ";validFrom=" + prepareDate("01.05.2002")
//          + ";validTo=" + prepareDate("19.09.2002")
//        );
//        stateS2.setA1("a");
//        stateS2.setA2(new Integer(1));
//        unitOfWork.commit();
//
//        System.out.println("show all");
//        //
//        // get all valid State
//        // Collection states = stateS2.getValidState();
//        //
//        StateDocQuery filter = pkg.createStateDocQuery();
//        filter.object_invalidatedAt().isNull();
////      filter.identity().equalTo(stateS2.getIdentity());
////      filter.statedObject().isNonNull();
//        filter.thereExistsStatedObject().equalTo(stateS2);
//        Collection states = noState.getStateDoc(filter);
//        //
//        for (Iterator i = states.iterator(); i.hasNext();) {
//          StateDoc stateSx = (StateDoc) i.next();
//          System.out.println(
//            "State : validFrom: "
//            + stateSx.getStateValidFrom()
//            + ", validTo: "
//            + stateSx.getStateValidTo()
//            + ", a1: "
//            + stateSx.getA1()
//            + ", valid: "
//            + Boolean.valueOf(stateSx.getObject_invalidatedAt() == null)
//          );
//        }
//
//        {
//
//          // initialising package        
//          Provider_1_0 provider =
//            new Provider_1(
//              new RequestCollection(header, AbstractTestNoState_1.connection),
//              false);
//
//          ObjectFactory_1_0 manager =
//            new Manager_1(new Connection_1(provider, false));
//
//          RefPackage_1_1 rootPkg =
//            new RefRootPackage_1(manager, null, null);
//
//          InclusivePackage pastPkg =
//          (InclusivePackage) rootPkg.refPackage("org:openmdx:test:compatibility:state1:inclusive");
//          Transaction pastUnitOfWork = rootPkg.refPersistenceManager().currentTransaction();
//
//          // get segment singleton
//          SegmentClass pastModelSegmentClass = pastPkg.getSegment();
//          pastUnitOfWork.begin();
//          Segment pastModelSegment =
//            pastModelSegmentClass.getSegment(new Path(rootSegment));
//          pastUnitOfWork.commit();
//
//          System.out.println(" get pastNoState...");
//          pastUnitOfWork.begin();
//          StateNoState pastNoState =
//            pastModelSegment.getStateNoState("id_noState");
//          pastUnitOfWork.commit();
//
//          // first get a certain instance of StateDoc
//          pastUnitOfWork.begin();
//          StateDoc pastState = pastNoState.getStateDoc("id_1");
//          pastUnitOfWork.commit();
//
//          System.out.println("show all past states at a certain time point");
//          // get all valid State
//          pastUnitOfWork.begin();
//          //
//          // states = pastState.getValidState();
//          //
//          filter = pkg.createStateDocQuery();
//          filter.object_invalidatedAt().isNull();
////        filter.identity().equalTo(pastState.getIdentity());
////        filter.statedObject().isNonNull();
//          filter.thereExistsStatedObject().equalTo(pastState);
//          //
//          for (Iterator i = states.iterator(); i.hasNext();) {
//            StateDoc stateSx = (StateDoc) i.next();
//            System
//              .out
//              .println(
//                "State : validFrom: "
//                + stateSx.getStateValidFrom()
//                + ", validTo: "
//                + stateSx.getStateValidTo()
//                + ", a1: "
//                + stateSx.getA1()
//                +
//            //        ", a2: " + stateSx.getA2() + 
//            ", valid: "
//              + Boolean.valueOf(stateSx.getObject_invalidatedAt() == null));
//          }
//          pastUnitOfWork.commit();
//
//        }
//
//        // delete it
//        unitOfWork.begin();
//        noState.getStateDoc().remove(stateDoc);
//        unitOfWork.commit();
//
//        unitOfWork.begin();
//        modelSegment.getStateNoState().remove(noState);
//        unitOfWork.commit();
//
//        System.out.println(" update with an empty object");
//
//      }
//      catch (Exception se) {
//        showException(se);
//        throw se;
//      }
//    }

  //-------------------------------------------------------------------------    
  /**
   * test the setOperation
   * <p>
   * just some very basic tests. 
   * 
   */
  public void testSetOperation()
  throws Exception
  {
    try {
      DataproviderObject work = null;
      DataproviderObject_1_0 result = null;
//      List results = null;
      ServiceException ex = null;

      String id = "set_test_1";

      // set the object initially (i.e. create)
      work = prepareObject("StateA", null, new String[] {"stateA", id + ";validFrom=ever;validTo=ever"}, "State 1", new String[] { "firstVal" }, null );
      ex = null;
      result = (DataproviderObject_1_0) executeOperation("set test: create", setOperation, work, null, null, ex);
      checkResult(result, "State 1", new String[] { "firstVal" }, null, null);

      // set the object to some other values (i.e. update)
      work = prepareObject("StateA", null, new String[] {"stateA", id + ";validFrom=ever;validTo=ever"}, "State 2", new String[] { "secondVal" }, null );
      ex = null;
      // need date to be accepted as stated client
//    this.setValidity(work, "01.01.1900", null); // t1: ----|11111111111111111111>    
      result = (DataproviderObject_1_0) executeOperation("set test: update", setOperation, work, null, null, ex);
//    checkResult(result, "State 2", new String[] { "secondVal" }, "01.01.1900", null);
      checkResult(result, "State 2", new String[] { "secondVal" }, null, null);

      // and don't forget to cleanup:
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add(id));
      work.values("selector").set(0,new Short(AttributeSelectors.ALL_ATTRIBUTES)); // helper construct for providing attributes   
      result = (DataproviderObject_1_0) executeOperation("remove object", removeOperation, work, null, "01.01.2000", ex);
//    checkResult(result, "State 2", new String[] { "secondVal" }, "01.01.1900", null);
      checkResult(result, "State 2", new String[] { "secondVal" }, null, null);
    }
    catch (Exception e) {
       showException(e);
       throw e;
    }
  }

  //-------------------------------------------------------------------------    
  public void testStateBasicNormalId(boolean supportsHistory
  ) throws Exception {
    basicTest("idOne", supportsHistory);
  }

//  //-------------------------------------------------------------------------    
//  public void testStateBasicSpecialId(boolean supportsHistory
//  ) throws Exception {
//    basicTest("xri:@openmdx:test.for.a.client/don:t/use/paths", supportsHistory);
//  }

  //-------------------------------------------------------------------------    
  /**
   * some first basic tests
 * @param supportsHistory 
   */
  public void basicTest(
      String objectId,
      boolean supportsHistory)
  throws Exception
   {
      // Careful: storage can't be deleted in the end!

      DataproviderObject work = null;
      DataproviderObject_1_0 result = null;
      ServiceException ex = null;

      // String objectId = "xri:@openmdx:test.for.a.client/don:t/use/paths";//"IdOne" + findTestID();
      System.out.println("testing with id: " + objectId);

      // **** create object
//      String t1minus = timeNow();
      work = prepareObject("StateA", null, new String[] {"stateA", objectId}, "State 1", new String[] { "firstVal" }, null );
//    setValidity(work, "01.01.2001", null); // t1: ----|11111111111111111111>    
      setValidity(work, null, null); // t1: ----|11111111111111111111>    
      ex = null;
      result = (DataproviderObject_1_0) executeOperation("create IdOne", createOperation, work, null, null, ex);
//    checkResult(result, "State 1", new String[] { "firstVal" }, "01.01.2001", null);
      checkResult(result, "State 1", new String[] { "firstVal" }, null, null);
      String t1 = (String) result.getValues(SystemAttributes.CREATED_AT).get(0);

      Thread.sleep(100); // to ensure gap

      // **** get the object 
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add(objectId));

      result = (DataproviderObject_1_0) executeOperation("get IdOne 1", getOperation, work, null, "02.01.2001", null);
//      checkResult(result, "State 1", new String[] { "firstVal" }, "01.01.2001", null);
      checkResult(result, "State 1", new String[] { "firstVal" }, null, null);

      result = (DataproviderObject_1_0) executeOperation("get IdOne 2", getOperation, work, null, "01.01.2001", null);
//      checkResult(result, "State 1", new String[] { "firstVal" }, "01.01.2001", null);
      checkResult(result, "State 1", new String[] { "firstVal" }, null, null);

//      ex = new ServiceException(StackedException.DEFAULT_DOMAIN, StackedException.NOT_FOUND, null, null);
      ex = null;
      result = (DataproviderObject_1_0) executeOperation("get IdOne 3", getOperation, work, null, "31.12.2000", ex);


      // **** set back the start (using modify)
      work = prepareObject("StateA", null, new String[] {"stateA", objectId}, "State 2", new String[] { "secondVal" } , null);
//    setValidity(work, "29.12.2000", null); // t2: -|22222222222222222222222>        
      setValidity(work, null, null); // t2: -|22222222222222222222222>        
      ex = null;
//    result = (DataproviderObject_1_0) executeOperation("set back start of IdOne", modifyOperation, work, null, null, ex);
      result = (DataproviderObject_1_0) executeOperation("set back start of IdOne", replaceOperation, work, null, null, ex);
//      checkResult(result, "State 2", new String[] { "secondVal" }, "29.12.2000", null);
      checkResult(result, "State 2", new String[] { "secondVal" }, null, null);


      // **** add state 3
      work = prepareObject("StateA", null, new String[] {"stateA", objectId}, "State 3", new String[] { "thirdVal" }, null );
//    setValidity(work, "01.10.2001", null); // t3: -|2222222333333333333333>        
      setValidity(work, null, null); // t3: -|2222222333333333333333>        
      ex = null;
//    result = (DataproviderObject_1_0) executeOperation("add state 3", modifyOperation, work, null, "01.11.2001", ex);
      result = (DataproviderObject_1_0) executeOperation("add state 3", replaceOperation, work, null, "01.11.2001", ex);
//      checkResult(result, "State 3", new String[] { "thirdVal" }, "01.10.2001", null);
      checkResult(result, "State 3", new String[] { "thirdVal" }, null, null);
      String t3 = (String) result.getValues(SystemAttributes.CREATED_AT).get(0);
      String t3plus = timeNow();

      Thread.sleep(500); // to ensure gap

      // **** add state 4
      work = prepareObject("StateA", null, new String[] {"stateA", objectId}, "State 4", new String[] { "forthVal" }, null );
//    setValidity(work, "01.07.2001", "19.10.2001"); // t4: -|22244444433333333333333>        
      setValidity(work, null, null); // t4: -|22244444433333333333333>        
      ex = null;
//    result = (DataproviderObject_1_0) executeOperation("add state 4", modifyOperation, work, null, "01.08.2001", ex);
      result = (DataproviderObject_1_0) executeOperation("add state 4", replaceOperation, work, null, "01.08.2001", ex);
//      checkResult(result, "State 4", new String[] { "forthVal" }, "01.07.2001", "19.10.2001");
      checkResult(result, "State 4", new String[] { "forthVal" }, null, null);

      Thread.sleep(100); // to ensure gap

      // **** add state 5 replacing former state
      work = prepareObject("StateA", null, new String[] {"stateA", objectId}, "State 5", new String[] { "fifthVal" } , null);
//    setValidity(work, "01.06.2001", "31.10.2001"); // t5: -|22555555555333333333333>       
      setValidity(work, null, null); // t5: -|22555555555333333333333>       
      ex = null;
      result = (DataproviderObject_1_0) executeOperation("add state 5", replaceOperation, work, null, "01.07.2001", ex);
//      checkResult(result, "State 5", new String[] { "fifthVal" }, "01.06.2001", "31.10.2001");
      checkResult(result, "State 5", new String[] { "fifthVal" }, null, null);


      // try to get the different states.
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add(objectId));

      result = (DataproviderObject_1_0) executeOperation("get state2", getOperation, work, null, "01.05.2001", null);
//      checkResult(result, "State 2", new String[] { "secondVal" }, "29.12.2000", "31.05.2001");
      checkResult(result, "State 5", new String[] { "fifthVal" }, null, null); // TODO verify

        result = (DataproviderObject_1_0) executeOperation("get state5 ", getOperation, work, null, "01.08.2001", null);
//        checkResult(result, "State 5", new String[] { "fifthVal" }, "01.06.2001", "31.10.2001");
        checkResult(result, "State 5", new String[] { "fifthVal" }, null, null);

      result = (DataproviderObject_1_0) executeOperation("get state3 ", getOperation, work, null, "01.12.2001", null);
//      checkResult(result, "State 3", new String[] { "thirdVal" }, "01.11.2001", null);
      checkResult(result, "State 5", new String[] { "fifthVal" }, null, null); // TODO verify

      result = (DataproviderObject_1_0) executeOperation("get state3 at start", getOperation, work, null, "01.11.2001", null);
//      checkResult(result, "State 3", new String[] { "thirdVal" }, "01.11.2001", null);
      checkResult(result, "State 5", new String[] { "fifthVal" }, null, null); // TODO verify

      
      if(supportsHistory) {
          // try to get states in the past (use requestedAt)
          result = (DataproviderObject_1_0) executeOperation("get t1 entry", getOperation, work, t1, "01.02.2001", null);
//          checkResult(result, "State 1", new String[] { "firstVal" }, "01.01.2001", null);
          checkResult(result, "State 1", new String[] { "firstVal" }, null, null);

          result = (DataproviderObject_1_0) executeOperation("get t3 entry", getOperation, work, t3, "01.02.2001", null);
//          checkResult(result, "State 2", new String[] { "secondVal" }, "29.12.2000", "30.09.2001");
          checkResult(result, "State 2", new String[] { "secondVal" }, null, null);

          result = (DataproviderObject_1_0) executeOperation("get t3 entry after creation", getOperation, work, t3plus, "01.09.2001", null);
//          checkResult(result, "State 2", new String[] { "secondVal" }, "29.12.2000", "30.09.2001");
          checkResult(result, "State 2", new String[] { "secondVal" }, null, null);
      }

      // try to reduce the lifetime of the object

      // cut the first state
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add(objectId));
      work.clearValues(SystemAttributes.OBJECT_CLASS).
          add(0, "org:openmdx:test:compatibility:state1:inclusive:StateA");
//    setValidity(work, "30.12.2000", null); // --|2555555555333333333333>
      setValidity(work, null, null); // --|2555555555333333333333>
      ex = null;
//    result = (DataproviderObject_1_0) executeOperation("reduce lifetime at start", modifyOperation, work, null, "31.12.2000", ex);
      result = (DataproviderObject_1_0) executeOperation("reduce lifetime at start", replaceOperation, work, null, "31.12.2000", ex);
//      checkResult(result, "State 2", new String[] { "secondVal" }, "30.12.2000", "31.05.2001");
      checkResult(result, "State 5", new String[] { "fifthVal" }, null, null); // TODO verify

      // remove the first state
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add(objectId));
      work.clearValues(SystemAttributes.OBJECT_CLASS).
          add(0, "org:openmdx:test:compatibility:state1:inclusive:StateA");
//    setValidity(work, "10.06.2001", null); // ------|555555333333333333>
      setValidity(work, null, null); // ------|555555333333333333>
//    result = (DataproviderObject_1_0) executeOperation("remove first state", modifyOperation, work, null, "11.06.2001", ex);
      result = (DataproviderObject_1_0) executeOperation("remove first state", replaceOperation, work, null, "11.06.2001", ex);
//      checkResult(result, "State 5", new String[] { "fifthVal" }, "10.06.2001", "31.10.2001");
      checkResult(result, "State 5", new String[] { "fifthVal" },null, null);

      // cut the last state
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add(objectId));
      work.clearValues(SystemAttributes.OBJECT_CLASS).
          add(0, "org:openmdx:test:compatibility:state1:inclusive:StateA");
//    setValidity(work, null, "31.01.2002"); // ------|5555553333333333|------
      setValidity(work, null, null); // ------|5555553333333333|------
//    result = (DataproviderObject_1_0) executeOperation("reduce lifetime at end", modifyOperation, work, null, "01.01.2002", ex);
      result = (DataproviderObject_1_0) executeOperation("reduce lifetime at end", replaceOperation, work, null, "01.01.2002", ex);
//      checkResult(result, "State 3", new String[] { "thirdVal" }, "01.11.2001", "31.01.2002");
      checkResult(result, "State 5", new String[] { "fifthVal" }, null, null); // TODO verify

      // remove the last state
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add(objectId));
      work.clearValues(SystemAttributes.OBJECT_CLASS).
          add(0, "org:openmdx:test:compatibility:state1:inclusive:StateA");
//    setValidity(work, null, "30.09.2001"); // ------|55555|----------------
      setValidity(work, null, null); // ------|55555|----------------
//    result = (DataproviderObject_1_0) executeOperation("remove last state", modifyOperation, work, null, "01.09.2001", ex);
      result = (DataproviderObject_1_0) executeOperation("remove last state", replaceOperation, work, null, "01.09.2001", ex);
//      checkResult(result, "State 5", new String[] { "fifthVal" }, "10.06.2001", "30.09.2001");
      checkResult(result, "State 5", new String[] { "fifthVal" }, null, null);

      // remove from the beginning and the end
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add(objectId));
      work.clearValues(SystemAttributes.OBJECT_CLASS).
          add(0, "org:openmdx:test:compatibility:state1:inclusive:StateA");
//    setValidity(work, "20.07.2001", "31.08.2001"); // -------|555|------------------
      setValidity(work, null, null); // -------|555|------------------
//    result = (DataproviderObject_1_0) executeOperation("cut from both ends", modifyOperation, work, null, "01.08.2001", ex);
      result = (DataproviderObject_1_0) executeOperation("cut from both ends", replaceOperation, work, null, "01.08.2001", ex);
//      checkResult(result, "State 5", new String[] { "fifthVal" }, "20.07.2001", "31.08.2001");
      checkResult(result, "State 5", new String[] { "fifthVal" }, null, null);

      // now remove the object
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add(objectId));
      work.values("selector").set(0,new Short(AttributeSelectors.ALL_ATTRIBUTES)); // helper construct for providing attributes   
      result = (DataproviderObject_1_0) executeOperation("remove object", removeOperation, work, null, "01.08.2001", ex);
//      checkResult(result, "State 5", new String[] { "fifthVal" }, "20.07.2001", "31.08.2001");
      checkResult(result, "State 5", new String[] { "fifthVal" }, null, null);
    }

  //-------------------------------------------------------------------------    
  /** 
   * test objects which are valid from ever to some date and which are valid
   * since ever to ever.
   */
  public void testEver(
  ) throws Exception {

      DataproviderObject work = null;
      DataproviderObject_1_0 result = null;
      ServiceException ex = null;


      // **** create IdEver1 (from ever for ever)
      work = prepareObject("StateA", null, new String[] {"stateA", "IdEver1" + findTestID()}, "Ever1 State 1", new String[] { "Ever1 firstVal" }, null );
      setValidity(work, null, null); // t1: <11111111111111111111>    
      ex = null;
      result = (DataproviderObject_1_0) executeOperation("create IdEver1", createOperation, work, null, null, ex);
      checkResult(result, "Ever1 State 1", new String[] { "Ever1 firstVal" }, null, null);

//      // **** create IdEver2 (from ever)
//      work = prepareObject("StateA", null, new String[] {"stateA", "IdEver2" + findTestID()}, "Ever2 State 1", new String[] { "Ever2 firstVal" }, null );
//      setValidity(work, null, "30.11.2001"); // t1: <1111111111111111|--------  
//      ex = null;
//      result = (DataproviderObject_1_0) executeOperation("create IdEver2", createOperation, work, null, null, ex);
//      checkResult(result, "Ever2 State 1", new String[] { "Ever2 firstVal" }, null, "30.11.2001");
//
//
//      // **** now some modifications
//      work = prepareObject("StateA", null, new String[] {"stateA", "IdEver1" + findTestID()}, "Ever 1 State 2", new String[] { "Ever 1 secondVal" }, null );
//      setValidity(work, "01.07.2001", "30.09.2001"); // t2: <11111122222222222211111111>         
//      ex = null;
//      result = (DataproviderObject_1_0) executeOperation("add state 2", replaceOperation, work, null, "01.08.2001", ex);
//      checkResult(result, "Ever 1 State 2", new String[] { "Ever 1 secondVal" }, "01.07.2001", "30.09.2001");
//
//
//      // **** put back those states again
//      work = prepareObject("StateA", null, new String[] {"stateA", "IdEver1" + findTestID()}, "Ever1 State 1", new String[] { "Ever1 firstVal" }, null );
//      setValidity(work, "15.06.2001", "14.10.2001"); // t3: <11111111111111111111111>         
//      ex = null;
//      result = (DataproviderObject_1_0) executeOperation("glue together", replaceOperation, work, null, "31.07.2001", ex);
//      checkResult(result, "Ever1 State 1", new String[] { "Ever1 firstVal" }, null, null);
//
//
//      // **** introduction at start
//      work = prepareObject("StateA", null, new String[] {"stateA", "IdEver1" + findTestID()}, "Ever1 State 3", new String[] { "-", "Ever1 thirdVal" }, null );
//      setValidity(work, null, "31.07.2001"); // t3: <333333311111111111111111>         
//      ex = null;
//      result = (DataproviderObject_1_0) executeOperation("change from start", modifyOperation, work, null, "01.07.2001", ex);
//      checkResult(result, "Ever1 State 3", new String[] { "Ever1 firstVal","Ever1 thirdVal" }, null, "31.07.2001");


      // now remove the states
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add("IdEver1" + findTestID()));
      result = (DataproviderObject_1_0) executeOperation("remove IdEver1", removeOperation, work, null, null, ex);

//      work = new DataproviderObject(new Path(rootSegment).add("stateA").add("IdEver2" + findTestID()));
//      result = (DataproviderObject_1_0) executeOperation("remove IdEver2", removeOperation, work, null, null, ex);
  }

  //-------------------------------------------------------------------------    
  public void testFind(boolean supportsHistory
  ) throws Exception {
      // introduce some objects to find
      DataproviderObject work = null;
      ServiceException ex = null;
      DataproviderObject_1_0 result = null;
      List results = null;

      // need distinctive values for the search against db persistence
      String firstVal = "firstVal" + findTestID();
      String secondVal = "secondVal" + findTestID();

      // **** create object "IdFind1"
      DataproviderObject_1_0 o1s1 = null;
      work = prepareObject("StateA", null, new String[] {"stateA", "IdFind1" + findTestID()}, "O1 State 1", new String[] { firstVal }, null );
//    setValidity(work, "01.02.2001", null);
      setValidity(work, null, null);
      ex = null;
      result = (DataproviderObject_1_0) executeOperation("create IdFind1", createOperation, work, null, "01.02.2001", ex);
//    checkResult(result, "O1 State 1", new String[] { firstVal }, "01.02.2001", null);
      checkResult(result, "O1 State 1", new String[] { firstVal }, null, null);
      o1s1 = result;

      // **** create object "IdFind2"
      DataproviderObject_1_0 o2s1 = null;
      work = prepareObject("StateA", null, new String[]{"stateA", "IdFind2" + findTestID()}, "O2 State 1", new String[] { firstVal }, null );
//    setValidity(work, "01.03.2001", null);
      setValidity(work, null, null);
      ex = null;
      result = (DataproviderObject_1_0) executeOperation("create IdFind2", createOperation, work, null, null, ex);
//    checkResult(result, "O2 State 1", new String[] { firstVal }, "01.03.2001", null);
      checkResult(result, "O2 State 1", new String[] { firstVal }, null, null);
      o2s1 = result;

      // **** create object "IdFind3"
      DataproviderObject_1_0 o3s1 = null;
      work = prepareObject("StateA", null, new String[] {"stateA", "IdFind3" + findTestID()}, "O3 State 1", new String[] { firstVal }, null );
//    setValidity(work, "01.05.2001", null);
      setValidity(work, null, null);
      ex = null;
      result = (DataproviderObject_1_0) executeOperation("create IdFind3", createOperation, work, null, null, ex);
//    checkResult(result, "O3 State 1", new String[] { firstVal }, "01.05.2001", null);
      checkResult(result, "O3 State 1", new String[] { firstVal }, null, null);
      o3s1 = result;


      Thread.sleep(100); // to ensure gap
      String t_allCreated = timeNow();
      Thread.sleep(100); // to ensure gap


      // **** update object "IdFind1"
//    DataproviderObject_1_0 o1s2 = null;
      work = prepareObject("StateA", null, new String[] {"stateA", "IdFind1" + findTestID()}, "O1 State 2", new String[] { secondVal } , null);
//    setValidity(work, "01.04.2001", "30.09.2001");
      setValidity(work, null, null);
      ex = null;
//    result = (DataproviderObject_1_0) executeOperation("second state for IdFind1", modifyOperation, work, null, "01.05.2001", ex);
      result = (DataproviderObject_1_0) executeOperation("second state for IdFind1", replaceOperation, work, null, "01.05.2001", ex);
//    checkResult(result, "O1 State 2", new String[] { secondVal }, "01.04.2001", "30.09.2001");
      checkResult(result, "O1 State 2", new String[] { secondVal }, null, null);
//    o1s2 = result;

      work = prepareObject("StateA", null, new String[] {"stateA", "IdFind1" + findTestID()}, "O1 State 1", new String[] { firstVal }, null );
//    setValidity(work, "01.01.2001", "31.01.2001");
      setValidity(work, null, null);
      ex = null;
//    result = (DataproviderObject_1_0) executeOperation("appending IdFind1 at start", modifyOperation, work, null, "01.01.2001", ex);
      result = (DataproviderObject_1_0) executeOperation("appending IdFind1 at start", replaceOperation, work, null, "01.01.2001", ex);
//    checkResult(result, "O1 State 1", new String[] { firstVal }, "01.01.2001", "31.03.2001");
      checkResult(result, "O1 State 1", new String[] { firstVal }, null, null);

      // **** update object "IdFind2"
//    DataproviderObject_1_0 o2s2 = null;
      work = prepareObject("StateA", null, new String[] {"stateA", "IdFind2" + findTestID()}, "O2 State 2", new String[] { secondVal }, null );
//    setValidity(work, "01.07.2001", "14.10.2001");
      setValidity(work, null, null);
      ex = null;
//    result = (DataproviderObject_1_0) executeOperation("second state for IdFind2", modifyOperation, work, null, "01.08.2001", ex);
      result = (DataproviderObject_1_0) executeOperation("second state for IdFind2", replaceOperation, work, null, "01.08.2001", ex);
//    checkResult(result, "O2 State 2", new String[] { secondVal }, "01.07.2001", "14.10.2001");
      checkResult(result, "O2 State 2", new String[] { secondVal }, null, null);
//    o2s2 = result;

      // **** update object "IdFind3"
      DataproviderObject_1_0 o3s2 = null;
      work = prepareObject("StateA", null, new String[] {"stateA", "IdFind3" + findTestID()}, "O3 State 2", new String[] { secondVal }, null );
//    setValidity(work, "01.08.2001", "31.01.2002");
      setValidity(work, null, null);
      ex = null;
//    result = (DataproviderObject_1_0) executeOperation("second state for IdFind2", modifyOperation, work, null, "01.09.2001", ex);
      result = (DataproviderObject_1_0) executeOperation("second state for IdFind2", replaceOperation, work, null, "01.09.2001", ex);
//    checkResult(result, "O3 State 2", new String[] { secondVal }, "01.08.2001", "31.01.2002");
      checkResult(result, "O3 State 2", new String[] { secondVal }, null, null);
      o3s2 = result;


      Thread.sleep(100); // to ensure gap
      String t_mostUpdated = timeNow();
      Thread.sleep(100); // to ensure gap


      // **** update object "IdFind2" after all the other updates took part
      work = prepareObject("StateA", null, new String[] {"stateA", "IdFind2" + findTestID()}, "O2 State 3", new String[] { "thirdVal" }, null );
//    setValidity(work, "15.10.2001", null);
      setValidity(work, null, null);
      ex = null;
//    result = (DataproviderObject_1_0) executeOperation("third state for IdFind2 (appending)", modifyOperation, work, null, "01.11.2001", ex);
      result = (DataproviderObject_1_0) executeOperation("third state for IdFind2 (appending)", replaceOperation, work, null, "01.11.2001", ex);
//    checkResult(result, "O2 State 3", new String[] { "thirdVal" }, "15.10.2001", null);
      checkResult(result, "O2 State 3", new String[] { "thirdVal" }, null, null);

      // **** now start finding in this mess
      // find at 1.9.01
      work = new DataproviderObject(new Path(rootSegment).add("stateA" ));
      work.values("value").add(secondVal);
      for(
        short operation = findOperation;
        operation <= findOperation; //TODO extentOperation;
        operation++
      ){
          ex = null;
          results = (List) executeOperation("find at 01.09.2001", operation, work, null, "01.09.2001", ex);
          checkResultList(
              results,
//            new DataproviderObject_1_0[] {o1s2, o2s2, o3s2 },
              new DataproviderObject_1_0[] {o3s2 }, // TODO verify
              null,
              false, supportsHistory
          );
      }

      // find at 01.07.2001 (onValidFrom)
      DataproviderObject o3s1a = new DataproviderObject(o3s1);
//    o3s1a.values(AbstractTestNoState_1.VALID_TO).set(0, prepareDate("31.07.2001"));
      o3s1a.values(AbstractTestNoState_1.VALID_TO).set(0, null);
      work = new DataproviderObject(new Path(rootSegment).add("stateA" ));
      work.values("value").add(firstVal);
      for(
          short operation = findOperation;
          operation <= extentOperation;
          operation++
      ){
          ex = null;
          results = (List) executeOperation("find at 01.07.2001 (on validFrom)", operation, work, null, "01.07.2001", ex);
          checkResultList(
              results,
//            new DataproviderObject_1_0[] {o3s1a},
              new DataproviderObject_1_0[] {o1s1},
              null,
              false, supportsHistory
          );
      }

      // find at 10.07.2001
      work = new DataproviderObject(new Path(rootSegment).add("stateA" ));
      work.values("value").add(firstVal);
      for(
          short operation = findOperation;
          operation <= extentOperation;
          operation++
      ){
          ex = null;
          results = (List) executeOperation("find at 10.07.2001", operation, work, null, "10.07.2001", ex);
          checkResultList(
              results,
//            new DataproviderObject_1_0[] {o3s1a},
              new DataproviderObject_1_0[] {o1s1}, // TODO verify
              null,
              false, supportsHistory
          );
      }

      // find at 1.11.2001
      work = new DataproviderObject(new Path(rootSegment).add("stateA" ));
      work.values("value").add(secondVal);
      for(
          short operation = findOperation;
          operation <= extentOperation;
          operation++
      ){
          ex = null;
          results = (List) executeOperation("find at 1.11.2001", operation, work, null, "01.11.2001", ex);
          checkResultList(
              results,
              new DataproviderObject_1_0[] {o3s2},
              null,
              false, supportsHistory
          );
      }

      // find at 1.1.2000 (before any values)
      work = new DataproviderObject(new Path(rootSegment).add( "stateA" ));
      work.values("value").add(secondVal);
      for(
          short operation = findOperation;
          operation <= extentOperation;
          operation++
      ){
          ex = null;
          results = (List) executeOperation("find at 1.1.2000", operation, work, null, "01.01.2000", ex);
          checkResultList(
              results,
//            new DataproviderObject_1_0[] {},
              new DataproviderObject_1_0[] {o3s2}, // TODO verify
              null,
              false, supportsHistory
          );
      }

      // find 1.3.2002 
      // use some temporary objects to correct the dates of the objects:
      DataproviderObject o1s1e = new DataproviderObject(o1s1);
      DataproviderObject o3s1e = new DataproviderObject(o3s1);

      o1s1e.values(AbstractTestNoState_1.VALID_FROM).set(0, prepareDate("01.10.2001"));
      o3s1e.values(AbstractTestNoState_1.VALID_FROM).set(0, prepareDate("01.02.2002"));

      work = new DataproviderObject(new Path(rootSegment).add("stateA" ));
      work.values("value").add(firstVal);
      for(
          short operation = findOperation;
          operation <= extentOperation;
          operation++
      ){
          ex = null;
          results = (List) executeOperation("find at 01.03.2002", operation, work, null, "01.03.2002", ex);
          checkResultList(
              results,
//            new DataproviderObject_1_0[] {o1s1e, o3s1e},
              new DataproviderObject_1_0[] {o1s1}, // TODO verify
              null,
              false, supportsHistory
          );
      }
      {
          // use some temporary objects to correct the dates of the objects:
          DataproviderObject o1s1f = new DataproviderObject(o1s1);
          DataproviderObject o2s1f = new DataproviderObject(o2s1);
          o1s1f.values(AbstractTestNoState_1.VALID_FROM).set(0, prepareDate("01.01.2001"));
          o1s1f.values(AbstractTestNoState_1.VALID_TO).set(0, prepareDate("31.03.2001"));
          o2s1f.values(AbstractTestNoState_1.VALID_TO).set(0, prepareDate("30.06.2001"));

          // find 1.3.2001 the way it can be seen now
          work = new DataproviderObject(new Path(rootSegment).add("stateA" ));
          work.values("value").add(firstVal);
          DataproviderObject_1_0[] nowFor20010301 = // new DataproviderObject_1_0[] {o1s1f, o2s1f};
              new DataproviderObject_1_0[] {o1s1}; // TODO verify
          for(
              short operation = findOperation;
              operation <= extentOperation;
              operation++
          ){
              ex = null;
              ex = null;
              results = (List) executeOperation("find at 01.03.2001", operation, work, null, "01.03.2001", ex);
              checkResultList(
                  results,
                  nowFor20010301,
                  null,
                  false, supportsHistory
              );
          }
      }
      {
          DataproviderObject o1s1f = new DataproviderObject(o1s1);
          DataproviderObject o2s1f = new DataproviderObject(o2s1);
          o1s1f.values(AbstractTestNoState_1.INVALIDATED_AT).set(0, INVALIDATION_INDICATOR); // just to indicate it must be invalidated
          o2s1f.values(AbstractTestNoState_1.INVALIDATED_AT).set(0, INVALIDATION_INDICATOR);

          // find 1.3.2001 the way it was after creation of the objects
          work = new DataproviderObject(new Path(rootSegment).add("stateA" ));
          work.values("value").add(firstVal);
          for(
              short operation = findOperation;
              operation <= extentOperation;
              operation++
          ){
              ex = null;
              results = (List) executeOperation("find at 01.03.2001 like it was after creation", operation, work, t_allCreated, "01.03.2001", ex);
              checkResultList(
                  results,
//                supportsHistory ? new DataproviderObject_1_0[] {o1s1f, o2s1f} : nowFor20010301,
                  new DataproviderObject_1_0[] {o1s1}, // TODO verify
                  null,
                  false, supportsHistory
              );
          }

      }
      {
          // use some temporary objects to correct the dates of the objects:
          DataproviderObject o1s1f = new DataproviderObject(o1s1);
          o1s1f.values(AbstractTestNoState_1.VALID_FROM).set(0, prepareDate("01.10.2001"));

          // find 01.12.2001 the way it is seen now
          work = new DataproviderObject(new Path(rootSegment).add("stateA" ));
          work.values("value").add(firstVal);
          DataproviderObject_1_0[] nowFor20011201 = // new DataproviderObject_1_0[] {o1s1f};
              new DataproviderObject_1_0[] {o1s1}; // TODO verify
          for(
              short operation = findOperation;
              operation <= extentOperation;
              operation++
          ){
              ex = null;
              results = (List) executeOperation("find at 01.12.2001", operation, work, null, "01.12.2001", ex);
              checkResultList(
                  results,
                  nowFor20011201,
                  null,
                  false, supportsHistory
              );
          }
      }
      {
          // find 01.12.2001 the way it was after that most of the objects where updated
          DataproviderObject o1s1f = new DataproviderObject(o1s1);
          DataproviderObject o2s1f = new DataproviderObject(o2s1);

          o1s1f.values(AbstractTestNoState_1.VALID_FROM).set(0, prepareDate("01.10.2001"));
          o2s1f.values(AbstractTestNoState_1.VALID_FROM).set(0, prepareDate("15.10.2001"));
          o2s1f.values(AbstractTestNoState_1.INVALIDATED_AT).set(0, "1");

          work = new DataproviderObject(new Path(rootSegment).add( "stateA" ));
          work.values("value").add(firstVal);
          for(
              short operation = findOperation;
              operation <= extentOperation;
              operation++
          ){
              ex = null;
              results = (List) executeOperation("find at 01.12.2001 like it was after some updates", operation, work, t_mostUpdated, "01.12.2001", ex);
              checkResultList(
                  results,
//                supportsHistory ? new DataproviderObject_1_0[] {o1s1f, o2s1f} : nowFor20011201,
                  new DataproviderObject_1_0[] {o1s1}, // TODO verify
                  null,
                  false, supportsHistory
              );
          }
      }

      {
          // just to proof it works, do the same query without requestedAt
          DataproviderObject o1s1g = new DataproviderObject(o1s1);

          o1s1g.values(AbstractTestNoState_1.VALID_FROM).set(0, prepareDate("01.10.2001"));

          work = new DataproviderObject(new Path(rootSegment).add("stateA" ));
          work.values("value").add(firstVal);
          for(
              short operation = findOperation;
              operation <= extentOperation;
              operation++
          ){
              ex = null;
              results = (List) executeOperation("find at 01.12.2001 like it is now", operation, work, null, "01.12.2001", ex);
              checkResultList(
                  results,
//                new DataproviderObject_1_0[] {o1s1g},
                  new DataproviderObject_1_0[] {o1s1}, // TODO verify
                  null,
                  false, supportsHistory
              );
          }
      }

      // now remove the objects
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add("IdFind1" + findTestID()));
      work.values("selector").set(0,new Short(AttributeSelectors.ALL_ATTRIBUTES)); // helper construct for providing attributes   
      result = (DataproviderObject_1_0) executeOperation("remove object IdFind1 (date at validFrom)", removeOperation, work, null, "01.01.2001", ex);
//    checkResult(result, "O1 State 1", new String[] { firstVal }, "01.01.2001", "31.03.2001");
      checkResult(result, "O1 State 1", new String[] { firstVal }, null, null);
      // now remove the objects
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add("IdFind2" + findTestID()));
      work.values("selector").set(0,new Short(AttributeSelectors.ALL_ATTRIBUTES)); // helper construct for providing attributes   
      result = (DataproviderObject_1_0) executeOperation("remove object IdFind2 (date at validFrom)", removeOperation, work, null, "01.07.2001", ex);
//    checkResult(result, "O2 State 2", new String[] { secondVal }, "01.07.2001", "14.10.2001");
      checkResult(result, "O2 State 3", new String[] { "thirdVal" }, null, null);
      // now remove the objects
      work = new DataproviderObject(new Path(rootSegment).add("stateA").add("IdFind3" + findTestID()));
      work.values("selector").set(0,new Short(AttributeSelectors.ALL_ATTRIBUTES)); // helper construct for providing attributes   
      result = (DataproviderObject_1_0) executeOperation("remove object IdFind3 (date at validFrom)", removeOperation, work, null, "01.02.2002", ex);
//    checkResult(result, "O3 State 1", new String[] { firstVal }, "01.02.2002", null);
      checkResult(result, "O3 State 2", new String[] { secondVal }, null, null);
  }

  //-------------------------------------------------------------------------    
  public void testExceptions(
  ) {
      DataproviderObject work = null;
      ServiceException ex = null;
//    DataproviderObject_1_0 result = null;

      // **** create object "IdEx1"
      work = prepareObject("StateA", null, new String[]{"stateA", "IdEx1" + findTestID()}, "O1 State 1", new String[] { "firstVal" }, null );
      this.setValidity(work, "01.02.2001", "31.10.2001");
      ex = new ServiceException(StackedException.DEFAULT_DOMAIN, StackedException.NOT_SUPPORTED, null, "This provider treats stated object like non-stated ones. That's why the mentionned feature should be null.");
//    result = (DataproviderObject_1_0) 
          executeOperation("create IdEx1", createOperation, work, null, "01.03.2001", ex);
//      checkResult(result, "O1 State 1", new String[] { "firstVal" }, "01.02.2001", "31.10.2001");
//      // **** create object "IdFind1"
//      work = prepareObject("StateA", null, new String[]{"stateA", "IdEx2" + findTestID()}, "O2 State 1", new String[] { "firstVal" }, null );
//      this.setValidity(work, "01.03.2001", null);
//      ex = null;
//      result = (DataproviderObject_1_0) executeOperation("create IdEx2", createOperation, work, null, "01.03.2001", ex);
//      checkResult(result, "O2 State 1", new String[] { "firstVal" }, "01.03.2001", null);
//
//      // test for reduction of lifetime with to large validFrom, validTo
//      // ie. a enlargement
//      {
//          // at start
//        work = new DataproviderObject(new Path(rootSegment).add("stateA").add("IdEx2" + findTestID()));
//        work.clearValues(SystemAttributes.OBJECT_CLASS).add(0, "org:openmdx:test:compatibility:state1:inclusive:StateA");
//        this.setValidity(work, "01.01.2001", "31.10.2001");
//        ex = new ServiceException(StackedException.DEFAULT_DOMAIN, StackedException.ASSERTION_FAILURE, null, "Trying to increase the lifetime at boundary.");
//        result = (DataproviderObject_1_0) executeOperation("extension at start", modifyOperation, work, null, "01.02.2001", ex);
//
//        // at end
//        work = new DataproviderObject(new Path(rootSegment).add("stateA").add("IdEx1" + findTestID()));
//        work.clearValues(SystemAttributes.OBJECT_CLASS).add(0, "org:openmdx:test:compatibility:state1:inclusive:StateA");
//        setValidity(work, "01.02.2001", "30.11.2002");
//        ex = new ServiceException(StackedException.DEFAULT_DOMAIN, StackedException.ASSERTION_FAILURE, null, "Trying to increase the lifetime at boundary.");
//        result = (DataproviderObject_1_0) executeOperation("extension at end", modifyOperation, work, null, "01.03.2001", ex);
//      }
//
//      // test with wrong sequence of validFrom, validTo 
//      // (in create, modify, update
//      {
//          // create
//        work = prepareObject("StateA", null, new String[]{"stateA", "IdEx3" + findTestID()}, "O3 State 1", new String[] { "firstVal" }, null );
//        setValidity(work, "01.08.2001", "31.12.2000");
//        ex = new ServiceException(StackedException.DEFAULT_DOMAIN, StackedException.ASSERTION_FAILURE, null, "validFrom after validTo.");
//        result = (DataproviderObject_1_0) executeOperation("create IdEx3", createOperation, work, null, "01.03.2001", ex);
//      }
//
//      /* holes are allowed, so no exception will be thrown here.
//       * except if the state plugin is configured that way it prohibits holes,
//       * but then another test sequence has to be 
//       *
//      // test creation of a hole
//      {
//          // at start
//        work = prepareObject("StateA", null, new String[]{"stateA", "IdEx2" + findTestID()}, "O2 State 2", new String[] { "secondVal" }, null );
//        setValidity(work, "01.01.2001", "31.01.2001");      
//        ex = new ServiceException(StackedException.DEFAULT_DOMAIN, StackedException.NOT_FOUND, null, "could not find valid states for the object in the period validFrom, validTo.");
//        // TODO: AssertionFailedError: test: hole at start expected exception class org.openmdx.base.exception.BasicException            
//        result = (DataproviderObject_1_0) executeOperation("hole at start", modifyOperation, work, null, "01.01.2001", ex); 
//
//        // at start
//        work = prepareObject("StateA", null, new String[]{"stateA", "IdEx1" + findTestID()}, "O1 State 2", new String[] { "secondVal" }, null );
//        setValidity(work, "01.12.2001", "31.01.2002");                  
//        ex = new ServiceException(StackedException.DEFAULT_DOMAIN, StackedException.NOT_FOUND, null, "could not find valid states for the object in the period validFrom, validTo.");
//        // TODO: AssertionFailedError: test: hole at end expected exception class org.openmdx.base.exception.BasicException            
//        result = (DataproviderObject_1_0) executeOperation("hole at end", modifyOperation, work, null, "01.01.2002", ex); 
//      }
//      */
//
//      // get at end of object --> not found exception
//      work = new DataproviderObject(new Path(rootSegment +"/stateA/IdEx1"+ findTestID()));
//
//      ex = new ServiceException(StackedException.DEFAULT_DOMAIN, StackedException.NOT_FOUND, null, "Object not found. Perhaps no valid state at requested_at, requested_for.");
//      result = (DataproviderObject_1_0) executeOperation("get at end of IdEx1", getOperation, work, null, "01.11.2001", ex);
//
//      // remove on the validTo should lead to an exception
//
//      // recreate object before removing it
//      work = prepareObject("StateA", null, new String[]{"stateA", "IdEx1" + findTestID()}, "O1 State 2", new String[] { "secondVal" }, null );
//      setValidity(work, "02.02.2001", "10.11.2001");
//      ex = new ServiceException(StackedException.DEFAULT_DOMAIN, StackedException.DUPLICATE, null, "Trying to create object which exists and is still valid.");
//      result = (DataproviderObject_1_0) executeOperation("create IdEx1 again without deletion", createOperation, work, null, "01.03.2001", ex);
//
//      // now remove the first object
//      work = new DataproviderObject(new Path(rootSegment).add("stateA").add("IdEx1" + findTestID()));
//      work.values("selector").set(0,new Short(AttributeSelectors.ALL_ATTRIBUTES)); // helper construct for providing attributes   
//      ex = null;
//      result = (DataproviderObject_1_0) executeOperation("remove object IdEx1", removeOperation, work, null, "01.08.2001", ex);
//      checkResult(result, "O1 State 1", new String[] { "firstVal" }, "01.02.2001", "31.10.2001");
//
//      try {
//          Thread.sleep(100);
//      }
//      catch(Exception e) {}
//
//      // now try creating with same id which should work now!
//      work = prepareObject("StateA", null, new String[]{"stateA", "IdEx1" + findTestID()}, "O1 State 2", new String[] { "secondVal" }, null );
//      setValidity(work, "02.02.2001", "10.11.2001");
//      ex = null; // new ServiceException(StackedException.DOMAIN, StackedException.DUPLICATE, null, null);            
//      result = (DataproviderObject_1_0) executeOperation("create IdEx1 again", createOperation, work, null, "01.03.2001", ex);
//
//      // now remove the first object
//      work = new DataproviderObject(new Path(rootSegment).add("stateA").add("IdEx2" + findTestID()));
//      work.values("selector").set(0,new Short(AttributeSelectors.ALL_ATTRIBUTES)); // helper construct for providing attributes   
//      ex = null;
//      result = (DataproviderObject_1_0) executeOperation("remove object IdEx2", removeOperation, work, null, "01.08.2001", ex);
//      checkResult(result, "O2 State 1", new String[] { "firstVal" }, "01.03.2001", null);
//
//        // need to remove IdEx1 again
//      work = new DataproviderObject(new Path(rootSegment).add("stateA").add("IdEx1" + findTestID()));
//      work.values("selector").set(0,new Short(AttributeSelectors.ALL_ATTRIBUTES)); // helper construct for providing attributes   
//      ex = null;
//      result = (DataproviderObject_1_0) executeOperation("remove object IdEx1", removeOperation, work, null, "01.08.2001", ex);
    }

    /**
     * Test Iteration on nonstated object, but with the state plugin
     * in the configuration.
     *  
     * @throws Exception
     */
    public void testIterationNonStated(
    ) throws Exception {
                String baseId = "ITER";
                String noStateId = baseId + "_sns_";
                int stateNum = 250;

                InclusivePackage pkg = createInclusivePackage(new ServiceHeader());
        Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();

                unitOfWork.begin();
                Segment segment = pkg.getSegment().getSegment(
                    new Path(rootSegment)
                );
                unitOfWork.commit();
                // must remove the object first 
                for (int i = 0; i < stateNum; i++) {
                        try {
                                // keep the sequence (because this is the sequence they were created
                                unitOfWork.begin();
//				segment.removeStateNoState(noStateId + i);
                                StateNoState stateNoState = segment.getStateNoState(noStateId + i);
                                if(stateNoState != null) stateNoState.refDelete();
//
                                unitOfWork.commit();
                        }
                        catch (JmiServiceException je) {
                                if (unitOfWork.isActive()) {
                                        unitOfWork.rollback();
                                }
//				if (!isNotFoundException(je.getExceptionStack())) {
//					throw je;
//				}
                } catch(JDOException je) {
                if (unitOfWork.isActive()) {
                    unitOfWork.rollback();
                }
                        }
            }
                // now create them
                StateNoStateClass noStateClass = pkg.getStateNoState();
                String stateAttrVal = "noState " + baseId;
                for (int i = 0; i < stateNum; i++) {
                        StateNoState noState =
                                noStateClass.createStateNoState(stateAttrVal);

                        unitOfWork.begin(); // needed for adding correct reference
                        segment.addStateNoState(noStateId + i, noState);
                        unitOfWork.commit();
                }

        // now try to get them through a new connection
              Dataprovider_1_0Connection remoteConnection =
                  Dataprovider_1ConnectionFactoryImpl.createGenericConnection(
                      SimpleServiceLocator.getInitialContext().lookup("org/openmdx/test/managing/explorer")
              );

              // intercept webservice transport for testing
              connection =
                remoteConnection;
//	      new Dataprovider_1_0ConnectionImpl("http://localhost:8080/dataproviders/junits");
//	      new Dataprovider_1_0ConnectionImpl(remoteConnection);      

              // initial paths
              AbstractTestNoState_1.rootSegment = getRootSegment();

                pkg = createInclusivePackage(new ServiceHeader());

                unitOfWork.begin();
                segment = pkg.getSegment().getSegment(
                                new Path(rootSegment)
                );
                unitOfWork.commit();

        // now try to get them 
//              Container container = (Container) segment.getStateNoState();
                StateNoStateQuery filter = pkg.createStateNoStateQuery();
                filter.stateAttr().equalTo(stateAttrVal);
                List findings = segment.getStateNoState(filter);
                int c = 0;
                for (
                        Iterator s = findings.iterator(); // container.subSet(filter).iterator(); 
                        s.hasNext();
                ) {
                        StateNoState sns = (StateNoState)s.next();
                        c++;
                        assertEquals("StateNoState.stateAttr()", stateAttrVal, sns.getStateAttr());
                }

                assertEquals("wrong number of stateNoState objects", stateNum, c);
    }


    /**
     * Test adding a reference to a stated object.
     * @throws Exception
     */
    public void testReferenceToStatedObject(
    ) throws Exception {
        try {

        String baseId = "REF";
        String stateAId1 = baseId + "_sa";
        String noStateId = baseId + "_sns";
//        String stateARefName = "refStateA";

        InclusivePackage pkg = createInclusivePackage(new ServiceHeader());
        Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();

        unitOfWork.begin();
        Segment segment = pkg.getSegment().getSegment(
                new Path(rootSegment)
        );
        unitOfWork.commit();

        // must remove the object first 
        try {
            // keep the sequence (because this is the sequence they were created
            unitOfWork.begin();
//          segment.removeStateA(stateAId1);
            StateA stateA = segment.getStateA(stateAId1);
            if(stateA != null) stateA.refDelete();
//
            unitOfWork.commit();
        }
        catch (JmiServiceException je) {
            if (unitOfWork.isActive()) {
                unitOfWork.rollback();
            }
//          if (!isNotFoundException(je.getExceptionStack())) {
//              throw je;
//          }
             } catch(JDOException je) {
                if (unitOfWork.isActive()) {
                    unitOfWork.rollback();
                }
        }
        try{
            unitOfWork.begin();
//          segment.removeStateNoState(noStateId);
            StateNoState stateNoState = segment.getStateNoState(noStateId);
            if(stateNoState != null) stateNoState.refDelete();
//
            unitOfWork.commit();
        }
        catch (JmiServiceException je) {
            if (unitOfWork.isActive()) {
                unitOfWork.rollback();
            }
//          if (!isNotFoundException(je.getExceptionStack())) {
//              throw je;
//          }
            } catch(JDOException je) {
                if (unitOfWork.isActive()) {
                    unitOfWork.rollback();
                }
        }


        StateA stateA = pkg.getStateA().createStateA("StateA " + baseId);

        unitOfWork.begin();

        // need date for beeing accepted as stated client
//      stateA.setStateValidFrom(prepareDateAsDate("01.01.1900"));
        stateA.setStateValidFrom(null);

        segment.addStateA(stateAId1, stateA);

        unitOfWork.commit(); // needed for adding correct reference

        StateNoState noState =
            pkg.getStateNoState()
                .createStateNoState("noState " + baseId);

        noState.setStateA(stateA);

        unitOfWork.begin(); // needed for adding correct reference

        segment.addStateNoState(noStateId, noState);

        unitOfWork.commit();

        //
        // CR0003587 
        //
        StateA stateA1 = segment.getStateA(
            stateAId1 + ";state=last"
        );
        assertEquals(
            "path not equal identity",
            refGetPath(stateA1).toXri(),
            stateA1.getIdentity() + ";state=last"
        );
        StateNoStateQuery stateNoStateQuery = pkg.createStateNoStateQuery();
        stateNoStateQuery.thereExistsStateA().equalTo(stateA1);
        List stateNoStates = segment.getStateNoState(stateNoStateQuery);
        assertEquals(
            "stateNoStates",
            Arrays.asList(new StateNoState[]{noState}),
            stateNoStates
        );
        stateNoStateQuery = pkg.createStateNoStateQuery();
//... TBD to be resolved!!!        
//        stateNoStateFilter.thereExistsStateA(
//            FilterOperators.IS_IN, 
//            Collections.singleton(new Path(stateA.getIdentity()))
//        );
//        stateNoStates = segment.getStateNoState(stateNoStateFilter);
//        assertEquals(
//            "stateNoStates", 
//            Arrays.asList(new StateNoState[]{noState}), 
//            stateNoStates
//        );


        // now try to get it:
        noState = segment.getStateNoState(noStateId);
        StateA stateARef = noState.getStateA();
        assertSame(
            "Created and referenced stateA",
            stateA,
            stateARef
        );

        StateA stateARetrieved = segment.getStateA(stateAId1);
        assertSame(
            "Created and retrievd stateA",
            stateARetrieved,
            stateA
        );

        // test deletion of multivalued attribute
        unitOfWork.begin();
        // need date for beeing accepted as stated client
//      stateA.setStateValidFrom(prepareDateAsDate("01.01.1900"));
        stateA.setStateValidFrom(null);
        stateA.setValue(new String[]{"test", "nulling", "multivalued"});
        unitOfWork.commit();

        assertEquals(
            "expected values to be set in value",
            "test", stateA.getValue(0)
        );

        unitOfWork.begin();
        // need date for beeing accepted as stated client
//      stateA.setStateValidFrom(prepareDateAsDate("01.01.1900"));
        stateA.setStateValidFrom(null);
        stateA.getValue().clear();
        unitOfWork.commit();

        assertTrue(
            "expected stateA to be empty/null",
            stateA.getValue() == null || stateA.getValue().size() == 0
        );


        }
        catch (Exception se) {
            showException(se);
            throw se;
        }
    }

    /**
     * CR0003589
     * 
     * @throws Exception 
     */
    public void testNonStatedObjectContainedInStated(
    ) throws Exception{
        String baseId = "REF";
        String stateAId1 = baseId + "_sa";
        try {
            InclusivePackage pkg = createInclusivePackage(new ServiceHeader());
            Segment segment = pkg.getSegment().getSegment (new Path(rootSegment));
            Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
            StateA stateA = segment.getStateA(stateAId1);
            StateA stateA1 = segment.getStateA(stateAId1 + ";state=last");
            unitOfWork.begin();
            NonStatedInStatedContainer nonStated = stateA.getNonStated("CR0003589");
            if(nonStated != null) nonStated.refDelete();
            unitOfWork.commit();
            //
            unitOfWork.begin();
            nonStated = pkg.getNonStatedInStatedContainer().createNonStatedInStatedContainer(
                stateAId1 + " CR0003589"
            );
            stateA1.addNonStated("CR0003589", nonStated);
            unitOfWork.commit();
            assertEquals(
                "stateNoStates",
                refGetPath(stateA).getDescendant(
                    new String[]{"nonStated", "CR0003589"}
                ),
                refGetPath(nonStated)
            );
        } catch (Exception se) {
            showException(se);
            throw se;
        }
    }

    /**
     * CR20006690
     * 
     * @throws Exception
     */
    public File testExport(
    ) throws Exception{
        ServiceHeader header = new ServiceHeader();
        File xmlFile = File.createTempFile(
            "CR20006690-",
            ".xml"
        );
        XmlExporter xmlExporter = new XmlExporter(
            header,
            AbstractTestNoState_1.connection,
            new PrintStream(
                new FileOutputStream(xmlFile),
                true,
                ENCODING
            ),
            AbstractTestNoState_1._model,
            ENCODING
        );
        xmlExporter.export(
            Collections.singletonList(AbstractTestNoState_1.rootSegment),
            "xri://+resource/org/openmdx/test/compatibility/state1/inclusive/xmi/inclusive.xsd"
        );
        System.out.println("Exported to " + xmlFile);
        return xmlFile;
    }

    /**
     * CR20006690
     * 
     * @param xmlFile the data to be loaded
     * 
     * @throws Exception
     */
    public void testImport(
        File xmlFile
    ) throws Exception{
        Date modifiedSince = new Date(
            System.currentTimeMillis() - 24 * 60 * 60 * 1000 // last day
        );
        ServiceHeader header = new ServiceHeader();
        XmlImporter xmlImporter = new XmlImporter(
            header,
            AbstractTestNoState_1.connection,
            modifiedSince,
            false, // transactional TODO 
            true // lenient
        );
        xmlImporter.process(
            new InputStream[]{
                new FileInputStream(xmlFile)
            }
        );
    }

    /**
     * CR0004063
     * 
     * @throws Exception 
     */
    public void testReferenceBetweenStatedObjects() throws Exception{
        String request = "CR0004063";
        InclusivePackage pkg = createInclusivePackage(new ServiceHeader());
        Transaction unitOfWork = ((RefPackage_1_1)pkg).refPersistenceManager().currentTransaction();
        Segment segment = pkg.getSegment().getSegment(new Path(rootSegment));
        try {
            unitOfWork.begin();
            StateA stateA = segment.getStateA(request);
            if(stateA != null) stateA.refDelete();
            unitOfWork.commit();
        } catch (JmiServiceException je) {
            je.printStackTrace();
        } catch(JDOException je) {
            je.printStackTrace();
        } finally {
            if (unitOfWork.isActive()) {
                unitOfWork.rollback();
            }
        }
        try {
            unitOfWork.begin();
            StateAC stateA = pkg.getStateAC().createStateAC(request);
            segment.addStateA(request, stateA);
            for(
               int i = 0;
               i < 3;
               i++
            ) {
                String cId = request + "." + i;
                StateC stateC = segment.getStateC(cId);
                if(stateC == null) {
                    stateC = pkg.getStateC().createStateC(cId);
                    segment.addStateC(cId, stateC);
                }
                stateA.addStateC(stateC);
            }
            unitOfWork.commit();
        } catch (JmiServiceException je) {
            je.printStackTrace();
        } catch(JDOException je) {
            je.printStackTrace();
        } finally {
            if (unitOfWork.isActive()) {
                unitOfWork.rollback();
            }
        }

    }

    static Path refGetPath(
        RefObject object
    ){
        return (Path) JDOHelper.getObjectId(object);
    }

    //---------------------------------------------------------------------------
    static private boolean deployed = false;

    static private Dataprovider_1_0 connection = null;
    static private ObjectFactory_1_0 manager = null;
    static private Model_1_0 _model = null;

    static final private short createOperation = 0;
    static final private short getOperation = 1;
    /**
     * findOperation must be the predecessor of extentOperation
     */
    static final private short findOperation = 2;
    /**
     * extentOperation must be the successor of findOperation
     */
    static final private short extentOperation = 3;
    static final private short modifyOperation = 4;
    static final private short replaceOperation = 5;
    static final private short removeOperation = 6;
    static final private short setOperation = 7;

    static final private String HISTORY_REQUEST_START = "historyRequestStart";
    static final private String HISTORY_REQUEST_END = "historyRequestEnd";
    static final private String VALID_REQUEST_START = "validRequestStart";
    static final private String VALID_REQUEST_END = "validRequestEnd";

    static private Path rootSegment = null;

    static String ID;

    static boolean IS_TRANSACTIONAL = false;
    static int FIND_BATCH_SIZE = 1000;
    static int EXTENT_BATCH_SIZE = 2;

    static final String ENCODING = "UTF-8";

    static private final String INVALIDATED_AT = State_1_Attributes.INVALIDATED_AT;
    static private final String VALID_FROM = State_1_Attributes.STATE_VALID_FROM;
    static private final String VALID_TO = State_1_Attributes.STATE_VALID_TO;
    static private final String REF_VALID = State_1_Attributes.REF_VALID;
    static private final String REF_STATE = State_1_Attributes.REF_STATE;
    static private final String REF_HISTORY = State_1_Attributes.REF_HISTORY;

    static private final Collection SHARED_REFS = Arrays.asList(
        new String[]{REF_VALID, REF_STATE, REF_HISTORY}
    );

    static String[] _args = new String[0];
    static private boolean SHOW_COMPARE = true;

    static private final String INVALIDATION_INDICATOR = DateFormat.getInstance().format(new Date(-1L));

}

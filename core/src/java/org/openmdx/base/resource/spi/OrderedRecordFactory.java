/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: OrderedRecordFactory.java,v 1.8 2008/09/04 06:49:13 hburger Exp $
 * Description: Java Connector Architecture: Extended Record Factory
 * Revision:    $Revision: 1.8 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/04 06:49:13 $
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
package org.openmdx.base.resource.spi;

import java.util.List;
import java.util.Map;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;

import org.openmdx.base.resource.cci.ExtendedRecordFactory;

/**
 * The OrderedRecordFactory is able to create variable-size and fixed-size
 * records.
 */
public class OrderedRecordFactory 
  implements ExtendedRecordFactory
{ 

  /**
   * No external instantiation allowed
   */
  private OrderedRecordFactory(
  ){
      super();
  }

  public static ExtendedRecordFactory getInstance(
  ){
      return OrderedRecordFactory.instance;
  }

  //--------------------------------------------------------------------------
  // Implements RecordFactory  
  //--------------------------------------------------------------------------

  /**
   * Creates a MappedRecord with the specified name and the given content.
   * <p>
   * The elements in this map are ordered according to their insertion.
   *
   * @param     recordName
   *            The name of the record acts as a pointer to the meta
   *            information (stored in the metadata repository) for a specific
   *            record type. 
   *
   * @exception ResourceException
   *            Failed to create an initialized MappedRecord.
   *            Example error cases are:<ul>
   *            <li>Invalid specification of record name</li>
   *            <li>Resource adapter internal error</li>
   *            <li>Failed to access metadata repository</li>
   *            </ul>
   * @exception NotSupportedException
   *            Operation not supported
   */
  public MappedRecord createMappedRecord(
    String recordName
  ) throws ResourceException {
    return new VariableSizeMappedRecord(recordName);
  }
  	
  /**
   * Creates an IndexedRecord with the specified name and the given content.  
   *
   * @param     recordName
   *            The name of the record acts as a pointer to the meta 
   *            information (stored in the metadata repository) for a specific
   *            record type. 
   *
   * @exception ResourceException
   *            Failed to create an initialized IndexedRecord.
   *            Example error cases are:<ul>
   *            <li>Invalid specification of record name</li>
   *            <li>Resource adapter internal error</li>
   *            <li>Failed to access metadata repository</li>
   *            </ul>
   * @exception NotSupportedException
   *            Operation not supported
   */
  public IndexedRecord createIndexedRecord(
    String recordName
  ) throws ResourceException {
    return new VariableSizeIndexedRecord(recordName);
  }


  //--------------------------------------------------------------------------
  // Implements ExtendedRecordFactory  
  //--------------------------------------------------------------------------

  /**
   * Creates a MappedRecord with the given name and short description.  
   *
   * @param     recordName
   *            The name of the record acts as a pointer to the meta 
   *            information (stored in the metadata repository) for a specific
   *            record type. 
   * @param     recordShortDescription
   *            The short description of the Record; or null.
   *
   * @exception ResourceException
   *            Failed to create an initialized MappedRecord.
   *            Example error cases are:<ul>
   *            <li>Invalid specification of record name</li>
   *            <li>Resource adapter internal error</li>
   *            <li>Failed to access metadata repository</li>
   *            </ul>
   * @exception NotSupportedException
   *            Operation not supported
   */
  public MappedRecord createMappedRecord(
    String recordName,
    String recordShortDescription
  ) throws ResourceException {
    return new VariableSizeMappedRecord(recordName,recordShortDescription);
	}  	
  	
  /**
   * Creates an initialized MappedRecord with the given name, short
   * description and content.  
   * <p>
   * The elements in this map are ordered according to their insertion 
   * retaining the order of the <code>content</code> argument.
   *
   * @param     recordName
   *            The name of the record acts as a pointer to the meta 
   *            information (stored in the metadata repository) for a specific
   *            record type. 
   * @param     recordShortDescription
   *            The short description of the Record; or null.
   * @param     content
   *            The content to be copied into an initialized record instance.
   *
   * @exception ResourceException
   *            Failed to create an initialized MappedRecord.
   *            Example error cases are:<ul>
   *            <li>Invalid specification of record name</li>
   *            <li>Resource adapter internal error</li>
   *            <li>Failed to access metadata repository</li>
   *            </ul>
   * @exception NotSupportedException
   *            Operation not supported
   * @exception	NullPointerException
   *						if <code>content</code> is <code>null</code>.
   */
  public MappedRecord createMappedRecord(
    String recordName,
    String recordShortDescription,
    Map<?,?> content
  ) throws ResourceException {
    return new VariableSizeMappedRecord(
      recordName,
      recordShortDescription,
      content
    );
  }
  	
  /**
   * Creates an initialized MappedRecord with the specified name, short description
   * and content.  
   * <p>
   * The elements in this map are ordered according to their insertion 
   * retaining the order of the <code>keys</code> and  <code>values</code>
   * arguments.
   *
   * @param     recordName
   *            The name of the record acts as a pointer to the meta
   *            information (stored in the metadata repository) for a specific 
   *            record type. 
   * @param     recordShortDescription
   *            The short description of the Record; or null.
   * @param     keys
   *            The keys of the mapped record
   * @param     values
   *            The values of the mapped record sorted according to the keys
   *
   * @exception ResourceException
   *            Failed to create an initialized MappedRecord.
   *            Example error cases are:<ul>
   *            <li>Invalid specification of record name</li>
   *            <li>Resource adapter internal error</li>
   *            <li>Failed to access metadata repository</li>
   *            </ul>
   * @exception NotSupportedException
   *            Operation not supported
   * @exception	NullPointerException
   *						if either <code>keys</code> or <code>values</code> is	null.
   */
  @SuppressWarnings("unchecked")
  public MappedRecord createMappedRecord(
    String recordName,
    String recordShortDescription,
    List<?> keys,
    List<?> values
  ) throws ResourceException {
    MappedRecord target = new VariableSizeMappedRecord(recordName);
    target.setRecordShortDescription(recordShortDescription);
    int size=keys.size();
    if(values.size()!=size)throw new ResourceException(
      "Bad sequence length: keys and values arguments have different size"
    );
    for(
      int index=0;
      index<size;
      index++
    )target.put(keys.get(index),values.get(index));
    return target;
  }
  	
  /**
   * Creates a initialized MappedRecord with the specified name, short description
   * and content.  
   * <p>
   * The MappedRecord is backed up by the given arrays.
   * <p>
   * The elements in this map are ordered according to their insertion 
   * retaining the order of the <code>keys</code> and  <code>values</code>
   * arguments.
   *
   * @param     recordName
   *            The name of the record acts as a pointer to the meta
   *            information (stored in the metadata repository) for a specific 
   *            record type. 
   * @param     recordShortDescription
   *            The short description of the Record; or null.
   * @param     keys
   *            The keys of the mapped record
   * @param     values
   *            The values of the mapped record sorted according to the keys
   *
   * @exception ResourceException
   *            Failed to create an initialized MappedRecord.
   *            Example error cases are:<ul>
   *            <li>Invalid specification of record name</li>
   *            <li>Resource adapter internal error</li>
   *            <li>Failed to access metadata repository</li>
   *            </ul>
   * @exception NotSupportedException
   *            Operation not supported
   * @exception	NullPointerException
   *						if <code>values</code> is null.
   */
  public MappedRecord asMappedRecord(
    String recordName,
    String recordShortDescription,
    Object keys,
    Object values
  ) throws ResourceException {
    return new FixedSizeMappedRecord(
      recordName,
      recordShortDescription,
      keys,
      values
    );
  }
  	
  /**
   * Creates an IndexedRecord with the given name and short description.  
   *
   * @param     recordName
   *            The name of the record acts as a pointer to the meta 
   *            information (stored in the metadata repository) for a specific
   *            record type. 
   * @param     recordShortDescription
   *            The short description of the Record; or null.
   *
   * @exception ResourceException
   *            Failed to create an initialized IndexedRecord.
   *            Example error cases are:<ul>
   *            <li>Invalid specification of record name</li>
   *            <li>Resource adapter internal error</li>
   *            <li>Failed to access metadata repository</li>
   *            </ul>
   * @exception NotSupportedException
   *            Operation not supported
   */
  public IndexedRecord createIndexedRecord(
    String recordName,
    String recordShortDescription
  ) throws ResourceException {
    return new VariableSizeIndexedRecord(recordName,recordShortDescription);
	}  	
    
  /**
   * Creates a initialized IndexedRecord with the specified name, short description
   * and content.  
   *
   * @param     recordName
   *            The name of the record acts as a pointer to the meta 
   *            information (stored in the metadata repository) for a specific
   *            record type. 
   * @param     recordShortDescription
   *            The short description of the Record; or null.
   * @param     values
   *            The values of the indexed record.
   *
   * @exception ResourceException
   *            Failed to create an initialized IndexedRecord.
   *            Example error cases are:<ul>
   *            <li>Invalid specification of record name</li>
   *            <li>Resource adapter internal error</li>
   *            <li>Failed to access metadata repository</li>
   *            </ul>
   * @exception NotSupportedException
   *            Operation not supported
   * @exception	NullPointerException
   *						if <code>values</code> is null.
   */
  public IndexedRecord createIndexedRecord(
    String recordName,
    String recordShortDescription,
    List<?> values
  ) throws ResourceException {
    return new VariableSizeIndexedRecord(
      recordName,
      recordShortDescription,
      values
    );
  }
    
  /**
   * Creates an IndexedRecord with the given name, description and content.  
   * <p>
   * The Record is backed up by the given array.
   *
   * @param     recordName
   *            The name of the record acts as a pointer to the meta 
   *            information (stored in the metadata repository) for a specific
   *            record type. 
   * @param     recordShortDescription
   *            The short description of the Record; or null.
   * @param     values
   *            The values of the indexed record represented by a List or an 
   *            array of objects or primitive types.
   *
   * @exception ResourceException
   *            Failed to create an initialized IndexedRecord.
   *            Example error cases are:<ul>
   *            <li>Invalid specification of record name</li>
   *            <li>Resource adapter internal error</li>
   *            <li>Failed to access metadata repository</li>
   *            </ul>
   * @exception NotSupportedException
   *            Operation not supported
   * @exception	NullPointerException
   *						if <code>values</code> is null.
   */
  public IndexedRecord asIndexedRecord(
    String recordName,
    String recordShortDescription,
    Object values
  ) throws ResourceException {
      if(values instanceof List) {
          return new DelegatingIndexedRecord(
              recordName,
              recordShortDescription,
              (List<?>) values
          );
      } else if (values.getClass().isArray()){
          return new FixedSizeIndexedRecord(
              recordName,
              recordShortDescription,
              values
          );
      } else throw new ResourceException(
    	  "The argument values is neither an array nor a List: " + values.getClass().getName() 
      );
  }
  
  /**
   * The record factory is not yet configurable.
   */
  private static final ExtendedRecordFactory instance = new OrderedRecordFactory();

} 

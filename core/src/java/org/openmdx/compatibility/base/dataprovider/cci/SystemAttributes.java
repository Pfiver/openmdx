/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: SystemAttributes.java,v 1.17 2008/09/11 18:01:07 hburger Exp $
 * Description: Generated constants for SystemAttributes
 * Revision:    $Revision: 1.17 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/11 18:01:07 $
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
package org.openmdx.compatibility.base.dataprovider.cci;


/**
 * The <code>SystemAttributes</code> class contains the system attributes'
 * names.
 */

public class SystemAttributes {

  
  protected SystemAttributes() {
   // Avoid instantiation
  }

  /**
   * The name of the attribute containing the object's class name.
   */
  static public final String OBJECT_CLASS = "object_class";

  /**
   * The name of the derived attribute containing the object's class name
   * as well as the names of its superclasses. There is no ordering
   * of the class names.
   */
  static public final String OBJECT_INSTANCE_OF = "object_instanceof";

  
  //--------------------------------------------------------------------------
  // ExtentCapable
  //--------------------------------------------------------------------------

  /**
   * The name of the attribute containing the object's identity.
   */
  static public final String OBJECT_IDENTITY = "identity";

  /**
   * Hint to use the object identity instead of the path
   */
  static public final String USE_OBJECT_IDENTITY_HINT = 
      ";use=" + SystemAttributes.OBJECT_IDENTITY;
  
  //--------------------------------------------------------------------------
  // ContextCapable
  //--------------------------------------------------------------------------

  /**
   * The ContextCapables's context reference.
   */
  static public final String CONTEXT_CAPABLE_CONTEXT = "context";

  /**
   * The prefix of a DataproviderObject's view attribute.
   */
  static public final String CONTEXT_PREFIX = CONTEXT_CAPABLE_CONTEXT + ':';

  /**
   * A Lockable object's Lock context qualifier.
   */
  static public final String LOCK_CONTEXT = "Lock";

  /**
   * The prefix of a DataproviderObject's Lock context attribute.
   */
  static public final String OBJECT_LOCK_PREFIX = CONTEXT_PREFIX + LOCK_CONTEXT + ':';

  /**
   * The optimistic lock class containing the digest.
   */
  static public final String OPTIMISTIC_LOCK_CLASS = "org:openmdx:lock1:OptimisticLock";

  /**
   * A Lock object's digest feature.
   */
  static public final String OBJECT_DIGEST = "digest";

  /**
   * A sequence enabled object's Sequence context qualifier.
   */
  static public final String SEQUENCE_CONTEXT = "Sequence";

  /**
   * The prefix of a DataproviderObject's Sequence context attribute.
   */
  static public final String SEQUENCE_PREFIX = CONTEXT_PREFIX + SEQUENCE_CONTEXT + ':';

  /**
   * The sequence context class
   */
  static public final String SEQUENCE_CLASS = "org:openmdx:compatibility:sequence1:Sequence";

  /**
   * Tells whether sequences are supported
   */
  static public final String SEQUENCE_SUPPORTED = "supported";

  /**
   * The sequences' names
   */
  static public final String SEQUENCE_NAME = "name";

  /**
   * The sequences' values
   */
  static public final String SEQUENCE_NEXT_VALUE = "nextValue";


  //--------------------------------------------------------------------------
  // BasicObject
  //--------------------------------------------------------------------------

  /**
   * 
   */
  static public final String CREATED_BY = "createdBy";

  /**
   * 
   */
  static public final String CREATED_AT = "createdAt";

  /**
   * 
   */
  static public final String MODIFIED_BY = "modifiedBy";

  /**
   * 
   */
  static public final String MODIFIED_AT = "modifiedAt";

}

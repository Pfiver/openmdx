/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * Copyright (c) 2009 by Oracle Corporation. All Rights Reserved.
 * 
 * openMDX/Dalvik Notice (February 2013):<br>
 * THIS CODE HAS BEEN MODIFIED AND ITS NAMESPACE HAS BEEN PREFIXED WITH
 * <code>org.openmdx.dalvik.uses.</code>
 *
 * @since openMDX 2.12
 * @author openMDX Team
 */
package org.openmdx.dalvik.uses.javax.xml.stream;

/**
 * This interface declares a simple filter interface that one can
 * create to filter XMLStreamReaders
 * @version 1.0
 * @author Copyright (c) 2009 by Oracle Corporation. All Rights Reserved.
 */
public interface StreamFilter {

  /**
   * Tests whether the current state is part of this stream.  This method
   * will return true if this filter accepts this event and false otherwise.
   *
   * The method should not change the state of the reader when accepting
   * a state.
   *
   * @param reader the event to test
   * @return true if this filter accepts this event, false otherwise
   */
  public boolean accept(XMLStreamReader reader);
}

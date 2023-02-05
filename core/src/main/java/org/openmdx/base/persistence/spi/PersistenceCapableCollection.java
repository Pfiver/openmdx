/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: Persistence Capable Collection
 * Owner:       the original authors.
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
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
package org.openmdx.base.persistence.spi;

import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.spi.PersistenceCapable;

/**
 * Persistence Capable Collection
 * <p>
 * An application program should not use this interface directly. It should use the 
 * {@code PersistenceManager} and {@code PersistenceHelper} API instead.
 * 
 * @see javax.jdo.PersistenceManager
 * @see org.openmdx.base.persistence.cci.PersistenceHelper
 */
public interface PersistenceCapableCollection extends PersistenceCapable {
    
    /** 
     * Retrieve the associated Data Object Manager.
     * 
     * @return the associated Data Object Manager
     */
    PersistenceManager openmdxjdoGetDataObjectManager();

    /**
     * Evict the collection
     * 
     * @param allMembers evict each cached member 
     * @param allSubSets evict each cached sub-set
     */
    void openmdxjdoEvict(
        boolean allMembers, 
        boolean allSubSets
    );
    
    /**
     * Refresh the collection
     */
    void openmdxjdoRefresh();

    /**
     * Load the collection into the cache
     * <p>
     * Retrieve field values of instances from the store.  This tells
     * the {@code PersistenceManager} that the application intends to use 
     * the instances, and their field values should be retrieved.  The fields
     * in the current fetch group must be retrieved, and the implementation
     * might retrieve more fields than the current fetch group.
     * 
     * @param fetchPlan the fetch plan to be used, or {@code null}
     */
    void openmdxjdoRetrieve(
        FetchPlan fetchPlan
    );

}

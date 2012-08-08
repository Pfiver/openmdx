/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: TransactionBranchId.java,v 1.7 2008/02/13 16:06:16 hburger Exp $
 * Description:  X/Open Transaction Branch Identifier
 * Revision:    $Revision: 1.7 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/02/13 16:06:16 $
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2004-2008, OMEX AG, Switzerland
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
 * This product includes or is based on software developed by other 
 * organizations as listed in the NOTICE file.
 */
package org.openmdx.kernel.application.container.transaction;

import javax.transaction.xa.Xid;

import org.openmdx.kernel.text.format.HexadecimalFormatter;

/**
 *  X/Open Transaction Branch Identifier
 */
class TransactionBranchId extends TransactionId {

    /**
     * Constructor
     * 
     * @param globalTransactionIdMostSignificantBits
     * @param globalTransactionIdLeastSignificantBits
     */
    TransactionBranchId(
        TransactionId transactionId,
        int branchQualifier
    ){
        super(
           transactionId.mostSigBits,
           transactionId.leastSigBits
        );
        this.branchQualifier = branchQualifier;
    }


    //------------------------------------------------------------------------
    // Implements Serializable
    //------------------------------------------------------------------------

    /**
     * @serial
     */
    protected int branchQualifier;

    protected TransactionBranchId(
    ){
        // Serialization Constructor
    }


    //------------------------------------------------------------------------
    // Extends Object
    //------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return super.hashCode() ^ this.branchQualifier;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if(!(obj instanceof Xid)) return false;
        if(!(obj.getClass() == TransactionBranchId.class)) return equal(this, (Xid) obj);
        TransactionBranchId that = (TransactionBranchId) obj;
        return
            this.mostSigBits == that.mostSigBits &&
            this.leastSigBits == that.leastSigBits &&
            this.branchQualifier == that.branchQualifier;
    }

    /**
     * Simplify toString subclassing
     * 
     * @return a string builder containing the Xid's String representation
     */
    protected StringBuilder toStringBuilder(
    ){
        return super.toStringBuilder(
        ).append(
            '-'
        ).append(
            new HexadecimalFormatter(this.branchQualifier)
        );
    }


    //------------------------------------------------------------------------
    // Implements Xid
    //------------------------------------------------------------------------

    /* (non-Javadoc)
     * @see javax.transaction.xa.Xid#getBranchQualifier()
     */
    public byte[] getBranchQualifier() {
        return new byte[]{
            (byte)(branchQualifier >> 24),
            (byte)(branchQualifier >> 16),
            (byte)(branchQualifier >> 8),
            (byte)(branchQualifier )
        };
    }

}

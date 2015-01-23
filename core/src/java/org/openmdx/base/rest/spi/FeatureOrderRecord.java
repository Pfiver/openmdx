/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: Feature Order Record 
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Copyright (c) 2010-2014, OMEX AG, Switzerland
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

package org.openmdx.base.rest.spi;

import org.openmdx.base.query.SortOrder;


/**
 * Feature Order Record
 */
public class FeatureOrderRecord 
    extends AbstractMappedRecord<org.openmdx.base.rest.cci.FeatureOrderRecord.Member>
    implements org.openmdx.base.rest.cci.FeatureOrderRecord 
{

	/**
     * Constructor 
     */
    public FeatureOrderRecord() {
    	super();
    }

	/**
     * Constructor 
     */
    public FeatureOrderRecord(
    	String feature,
    	SortOrder sortOrder
    ) {
        this.feature = feature;
        this.sortOrder = sortOrder;
    }
    
    /**
     * Constructor 
     *
     * @param that
     */
    protected FeatureOrderRecord(
    	FeatureOrderRecord that
    ){
        super(that);
    }
    
    /**
     * Allows to share the member information among the instances
     */
    private static final Members<Member> MEMBERS = Members.newInstance(Member.class);
    
    /**
     * Implements <code>Serializable</code>
     */
	private static final long serialVersionUID = 529577445898774041L;

    /**
     * The name of the feature the order should be applied to
     */
    private String feature;
    
    /**
     * The sort oder
     */
    private SortOrder sortOrder;
    

    /* (non-Javadoc)
     * @see org.openmdx.base.resource.spi.AbstractRecord#clone()
     */
    @Override
    public FeatureOrderRecord clone(
    ){
        return new FeatureOrderRecord(this);
    }

    /* (non-Javadoc)
     * @see javax.resource.cci.Record#getRecordName()
     */
    @Override
    public String getRecordName() {
        return NAME;
    }
    
    /**
     * Retrieve a value by index
     * 
     * @param index the index
     * @return the value
     */
    @Override
    protected Object get(
        Member index
    ){
        switch(index) {
            case feature: return getFeature();
            case sortOrder: return jcaValue(getSortOrder());
            default: return super.get(index);
        }
    }

    /**
     * Set a value by index 
     * 
     * @param index the index
     * @param value the new value
     * 
     * @return the old value
     */
    @Override
    protected void put(
        Member index,
        Object value
    ){
        switch(index) {
            case feature:
                setFeature((String) value);
                break;
            case sortOrder: 
                setSortOrder(SortOrder.valueOf(((Short)value).shortValue()));
                break;
            default:
            	super.put(index, value);
        }
    }

	@Override
	public SortOrder getSortOrder() {
		return this.sortOrder;
	}

	protected void setSortOrder(SortOrder sortOrder) {
		assertMutability();
		this.sortOrder = sortOrder;
	}

	@Override
	public String getFeature() {
		return this.feature;
	}

	protected void setFeature(String feature) {
		assertMutability();
		this.feature = feature;
	}
    
    /* (non-Javadoc)
	 * @see org.openmdx.base.rest.spi.AbstractMappedRecord#getRecordShortDescription()
	 */
	@Override
	public String getRecordShortDescription() {
		if(this.feature == null) {
			return super.getRecordShortDescription();
		} else {
			StringBuilder description = new StringBuilder(
			).append(
					this.sortOrder == null ? ' ' : this.sortOrder.symbol()
			).append(
			    this.feature
			);
	        return description.toString();
		}
	}

	/* (non-Javadoc)
	 * @see org.openmdx.base.rest.spi.AbstractMappedRecord#members()
	 */
	@Override
	protected Members<Member> members() {
		return MEMBERS;
	}

}

/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Name:        $Id: Object_1.java,v 1.71 2008/09/18 12:13:37 hburger Exp $
 * Description: Object_1 class
 * Revision:    $Revision: 1.71 $
 * Owner:       OMEX AG, Switzerland, http://www.omex.ch
 * Date:        $Date: 2008/09/18 12:13:37 $
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
package org.openmdx.compatibility.base.dataprovider.transport.delegation;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import javax.resource.ResourceException;
import javax.resource.cci.MappedRecord;

import org.openmdx.base.accessor.generic.cci.LargeObject_1_0;
import org.openmdx.base.accessor.generic.cci.Object_1_0;
import org.openmdx.base.accessor.generic.cci.Object_1_2;
import org.openmdx.base.accessor.generic.cci.Object_1_3;
import org.openmdx.base.accessor.generic.cci.Structure_1_0;
import org.openmdx.base.accessor.generic.spi.AbstractObject_1;
import org.openmdx.base.accessor.generic.spi.MarshallingStructure_1;
import org.openmdx.base.collection.FilterableMap;
import org.openmdx.base.collection.MarshallingSequentialList;
import org.openmdx.base.exception.ExtendedIOException;
import org.openmdx.base.exception.RuntimeServiceException;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.resource.Records;
import org.openmdx.base.stream.cci.Source_1_0;
import org.openmdx.base.text.conversion.UUIDConversion;
import org.openmdx.compatibility.base.collection.Container;
import org.openmdx.compatibility.base.collection.SparseList;
import org.openmdx.compatibility.base.dataprovider.cci.SystemAttributes;
import org.openmdx.compatibility.base.dataprovider.layer.model.State_1_Attributes;
import org.openmdx.compatibility.base.dataprovider.spi.SystemOperations;
import org.openmdx.compatibility.base.dataprovider.transport.cci.Provider_1_0;
import org.openmdx.compatibility.base.dataprovider.transport.cci.Provider_1_1;
import org.openmdx.compatibility.base.dataprovider.transport.rmi.inprocess.StreamMarshaller.BinaryHolder;
import org.openmdx.compatibility.base.dataprovider.transport.rmi.inprocess.StreamMarshaller.CharacterHolder;
import org.openmdx.compatibility.base.dataprovider.transport.spi.Connection_1_3;
import org.openmdx.compatibility.base.event.InstanceCallbackEvent;
import org.openmdx.compatibility.base.event.InstanceCallbackListener;
import org.openmdx.compatibility.base.naming.Path;
import org.openmdx.compatibility.base.naming.PathComponent;
import org.openmdx.kernel.exception.BasicException;
import org.openmdx.kernel.id.UUIDs;
import org.openmdx.kernel.id.cci.UUIDGenerator;
import org.openmdx.kernel.text.format.IndentingFormatter;

/**
 * Object_1_0 implementation
 */
@SuppressWarnings({"unchecked", "deprecation"})
public class Object_1 implements Serializable, Object_1_2, Object_1_3, Evictable {

    /**
     * Constructor for transient objects
     * @param       objectClass
     *              The model class id
     *
     * @exception   ServiceException  BAD_PARAMETER
     *              if objectClass is null
     */
    public Object_1(
        String objectClass,
        Connection_1_3 manager
    )throws ServiceException{
        this.identity = null;
        this.manager = manager;
        this.provider = null;
        this.deleted = false;
        this.transactionalValuesRecordName = objectClass;
        if(objectClass == null) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.BAD_PARAMETER,
                null,
                "Argument objectClass is null"
            );
        }
    }

    /**
     * Constructor for persistent objects
     */
    Object_1(
        Path identity,
        Connection_1_3 manager,
        Provider_1_0 provider
    )throws ServiceException{
        this.identity = identity;
        this.provider = provider;
        this.manager = manager;
        this.deleted = false;
        this.transactionalValuesRecordName = null;
        if(
                identity == null || manager == null || provider == null
        ) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.BAD_PARAMETER,
                "Invalid null argument",
                new BasicException.Parameter("accessPath",identity == null ? "null" : ""),
                new BasicException.Parameter("manager",identity == null ? "null" : ""),
                new BasicException.Parameter("provider",identity == null ? "null" : "")
            );
        }
    }

    /**
     * Constructor
     *
     * @param identity
     * @param that
     * @param completelyDirty
     * @throws ServiceException
     */
    protected Object_1(
        Path identity,
        Object_1 that,
        boolean completelyDirty
    )throws ServiceException{
        if(
                that.transactionalValuesRecordName == null &&
                that.objIsHollow()
        ) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.ILLEGAL_STATE,
                null,
                "The object is hollow"
            );
        }
        this.provider = that.provider;
        this.manager = that.manager;
        propagate(that, completelyDirty);
        if(identity != null) {
            int p = identity.size() - 2;
            Object_1_0 parent = this.manager.getObject(identity.getPrefix(p));
            FilterableMap there = parent.objGetContainer(identity.get(p));
            objMove(
                there,
                identity.get(p + 1)
            );
        }
    }

    static final String[] GET_STREAM_ARGUMENTS = {
        SystemOperations.GET_STREAM_FEATURE,
        SystemOperations.GET_STREAM_POSITION,
        SystemOperations.GET_STREAM_VALUE
    };

    private final TransactionalState_1 getState(
        boolean optional
    ) throws ServiceException{
        return 
        optional && (this.manager == null || this.manager.isClosed()) ? null :
            ((UnitOfWork_1)this.manager.getUnitOfWork()).getState(this,optional);        
    }

    void propagate(
        Object_1 that,
        boolean completelyDirty
    ) throws ServiceException{
        this.transactionalValuesRecordName = that.transactionalValuesRecordName != null ?
            that.transactionalValuesRecordName :
                that.persistentValues.getRecordName();
        if(objIsHollow()) {
            this.persistentValues = Object_1.createMappedRecord(this.transactionalValuesRecordName);
        }
        this.deleted = that.deleted;
        this.digest = that.digest;

        if(that.persistentValues != null) {
            for(Map.Entry e : (Set<Map.Entry>)that.persistentValues.entrySet()) {
                Object feature = e.getKey();
                Object candidate = e.getValue();
                if(
                        candidate instanceof Container ||
                        candidate instanceof FilterableMap
                ) {
                    // ignore containments
                } else {
                    this.persistentValues.put(feature, candidate);
                }
            }
        }
        TransactionalState_1 thisState = this.getState(false);
        TransactionalState_1 thatState = that.getState(false);
        for(Map.Entry<String,Object> e : thatState.values(false).entrySet()){
            String feature = e.getKey();
            Object candidate = e.getValue();
            if(
                    candidate instanceof Container ||
                    candidate instanceof FilterableMap
            ) {
                // ignore containments
            } else if (candidate instanceof ManagedSet) {
                if(completelyDirty || thatState.dirtyFeatures(true).contains(feature)) {
                    Set target = this.objGetSet(feature);
                    target.clear();
                    target.addAll((Set)candidate);
                }
            } else if (candidate instanceof ManagedList) {
                if(completelyDirty || thatState.dirtyFeatures(true).contains(feature)) {
                    List target = this.objGetList(feature);
                    target.clear();
                    target.addAll((List)candidate);
                }
            } else if (candidate instanceof ManagedSparseArray) {
                if(completelyDirty || thatState.dirtyFeatures(true).contains(feature)) {
                    SortedMap target = this.objGetSparseArray(feature);
                    target.clear();
                    target.putAll((SortedMap)candidate);
                }
            } else {
                thisState.values(false).put(feature, candidate);
            }
        }

        if(completelyDirty) {
            thisState.dirtyFeatures(false).addAll(thisState.values(false).keySet());
            if(that.persistentValues != null) {
                thisState.dirtyFeatures(false).addAll(that.persistentValues.keySet());
            }
        } else {
            thisState.dirtyFeatures(false).addAll(thatState.dirtyFeatures(true));
        }
    }

    /**
     * Returns the object's model class.
     *
     * @return  the object's model class
     *
     * @exception   ServiceException
     *              if the information is unavailable
     */
    public String objGetClass(
    ) throws ServiceException {
        if(this.transactionalValuesRecordName == null) {
            try {
                assertObjectIsAccessible();
                this.transactionalValuesRecordName = this.persistentValues.getRecordName();
            } catch (NullPointerException exception){
                throw new ServiceException(
                    exception,
                    BasicException.Code.DEFAULT_DOMAIN,
                    BasicException.Code.NOT_AVAILABLE,
                    null,
                    "Object class can't be determined"
                );
            }
        }
        return this.transactionalValuesRecordName;
    }

    /**
     * Returns the object's identity.
     *
     * @return    the object's identity;
     *            or null for transient objects
     */
    public Path objGetPath(
    ){
        return this.identity;
    }

    /**
     * Returns the object's access path.
     *
     * @return  the object's access path;
     *          or null for transient or new objects
     */
    public Object objGetResourceIdentifier(
    ){
        return objIsPersistent() && !objIsNew() ? this.identity.toUri() : null;
    }

    /**
     * Returns a new set containing the names of the features in the default
     * fetch group.
     * <p>
     * The returned set is a copy of the original set, i.e. interceptors are
     * free to modify it before passing it on.
     *
     * @return  the names of the features in the default fetch group
     * @throws ServiceException 
     */
    public Set objDefaultFetchGroup(
    ) throws ServiceException{
        Set result = new HashSet();
        TransactionalState_1 state = getState(true);
        if(state != null) {
            result.addAll(state.values(true).keySet());
        }
        if(!objIsHollow()) {
            result.addAll(this.persistentValues.keySet());
        }            
        return result;
    }

    /**
     *
     */
    private static byte[] getDigest(
        MappedRecord source
    ){
        SparseList holder = (SparseList)source.get(
            SystemAttributes.OBJECT_LOCK_PREFIX + SystemAttributes.OBJECT_DIGEST
        );
        return holder == null ? null : (byte[])holder.get(0);
    }

    /**
     * Get a sequence
     *
     * @param reference
     * @return
     * @throws ServiceException
     */
    long getSequence(
        String reference
    ) throws ServiceException{
        if(objIsNew()) {
            return AbstractContainer.SEQUENCE_MIN_VALUE;
        }
        assertObjectIsAccessible();
        SparseList supported = (SparseList)this.persistentValues.get(
            SystemAttributes.SEQUENCE_PREFIX + SystemAttributes.SEQUENCE_SUPPORTED
        );
        if(supported != null && ((Boolean)supported.get(0)).booleanValue()) {
            SparseList names = (SparseList)this.persistentValues.get(
                SystemAttributes.SEQUENCE_PREFIX + SystemAttributes.SEQUENCE_NAME
            );
            int index = names.indexOf(reference);
            if(index < 0) {
                return AbstractContainer.SEQUENCE_MIN_VALUE;
            }
            Long value = (Long) (
                    (SparseList)this.persistentValues.get(
                        SystemAttributes.SEQUENCE_PREFIX + SystemAttributes.SEQUENCE_NEXT_VALUE
                    )
            ).get(index);
            return value == null ?
                AbstractContainer.SEQUENCE_MIN_VALUE :
                    value.longValue();
        } else {
            return AbstractContainer.SEQUENCE_NOT_SUPPORTED;
        }
    }

    /**
     * Set the default fetch group
     *
     * @param logMessage
     * @param defaultFetchGroup
     * @throws ServiceException
     */
    private void set(
        String logMessage,
        MappedRecord defaultFetchGroup
    ) throws ServiceException {
        this.persistentValues = defaultFetchGroup;
        if(this.digest == null) {
            this.digest = getDigest(defaultFetchGroup);
        }
        fireInstanceCallback(InstanceCallbackEvent.POST_LOAD);
    }

    /**
     * Merge the current and newly retrieved values
     *
     * @param logMessage
     * @param primary
     * @param secondary
     * @param notify
     * @throws ServiceException
     */
    private void merge(
        String logMessage,
        MappedRecord primary,
        MappedRecord secondary,
        boolean notify
    ) throws ServiceException {
        MappedRecord target = Object_1.createMappedRecord(objGetClass());
        target.putAll(secondary);
        target.putAll(primary);
        this.persistentValues = target;
        byte[] digest = getDigest(primary);
        if(digest != null) {
            this.digest = digest;
        }
        if(notify) {
            fireInstanceCallback(InstanceCallbackEvent.POST_RELOAD);
        }
    }

    /**
     *
     * @param fetchGroup
     */
    void fetched(
        MappedRecord fetchGroup
    ){
        try {
            if(objIsHollow()){
                set(
                    "pre-loaded",
                    fetchGroup
                );
            } else {
                merge(
                    "pre-loaded",
                    this.persistentValues,
                    fetchGroup,
                    false
                );
            }
        } catch (Exception exception) {
            new RuntimeServiceException(exception).log();
        }
    }

    /**
     *
     * @param pushGroup
     */
    void updated(
        MappedRecord pushGroup
    ){
        if(this.refreshAsynchronously) {
            try {
                if(pushGroup == null) {
                    evict();
                } else if(objIsHollow()){
                    set(
                        "updated",
                        pushGroup
                    );
                } else {
                    merge(
                        "updated",
                        pushGroup,
                        this.persistentValues,
                        true // notify
                    );
                }
            } catch (Exception exception) {
                new RuntimeServiceException(exception).log();
            }
        }
    }

    /**
     *
     * @param makeInaccessable
     */
    public void invalidate(
        boolean makeInaccessable
    ){
        if(makeInaccessable) {
            try {
                evict();
            } catch (Exception e) {
                // Just ignore it
            }
            this.manager = null;
        }
        this.identity = null;
        this.provider = null;
    }

    /**
     * Ask the persistence framework for the object's content
     */
    private void load(
    ) throws ServiceException {
        try {
            set(
                "loaded",
                this.provider.getDefaultFetchGroup(
                    this.identity,
                    null,
                    this.manager
                )
            );
            this.inaccessabilityReason = null;
        } catch (ServiceException exception){
            this.inaccessabilityReason = exception;
            if(this.manager != null){
                this.manager.invalidate(this.identity, true);
            } else {
                this.invalidate(true);
            }
            throw exception;
        }
    }

    /**
     * Refresh the state of the instance from its provider.
     *
     * @exception   ServiceException
     *              if the object can't be synchronized
     */
    public void objRefresh(
    ) throws ServiceException {
        if(objIsPersistent() && !objIsNew()) {
            try {
                load();
            } finally {
                objMakeClean();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.accessor.generic.cci.Object_1_3#objMakeClean()
     */
    public void objMakeClean(
    ) throws ServiceException {
        TransactionalState_1 state = getState(true);
        if(state != null) {
            state.clear();
        }
        objRemoveFromUnitOfWork();
    }

    /**
     * Mark an object as volatile, i.e POST_RELOAD InstanceCallbackEvents
     * may be fired.
     *
     * @exception   ServiceException
     *              if the object can't be made volatile.
     */
    public void objMakeVolatile(
    ) throws ServiceException {
        this.refreshAsynchronously = true;
    }

    //------------------------------------------------------------------------
    // Unit of work boundaries
    //------------------------------------------------------------------------

    /**
     * After this call the object observes unit of work boundaries.
     * <p>
     * This method is idempotent.
     *
     * @exception   ServiceException ILLEGAL_STATE
     *              if no unit of work is in progress
     * @exception   ServiceException
     *              if the object can't be added to the unit of work for
     *                another reason.
     */
    public void objAddToUnitOfWork(
    ) throws ServiceException {
        if(!objIsPersistent()) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.NOT_SUPPORTED,
                null,
                "TRANSIENT_CLEAN and TRANSIENT_DIRTY not supported"
            );
        }
        ((UnitOfWork_1)this.manager.getUnitOfWork()).add(this);
    }

    /**
     * After this call the object ignores unit of work boundaries.
     * <p>
     * This method is idempotent.
     *
     * @exception   ServiceException ILLEGAL_STATE
     *              if the object is dirty.
     * @exception   ServiceException
     *              if the object can't be removed from its unit of work for
     *                another reason
     */
    public void objRemoveFromUnitOfWork(
    ) throws ServiceException {
        if(objIsDirty()) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.ILLEGAL_STATE,
                null,
                "A dirty object can't be removed from the unit of work"
            );
        }
        ((UnitOfWork_1)this.manager.getUnitOfWork()).remove(this);
    }


    //------------------------------------------------------------------------
    // Life Cycle Operations
    //------------------------------------------------------------------------

    /**
     * The copy operation makes a copy of the object. The copy is located in
     * the scope of the container passed as the first parameter and includes
     * the object's default fetch set.
     *
     * @return    an object initialized from the existing object.
     *
     * @param     there
     *            the new object's container or <code>null</code>, in which
     *            case the object will not belong to any container until it is
     *            moved to a container.
     * @param     criteria
     *            The criteria is used to add the object to the container or
     *            <code>null</null>, in which case it is up to the
     *            implementation to define the criteria.
     *
     * @exception ServiceException
     *            if the copy operation fails.
     */
    public Object_1_0 objCopy(
        FilterableMap there,
        String criteria
    ) throws ServiceException {
        throw new ServiceException (
            BasicException.Code.DEFAULT_DOMAIN,
            BasicException.Code.NOT_IMPLEMENTED,
            getExceptionParameters(),
            "Copy not implemented yet"
        ); //... Copy not implemented yet
    }

    /**
     * The move operation moves the object to the scope of the container
     * passed as the first parameter. The object remains valid after move has
     * successfully executed.
     *
     * @param     there
     *            the object's new container.
     * @param     criteria
     *            The criteria is used to move the object to the container or
     *            <code>null</null>, in which case it is up to the
     *            implementation to define the criteria.
     *
     * @exception ServiceException  ILLEGAL_STATE
     *            if the object is persistent.
     * @exception ServiceException BAD_PARAMETER
     *            if <code>there</code> is not an instance of <code>DelegatingContainer</code>.
     * @exception ServiceException
     *            if the move operation fails.
     */
    public void objMove(
        FilterableMap there,
        String criteria
    ) throws ServiceException{
        if(objIsPersistent() && !objIsNew()) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.ILLEGAL_STATE,
                "Attempt to move a persistent object",
                new BasicException.Parameter(SystemAttributes.OBJECT_IDENTITY,objGetPath().toUri())
            );
        }
        if(there != null && !(there instanceof DelegatingContainer)) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.BAD_PARAMETER,
                "There is not an instance of DelegatingContainer",
                new BasicException.Parameter("there",there == null ? null : there.getClass().getName()),
                new BasicException.Parameter("criteria",criteria)
            );
        }
        Container container = there == null ? null : ((DelegatingContainer)there).container;
        if(
                this.transientContainer != null
        ){
            this.transientContainer.remove(this);
            this.transientContainer = null;
        }
        if(container instanceof PersistentContainer_1){
            TransactionalState_1 state = getState(false);
            state.setLifeCycleEventPending(true);
            this.persistentContainer=(PersistentContainer_1)container;
            Path containerPath = persistentContainer.getReferenceFilter();
            PathComponent qualifier = criteria == null ? null : new PathComponent(criteria);
            if(this.manager.getUnitOfWork().isOptimistic()) {
                if(qualifier == null) {
                    qualifier = PathComponent.createPlaceHolder();
                }
            } else {
                if(qualifier == null || qualifier.isPlaceHolder()) {
                    qualifier = new PathComponent(
                        createDefaultQualifier(
                            (AbstractContainer)container,
                            this.manager.getDefaultQualifierType()
                        )
                    );
                }
            }
            this.identity = containerPath.getChild(qualifier);
            if(this.manager.containsKey(this.identity)){
                try {
                    this.manager.getObject(this.identity).objGetClass();
                } catch (Exception exception) {
                    // The candidate could be evicted now
                }
                if(this.manager.containsKey(this.identity)){
                    this.identity = null;
                    state.setLifeCycleEventPending(false);
                    this.persistentContainer = null;
                    throw new ServiceException(
                        BasicException.Code.DEFAULT_DOMAIN,
                        BasicException.Code.DUPLICATE,
                        "There is already an object with the same qualifier in the container",
                        new BasicException.Parameter("container",containerPath),
                        new BasicException.Parameter("qualifier", qualifier)
                    );
                }
            }
            this.manager.getObject(containerPath.getParent()).objAddToUnitOfWork();
            this.manager.cache(this.identity,this);
            this.fireInstanceCallback(InstanceCallbackEvent.POST_CREATE);
            objAddToUnitOfWork();
            for(Map.Entry e : state.values(false).entrySet()){
                if(e.getValue() instanceof DelegatingContainer){
                    DelegatingContainer dc = (DelegatingContainer)e.getValue();
                    TransientContainer_1 source = (TransientContainer_1)dc.container;
                    dc.container = new PersistentContainer_1(
                        identity.getChild((String)e.getKey()),
                        this.persistentContainer.getManager(),
                        this.persistentContainer.getProvider(),
                        new EvictablePersistentObjects((String)e.getKey()),
                        this
                    );
                    Set<Map.Entry<String,Object_1_0>> children=new HashSet<Map.Entry<String,Object_1_0>>(source.getEntrySet());
                    for(Map.Entry<String,Object_1_0> f : children) {
                        f.getValue().objMove(dc,f.getKey());
                    }
                }
            }
        }else if(container!=null){
            this.transientContainer = (TransientContainer_1)container;
            this.transientContainer.add(criteria,this);
        }
    }

    /**
     * Create a new qualifier depending on the default qualifier type.
     *
     * @param container
     * @param defaultQualifierType
     *
     * @return a newly created default qualifier
     *
     * @throws ServiceException
     */
    private String createDefaultQualifier(
        AbstractContainer container,
        String defaultQualifierType
    ) throws ServiceException{
        if("SEQUENCE".equals(defaultQualifierType)) {
            String qualifier = container.nextQualifier();
            if(qualifier != null) {
                return qualifier;
            }
        }
        if(this.uuidGenerator == null) {
            this.uuidGenerator = UUIDs.getGenerator();
        }
        UUID uuid = this.uuidGenerator.next();
        if("UUID".equals(defaultQualifierType)){
            return uuid.toString();
        } else if ("UID".equals(defaultQualifierType)) {
            return UUIDConversion.toUID(uuid);
        } else if (
                "URN".equals(defaultQualifierType) ||
                "SEQUENCE".equals(defaultQualifierType)
        ) {
            return '(' + UUIDConversion.toURN(uuid) + ')';
        } else {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.INVALID_CONFIGURATION,
                "The following default qualifier types are supported: [UUID, UID, URN, SEQUENCE]",
                new BasicException.Parameter("defaultQualifierType",defaultQualifierType)
            );
        }
    }

    /**
     * Removes an object.
     * <p>
     * Neither <code>getValue()</code> nor <code>setValue()</code>
     * calls are allowed after an <code>remove()</code> invocation and
     * <code>isDeleted()</code> will return <code>true</code> unless the
     * object has beeen transient.
     *
     * @exception   ServiceException NOT_SUPPORTED
     *              If the object refuses to be removed.
     * @exception   ServiceException
     *              if the object can't be removed
     */
    public void objRemove(
    ) throws ServiceException {
        if(!objIsPersistent()) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.ILLEGAL_STATE,
                null,
                "Attempt to remove a transient object"
            );
        }
        objAddToUnitOfWork();
        this.manager.getObject(
            this.identity.getPrefix(this.identity.size()-2)
        ).objAddToUnitOfWork();
        fireInstanceCallback(InstanceCallbackEvent.PRE_DELETE);
        this.deleted = true;
        getState(false).setLifeCycleEventPending(true);
    }

    /**
     *
     * @param source
     * @throws ServiceException
     */
    private final boolean unflushable (
        Collection source
    ) throws ServiceException{
        if(source instanceof Container) {
            return false;
        }
        for(Object o : source) {
            if(unflushable(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param source
     * @throws ServiceException
     */
    private final boolean unflushable (
        Path source
    ) throws ServiceException{
        for(
                int i = 0;
                i < source.size();
                i++
        ) {
            if(source.getComponent(i).isPlaceHolder()) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param source
     * @throws ServiceException
     */
    private final boolean unflushable (
        Object_1_0 source
    ) throws ServiceException{
        return !source.objIsPersistent() || unflushable (source.objGetPath());
    }

    /**
     * @param feature
     */
    private boolean unflushable(
        Object source
    ) throws ServiceException{
        return
        (source instanceof Object_1_0 && unflushable((Object_1_0)source)) ||
        (source instanceof Collection && unflushable((Collection)source));
    }

    /**
     * Flush the state of the instance to its provider.
     *
     * @return      true if all attributes could be flushed,
     *              false if some attributes contained placeholders
     *
     * @exception   ServiceException NOT_SUPPORTED
     *              if the unit of work is optimistic
     * @exception   ServiceException ILLEGAL_STATE
     *              if the object is not persistent
     * @exception   ServiceException
     *              if the object can't be synchronized
     */
    public boolean objFlush(
    ) throws ServiceException {
        if(this.manager.getUnitOfWork().isOptimistic()) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.NOT_SUPPORTED,
                null,
                "Optimistc units of work can't flush"
            );
        }
        if(!objIsPersistent()) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.ILLEGAL_STATE,
                null,
                "Transient objects can't be flushed"
            );
        }
        Path current = objGetPath();
        if(current.size() == 1) {
            return true; // No need to flush the authority
        }
        Path parent = current.getPrefix(current.size() - 2);
        TransactionalState_1 parentState = ((Object_1)this.manager.getObject(parent)).getState(true);
        if(
                parentState != null &&
                parentState.isLifeCycleEventPending()
        ) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.ILLEGAL_STATE,
                "Life cycle event pending for parent object",
                new BasicException.Parameter("current", current),
                new BasicException.Parameter("parent", current)
            );
        }
        prepare();
        flush(true);
        return !objIsDirty();
    }

    void prepare(
    ) throws ServiceException {
        if(objIsDirty() && !objIsDeleted()) {
            fireInstanceCallback(
                InstanceCallbackEvent.PRE_STORE
            );
        }
        getState(false).setPrepared(true);
    }

    /**
     * Flush the state of the instance to its provider.
     *
     * @exception   ServiceException
     *              if the object can't be synchronized
     */
    void flush(
        boolean excludePlaceholders
    ) throws ServiceException {
        Set retained = new HashSet();
        TransactionalState_1 state = getState(false);
        if(objIsDeleted()){
            if(state.isLifeCycleEventPending()){
                if(!objIsNew()) {
                    this.provider.removeObject(objGetPath(),this.manager);
                }
                state.setLifeCycleEventPending(false);
            }
        } else if(objIsNew() && state.isLifeCycleEventPending()){
            this.manager = this.persistentContainer.getManager();
            this.provider = this.persistentContainer.getProvider();
            this.persistentValues=Object_1.createMappedRecord(
                objGetClass()
            );
            for(Map.Entry e : state.values(false).entrySet()) {
                Object feature = e.getKey();
                Object source = e.getValue();
                if(excludePlaceholders && unflushable(source)){
                    retained.add(feature);
                } else if(source instanceof ManagedLargeObject){
                    ManagedLargeObject largeObject = (ManagedLargeObject)source;
                    List target = new ArrayList();
                    target.add(largeObject.transientStream);
                    target.add(new Long(largeObject.transientLength));
                    this.persistentValues.put(feature, target);
                } else if(source instanceof Collection){
                    if(source instanceof ManagedList){
                        ManagedList collection = (ManagedList)source;
                        List target = new ArrayList();
                        for(Object o : collection) {
                            target.add(this.manager.unmarshal(o));
                        }
                        this.persistentValues.put(feature, target);
                    }else if(source instanceof ManagedSet){
                        ManagedSet collection = (ManagedSet)source;
                        Set target = new HashSet();
                        for(Object o : collection) {
                            target.add(this.manager.unmarshal(o));
                        }
                        this.persistentValues.put(feature, target);
                    } else {
                        throw new ServiceException(
                            BasicException.Code.DEFAULT_DOMAIN,
                            BasicException.Code.ASSERTION_FAILURE,
                            "Unsupported Collection class",
                            new BasicException.Parameter("class",source.getClass().getName())
                        );
                    }
                }else if(source instanceof Map){
                    if(source instanceof SortedMap){
                        SortedMap<?,?> collection = (SortedMap<?,?>)source;
                        SortedMap target = new TreeMap();
                        for(Map.Entry<?,?> k : collection.entrySet()) {
                            target.put(
                                k.getKey(),
                                this.manager.unmarshal(k.getValue())
                            );
                        }
                        this.persistentValues.put(feature, target);
                    } else if (source instanceof FilterableMap) {
                        // handled below
                    } else {
                        throw new ServiceException(
                            BasicException.Code.DEFAULT_DOMAIN,
                            BasicException.Code.ASSERTION_FAILURE,
                            "Unsupported Map class",
                            new BasicException.Parameter("class",source.getClass().getName())
                        );
                    }
                }else{
                    this.persistentValues.put(
                        feature,
                        this.manager.unmarshal(source)
                    );
                }
            }
            this.provider.createObject(
                this.identity,
                this.persistentValues,
                this.manager
            );
            this.persistentContainer = null;
            state.setLifeCycleEventPending(false);
            this.transientOnRollback = this.provider instanceof Provider_1_1 ?
                ((Provider_1_1)this.provider).doPeristentNewObjectsBecomeTransientUponRollback() :
                    this.manager.getUnitOfWork().isTransactional();
        } else if(
                objIsDirty() || objIsPersistent() && this.digest != null &&
                objGetPath().size() > 4 // exclude Authorities and Providers
        ){
            MappedRecord arguments = null;
            arguments = Object_1.createMappedRecord(objGetClass());
            arguments.put(
                SystemAttributes.OBJECT_LOCK_PREFIX + SystemAttributes.OBJECT_CLASS,
                SystemAttributes.OPTIMISTIC_LOCK_CLASS
            );
            arguments.put(
                SystemAttributes.OBJECT_LOCK_PREFIX + SystemAttributes.OBJECT_DIGEST,
                digest
            );
            for(String feature : state.dirtyFeatures(true)){
                if(state.values(false).containsKey(feature)) {
                    Object source = state.values(false).get(feature);
                    if(excludePlaceholders && unflushable(source)){
                        retained.add(feature);
                    } else if(source instanceof ManagedLargeObject){
                        ManagedLargeObject largeObject = (ManagedLargeObject)source;
                        List target = new ArrayList();
                        target.add(largeObject.transientStream);
                        target.add(new Long(largeObject.transientLength));
                        this.persistentValues.put(feature, target);
                    } else if(source instanceof Collection){
                        if(source instanceof ManagedList){
                            ManagedList collection = (ManagedList)source;
                            List target = new ArrayList();
                            for(Object o : collection) {
                                target.add(this.manager.unmarshal(o));
                            }
                            arguments.put(feature, target);
                        }else if(source instanceof ManagedSet){
                            ManagedSet collection = (ManagedSet)source;
                            Set target = new HashSet();
                            for(Object o : collection) {
                                target.add(this.manager.unmarshal(o));
                            }
                            arguments.put(feature, target);
                        }else{
                            //Container not handled here
                        }
                    }else if(source instanceof ManagedSparseArray){
                        SortedMap<?,?> collection = (SortedMap)source;
                        SortedMap target = new TreeMap();
                        for(Map.Entry<?,?> k : collection.entrySet()){
                            target.put(
                                k.getKey(),
                                this.manager.unmarshal(k.getValue())
                            );
                        }
                        arguments.put(feature, target);
                    }else{
                        arguments.put(
                            feature,
                            this.manager.unmarshal(source)
                        );
                    }
                } else {
                    arguments.put(
                        feature,
                        this.persistentValues.get(feature)
                    );
                }
            }
            this.provider.editObject(
                this.identity,
                arguments,
                this.manager
            );
        }
        Queue<Operation> operations = state.operations(true);
        for(
                Operation operation = operations.poll();
                operation != null;
                operation = operations.poll()
        ){
            operation.invoke();
        }
        Set<String> dirty = state.dirtyFeatures(false);
        dirty.clear();
        dirty.addAll(retained);
    }

    void afterCompletion(boolean committed) throws ServiceException {
        if (committed) {
            if(objIsDeleted()){
                this.manager.invalidate(this.identity, true);
            } else {
                evict(); //... depending on configuration
            }
        } else {
            getState(false).setLifeCycleEventPending(false);
            if(objIsDeleted()){
                this.deleted = false;
            } else {
                evict(); //... depending on configuration
            }
            if(isTransientOnRollback()){
                if(this.manager != null) {
                    this.manager.evict(this);
                }
                this.persistentContainer = null;
                this.identity = null;
                this.provider = null;
            } else if (this.transientContainer != null){
                this.transientContainer.remove(this);
                this.transientContainer = null;
            }
        }
        this.transientOnRollback = false;
    }

    public void evict(
    ){
        InstanceCallbackEvent event = new InstanceCallbackEvent(
            InstanceCallbackEvent.PRE_CLEAR,
            this, 
            null
        );
        InstanceCallbackListener[] listeners;
        try {
            listeners = (InstanceCallbackListener[])objGetEventListeners(
                null,
                InstanceCallbackListener.class
            );
            for(
                    int i = 0;
                    i < listeners.length;
                    i++
            ) {
                try {
                    listeners[i].preClear(event);
                } catch (ServiceException preclearException) {
                    preclearException.log();
                }
            }
        } catch (ServiceException listenerException) {
            listenerException.log();
        }
        try {
            TransactionalState_1 state = getState(true);
            if(state != null){ 
                for(
                        Iterator i = state.values(false).entrySet().iterator();
                        i.hasNext();
                ){
                    Map.Entry j = (Entry)i.next();
                    Object value = j.getValue();
                    if(value instanceof Evictable && value instanceof Collection){
                        ((Evictable)value).evict();
                    } else if (
                            value instanceof DelegatingContainer &&
                            ((DelegatingContainer)value).container instanceof PersistentContainer_1
                    ){
                        if(this.identity == null){ // Maybe there was a BasicException.Code.DUPLICATE...
                            ((DelegatingContainer)value).container = new TransientContainer_1();
                            //... forget about potential children
                        } else {
                            String feature = (String)j.getKey();
                            PersistentContainer_1 container = (PersistentContainer_1)((DelegatingContainer)value).container;
                            Path expectedIdentity = this.identity.getChild(feature);
                            if(expectedIdentity.equals(container.getReferenceFilter())){
                                container.evict();
                            } else {
                                ((DelegatingContainer)value).container = new PersistentContainer_1(
                                    this.identity.getChild(feature),
                                    this.manager,
                                    this.provider,
                                    new EvictablePersistentObjects(feature),
                                    this
                                );
                            }
                        }
                    } else {
                        i.remove();
                    }
                }
            }
        } catch (ServiceException ignore) {
            // Eviction should nevertheless be successful
        }
        this.digest = null;
        this.persistentValues = null;
    }


    //------------------------------------------------------------------------
    // Event Handling
    //------------------------------------------------------------------------

    private void verifyListenerArguments(
        String feature,
        EventListener listener
    ) throws ServiceException {
        verifyListenerArguments(
            feature,
            listener == null ? null : listener.getClass()
        );
    }

    private void verifyListenerArguments(
        String feature,
        Class listenerType
    ) throws ServiceException {
        if(listenerType == null) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.BAD_PARAMETER,
                null,
                "The listener argument must not be null"
            );
        }
        if(InstanceCallbackListener.class.isAssignableFrom(listenerType)){
            if(feature != null) {
                throw new ServiceException(
                    BasicException.Code.DEFAULT_DOMAIN,
                    BasicException.Code.BAD_PARAMETER,
                    "Instance level events must not be associated with a feature",
                    new BasicException.Parameter(
                        "feature",
                        feature
                    ),
                    new BasicException.Parameter(
                        "listenerType",
                        listenerType.getName()
                    )
                );
            }
        } else if (
                PropertyChangeListener.class.isAssignableFrom(listenerType) ||
                VetoableChangeListener.class.isAssignableFrom(listenerType)
        ){
            // Feature scope o.k.
        } else {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.NOT_SUPPORTED,
                "Unsupported listener class",
                new BasicException.Parameter(
                    "listenerType",
                    listenerType.getName()
                ),
                new BasicException.Parameter(
                    "supported",
                    InstanceCallbackListener.class.getName(),
                    PropertyChangeListener.class.getName(),
                    VetoableChangeListener.class.getName()
                )
            );
        }
    }

    /**
     * Add an event listener.
     *
     * @param feature
     *        restrict the listener to this feature;
     *        or null if the listener is interested in all features
     * @param listener
     *        the event listener to be added
     * <p>
     * It is implementation dependent whether the feature name is verified or
     * not.
     *
     * @exception   ServiceException BAD_MEMBER_NAME
     *              if the object has no such feature or if a non-null
     *              feature name is specified for an instance level event
     * @exception   ServiceException NOT_SUPPORTED
     *              if the listener's class is not supported
     * @exception   ServiceException TOO_MANY_EVENT_LISTENERS
     *              if an attempt is made to register more than one
     *              listener for a unicast event.
     * @exception   ServiceException BAD_PARAMETER
     *              If the listener is null
     */
    public void objAddEventListener(
        String feature,
        EventListener listener
    ) throws ServiceException {
        verifyListenerArguments(feature,listener);
        if(feature == null){
            this.listeners.put(listener, null);
        } else {
            Set features = (Set)this.listeners.get(listener);
            if(features == null){
                if (this.listeners.containsKey(listener)) {
                    return;
                }
                this.listeners.put(
                    listener,
                    features = new HashSet()
                );
            }
            features.add(feature);
        }
    }

    /**
     * Remove an event listener.
     * <p>
     * It is implementation dependent whether feature name and listener
     * class are verified.
     *
     * @param feature
     *        the name of the feature that was listened on,
     *        or null if the listener is interested in all features
     * @param listener
     *        the event listener to be removed
     *
     * @exception   ServiceException BAD_MEMBER_NAME
     *              if the object has no such feature or if a non-null
     *              feature name is specified for an instance level event
     * @exception   ServiceException NOT_SUPPORTED
     *              if the listener's class is not supported
     * @exception   ServiceException BAD_PARAMETER
     *              If the listener is null
     */
    public void objRemoveEventListener(
        String feature,
        EventListener listener
    ) throws ServiceException {
        verifyListenerArguments(feature,listener);
        if(feature == null){
            this.listeners.remove(listener);
        } else {
            Set features = (Set)this.listeners.get(listener);
            if(features != null) {
                features.remove(feature);
            }
        }
    }

    /**
     * Get event listeners.
     * <p>
     * The <code>feature</code> argument is ignored for listeners registered
     * with a <code>null</code> feature argument.
     * <p>
     * It is implementation dependent whether feature name and listener
     * type are verified.
     *
     * @param feature
     *        the name of the feature that was listened on,
     *        or null for listeners interested in all features
     * @param listenerType
     *        the type of the event listeners to be returned
     *
     * @return an array of listenerType containing the matching event
     *         listeners
     *
     * @exception   ServiceException BAD_MEMBER_NAME
     *              if the object has no such feature or if a non-null
     *              feature name is specified for an instance level event
     * @exception   ServiceException BAD_PARAMETER
     *              If the listener's type is not a subtype of EventListener
     * @exception   ServiceException NOT_SUPPORTED
     *              if the listener type is not supported
     */
    public EventListener[] objGetEventListeners(
        String feature,
        Class listenerType
    ) throws ServiceException {
        verifyListenerArguments(feature,listenerType);
        List matchingListeners = new ArrayList();
        for(
                Iterator i = this.listeners.entrySet().iterator();
                i.hasNext();
        ){
            Map.Entry e = (Map.Entry) i.next();
            EventListener l = (EventListener) e.getKey();
            Set f = (Set) e.getValue();
            if(
                    listenerType.isInstance(l) && (
                            f == null || f.contains(feature)
                    )
            ) {
                matchingListeners.add(l);
            }
        }
        return (EventListener[]) matchingListeners.toArray(
            (EventListener[])Array.newInstance(
                listenerType,
                matchingListeners.size()
            )
        );
    }

    /**
     * Fire an instance callback
     *
     * @param type
     * @throws ServiceException
     */
    protected void fireInstanceCallback (
        short type
    ) throws ServiceException {
        InstanceCallbackListener[] listeners = (InstanceCallbackListener[]) objGetEventListeners(
            null,
            InstanceCallbackListener.class
        );
        if(listeners.length != 0) {
            InstanceCallbackEvent event = new InstanceCallbackEvent(
                type,
                this, 
                null
            );
            for(InstanceCallbackListener listener : listeners) {
                switch (type) {
                    case InstanceCallbackEvent.POST_LOAD :
                    case InstanceCallbackEvent.POST_RELOAD :
                        listener.postLoad(event);
                        break;
                    case InstanceCallbackEvent.PRE_CLEAR :
                        listener.preClear(event);
                        break;
                    case InstanceCallbackEvent.PRE_DELETE :
                        listener.preDelete(event);
                        break;
                    case InstanceCallbackEvent.PRE_STORE :
                        listener.preStore(event);
                        break;
                    case InstanceCallbackEvent.POST_CREATE :
                        listener.postCreate(event);
                        break;
                }
            }
        }
    }

    /**
     * Tests whether this object is dirty. Instances that have been modified,
     * deleted, or newly made persistent in the current unit of work return
     * true.
     * <p>
     * Transient instances return false.
     *
     * @return true if this instance has been modified in the current unit
     *         of work.
     * @throws ServiceException 
     */
    public boolean objIsDirty(
    ) throws ServiceException{
        if(objIsPersistent()) {
            TransactionalState_1 state = getState(true);
            return state != null && !state.dirtyFeatures(true).isEmpty();
        } else {
            //
            // transient-dirty and persistent-non-transactional not yet supported
            //
            return false;
        }
    }

    /**
     * Tests whether this object is persistent. Instances that represent
     * persistent objects in the data store return true.
     *
     * @return true if this instance is persistent.
     */
    public boolean objIsPersistent(
    ){
        return this.identity != null;
    }

    /**
     * Tests whether this object has been newly made persistent. Instances
     * that have been made persistent in the current unit of work return true.
     * <p>
     * Transient instances return false.
     *
     * @return  true if this instance was made persistent in the current unit
     *          of work.
     */
    public boolean objIsNew(
    ){
        return this.persistentContainer != null;
    }

    /**
     * Tests whether this object is hollow
     * 
     * @return <code>true</code> if the object is hollow
     */
    private boolean objIsHollow(){
        return this.persistentValues == null;
    }

    /**
     * Tests whether this object becomes transient on rollback.
     *
     * @return  true if this instance becomes transient on rollback
     */
    public boolean isTransientOnRollback(
    ){
        return objIsNew() || this.transientOnRollback;
    }

    /**
     * Tests whether this object has been deleted. Instances that have been
     * deleted in the current unit of work return true.
     * Transient instances return false.
     *
     * @return  true if this instance was deleted in the current unit of work.
     */
    public boolean objIsDeleted(
    ){
        return this.deleted;
    }

    /**
     * Tests whether this object belongs to the current unit of work.
     *
     * @return  true if this instance belongs to the current unit of work.
     */
    public boolean objIsInUnitOfWork(
    ) throws ServiceException {
        return ((UnitOfWork_1)this.manager.getUnitOfWork()).contains(this);
    }

    /**
     * Tests whether this object can't leave its hollow state
     *
     * @return  true if this instance is inaccessable
     */
    public boolean objIsInaccessable(
    ){
        return this.inaccessabilityReason != null;
    }

    /**
     * Retrieve inaccessabilityReason.
     *
     * @return Returns the inaccessabilityReason.
     */
    public ServiceException getInaccessabilityReason() {
        return this.inaccessabilityReason;
    }

    /**
     * Ensure that the object is not write protected
     *
     * @param     feature
     *            The feature to be modified
     * @exception   ServiceException    ILLEGAL_STATE
     *                  if the object is write protected
     */
    protected void objMakeDirty(
        String feature
    ) throws ServiceException {
        TransactionalState_1 state = getState(false);
        state.setPrepared(false);
        state.dirtyFeatures(false).add(feature);
        assertObjectIsAccessible();
        if(objIsPersistent()) {
            objAddToUnitOfWork();
        }
    }

    /**
     * Marks an object as dirty
     *
     * @param       feature
     *              The feature to be modified
     *
     * @exception   IllegalStateException
     *              if the object is write-protected
     */
    protected void makeCollectionDirty(
        String feature
    ){
        try{
            objMakeDirty(feature);
        } catch (ServiceException exception) {
            throw new IllegalStateException(exception.getMessage());
        }
    }


    /**
     * Ensure that the object is read enabled
     *
     * @exception   ServiceException    ILLEGAL_STATE
     *                  if the object is deleted
     */
    protected void assertObjectIsAccessible(
    ) throws ServiceException {
        if (objIsDeleted()) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.ILLEGAL_STATE,
                "The object is deleted",
                new BasicException.Parameter("identitity", this.identity)
            );
        }
        if(objIsInaccessable()){
            throw getInaccessabilityReason();
        } else if(
                objIsPersistent() && 
                !objIsNew() && 
                objIsHollow()
        ){
            load();
        }
    }

    /**
     * Ensure that the attribute is fetched.
     *
     * @param     name
     *            attribute name
     * @param     stream
     *            defines whether the value is a stream or not
     *
     * @exception   ServiceException    ILLEGAL_STATE
     *                  if the object is deleted
     */
    protected Object getPersistentAttribute(
        String name,
        boolean stream
    ) throws ServiceException {
        if(objIsPersistent() && !objIsNew()){
            assertObjectIsAccessible();
            Object attribute = this.persistentValues.get(name);
            if(
                    attribute == null &&
                    !this.persistentValues.containsKey(name)
            ) {
                MappedRecord persistentValues = this.persistentValues=Object_1.createMappedRecord(
                    this.persistentValues
                );
                this.provider.getAttribute(
                    this.identity,
                    name,
                    persistentValues,
                    this.manager
                );
                Object result = stream ? persistentValues.remove(name) : persistentValues.get(name);
                this.persistentValues = persistentValues;
                return result;
            } else {
                return attribute;
            }
        } else {
            return null;
        }
    }

    /**
     * Ensure that the argument is single valued
     *
     * @param argument
     *        the argument to be checked
     *
     * @exception   ServiceException    BAD_PARAMETER
     *                  if the argument is multi-valued
     */
    protected void assertSingleValued(
        Object argument
    ) throws ServiceException {
        if(argument instanceof Collection) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN, BasicException.Code.BAD_PARAMETER,
                "Single valued argument expected",
                new BasicException.Parameter("class", argument.getClass().getName())
            );
        }
    }


    //------------------------------------------------------------------------
    // Values
    //------------------------------------------------------------------------

    /**
     * Set an attribute's value.
     * <p>
     * This method returns a <code>BAD_PARAMETER</code> exception unless the
     * feature is single valued or a stream.
     *
     * @param       feature
     *              the attribute's name
     * @param       to
     *              the object.
     *
     * @exception   ServiceException ILLEGAL_STATE
     *              if the object is write protected.
     * @exception   ServiceException BAD_PARAMETER
     *              if the feature is multi-valued
     * @exception   ServiceException NOT_SUPPORTED
     *              if the object has no such feature
     * @exception   ServiceException
     *              if the object is not accessible
     */
    public void objSetValue(
        String _feature,
        Object to
    ) throws ServiceException {
        assertSingleValued(to);
        if(_feature.startsWith("$")) {
            String feature = _feature.substring(1);
            if(SystemAttributes.OBJECT_IDENTITY.equals(feature)) {
                this.identity = (Path)to;
            } else if(SystemAttributes.OBJECT_CLASS.equals(feature)) {
                this.transactionalValuesRecordName = (String) to;
            } else if (
                    SystemAttributes.CREATED_AT.equals(feature) ||
                    State_1_Attributes.STATE_VALID_FROM.equals(feature) ||
                    State_1_Attributes.STATE_VALID_TO.equals(feature)
            ) {
                assertSingleValued(to);
                getState(false).values(false).put(feature, to);
            } else throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.BAD_PARAMETER,
                "The given feature has no private accessor starting with '$'",
                new BasicException.Parameter("feature", feature)
            );
        } else {
            objMakeDirty(_feature);
            getState(false).values(false).put(_feature, to);
        }
    }

    /**
     * Get an attribute.
     * <p>
     * Note: This specific implementation may allow to return multivalued
     * attributes as well!
     *
     * @param       feature
     *              the feature's name
     *
     * @return      the object representing the feature;
     *              or null if the feature's value hasn't been set yet.
     *
     * @exception   ServiceException NOT_SUPPORTED
     *              if the object has no such feature
     * @exception   ServiceException
     *              if the object is not accessible
     */
    public Object objGetValue(
        String feature
    ) throws ServiceException {
        if(feature.startsWith("?")) {
            return getRawValue(feature.substring(1));
        } else if (feature.startsWith("!")) {
            return getCachedValue(feature.substring(1));
        } else if(('$' + SystemAttributes.OBJECT_CLASS).equals(feature)) {
            return this.transactionalValuesRecordName;
        } else {
            TransactionalState_1 state = getState(false);
            Object transactionalValue = state.values(false).get(feature);
            if(
                    transactionalValue == null &&
                    ! state.values(false).containsKey(feature)
            ){
                Object persistentValue = this.getPersistentAttribute(feature, false);
                if(persistentValue instanceof SparseList){
                    SparseList collection = (SparseList)persistentValue;
                    if(collection.size()>1) {
                        throw new ServiceException(
                            BasicException.Code.DEFAULT_DOMAIN,
                            BasicException.Code.BAD_SEQUENCE_LENGTH,
                            "The size of a sparse list representing a single-valued" +
                            " attribute must be less or equal 1",
                            new BasicException.Parameter("feature",feature),
                            new BasicException.Parameter("size", collection.size())
                        );
                    }
                    persistentValue = collection.get(0);
                }
                if(persistentValue!=null) {
                    transactionalValue=this.manager.marshal(
                        persistentValue
                    );
                }
                state.values(false).put(feature,transactionalValue);
            } else {
                assertSingleValued(transactionalValue);
            }
            return transactionalValue;
        }
    }

    /**
     * Get an attribute.
     * <p>
     * Note: This specific implementation may allow to return multivalued
     * attributes as well!
     *
     * @param       feature
     *              the feature's name
     *
     * @return      the object representing the feature;
     *              or null if the feature's value hasn't been set yet.
     *
     * @exception   ServiceException NOT_SUPPORTED
     *              if the object has no such feature
     * @exception   ServiceException
     *              if the object is not accessible
     */
    Object getRawValue(
        String feature
    ) throws ServiceException {
        TransactionalState_1 state = getState(true);
        if(state == null){
            return this.getPersistentAttribute(feature, false);
        } else {
            Object transactionalValue = state.values(false).get(feature);
            return transactionalValue == null && !state.values(false).containsKey(feature) ?
                this.getPersistentAttribute(feature, false) :
                    this.manager.unmarshal(transactionalValue);
        }
    }

    /**
     * Get a cached attribute.
     * <p>
     * Note: This specific implementation may allow to return multivalued
     * attributes as well!
     *
     * @param       feature
     *              the feature's name
     *
     * @return      the object representing the feature;
     *              or null if the feature's value hasn't been cached.
     */
    private Object getCachedValue(
        String feature
    ){
        return objIsHollow() ? null : this.persistentValues.get(feature);
    }

    /**
     * Get a List attribute.
     * <p>
     * This method never returns <code>null</code> as an instance of the
     * requested class is created on demand if it hasn't been set yet.
     *
     * @param       feature
     *              The feature's name.
     *
     * @return      a collection which may be empty but never null.
     *
     * @exception   ServiceException ILLEGAL_STATE
     *              if the object is deleted
     * @exception   ServiceException NOT_SUPPORTED
     *              if the object has no such feature
     * @exception   ClassCastException
     *              if the feature's value is not a list
     */
    public List objGetList(
        String feature
    ) throws ServiceException {
        List values = (List)getState(false).values(false).get(feature);
        if(values==null) {
            getState(false).values(false).put(
                feature,
                values = new ManagedList(
                    feature,
                    (Collection)getPersistentAttribute(feature, false)
                )
            );
        }
        return values;
    }

    /**
     * Get a Set attribute.
     * <p>
     * This method never returns <code>null</code> as an instance of the
     * requested class is created on demand if it hasn't been set yet.
     *
     * @param       feature
     *              The feature's name.
     *
     * @return      a collection which may be empty but never null.
     *
     * @exception   ServiceException ILLEGAL_STATE
     *              if the object is deleted
     * @exception   ServiceException NOT_SUPPORTED
     *              if the object has no such feature
     * @exception   ClassCastException
     *              if the feature's value is not a set
     */
    public Set objGetSet(
        String feature
    ) throws ServiceException {
        TransactionalState_1 state = getState(false);
        Set values = (Set)state.values(false).get(feature);
        if(values==null) {
            state.values(false).put(
                feature,
                values = new ManagedSet(
                    feature,
                    (Collection)getPersistentAttribute(feature, false)
                )
            );
        }
        return values;
    }

    /**
     * Get a SparseArray attribute.
     * <p>
     * This method never returns <code>null</code> as an instance of the
     * requested class is created on demand if it hasn't been set yet.
     *
     * @param       feature
     *              The feature's name.
     *
     * @return      a collection which may be empty but never null.
     *
     * @exception   ServiceException ILLEGAL_STATE
     *              if the object is deleted
     * @exception   ClassCastException
     *              if the feature's value is not a sparse array
     * @exception   ServiceException NOT_SUPPORTED
     *              if the object has no such feature
     */
    public SortedMap objGetSparseArray(
        String feature
    ) throws ServiceException {
        TransactionalState_1 state = getState(false);
        SortedMap values=(SortedMap)state.values(false).get(feature);
        if(values==null) {
            state.values(false).put(
                feature,
                values = new ManagedSparseArray(
                    feature,
                    getPersistentAttribute(feature, false)
                )
            );
        }
        return values;
    }

    /**
     * Get a large object feature
     * <p>
     * This method returns a new LargeObject.
     *
     * @param       feature
     *              The feature's name.
     *
     * @return      a large object which may be empty but never is null.
     *
     * @exception   ServiceException ILLEGAL_STATE
     *              if the object is deleted
     * @exception   ClassCastException
     *              if the feature's value is not a large object
     * @exception   ServiceException BAD_MEMBER_NAME
     *              if the object has no such feature
     */
    public LargeObject_1_0 objGetLargeObject(
        String feature
    ) throws ServiceException {
        assertObjectIsAccessible();
        TransactionalState_1 state = getState(false);
        LargeObject_1_0 values=(LargeObject_1_0)state.values(false).get(feature);
        if(values==null) {
            state.values(false).put(
                feature,
                values = new ManagedLargeObject(feature)
            );
        }
        return values;
    }

    /**
     * Get a reference feature.
     * <p>
     * This method never returns <code>null</code> as an instance of the
     * requested class is created on demand if it hasn't been set yet.
     *
     * @param       feature
     *              The feature's name.
     *
     * @return      a collection which may be empty but never null.
     *
     * @exception   ServiceException ILLEGAL_STATE
     *              if the object is deleted
     * @exception   ClassCastException
     *              if the feature is not a reference
     * @exception   ServiceException NOT_SUPPORTED
     *              if the object has no such feature
     */
    public FilterableMap objGetContainer(
        String feature
    ) throws ServiceException{
        TransactionalState_1 state = getState(false);
        FilterableMap values = (FilterableMap)state.values(false).get(feature);
        if(values==null) {
            state.values(false).put(
                feature,
                values = new DelegatingContainer(
                    objIsPersistent() ? (Container)(
                            objIsInaccessable() || objIsNew() ? new PersistentContainer_1(
                                this.identity.getChild(feature),
                                this.persistentContainer.getManager(),
                                this.persistentContainer.getProvider(),
                                new EvictablePersistentObjects(feature),
                                this
                            ) : new PersistentContainer_1(
                                this.identity.getChild(feature),
                                this.manager,
                                this.provider,
                                new EvictablePersistentObjects(feature),
                                this
                            )
                    ) : (Container)new TransientContainer_1()
                )
            );
        }
        return values;
    }


    //------------------------------------------------------------------------
    // Operations
    //------------------------------------------------------------------------

    /**
     * Invokes an operation asynchronously.
     *
     * @param       operation
     *              The operation name
     * @param       arguments
     *              The operation's arguments object.
     *
     * @return      a structure with the result's values if the manager is
     *              going to populate it after the unit of work has committed
     *              or null if the operation's return value(s) will never be
     *              available to the manager.
     *
     * @exception   ServiceException ILLEGAL_STATE
     *              if no unit of work is in progress
     * @exception   ServiceException NOT_SUPPORTED
     *              if either asynchronous calls are not supported by the
     *              manager or the requested operation is not supportd by the
     *              object.
     * @exception   ServiceException
     *              if the invocation fails for another reason
     */
    public Structure_1_0 objInvokeOperationInUnitOfWork(
        String operation,
        Structure_1_0 arguments
    ) throws ServiceException {
        Operation entry = new Operation(
            operation,
            arguments
        );
        objAddToUnitOfWork();
        getState(false).operations(false).offer(entry);
        return entry;
    }

    /**
     * Invokes an operation synchronously.
     * <p>
     * Only query operations can be invoked synchronously unless the unit of
     * work is non-optimistic or committing.
     *
     * @param       operation
     *              The operation name
     * @param       arguments
     *              The operation's arguments object.
     *
     * @return      the operation's return object
     *
     * @exception   ServiceException ILLEGAL_STATE
     *              if a non-query operation is called in an inappropriate
     *              state of the unit of work.
     * @exception   ServiceException NOT_SUPPORTED
     *              if either synchronous calls are not supported by the
     *              manager or the requested operation is not supportd by the
     *              object.
     * @exception   ServiceException
     *              if a checked exception is thrown by the implementation or
     *              the invocation fails for another reason.
     */
    public Structure_1_0 objInvokeOperation(
        String operation,
        Structure_1_0 arguments
    ) throws ServiceException {
        Operation entry = new Operation(
            operation,
            arguments
        );
        entry.invoke();
        return entry;
    }


    //------------------------------------------------------------------------
    // Implements Object
    //------------------------------------------------------------------------

    /**
     *
     */
    String stateToString(
    ){
        try {
            Set state = new HashSet();
            if(objIsDeleted()) {
                state.add("deleted");
            }
            if(objIsDirty()) {
                state.add("dirty");
            }
            if(objIsInUnitOfWork()) {
                state.add("inUnitOfWork");
            }
            if(objIsNew()) {
                state.add("new");
            }
            if(objIsPersistent()) {
                state.add("persistent");
            }
            return state.toString();
        } catch (Exception exception) {
            return "(" + exception.getMessage() + ")";
        }
    }

    Object noContent (
        Object source
    ){
        if(source instanceof Object_1){
            Object_1 object = (Object_1) source;
            return AbstractObject_1.toString(
                object, 
                object.transactionalValuesRecordName, 
                null
            );
        } else {
            return source;
        }
    }

    /**
     *
     */
    public String toString(
    ){
        if(this.objIsInaccessable()) {
            return AbstractObject_1.toString(this, null);
        } else {
            try {
                Map content = new HashMap();
                TransactionalState_1 state = getState(true);
                if(state != null) {
                    for(Map.Entry<String,Object> e : state.values(false).entrySet()) {
                        Object v = e.getValue();
                        if(v instanceof Collection) {
                            if (v instanceof List) {
                                List t = new ArrayList();
                                for(
                                        Iterator j = ((List)v).iterator();
                                        j.hasNext();
                                ) {
                                    t.add(
                                        noContent(j.next())
                                    );
                                }
                            } else if (v instanceof Set) {
                                Set t = new HashSet();
                                for(
                                        Iterator j = ((Set)v).iterator();
                                        j.hasNext();
                                ) {
                                    t.add(
                                        noContent(j.next())
                                    );
                                }
                            } // else ignore
                        } else if (v instanceof SortedMap) {
                            SortedMap t = new TreeMap();
                            for(
                                    Iterator j = ((SortedMap)v).entrySet().iterator();
                                    j.hasNext();
                            ){
                                Map.Entry k = (Map.Entry)j.next();
                                t.put(
                                    k.getKey(),
                                    noContent(k.getValue())
                                );
                            }
                        } else {
                            content.put(e.getKey(), noContent(v));
                        }
                    }
                }
                return AbstractObject_1.toString(
                    this, 
                    this.transactionalValuesRecordName, 
                    null
                ) + ", attributes=" + IndentingFormatter.toString(
                    content
                );
            } catch (Exception exception) {
                return AbstractObject_1.toString(
                    this, 
                    this.transactionalValuesRecordName, 
                    exception.getMessage()
                );
            }
        }
    }


    //------------------------------------------------------------------------
    // Class Methods
    //------------------------------------------------------------------------

    static MappedRecord createMappedRecord(
        String name
    )throws ServiceException{
        try{
            return Records.getRecordFactory().createMappedRecord(name);
        }catch(ResourceException exception){
            throw new ServiceException(exception);
        }
    }

    static MappedRecord createMappedRecord(
        MappedRecord that
    )throws ServiceException{
        try{
            return Records.getRecordFactory().createMappedRecord(
                that.getRecordName(),
                that.getRecordShortDescription(),
                that
            );
        } catch(ResourceException exception){
            throw new ServiceException(exception);
        }
    }

    private BasicException.Parameter[] getExceptionParameters(
    ){
        try {
            return new BasicException.Parameter[]{
                new BasicException.Parameter("path",objGetPath()), //...
                new BasicException.Parameter("class",objGetClass()),
                new BasicException.Parameter("dirty",objIsDirty()),
                new BasicException.Parameter("persistent",objIsPersistent()),
                new BasicException.Parameter("new",objIsNew()),
                new BasicException.Parameter("deleted",objIsDeleted())
            };
        } catch (ServiceException e) {
            return new BasicException.Parameter[]{
                new BasicException.Parameter("state","n/a"),
                new BasicException.Parameter("exceptionDomain",e.getExceptionDomain()),
                new BasicException.Parameter("exceptionCode",e.getExceptionCode()),
                new BasicException.Parameter("exceptionMessage",e.getMessage())
            };
        }
    }


    //------------------------------------------------------------------------
    // Class Members
    //------------------------------------------------------------------------

    /**
     * Implements <code>Serializable</code>
     */
    private static final long serialVersionUID = 6246865175754052117L;


    //------------------------------------------------------------------------
    // Instance Members
    //------------------------------------------------------------------------

    /**
     * The object's identity
     *
     * @serial
     */
    protected Path identity;

    /**
     * @serial
     */
    protected Provider_1_0 provider;

    /**
     * @serial
     */
    protected Connection_1_3 manager;

    /**
     * @serial
     */
    private String transactionalValuesRecordName;

    /**
     * @serial
     */
    private TransientContainer_1 transientContainer = null;

    /**
     * @serial
     */
    private PersistentContainer_1 persistentContainer = null;

    /**
     * @serial
     */
    private boolean refreshAsynchronously = false;

    /**
     * @serial
     */
    private boolean deleted;

    /**
     *
     */
    protected transient MappedRecord persistentValues = null;

    /**
     * @serial
     */
    protected byte[] digest = null;

    /**
     * @serial
     */
    private boolean transientOnRollback = false;

    /**
     * Such an object cant't leave its hollow state
     */
    private transient ServiceException inaccessabilityReason = null;

    /**
     *
     */
    private transient Map listeners = new WeakHashMap();

    /**
     * To generate default qualifiers if no sequences are used.
     */
    private transient UUIDGenerator uuidGenerator = null;


    //------------------------------------------------------------------------
    // Implements Serializable
    //------------------------------------------------------------------------

    /**
     * Save the data of the <tt>Object_1_0</tt> instance to a stream (that
     * is, serialize it).
     *
     * @serialData The objects data
     */
    private synchronized void writeObject(
        java.io.ObjectOutputStream stream
    ) throws java.io.IOException {
        try {
            TransactionalState_1 state = getState(true);
            stream.defaultWriteObject();
            if(state == null) {
                stream.writeInt(0);
            } else {
                Set<String> features = state.dirtyFeatures(true);
                Map<String,Object> source = state.values(false);
                stream.writeInt(features.size());
                for(String feature : features) {
                    stream.writeObject(feature);
                    stream.writeObject(source.get(feature));
                }
            }
        } catch (ServiceException exception) {
            throw new ExtendedIOException(exception);
        }
    }

    /**
     * Reconstitute the <tt>Object_1_0</tt> instance from a stream (that is,
     * deserialize it).
     */
    private synchronized void readObject(
        java.io.ObjectInputStream stream
    ) throws java.io.IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.listeners = new WeakHashMap();
        int count = stream.readInt();
        if(count > 0) {
            TransactionalState_1 state;
            try {
                state = getState(false);
            } catch (ServiceException exception) {
                throw new ExtendedIOException(exception);
            }
            Set<String> features = state.dirtyFeatures(false);
            Map<String,Object> target = state.values(false);
            for(int i = 0; i < count; i++) {
                String feature = (String) stream.readObject();
                features.add(feature);
                target.put(feature, stream.readObject());
            }
        }
        this.manager.cache(this.identity, this);
    }


    //------------------------------------------------------------------------
    // Class EvictablePersistentObjects
    //------------------------------------------------------------------------

    /**
     * Evictable Persistent Objects
     */
    private class EvictablePersistentObjects
        extends MarshallingSequentialList
        implements Evictable, Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = 3257285812084159024L;

        /**
         * Constructor 
         *
         * @param feature
         */
        EvictablePersistentObjects(
            String feature
        ){
            super(manager, null);
            this.feature = feature;
        }

        /* (non-Javadoc)
         */
        protected List getDelegate() {
            if(super.list == null) {
                try {
                    if(objIsNew()){
                        super.list = Collections.EMPTY_LIST;
                    } else {
                        if(persistentValues != null) {
                            super.list = (List)persistentValues.get(feature);
                        }
                        if(super.list == null) {
                            super.list = provider.find(
                                identity.getChild(feature),
                                null,
                                null,
                                manager
                            );
                        }
                    }
                } catch (ServiceException e){
                    throw new RuntimeServiceException(e);
                }
            }
            return super.list;
        }

        /**
         *
         */
        private String feature;

        /* (non-Javadoc)
         */
        public void evict() {
            super.list = null;
        }

        /* (non-Javadoc)
         * @see java.util.Collection#size()
         */
        public int size() {
            return super.list == null ? Integer.MAX_VALUE : super.size();
        }

    }


    //------------------------------------------------------------------------
    // Class ManagedLargeObject
    //------------------------------------------------------------------------

    /**
     *
     * @author hburger
     *
     * To change the template for this generated type comment go to
     * Window>Preferences>Java>Code Generation>Code and Comments
     */
    final class ManagedLargeObject
        implements LargeObject_1_0, Evictable
    {

        /**
         *
         */
        public ManagedLargeObject(
            String feature
        ){
            super();
            this.feature = feature;
        }

        /**
         *
         */
        private final String feature;

        /**
         *
         */
        private static final long UNKNOWN = -1L;

        /**
         *
         */
        private static final long UNINITIALIZED = -1L;

        /**
         *
         */
        long transientLength = UNINITIALIZED;

        /**
         *
         */
        private Object persistentSource = null;

        /**
         *
         */
        Object transientStream = null;

        /**
         * Get the stream object and save the source
         *
         * @return the stream object
         *
         * @throws ServiceException
         */
        private Object getStream(
        ) throws ServiceException {
            Object source = getPersistentAttribute(
                this.feature,
                true
            );
            if(source instanceof List){
                List values = (List)source;
                this.persistentSource = values.isEmpty() ? null : values.get(0);
            } else {
                this.persistentSource = source;
            }
            return this.persistentSource;
        }

        /* (non-Javadoc)
         */
        public long length() throws ServiceException {
            if(this.persistentSource == null) {
                getStream();
            }
            if(this.persistentSource instanceof Source_1_0) {
                try {
                    this.persistentSource = new Long(
                        ((Source_1_0)this.persistentSource).length()
                    );
                } catch (IOException exception) {
                    throw new ServiceException(exception);
                }
            } else if (this.persistentSource instanceof List) {
                List source = (List) this.persistentSource;
                this.persistentSource = source.isEmpty() ? null : source.get(0);
            }
            return this.persistentSource instanceof Long ?
                ((Long)this.persistentSource).longValue() :
                    UNKNOWN;
        }

        /**
         * Marks the instance as dirty
         *
         * @exception   IllegalStateException
         *              if the object is not write enabled
         */
        private void makeDirty(
        ){
            makeCollectionDirty(this.feature);
        }

        /* (non-Javadoc)
         */
        public byte[] getBytes(long position, int capacity) throws ServiceException {
            byte[] buffer = new byte[capacity];
            InputStream stream = getBinaryStream();
            try {
                stream.skip(position);
                int length = stream.read(buffer, 0, capacity);
                if(length < capacity) {
                    byte[] result = new byte[length];
                    System.arraycopy(buffer, 0, result, 0, length);
                    return result;
                } else {
                    return buffer;
                }
            } catch (IOException e) {
                throw new ServiceException(e);
            }
        }

        /* (non-Javadoc)
         */
        public InputStream getBinaryStream() throws ServiceException {
            Object stream = getStream();
            return stream instanceof BinaryHolder ? 
                ((BinaryHolder)stream).toStream() :
                    (InputStream)stream;
        }

        /* (non-Javadoc)
         */
        public void getBinaryStream(OutputStream stream, long position) throws ServiceException {
            this.persistentSource = objInvokeOperation(
                SystemOperations.GET_BINARY_STREAM,
                manager.createStructure(
                    SystemOperations.GET_BINARY_STREAM_ARGUMENTS,
                    GET_STREAM_ARGUMENTS,
                    new Object[]{
                        this.feature,
                        Long.valueOf(position),
                        stream
                    }
                )
            ).objGetValue(SystemOperations.GET_STREAM_LENGTH);
        }

        /* (non-Javadoc)
         */
        public char[] getCharacters(long position, int capacity) throws ServiceException {
            char[] buffer = new char[capacity];
            Reader stream = getCharacterStream();
            try {
                stream.skip(position);
                int length = stream.read(buffer, 0, capacity);
                if(length < capacity) {
                    char[] result = new char[length];
                    System.arraycopy(buffer, 0, result, 0, length);
                    return result;
                } else {
                    return buffer;
                }
            } catch (IOException e) {
                throw new ServiceException(e);
            }
        }

        /* (non-Javadoc)
         */
        public Reader getCharacterStream() throws ServiceException {
            Object stream = getStream();
            return stream instanceof CharacterHolder ? 
                ((CharacterHolder)stream).toStream() : 
                    (Reader)stream;
        }

        /* (non-Javadoc)
         */
        public void getCharacterStream(Writer writer, long position) throws ServiceException {
            this.persistentSource = objInvokeOperation(
                SystemOperations.GET_CHARACTER_STREAM,
                manager.createStructure(
                    SystemOperations.GET_CHARACTER_STREAM_ARGUMENTS,
                    GET_STREAM_ARGUMENTS,
                    new Object[]{
                        this.feature,
                        new Long(position),
                        writer
                    }
                )
            ).objGetValue(SystemOperations.GET_STREAM_LENGTH);
        }

        /* (non-Javadoc)
         */
        public void truncate(long length) throws ServiceException {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.NOT_SUPPORTED,
                null,
                "Partial update not supported"
            );
        }

        /* (non-Javadoc)
         */
        public void setBytes(long position, byte[] content) throws ServiceException {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.NOT_SUPPORTED,
                null,
                "Partial update not supported"
            );
        }

        /* (non-Javadoc)
         */
        public void setBinaryStream(InputStream stream, long size) throws ServiceException {
            makeDirty();
            this.transientLength = size;
            this.transientStream = stream;
        }

        /* (non-Javadoc)
         */
        public OutputStream setBinaryStream(long position) throws ServiceException {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.NOT_SUPPORTED,
                null,
                "Output streams not supported"
            );
        }

        /* (non-Javadoc)
         */
        public void setCharacters(long position, char[] content) throws ServiceException {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.NOT_SUPPORTED,
                null,
                "Partial update not supported"
            );
        }

        /* (non-Javadoc)
         */
        public void setCharacterStream(Reader stream, long size) throws ServiceException {
            makeDirty();
            this.transientLength = size;
            this.transientStream = stream;
        }

        /* (non-Javadoc)
         */
        public Writer setCharacterStream(long position) throws ServiceException {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.NOT_SUPPORTED,
                null,
                "Writer not supported"
            );
        }

        /* (non-Javadoc)
         */
        public void evict(
        ){
            this.persistentSource = null;
            this.transientLength = UNKNOWN;
            this.transientStream = null;
        }

    }

    //--------------------------------------------------------------------------
    // Class ManagedList
    //--------------------------------------------------------------------------

    final class ManagedList
    extends AbstractList
    implements Evictable
    {

        /**
         * Creates a managed list.
         *
         * @param feature
         *        The feature name
         * @param   source
         *            The persistent collection;
         *        or null for transient objects.
         */
        ManagedList(
            String feature,
            Collection source
        ) throws ServiceException{
            this.feature = feature;
            refresh(source);
        }

        private List collection;
        boolean fetched = true;
        private final String feature;

        /**
         * Propagates the contents of the persistent collection to the
         * working collection replacing paths by the corresponding Object_1_0
         * instances.
         */
        void refresh(
            Collection source
        ) throws ServiceException{
            this.collection = new ArrayList(source == null ? 0 : source.size());
            if(source != null) {
                for(Object i : source) {
                    this.collection.add(
                        Object_1.this.manager.marshal(i)
                    );
                }
            }
        }

        /**
         * Marks the instance as dirty
         *
         * @exception   IllegalStateException
         *              if the object is not write enabled
         */
        private void makeDirty(
        ){
            makeCollectionDirty(this.feature);
            assertCollectionIsAccessible();
        }

        private void assertCollectionIsAccessible(
        ){
            if(!this.fetched){
                try {
                    refresh((Collection)getPersistentAttribute(this.feature, false));
                } catch (ServiceException exception) {
                    throw new RuntimeServiceException(exception);
                }
                this.fetched = true;
            }
        }

        public Object get(
            int index
        ) {
            assertCollectionIsAccessible();
            return this.collection.get(index);
        }

        public int size(
        ){
            assertCollectionIsAccessible();
            return this.collection.size();
        }

        public Object set(
            int index,
            Object element
        ){
            makeDirty();
            return this.collection.set(index, element);
        }

        public void add(
            int index,
            Object element
        ){
            makeDirty();
            this.collection.add(index, element);
        }

        public Object remove(
            int index
        ){
            makeDirty();
            return this.collection.remove(index);
        }

        /* (non-Javadoc)
         */
        public void evict(
        ){
            this.fetched = false;
        }

    }


    //------------------------------------------------------------------------
    // Class ManagedSet
    //------------------------------------------------------------------------

    final class ManagedSet
    extends AbstractSet
    implements Evictable
    {

        /**
         * Creates a managed list.
         *
         * @param feature
         *        The feature name
         * @param   source
         *            The persistent collection;
         *        or null for transient objects.
         */
        ManagedSet(
            String feature,
            Collection source
        ) throws ServiceException{
            this.feature = feature;
            refresh(source);
        }

        protected Set collection;
        boolean fetched = true;
        private final String feature;

        /**
         * Propagates the contents of the persistent collection to the
         * working collection replacing paths by the corresponding Object_1_0
         * instances.
         */
        void refresh(
            Collection source
        ) throws ServiceException{
            this.collection = new HashSet(source == null ? 0 : source.size());
            if(source != null){
                for(
                        Iterator i = source instanceof SparseList ?
                            ((SparseList)source).populationIterator() :
                                source.iterator();
                            i.hasNext();
                ) {
                    this.collection.add(
                        Object_1.this.manager.marshal(i.next())
                    );
                }
            }
        }

        /**
         * Marks the instance as dirty
         *
         * @exception   IllegalStateException
         *              if the object is not write enabled
         */
        protected void makeDirty(
        ){
            makeCollectionDirty(this.feature);
            assertCollectionIsAccessible();
        }

        private void assertCollectionIsAccessible(
        ){
            if(!this.fetched){
                try {
                    refresh((Collection)getPersistentAttribute(this.feature, false));
                } catch (ServiceException exception) {
                    throw new RuntimeServiceException(exception);
                }
                this.fetched = true;
            }
        }

        public int size(
        ){
            assertCollectionIsAccessible();
            return this.collection.size();
        }

        public Iterator iterator(
        ){
            assertCollectionIsAccessible();
            return new ManagedIterator();
        }

        public boolean add(
            Object element
        ){
            makeDirty();
            return this.collection.add(element);
        }

        /* (non-Javadoc)
         */
        public void evict(
        ){
            this.fetched = false;
        }

        class ManagedIterator implements Iterator {

            public boolean hasNext(
            ){
                return this.iterator.hasNext();
            }

            public Object next(
            ){
                return this.iterator.next();
            }

            public void remove(
            ){
                makeDirty();
                this.iterator.remove();
            }

            private final Iterator iterator =
                ManagedSet.this.collection.iterator();

        }

    }


    //------------------------------------------------------------------------
    // Class ManagedSparseArray
    //------------------------------------------------------------------------

    final class ManagedSparseArray
    extends TreeMap
    implements SortedMap, Evictable
    {

        /**
         *
         */
        private static final long serialVersionUID = 3258412828682762292L;

        /**
         * Creates a managed list.
         *
         * @param feature
         *        The feature name
         * @param   source
         *            The persistent collection;
         *        or null for transient objects.
         */
        ManagedSparseArray(
            String feature,
            Object source
        ) throws ServiceException{
            this.feature = feature;
            refresh(source);
        }

        boolean fetched = true;
        private final String feature;

        /**
         * Propagates the contents of the persistent collection to the
         * working collection replacing paths by the corresponding Object_1_0
         * instances.
         */
        void refresh(
            Object source
        ) throws ServiceException{
            super.clear();
            if(source != null){
                if(source instanceof SparseList){
                    for(
                            ListIterator i = ((SparseList)source).populationIterator();
                            i.hasNext();
                    ) {
                        super.put(
                            new Integer(i.nextIndex()),
                            Object_1.this.manager.marshal(i.next())
                        );
                    }
                } else {
                    for(Map.Entry<?,?> j : ((SortedMap<?,?>)source).entrySet()) {
                        super.put(
                            j.getKey(),
                            Object_1.this.manager.marshal(j.getValue())
                        );
                    }
                }
            }
        }

        /**
         * Marks the instance as dirty
         *
         * @exception   IllegalStateException
         *              if the object is not write enabled
         */
        protected void makeDirty(
        ){
            makeCollectionDirty(this.feature);
            assertCollectionIsAccessible();
        }

        private void assertCollectionIsAccessible(
        ){
            if(!this.fetched){
                try {
                    refresh(getPersistentAttribute(this.feature, false));
                } catch (ServiceException exception) {
                    throw new RuntimeServiceException(exception);
                }
                this.fetched = true;
            }
        }

        public int size(
        ){
            assertCollectionIsAccessible();
            return super.size();
        }

        /* (non-Javadoc)
         */
        public void evict(
        ){
            super.clear();
            this.fetched = false;
        }

        /* (non-Javadoc)
         * @see java.util.Map#clear()
         */
        public void clear() {
            assertCollectionIsAccessible();
            super.clear();
        }

        /* (non-Javadoc)
         * @see java.util.Map#put(java.lang.Object, java.lang.Object)
         */
        public Object put(Object key, Object value) {
            assertCollectionIsAccessible();
            return super.put(key, value);
        }

        /* (non-Javadoc)
         * @see java.util.Map#remove(java.lang.Object)
         */
        public Object remove(Object key) {
            assertCollectionIsAccessible();
            return super.remove(key);
        }

    }

    //------------------------------------------------------------------------
    // Class Operation
    //------------------------------------------------------------------------

    final class Operation extends MarshallingStructure_1 {

        /**
         *
         */
        private static final long serialVersionUID = 3257849887319143218L;
        Operation(
            String operation,
            Structure_1_0 arguments
        ) throws ServiceException {
            super(null, manager);
            this.operation = operation;
            this.arguments=Object_1.createMappedRecord(
                arguments.objGetType()
            );
            for(String fieldName : arguments.objFieldNames()){
                Object source = arguments.objGetValue(fieldName);
                if(source instanceof Collection){
                    if(source instanceof List){
                        List collection = (List)source;
                        List target = new ArrayList();
                        for(Object j : collection){
                            target.add(
                                super.marshaller.unmarshal(j)
                            );
                        }
                        this.arguments.put(fieldName, target);
                    }else if(source instanceof SortedMap){
                        SortedMap target = new TreeMap();
                        for(Map.Entry<?,?> k : ((SortedMap<?,?>)source).entrySet()) {
                            target.put(
                                k.getKey(),
                                super.marshaller.unmarshal(k.getValue())
                            );
                        }
                        this.arguments.put(fieldName, target);
                    }else if(source instanceof Set){
                        Set target = new HashSet();
                        for(Object j : (Set)source) {
                            target.add(
                                super.marshaller.unmarshal(j)
                            );
                        }
                        this.arguments.put(fieldName, target);
                    }else{
                        throw new ServiceException(
                            BasicException.Code.DEFAULT_DOMAIN, BasicException.Code.ASSERTION_FAILURE,
                            "Structure_1_0 field has an invalid Collection class",
                            new BasicException.Parameter("class",source.getClass().getName())
                        );
                    }
                }else{
                    this.arguments.put(fieldName, super.marshaller.unmarshal(source));
                }
            }
        }

        void invoke(
        ) throws ServiceException {
            try {
                super.setDelegate(
                    new MapStructure_1(
                        provider.invokeOperation(
                            identity,
                            this.operation,
                            this.arguments
                        )
                    )
                );
            } catch(ServiceException e) {
                throw e;
            } catch (Exception exception) {
                throw new ServiceException(
                    exception,
                    exception instanceof BasicException.Wrapper ? ((BasicException.Wrapper)exception).getExceptionDomain() : BasicException.Code.DEFAULT_DOMAIN,
                    exception instanceof BasicException.Wrapper ? ((BasicException.Wrapper)exception).getExceptionCode() : BasicException.Code.GENERIC,
                    "Operation failed",
                    new BasicException.Parameter("object", identity),
                    new BasicException.Parameter("operation",this.operation),
                    new BasicException.Parameter("arguments",this.arguments)
                );
            }
        }

        final String operation;
        final MappedRecord arguments;

    }

}

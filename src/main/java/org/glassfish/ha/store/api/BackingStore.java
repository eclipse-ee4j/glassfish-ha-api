/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation. All rights reserved.
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.ha.store.api;

import org.glassfish.ha.store.spi.*;

import java.io.*;

/**
 * An object that stores a given value against an id. This class defines the
 * set of operations that a container could perform on a store.
 * <p/>
 * <p/>
 * An instance of BackingStore is created by calling
 * <code>BackingStoreFactory.createBackingStore()</code> method.
 * <p/>
 * <p/>
 * The BackingStore instance is created and used for storing data that belongs
 * to a single application or container.
 * <p/>
 * <p/>
 * The store implementation must be thread safe.
 * <p/>
 * <p/>
 *
 * @author Mahesh.Kannan@Sun.Com
 * @author Larry.White@Sun.Com
 */
public abstract class BackingStore<K extends Serializable, V extends Serializable> {

    BackingStoreConfiguration<K, V> conf;

    protected void initialize(BackingStoreConfiguration<K, V> conf)
        throws BackingStoreException {
        this.conf = conf;
    }

    protected BackingStoreConfiguration<K, V> getBackingStoreConfiguration() {
        return conf;
    }


    public abstract BackingStoreFactory getBackingStoreFactory();

    /**
     * Load and return the data for the given id. The store is expected to
     * return the largest ever version that was saved in the stored using the
     * <code>save()</code> method.
     *
     * @param key the key whose value must be returned
     * @return the value if this store contains it or null. The implementation
     *         must return the exact same type as that was passed to it in the
     *         save method.
     * @throws NullPointerException  if the id is null
     * @throws BackingStoreException if the underlying store implementation encounters any
     *                               exception
     */
    public abstract V load(K key, String version) throws BackingStoreException;

    /**
     * Save the value whose key is id. The store is NOT expected to throw an exception if
     * isNew is false but the entry doesn't exist in the store. (This is possible in
     * some implementations (like in-memory) where packets could be lost.)
     *
     * @param key   the id
     * @param value The Metadata to be stored
     * @throws BackingStoreException if the underlying store implementation encounters any
     *                               exception
     * @pram isNew
     * A flag indicating if the entry is new or not.
     * @return A (possibly null) String indicating the instance name where the data was saved.
     */
    public abstract String save(K key, V value, boolean isNew) throws BackingStoreException;

    /**
     * Remove the association for the id.
     * <p/>
     * After this call, any call to <code>load(id)</code> <b>must</b> return
     * null. In addition, any association between <code>id</code> and
     * container extra params must also be removed.
     *
     * @param key the id of the Metadata
     * @throws BackingStoreException if the underlying store implementation encounters any
     *                               exception
     */
    public abstract void remove(K key) throws BackingStoreException;

    /** TODO: BEGIN: REMOVE after shoal integration **/
    public void updateTimestamp(K key, long time) throws BackingStoreException {}
    public int removeExpired(long idleForMillis)
             throws BackingStoreException {return 0;}
    /** TODO: END: REMOVE AFTER SHOAL INTEGRATION **/

    /**
     * Recomended way is to just do a save(k, v)
     * @param key
     * @param version
     * @param accessTime
     * @throws BackingStoreException
     */
    public String updateTimestamp(K key, String version, Long accessTime)
            throws BackingStoreException {return "";}

    /**
     * Remove expired entries
     */
    public int removeExpired()
             throws BackingStoreException {
        return 0;
    }
    
    /**
     * Get the current size of the store
     *
     * @return the (approximate) number of entries in the store
     * @throws BackingStoreException if the underlying store implementation encounters any
     *                               exception
     */
    public abstract int size() throws BackingStoreException;

    /**
     * Typically called during shutdown of the process. The store must not be used after this call
     *
     * @throws BackingStoreException
     */
    public void close()
        throws BackingStoreException {

    }

    /**
     * Called when the store is no longer needed. Must clean up and close any
     * opened resources. The store must not be used after this call.
     */
    public void destroy()
            throws BackingStoreException {
        
    }



    protected ObjectOutputStream createObjectOutputStream(OutputStream os)
        throws IOException {
        ObjectInputOutputStreamFactory oosf = ObjectInputOutputStreamFactoryRegistry.getObjectInputOutputStreamFactory();
        return (oosf == null) ? new ObjectOutputStream(os) : oosf.createObjectOutputStream(os);
    }

    protected ObjectInputStream createObjectInputStream(InputStream is)
        throws IOException {
        /*
        ObjectInputOutputStreamFactory oosf = ObjectInputOutputStreamFactoryRegistry.getObjectInputOutputStreamFactory();
        return oosf.createObjectInputStream(is, vClazz.getClassLoader());
        */

        return new ObjectInputStreamWithLoader(is, conf.getValueClazz().getClassLoader());
    }
}

/*
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

package org.glassfish.ha.api.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.glassfish.ha.store.api.*;
import org.glassfish.ha.store.impl.NoOpBackingStoreFactory;
import org.glassfish.ha.store.spi.BackingStoreFactoryRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * Unit test for simple App.
 */
public class BackingStoreFactoryRegistryTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BackingStoreFactoryRegistryTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(BackingStoreFactoryRegistryTest.class);
    }


    public void testBackingStoreFactoryRegistry() {
        boolean result = false;
        try {
            BackingStoreFactory nbsf = BackingStoreFactoryRegistry.getFactoryInstance("noop");
            result = true;
        } catch (BackingStoreException bsEx) {

        }

        assert(result);
    }

    public void testBackingStore() {
        boolean result = false;
        try {
            BackingStoreFactory nbsf = BackingStoreFactoryRegistry.getFactoryInstance("noop");
            BackingStoreConfiguration<String, NoopData> conf = null;
            BackingStore<String, NoopData> bs = nbsf.createBackingStore(conf);

            result = true;
        } catch (BackingStoreException bsEx) {

        }
    }

    public void testBackingStoreSave() {
        boolean result = false;
        try {
            BackingStoreFactory nbsf = BackingStoreFactoryRegistry.getFactoryInstance("noop");
            BackingStoreConfiguration<String, NoopData> conf = null;
            BackingStore<String, NoopData> bs = nbsf.createBackingStore(conf);

            bs.save("k1", null, true);
            bs.save("k1", new NoopData(), true);
            bs.save("k1", null, false);
            bs.save("k1", null, true);

            bs.load(null,null);
            bs.load(null, "6");
            bs.load("k1", null);
            bs.load("k1", "6");


            bs.remove(null);
            bs.remove("k1");


            bs.updateTimestamp(null, "6", -1L);
            bs.updateTimestamp("k1", "6", -1L);


            bs.removeExpired();

            result = true;
        } catch (BackingStoreException bsEx) {

        }

        assert(result);
    }

    public void testBackingStoreLoad() {
        boolean result = false;
        try {
            BackingStoreFactory nbsf = BackingStoreFactoryRegistry.getFactoryInstance("noop");
            BackingStoreConfiguration<String, NoopData> conf = null;
            BackingStore<String, NoopData> bs = nbsf.createBackingStore(conf);

            bs.load(null,null);
            bs.load(null, "6");
            bs.load("k1", null);
            bs.load("k1", "6");

            result = true;
        } catch (BackingStoreException bsEx) {

        }

        assert(result);
    }
    public void testBackingStoreRemove() {
        boolean result = false;
        try {
            BackingStoreFactory nbsf = BackingStoreFactoryRegistry.getFactoryInstance("noop");
            BackingStoreConfiguration<String, NoopData> conf = null;
            BackingStore<String, NoopData> bs = nbsf.createBackingStore(conf);

            bs.remove(null);
            bs.remove("k1");

            result = true;
        } catch (BackingStoreException bsEx) {

        }

        assert(result);
    }
    public void testBackingStoreUpdateTimestamp() {
        boolean result = false;
        try {
            BackingStoreFactory nbsf = BackingStoreFactoryRegistry.getFactoryInstance("noop");
            BackingStoreConfiguration<String, NoopData> conf = null;
            BackingStore<String, NoopData> bs = nbsf.createBackingStore(conf);

            bs.updateTimestamp(null, "3", -1L);
            bs.updateTimestamp("k1", "2", 0L);

            result = true;
        } catch (BackingStoreException bsEx) {

        }

        assert(result);
    }
    public void testBackingStoreRemoveExpired() {
        boolean result = false;
        try {
            BackingStoreFactory nbsf = BackingStoreFactoryRegistry.getFactoryInstance("noop");
            BackingStoreConfiguration<String, NoopData> conf = null;
            BackingStore<String, NoopData> bs = nbsf.createBackingStore(conf);

            bs.removeExpired();

            result = true;
        } catch (BackingStoreException bsEx) {

        }

        assert(result);
    }

    public void testBackingStoreFactoryRegistryGetRegistered() {
        boolean result = false;
        BackingStoreFactoryRegistry.register("foo", new NoOpBackingStoreFactory());
        Set<String> types = BackingStoreFactoryRegistry.getRegisteredTypes();
        if (types.size() != 2) {
            result = false;
            return;
        }
        for (String type : types) {
            System.out.println("Registered type:" + type);
        }
        BackingStoreFactoryRegistry.unregister("foo");
        types = BackingStoreFactoryRegistry.getRegisteredTypes();
        if (types.size() != 1) {
            result = false;
            return;
        }
        result = true;
    }

    private static final class NoopData
        implements Storeable {

        public long _storeable_getVersion() {
            return 0;
        }

        public void _storeable_setVersion(long version) {
        }

        public long _storeable_getLastAccessTime() {
            return 0;
        }

        public void _storeable_setLastAccessTime(long version) {
        }

        public long _storeable_getMaxIdleTime() {
            return 0;
        }

        public void _storeable_setMaxIdleTime(long version) {
        }

        public String[] _storeable_getAttributeNames() {
            return new String[0];
        }

        public boolean[] _storeable_getDirtyStatus() {
            return new boolean[0];
        }

        public void _storeable_writeState(OutputStream os) throws IOException {
        }

        public void _storeable_readState(InputStream is) throws IOException {
        }
    }


}

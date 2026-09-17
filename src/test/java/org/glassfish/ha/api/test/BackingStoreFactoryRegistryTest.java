/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.glassfish.ha.store.api.BackingStore;
import org.glassfish.ha.store.api.BackingStoreFactory;
import org.glassfish.ha.store.api.Storeable;
import org.glassfish.ha.store.impl.NoOpBackingStoreFactory;
import org.glassfish.ha.store.spi.BackingStoreFactoryRegistry;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for simple App.
 */
public class BackingStoreFactoryRegistryTest {

    @Test
    public void testBackingStoreLifecycle() throws Exception {
        BackingStoreFactory backingStoreFactory = BackingStoreFactoryRegistry.getFactoryInstance("noop");
        assertNotNull(backingStoreFactory, "backingStoreFactory");
        try (BackingStore<Serializable, Serializable> backingStore = backingStoreFactory.createBackingStore(null)) {
            assertNotNull(backingStore, "backingStore");
            backingStore.save("k1", null, true);
            backingStore.save("k1", new NoopData(), true);
            backingStore.save("k1", null, false);
            backingStore.save("k1", null, true);

            backingStore.load(null,null);
            backingStore.load(null, "6");
            backingStore.load("k1", null);
            backingStore.load("k1", "6");

            backingStore.remove(null);
            backingStore.remove("k1");

            backingStore.updateTimestamp(null, "6", -1L);
            backingStore.updateTimestamp("k1", "6", -1L);

            backingStore.removeExpired();
        }
    }

    @Test
    public void testBackingStoreUpdateTimestamp() throws Exception {
        BackingStoreFactory backingStoreFactory = BackingStoreFactoryRegistry.getFactoryInstance("noop");
        try (BackingStore<Serializable, Serializable> backingStore = backingStoreFactory.createBackingStore(null)) {
            assertNotNull(backingStore, "backingStore");
            assertNull(backingStore.updateTimestamp(null, "3", -1L));
            assertNull(backingStore.updateTimestamp("k1", "2", 0L));
        }
    }

    @Test
    public void testBackingStoreRemoveExpired() throws Exception {
        BackingStoreFactory backingStoreFactory = BackingStoreFactoryRegistry.getFactoryInstance("noop");
        try (BackingStore<Serializable, Serializable> backingStore = backingStoreFactory.createBackingStore(null)) {
            assertEquals(0, backingStore.removeExpired());
        }
    }

    @Test
    public void testBackingStoreFactoryRegistryGetRegistered() {
        assertNull(BackingStoreFactoryRegistry.register("foo", new NoOpBackingStoreFactory()));
        assertThat(BackingStoreFactoryRegistry.getRegisteredTypes(), contains("noop", "memory", "file", "foo"));
        BackingStoreFactoryRegistry.unregister("foo");
        assertThat(BackingStoreFactoryRegistry.getRegisteredTypes(), contains("noop", "memory", "file"));
    }

    private static final class NoopData implements Storeable {

        private static final long serialVersionUID = 2714437246016654909L;

        @Override
        public long _storeable_getVersion() {
            return 0;
        }

        @Override
        public void _storeable_setVersion(long version) {
        }

        @Override
        public long _storeable_getLastAccessTime() {
            return 0;
        }

        @Override
        public void _storeable_setLastAccessTime(long version) {
        }

        @Override
        public long _storeable_getMaxIdleTime() {
            return 0;
        }

        @Override
        public void _storeable_setMaxIdleTime(long version) {
        }

        @Override
        public String[] _storeable_getAttributeNames() {
            return new String[0];
        }

        @Override
        public boolean[] _storeable_getDirtyStatus() {
            return new boolean[0];
        }

        @Override
        public void _storeable_writeState(OutputStream os) throws IOException {
        }

        @Override
        public void _storeable_readState(InputStream is) throws IOException {
        }
    }


}

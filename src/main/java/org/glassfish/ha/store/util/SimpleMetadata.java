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

package org.glassfish.ha.store.util;

import org.glassfish.ha.store.annotations.Attribute;
import org.glassfish.ha.store.api.Storeable;

import java.io.*;


public class SimpleMetadata implements Storeable {

    private long version = -1;

    private long lastAccessTime;

    private long maxInactiveInterval;

    private byte[] state;

    private static final String[] attributeNames = new String[] {"state"};

    private static final boolean[] dirtyStatus = new boolean[] {true};

    //Default No arg constructor required for BackingStore

    public SimpleMetadata() {

    }

    /**
     * Construct a SimpleMetadata object
     *
     * @param version             The version of the data. A freshly created state has a version == 0
     * @param lastAccesstime      the last access time of the state. This must be used in
     *                            conjunction with getMaxInactiveInterval to determine if the
     *                            state is idle enough to be removed.
     * @param maxInactiveInterval the maximum time that this state can be idle in the store
     *                            before it can be removed.
     */
    public SimpleMetadata(long version, long lastAccesstime,
                          long maxInactiveInterval, byte[] state)

    {
        this.version = version;
        this.lastAccessTime = lastAccesstime;
        this.maxInactiveInterval = maxInactiveInterval;
        this.state = state;
    }

    /**
     * Get the verion of the state. A freshly created state has a version == 0
     *
     * @return the version.
     */
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    /**
     * Get the last access time of the state. This must be used in conjunction
     * with getMaxInactiveInterval to determine if the state is idle enough to
     * be removed.
     *
     * @return The time when the state was accessed last
     */
    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    /**
     * Get the maximum time that this state can be idle in the store before it
     * can be removed.
     *
     * @return the maximum idle time. If zero or negative, then the component
     *         has no idle timeout limit
     */
    public long getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    public void setMaxInactiveInterval(long maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public byte[] getState() {
        return this.state;
    }

    @Attribute("state")
    public void setState(byte[] state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SimpleMetadata->state");
        if (state != null) {
            for (byte b : state) {
                sb.append(b + "_");
            }
        } else {
            sb.append("null");
        }
        return "SimpleMetadata{" +
                "version=" + version +
                ", lastAccessTime=" + lastAccessTime +
                ", maxInactiveInterval=" + maxInactiveInterval +
                ", state.length=" + (state == null ? 0 : state.length) +
                ", state=" + sb.toString() +
                '}';
    }

    public long _storeable_getVersion() {
        return this.version;
    }

    public void _storeable_setVersion(long val) {
        this.version = val;
    }

    public long _storeable_getLastAccessTime() {
        return this.lastAccessTime;
    }

    public void _storeable_setLastAccessTime(long val) {
        this.lastAccessTime = val;
    }

    public long _storeable_getMaxIdleTime() {
        return this.maxInactiveInterval;
    }

    public void _storeable_setMaxIdleTime(long val) {
        this.maxInactiveInterval = val;
    }

    public String[] _storeable_getAttributeNames() {
        return attributeNames;
    }

    public boolean[] _storeable_getDirtyStatus() {
        return dirtyStatus;
    }

    public void _storeable_writeState(OutputStream os) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        try {
            oos.writeInt(state.length);
            oos.write(state);
        } finally {
            try {
                oos.close();
            } catch (Exception ex) {
            }
        }
    }

    public void _storeable_readState(InputStream is) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(is);
        try {
            int len = ois.readInt();
            state = new byte[len];
            ois.readFully(state);
        } finally {
            try {
                ois.close();
            } catch (Exception ex) {
            }
        }
    }
}

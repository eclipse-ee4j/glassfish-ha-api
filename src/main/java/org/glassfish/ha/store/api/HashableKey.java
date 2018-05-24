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

package org.glassfish.ha.store.api;

/**
 * This interface is implemented by objects that are used as Keys in BackingStore operations. The main use
 *   of this interface is to provide a <i>hint</i> to the BackingStore providers so that all keys that return
 *   the same object (actually same hashcode) from getHashKey  will be 'grouped' together. For example,
 *   for a BackingStore that uses memory replication, if two keys k1 and k2 implement this interface and return
 *   the same Object from getHashKey, then their values will be replicated to the same replication instance.
 *
 * @author Mahesh Kannan
 */
public interface HashableKey {

    public Object getHashKey();

}

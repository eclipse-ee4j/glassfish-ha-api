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

package org.glassfish.ha.store.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.glassfish.ha.store.api.BackingStore;
import org.glassfish.ha.store.spi.Storable;

import java.lang.annotation.ElementType;

/**
 * An annotation to describe a class as a SToreEntry. For each class a.b.X
 *  that is annotated with {@code @StoreEntry}, an APT will be used to generate
 *  a class a.b.X_
 *
 * For each attribute A of type T in X, a.b.X_ contains a (static) field whose
 *  type is {@code SessionAttributeMetadata<X, T>. SessionAttributeMetadata}
 *  describes the attribute by giving its (java) type, name etc.
 *
 * Also, for each a.b.X a sub-class by name a.b.X_Storable will also be generated.
 *  a.b.X_Storable will implement {@link Storable} interface. The {@link Storable} interface allows
 *  a Store implementation to detect dirty attributes. An attribute A is dirty, if
 *  its setter method (or the method that was annotated with {@code @Attribute("A")} was
 *  invoked). An instance of a.b.X {@link Storable} can be obtained by calling
 * {@link BackingStore#load(java.io.Serializable, String)}
 *
 * An annotation that can be used to declare a String Attribute
 *  as a Version. Version attribute is a special attribute of
 *  a StoreEntry.
 *
 * @author Mahesh.Kannan@Sun.Com
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StoreEntry {
    String value() default "";
}

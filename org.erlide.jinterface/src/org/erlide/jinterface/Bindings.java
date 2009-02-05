/*******************************************************************************
 * Copyright (c) 2008 Vlad Dumitrescu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution.
 *
 * Contributors:
 *     Vlad Dumitrescu
 *******************************************************************************/
package org.erlide.jinterface;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.ericsson.otp.erlang.OtpErlangObject;

public class Bindings {

	private final Map<String, OtpErlangObject> bindings;

	public Bindings() {
		this.bindings = new HashMap<String, OtpErlangObject>();
	}

	public Bindings(Bindings other) {
		this();
		merge(other);
	}

	public void merge(Bindings other) {
		this.bindings.putAll(other.bindings);
	}

	public OtpErlangObject get(String name) {
		return this.bindings.get(name);
	}

	public void put(String name, OtpErlangObject value) {
		this.bindings.put(name, value);
	}

	public Map<String, OtpErlangObject> getAll() {
		return Collections.unmodifiableMap(this.bindings);
	}

	@Override
	public String toString() {
		return bindings.toString();
	}

}

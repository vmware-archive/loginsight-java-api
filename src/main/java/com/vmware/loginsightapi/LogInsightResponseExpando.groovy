/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi

class LogInsightResponseExpando {
	private def map = [:]
	private def contentPackFields = [:]

	LogInsightResponseExpando(Map map) {
		this.map = map;
	}

	def getProperty(String name) {
		if (!map.containsKey(name)) {
			String text = map['text']
			if (contentPackFields[name] == null) {
				return "";
			}
			def list = contentPackFields[name];
			def startPosition = list[0].toInteger();
			def length = list[1].toInteger();
			return text.substring(startPosition, startPosition + length);
		} else {
			if (map[name] == null) {
				return "";
			}
			return map[name];
		}
	}

	void setProperty(String name, Object value) {
		map[name] = value;
	}

	def getAllProperties() {
		return map;
	}

	void setContentPackFields(Map map) {
		this.contentPackFields = map;
	}
}

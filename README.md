
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vmware.loginsightapi/loginsight-java-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.vmware.loginsightapi/loginsight-java-api)
[![Gitter](https://badges.gitter.im/vmware/loginsight-java-api.svg)](https://gitter.im/vmware/loginsight-java-api?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Javadocs](http://javadoc.io/badge/com.vmware.loginsightapi/loginsight-java-api.svg)](http://javadoc.io/doc/com.vmware.loginsightapi/loginsight-java-api)
[![Build Status](https://ci.vmware.run/api/badges/vmware/loginsight-java-api/status.svg)](https://ci.vmware.run/vmware/loginsight-java-api)
[![Coverage Status](https://coveralls.io/repos/github/vmware/loginsight-java-api/badge.svg?branch=master&test=true)](https://coveralls.io/github/vmware/loginsight-java-api?branch=master&test=true)
[//]: [![javadoc](https://img.shields.io/badge/view-javadoc-lightgrey.svg)](https://vmware.github.io/loginsight-java-api/javadoc/)
# loginsight-java-api

Please do not use this API in production as it is still in **beta phase**.

## Overview

LogInsight Java API provides a fluent API to interact with VMware vRealize LogInsight 
* message queries
* aggregate queries
* ingesion

Join us [@gitter](https://gitter.im/vmware/loginsight-java-api) to discuss on any issues on using this API.

## Usage

### Getting the jar
Functionality of this package is contained in Java package com.vmware.loginsightapi.
To use the package, you need to use following Maven dependency:

~~~xml
<dependency>
    <groupId>com.vmware.loginsightapi</groupId>
    <artifactId>loginsight-java-api</artifactId>
    <version>0.1.0</version>
</dependency>
~~~

In case of gradle project please use the following

~~~groovy
compile group: 'com.vmware.loginsightapi', name: 'loginsight-java-api', version: '0.1.0'
~~~

### API

####1. Connecting to LogInsight

~~~java
LogInsightClient client = new LogInsightClient("host-name", "username", "password");
~~~

####2. Ingestion of messages to LogInsight

~~~java

IngestionRequest request = new IngestionRequestBuilder()
	.message(new Message("message line 1"))
	.message(new MessageBuilder("message line 2")
	.field("field1", "content 1").build())
	.build();
CompletableFuture<IngestionResponse> responseFuture = client.ingest(request);

~~~

####3. Event Queries

~~~java
MessageQuery mqb = (MessageQuery) new MessageQuery()
	.addConstraint("field_1", FieldConstraint.Operator.EQ, "value1")
	.addContentPackField("test");
CompletableFuture<MessageQueryResponse> responseFuture = client.messageQuery(mqb.toUrlString());
~~~


####4. Aggregation Queries

Default aggregation function is COUNT as defined by LogInsight API.

~~~java
AggregateQuery aqb = (AggregateQuery) new AggregateQuery()
	.addConstraint("field_1", FieldConstraint.Operator.EQ,"value1")
	.addContentPackField("test");
CompletableFuture<AggregateQueryResponse> responseFuture = client.aggregateQuery(aqb.toUrlString());
~~~


## Build from source

### Prerequisites

* Java 8 JDK installed and set your JAVA_HOME to home of Java8 JDK
* vRealize LogInsight 3.3 onwards

 
### Build & Run

~~~bash
$ ./gradlew clean build
~~~


## Contributing

The loginsight-java-api project team welcomes contributions from the community. If you wish to contribute code and you have not
signed our contributor license agreement (CLA), our bot will update the issue when you open a Pull Request. For any
questions about the CLA process, please refer to our [FAQ](https://cla.vmware.com/faq). For more detailed information,
refer to [CONTRIBUTING.md](CONTRIBUTING.md).

## License
Copyright © 2016 VMware, Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the “License”); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

Some files may be comprised of various open source software components, each of which has its own license that is located in the source code of the respective component.

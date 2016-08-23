
[![Gitter](https://badges.gitter.im/vmware/loginsight-java-api.svg)](https://gitter.im/vmware/loginsight-java-api?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Javadocs](http://javadoc.io/badge/com.vmware.loginsightapi/loginsight-java-api.svg)](http://javadoc.io/doc/com.vmware.loginsightapi/loginsight-java-api)
[![Build Status](https://ci.vmware.run/api/badges/vmware/loginsight-java-api/status.svg)](https://ci.vmware.run/vmware/loginsight-java-api)
[![Coverage Status](https://coveralls.io/repos/github/vmware/loginsight-java-api/badge.svg?branch=master&dummy=true)](https://coveralls.io/github/vmware/loginsight-java-api?branch=master)
[//]: [![javadoc](https://img.shields.io/badge/view-javadoc-lightgrey.svg)](https://vmware.github.io/loginsight-java-api/javadoc/)
# loginsight-java-api

Please do not use this API in production as it is still in **beta phase**.

## Overview

LogInsight Java API provides a fluent API to interact with VMware vRealize LogInsight 
* message queries
* aggregate queries
* ingesion
 
You can access the **javadoc** for this libary [here](https://vmware.github.io/loginsight-java-api/javadoc/)

Join us [@gitter](https://gitter.im/vmware/loginsight-java-api) to discuss on any issues on using this API.

## Try it out

### Prerequisites

* Java 8 JDK installed and set your JAVA_HOME to home of Java8 JDK
* vRealize LogInsight 3.3 onwards
 
### Build & Run

~~~bash
$ ./gradlew build
~~~


## Documentation

### Connecting to LogInsight
~~~java
LogInsightClient client = new LogInsightClient("host-name", "username", "password");
~~~

### Ingestion of messages to LogInsight

~~~java

IngestionRequest request = new IngestionRequestBuilder()
		.withMessage(new Message("message line 1"))
                .withMessage(new MessageBuilder("message line 2").withField("field1", "content 1").build())
                .build();
IngestionResponse response = client.ingest(request);
~~~

### Message Queries

#### Synchronous Query

~~~java
List<FieldConstraint> constraints = RequestBuilders.constraint().eq("field", "value").gt("timestamp", "0").build();
String urlString = RequestBuilders.messageQuery().limit(100).setConstraints(constraints).toUrlString();
MessageQueryResponse messages = client.messageQuery(urlString);
Assert.assertTrue("Invalid number of messages", messages.getEvents().size() <= 100);
~~~

#### Asynchronous Query with callbacks

~~~java
final CountDownLatch latch = new CountDownLatch(1);
List<FieldConstraint> constraints = RequestBuilders.constraint().eq("field", "value").gt("timestamp", "0").build();
MessageQueryBuilder mqb = (MessageQueryBuilder) RequestBuilders.messageQuery().limit(100).setConstraints(constraints);
client.messageQuery(mqb.toUrlString(), (MessageQueryResponse response, LogInsightApiError error) -> {
	if (error.isError()) {
		// Handle error
	} else {
		// Handle response
	}
	latch.countDown(); //release the latch
});
try {
	latch.await(); // wait for completion of the callback
} catch (InterruptedException e1) {
	e1.printStackTrace();
}
~~~

### Aggregation Queries

Default aggregation function is COUNT as defined by LogInsight API.

#### Synchronous Query

~~~java
List<FieldConstraint> constraints = RequestBuilders.constraint().eq("field", "value").gt("timestamp", "0").build();
AggregateQueryBuilder aqb = (AggregateQueryBuilder) RequestBuilders.aggreateQuery().limit(100).setConstraints(constraints);
client.aggregateQuery(aqb.toUrlString());
~~~

#### Asynchronous Query

~~~java
final CountDownLatch latch = new CountDownLatch(1);
List<FieldConstraint> constraints = RequestBuilders.constraint().eq("field", "value").gt("timestamp", "0").build();
AggregateQueryBuilder aqb = (AggregateQueryBuilder) RequestBuilders.aggreateQuery().limit(100).setConstraints(constraints);
client.aggregateQuery(aqb.toUrlString(), (AggregateResponse response, LogInsightApiError error) -> {
	if (error.isError()) {
		// Handle Response
	} else {
		// Handle Error
 	}
 	latch.countDown(); // release the latch
});
try {
	latch.await();  // wait for the completion of the callback
} catch (InterruptedException e1) {
	e1.printStackTrace();
}
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

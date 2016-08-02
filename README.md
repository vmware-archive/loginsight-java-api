

# loginsight-java-api

Please do not use this API in production as it is still in **beta phase**.

## Overview

LogInsight Java API provides a fluent API to interact with VMware vRealize LogInsight 
* message queries
* aggregate queries
* ingesion
 
You can access the **javadoc** for this libary [here](https://vmware.github.io/loginsight-java-api/javadoc/)

## Try it out

### Prerequisites

* Java 8 JDK installed and set your JAVA_HOME to home of Java8 JDK
* vRealize LogInsight 3.3 onwards
 
### Build & Run

~~~bash
$ ./gradlew assemble
~~~


## Documentation

### Connecting to LogInsight
~~~java
LogInsightClient client = new LogInsightClient(ip, user, password);
client.connect();
~~~

### Ingestion of messages to LogInsight

~~~java
Message msg1 = new Message("Testing the ingestion");
msg1.addField("field", "value");
IngestionRequest request = new IngestionRequest();
request.addMessage(msg1);
IngestionResponse response = client.injest(request);
~~~

### Message Queries

#### Synchronous Query

~~~java
List<FieldConstraint> constraints = RequestBuilders.constraint().eq("field", "value").gt("timestamp", "0").build();
MessageQueryBuilder mqb = (MessageQueryBuilder) RequestBuilders.messageQuery().limit(100).setConstraints(constraints);
MessageQueryResponse messages = client.messageQuery(mqb.toUrlString());
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
		latch.countDown();
	}
});
try {
	latch.await();
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
		latch.countDown();
 	}
});
try {
	latch.await();
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

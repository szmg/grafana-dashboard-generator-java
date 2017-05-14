# Grafana dashboard builder in Java

[![Build Status](https://travis-ci.org/szmg/grafana-dashboard-generator-java.svg?branch=master)](https://travis-ci.org/szmg/grafana-dashboard-generator-java)
[![Maven Central](https://img.shields.io/maven-central/v/uk.co.szmg.grafana/grafana-dashboard-generator.svg)](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22grafana-dashboard-generator%22)


```
This is Work in progress
```

Code your Grafana dashboards in Java, more or less type-safe, with auto-complete. Then upload them to Grafana. As part of your build process if you fancy.

## Features

* create dashboards in Java
* create and _reuse_ your own frequently used components
* upload your dashboards to Grafana
* ApiKey or session cookie authentication
* [PLAN] upload as part of your build process (Maven plugin)
* [PLAN] upload with a nice command line tool
* do not get stuck if the lib is missing a property or a type: add it without needing to recompile the lib (see [below](#flexible-domain-objects))


## Usage

### Maven

```
<dependency>
    <groupId>uk.co.szmg.grafana</groupId>
    <artifactId>grafana-dashboard-generator</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Basics

I suggest you play with this and learn it that way. It's really straightforward if you are familiar with Grafana. `uk.co.szmg.grafana.domain.DomainFactories` is a good place to start.

```java
//import static uk.co.szmg.grafana.domain.DomainFactories.*

Target target = newTarget()
        .withTarget("maxSeries(humidity.peti.test.sensors)");

Row row1 = newRow()
        .withHeight("100px")
        .addPanel(newSingleStat()
                .withTitle("Single stat test")
                .addTarget(target)
                .withSpan(2))
        .addPanel(newText()
                .withContent("<div class=\"text-center dashboard-header\"><span>This is the test</span></div>")
                .withMode("html")
                .withTransparent(true)
                .withSpan(8))
        .addPanel(newSingleStat()
                .withTitle("Single stat test")
                .addTarget(target)
                .withSpan(2));

Row row2 = newRow()
        .addPanel(newGraph()
                .addTarget(target)
                .withSpan(12));

Dashboard dashboard = newDashboard()
        .withTitle("Test dashboard")
        .addRow(row1)
        .addRow(row2);

```

### Higher level bits and bobs

TODO: implement :)

### Write to stream (optional)

Convert to string or write onto a stream:

```java
DashboardSerializer serializer = new DashboardSerializer();

// to String
System.out.println(serializer.toString(dashboard));

// write to Stream
serializer.write(testDashboard(), System.out);
```

### Upload to Grafana

It's easy to update your dashboards from code.

```java

// Create an endpoint first...
// Can use either API key or session cookie auth.
GrafanaEndpoint endpoint = new GrafanaEndpoint();
endpoint.setBaseUrl("https://grafana.mydomain.com");

// see http://docs.grafana.org/http_api/auth/
endpoint.setApiKey("some api key");

// copy "grafana_sess" cookie
//endpoint.setSessionCookie("123456789asd");

// for insecure Grafana installations...
endpoint.setSkipSSLValidation(true);


// ...then the uploader...
DashboardUploader uploader = new DashboardUploader(endpoint);


// ...and upload, overwriting any existing dashboard with
// that title. ("Test dashboard")
uploader.upload(dashboard, true);


```

## Flexible domain objects

If a field that you want to use is missing or its type has changed (e.g., a new type of panel), you can
quickly add/override it without changing the library code itself. (Although a PR is welcomed.)

So no one have to clone/modify/compile/raise PR/wait for trying out an unusual/new/forgotten field/type.

Every domain object has a generic setter, getter and "with" method:

```java

// setter
dashboard.setField("templateInstance", "my favorite");
assert dashboard.getField("templateInstance") == "my favorite";

// "with" method
Dashboard sameDashboard = dashboard.withField("templateInstance", "my other favorite");
assert dashboard.getField("templateInstance") == "my other favorite"; // it's not immutable
assert sameDashboard.getField("templateInstance") == "my other favorite";

// use a new type of panel
row1.addPanel(new Panel.Generic()
        .withValue("type", "new type")
        .withValue("newField", "value"));
```

Technical stuff: every domain object is a `Map<String, Object>` wrapped into their class; every typed property reads and writes that map. By using the generic setters/getter, you can write that map, too.

You might want to look at the source of e.g., `Graph` to see how it works. And look for the file `graph.yaml` to see what it is generated from.

## License

Apache License Version 2.0

Copyright (c) 2017 Mate Gabor Szvoboda

# Grafana dashboard builder in Java

```
This is Work in progress
```

Code your Grafana dashboards in Java, more or less type-safe, with auto-complete.

E.g.,

```java
//import static com.szmg.grafana.domain.DomainFactories.*

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

#### Higer level bits and bobs

TODO: implement :)

#### To String/Stream

Convert to string or write onto a stream:

```java
DashboardSerializer serializer = new DashboardSerializer();

// to String
System.out.println(serializer.toString(dashboard));

// write to Stream
serializer.write(testDashboard(), System.out);
```

#### Upload it to Grafana

It's easy to update your dashboards from code.

TODO: create maven plugin for upload

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


#### License

MIT

Copyright (c) 2017 Mate Gabor Szvoboda

package uk.co.szmg.grafana;

/*-
 * #%L
 * dashboard-builder-main
 * %%
 * Copyright (C) 2017 Mate Gabor Szvoboda
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Simple and stupid Grafana client. Without any 3rd party dependencies (but java).
 */
public class GrafanaClient {

    private static final String USER_AGENT = "NaiveGrafanaClient";
    private static final String API_DASHBOARDS_PATH = "api/dashboards/db";
    private GrafanaEndpoint endpoint;

    public GrafanaClient(GrafanaEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Uploads a dashboard to Grafana.
     *
     * @param dashboard JSON representation of the dashboard
     * @param overwrite should an existing dashboard with the same name be overwritten?
     * @throws IOException when some error happens, e.g., {@link java.net.MalformedURLException}
     * @throws UnexpectedGrafanaResponseException when response code is not in the 2xx zone
     */
    public void uploadDashboard(String dashboard, boolean overwrite) throws IOException {
        String url = getUrl(API_DASHBOARDS_PATH);

        // poor man's json builder
        String content = "{\"overwrite\": " + overwrite + ", \"dashboard\": " + dashboard + "}";
        postToGrafana(url, content);
    }

    /**
     * Do a Grafana tailored POST with the given content and returns response code.
     * Throws UnexpectedGrafanaResponseException if response code is not in the 2xx
     * domain.
     *
     * @param url full URL, with protocol and stuff
     * @param content POST payload to send
     * @return response code if it is 2xx
     * @throws IOException when some error happens, e.g., {@link java.net.MalformedURLException}
     * @throws UnexpectedGrafanaResponseException when response code is not in the 2xx zone
     */
    protected int postToGrafana(String url, String content) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        if (endpoint.isSkipSSLValidation() && conn instanceof HttpsURLConnection) {
            disableSslCertValidation((HttpsURLConnection) conn);
        }

        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        addAuth(conn);
        conn.setDoOutput(true);
        try (OutputStream stream = conn.getOutputStream()) {
            stream.write(content.getBytes());
            stream.flush();
        }

        int responseCode = conn.getResponseCode();
        String response = readResponse(conn);

        if (responseCode / 100 == 2) {
            return responseCode;
        } else {
            throw new UnexpectedGrafanaResponseException(
                    String.format("Unexpected response from [%s]; responseCode: [%d]; response: [%s]", url, responseCode, response),
                    responseCode,
                    response
            );
        }
    }

    private void disableSslCertValidation(HttpsURLConnection conn) {
        conn.setSSLSocketFactory(SslCertValidationSkipper.noCheckSslContext.getSocketFactory());
        conn.setHostnameVerifier(SslCertValidationSkipper.allValidHostname);
    }

    protected void addAuth(HttpURLConnection conn) {
        if (endpoint.getApiKey() != null) {
            conn.setRequestProperty("Authorization", "Bearer " + endpoint.getApiKey());
        } else if (endpoint.getSessionCookie() != null) {
            conn.setRequestProperty("Cookie", "grafana_sess=" + endpoint.getSessionCookie());
        }
    }

    protected String getUrl(String path) {
        String url = endpoint.getBaseUrl();
        if (!url.endsWith("/")) {
            url += "/";
        }
        url += path;
        return url;
    }

    private String readResponse(HttpURLConnection conn) throws IOException {
        StringBuffer response = new StringBuffer();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    public static class UnexpectedGrafanaResponseException extends IllegalStateException {

        private int responseCode;
        private String response;

        public UnexpectedGrafanaResponseException(String msg, int responseCode, String response) {
            super(msg);
            this.responseCode = responseCode;
            this.response = response;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public String getResponse() {
            return response;
        }
    }

    private static class SslCertValidationSkipper {
        private static SSLContext noCheckSslContext;
        private static HostnameVerifier allValidHostname;

        static {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            try {
                noCheckSslContext = SSLContext.getInstance("SSL");
                noCheckSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Cannot skip SSL cert validation", e);
            } catch (KeyManagementException e) {
                throw new RuntimeException("Cannot skip SSL cert validation", e);
            }

            // Create all-trusting host name verifier
            allValidHostname = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

        }
    }
}

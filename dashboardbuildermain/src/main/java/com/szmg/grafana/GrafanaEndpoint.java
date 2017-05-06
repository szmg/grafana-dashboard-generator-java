package com.szmg.grafana;

/**
 * Grafana endpoint bean with auth data.
 */
public class GrafanaEndpoint {

    private String baseUrl;
    private String apiKey;
    private String sessionCookie;
    private boolean skipSSLValidation;

    /**
     * Gets Grafana base URL.
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Sets Grafana base URL.
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Gets API key.
     * @return API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * True if SSL cert check should be forced to pass.
     * @return value of skipSSLValidation
     */
    public boolean isSkipSSLValidation() {
        return skipSSLValidation;
    }

    /**
     * Sets API gey.
     *
     * http://docs.grafana.org/http_api/auth/
     *
     * You need either this or the sessionCookie for authentication.
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Gets session cookie.
     * @return session cookie
     */
    public String getSessionCookie() {
        return sessionCookie;
    }

    /**
     * Sets session cookie.
     *
     * To get the value log in and copy the value of the "grafana_sess" cookie.
     *
     * You need either this or the apiKey for authentication.
     */
    public void setSessionCookie(String sessionCookie) {
        this.sessionCookie = sessionCookie;
    }

    /**
     * Set to true if you don't want SSL cert validation.
     * @param skipSSLCertValidation true if SSL cert validation should be skipped
     */
    public void setSkipSSLValidation(boolean skipSSLCertValidation) {
        this.skipSSLValidation = skipSSLCertValidation;
    }
}

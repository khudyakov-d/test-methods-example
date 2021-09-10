package org.nsu.fit.services.browser;

public class BrowserService {
    private static final String AUTH_URI = "http://localhost:8090/tm-frontend";

    public static Browser openNewBrowser() {
        return new Browser().openPage(AUTH_URI);
    }
}

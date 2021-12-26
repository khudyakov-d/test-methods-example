package org.nsu.fit.shared;

import org.nsu.fit.services.browser.Browser;

public abstract class Screen {
    protected Browser browser;

    public Screen(Browser browser) {
        this.browser = browser;
        waitLoading();
    }

    abstract protected void waitLoading();

}

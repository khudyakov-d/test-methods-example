package ru.nsu.fit.endpoint.rest;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.util.logging.LogManager;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class CustomApplication extends ResourceConfig {
    public CustomApplication() {
        packages("ru.nsu.fit.endpoint.rest");
        register(LoggingFilter.class);
        register(AuthenticationFilter.class);
        register(GsonMessageBodyHandler.class);

        try {
            LogManager.getLogManager().readConfiguration(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("./logging.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}

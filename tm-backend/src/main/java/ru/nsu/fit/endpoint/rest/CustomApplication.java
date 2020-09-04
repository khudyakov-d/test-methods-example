package ru.nsu.fit.endpoint.rest;

import org.glassfish.jersey.server.ResourceConfig;
import ru.nsu.fit.endpoint.security.api.filter.AuthenticationFilter;
import ru.nsu.fit.endpoint.security.api.filter.AuthorizationFilter;

import java.io.IOException;
import java.util.logging.LogManager;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class CustomApplication extends ResourceConfig {
    public CustomApplication() {
        try {
            LogManager.getLogManager().readConfiguration(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("./logging.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        packages("ru.nsu.fit.endpoint.rest");

        register(AuthenticationFilter.class);
        register(AuthorizationFilter.class);
        register(CORSFilter.class);

        register(GsonMessageBodyHandler.class);
    }
}

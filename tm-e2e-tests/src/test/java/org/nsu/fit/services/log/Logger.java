package org.nsu.fit.services.log;

import io.qameta.allure.Attachment;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;
import org.nsu.fit.services.rest.data.AccountTokenPojo;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

public class Logger {
    private static final org.apache.log4j.Logger LOGGER;

    static {
        PropertyConfigurator.configure(Logger.class.getClassLoader().getResourceAsStream("log4.properties"));
        LOGGER = org.apache.log4j.Logger.getLogger(Logger.class.getName());
    }

    public static org.apache.log4j.Logger getLogger() {
        return LOGGER;
    }

    public static void error(String message, Throwable t) {
        log(Level.ERROR, message, t);
    }

    public static void debug(String message, Throwable t) {
        log(Level.DEBUG, message, t);
    }

    public static void warn(String message, Throwable t) {
        log(Level.WARN, message, t);
    }

    public static void error(String message) {
        log(Level.ERROR, message, null);
    }

    public static void warn(String message) {
        log(Level.WARN, message, null);
    }

    public static void info(String message) {
        log(Level.INFO, message, null);
    }

    public static void debug(String message) {
        log(Level.DEBUG, message, null);
    }

    private static void log(Priority priority, String message, Throwable throwable) {
        getLogger().log(priority, message, throwable);

        String messageForAttachment = "[" + priority + "]: " + message;

        if (throwable != null) {
            message += "\n\n" + throwable.toString();
        }

        attachMessage(
                messageForAttachment.substring(0, Math.min(messageForAttachment.length(), 80)),
                message);
    }

    @Attachment(value = "{0}", type = "text/plain")
    private static String attachMessage(String attachName, String message) {
        return message;
    }

    public static void logContext(ClientRequestContext context) {
        Logger.debug(new StrBuilder()
                .appendNewLine()
                .append("Perform request.")
                .appendNewLine()
                .append("Method: ")
                .append(context.getMethod())
                .appendNewLine()
                .append("Headers: ")
                .append(getHeaders(context))
                .build());
    }

    private static String getHeaders(ClientRequestContext context) {
        MultivaluedMap<String, Object> headers = context.getHeaders();

        StrBuilder builder = new StrBuilder();
        builder.appendNewLine();

        for (Map.Entry<String, List<Object>> entry : headers.entrySet()) {
            builder
                    .append("* ")
                    .append(entry.getKey())
                    .append(":")
                    .append(entry.getValue())
                    .appendNewLine();
        }

        return builder.build();
    }

    public static void logRequestDetails(String path) {
        Logger.debug(new StrBuilder()
                .appendNewLine()
                .append("Request details:")
                .appendNewLine()
                .append("Path: ")
                .appendNewLine()
                .append(path)
                .build());
    }

    public static void logRequestDetails(String path, String body) {
        Logger.debug(new StrBuilder()
                .appendNewLine()
                .append("Request details:")
                .appendNewLine()
                .append("Path: ")
                .append(path)
                .appendNewLine()
                .append("Body: ")
                .append(body)
                .build());
    }

    public static void logResponse(String response) {
        Logger.debug(new StrBuilder()
                .appendNewLine()
                .append("Get response: ")
                .appendNewLine()
                .append(response)
                .build());
    }

    public static void logError(String message) {
        Logger.error(new StrBuilder()
                .appendNewLine()
                .append("Get error: ")
                .appendNewLine()
                .append(message)
                .build());
    }

}

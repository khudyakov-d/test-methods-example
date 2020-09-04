package ru.nsu.fit.endpoint.security.exception;

/**
 * Thrown if errors occur during the authentication process.
 *
 * @author cassiomolin
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

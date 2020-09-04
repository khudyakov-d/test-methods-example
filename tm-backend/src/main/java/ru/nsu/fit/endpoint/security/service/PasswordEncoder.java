package ru.nsu.fit.endpoint.security.service;

import org.mindrot.jbcrypt.BCrypt;

/**
 * bcrypt password encoder.
 *
 * @author cassiomolin
 */
public class PasswordEncoder {

    /**
     * Hashes a password using BCrypt.
     */
    public String hashPassword(String plainTextPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    /**
     * Checks a password against a stored hash using BCrypt.
     */
    public boolean checkPassword(String plainTextPassword, String hashedPassword) {

        if (null == hashedPassword || !hashedPassword.startsWith("$2a$")) {
            throw new RuntimeException("Hashed password is invalid");
        }

        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}

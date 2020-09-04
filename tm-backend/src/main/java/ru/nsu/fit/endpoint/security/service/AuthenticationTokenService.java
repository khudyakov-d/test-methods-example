package ru.nsu.fit.endpoint.security.service;

import ru.nsu.fit.endpoint.security.api.AuthenticationTokenDetails;
import ru.nsu.fit.endpoint.security.domain.Authority;
import ru.nsu.fit.endpoint.security.exception.AuthenticationTokenRefreshmentException;
import ru.nsu.fit.endpoint.shared.Globals;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Service which provides operations for authentication tokens.
 */
public class AuthenticationTokenService {
    /**
     * Issue a token for a user with the given authorities.
     */
    public String issueToken(String username, Set<Authority> authorities) {

        String id = generateTokenIdentifier();
        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = calculateExpirationDate(issuedDate);

        AuthenticationTokenDetails authenticationTokenDetails = new AuthenticationTokenDetails(
                id,
                username,
                authorities,
                issuedDate,
                expirationDate,
                0,
                Globals.AUTHENTICATION_JWT_REFRESH_LIMIT);

        return new AuthenticationTokenIssuer().issueToken(authenticationTokenDetails);
    }

    /**
     * Parse and validate the token.
     */
    public AuthenticationTokenDetails parseToken(String token) {
        return new AuthenticationTokenParser().parseToken(token);
    }

    /**
     * Refresh a token.
     */
    public String refreshToken(AuthenticationTokenDetails currentTokenDetails) {
        if (!currentTokenDetails.isEligibleForRefreshment()) {
            throw new AuthenticationTokenRefreshmentException("This token cannot be refreshed");
        }

        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = calculateExpirationDate(issuedDate);

        // Reuse the same id.
        AuthenticationTokenDetails newTokenDetails = new AuthenticationTokenDetails(
                currentTokenDetails.getId(),
                currentTokenDetails.getUserName(),
                currentTokenDetails.getAuthorities(),
                issuedDate,
                expirationDate,
                currentTokenDetails.getRefreshCount() + 1,
                Globals.AUTHENTICATION_JWT_REFRESH_LIMIT);

        return new AuthenticationTokenIssuer().issueToken(newTokenDetails);
    }

    /**
     * Calculate the expiration date for a token.
     */
    private ZonedDateTime calculateExpirationDate(ZonedDateTime issuedDate) {
        return issuedDate.plusSeconds(Globals.AUTHENTICATION_JWT_VALID_FOR);
    }

    /**
     * Generate a token identifier.
     */
    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }
}

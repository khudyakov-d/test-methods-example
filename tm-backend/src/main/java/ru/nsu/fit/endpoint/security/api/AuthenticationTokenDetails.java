package ru.nsu.fit.endpoint.security.api;

import ru.nsu.fit.endpoint.security.domain.Authority;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Model that holds details about an authentication token.
 */
public final class AuthenticationTokenDetails {
    private final String id;
    private final String userName;
    private final Set<Authority> authorities;
    private final ZonedDateTime issuedDate;
    private final ZonedDateTime expirationDate;
    private final int refreshCount;
    private final int refreshLimit;

    public AuthenticationTokenDetails(
            String id,
            String userName,
            Set<Authority> authorities,
            ZonedDateTime issuedDate,
            ZonedDateTime expirationDate,
            int refreshCount,
            int refreshLimit) {
        this.id = id;
        this.userName = userName;
        this.authorities = authorities;
        this.issuedDate = issuedDate;
        this.expirationDate = expirationDate;
        this.refreshCount = refreshCount;
        this.refreshLimit = refreshLimit;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public ZonedDateTime getIssuedDate() {
        return issuedDate;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public int getRefreshCount() {
        return refreshCount;
    }

    public int getRefreshLimit() {
        return refreshLimit;
    }

    /**
     * Check if the authentication token is eligible for refreshment.
     */
    public boolean isEligibleForRefreshment() {
        return refreshCount < refreshLimit;
    }
}

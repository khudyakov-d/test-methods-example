package ru.nsu.fit.endpoint.security.api;

import ru.nsu.fit.endpoint.security.domain.Authority;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

public class AuthenticatedUserDetails implements Principal {
    private final String userId;
    private final String userName;
    private final Set<Authority> authorities;

    public AuthenticatedUserDetails(String userId, String userName, Set<Authority> authorities) {
        this.userId = userId;
        this.userName = userName;
        this.authorities = Collections.unmodifiableSet(authorities);
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getName() {
        return userName;
    }

    public boolean isAdmin() {
        return authorities.contains(Authority.ADMIN);
    }

    public boolean isCustomer() {
        return authorities.contains(Authority.CUSTOMER);
    }
}

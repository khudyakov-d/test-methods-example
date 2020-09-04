package ru.nsu.fit.endpoint.manager;

import org.slf4j.Logger;
import ru.nsu.fit.endpoint.MainFactory;
import ru.nsu.fit.endpoint.database.IDBService;
import ru.nsu.fit.endpoint.database.data.AccountTokenPojo;
import ru.nsu.fit.endpoint.database.data.CredentialsPojo;
import ru.nsu.fit.endpoint.database.data.CustomerPojo;
import ru.nsu.fit.endpoint.security.api.AuthenticatedUserDetails;
import ru.nsu.fit.endpoint.security.api.AuthenticationTokenDetails;
import ru.nsu.fit.endpoint.security.domain.Authority;
import ru.nsu.fit.endpoint.security.exception.AuthenticationException;
import ru.nsu.fit.endpoint.security.exception.InvalidAuthenticationTokenException;
import ru.nsu.fit.endpoint.security.service.AuthenticationTokenService;

import java.util.Collections;
import java.util.UUID;

public class AuthenticationTokenManager extends ParentManager {
    public AuthenticationTokenManager(IDBService dbService, Logger flowLog) {
        super(dbService, flowLog);
    }

    public AccountTokenPojo authenticate(CredentialsPojo credentialsPojo) {
        AccountTokenPojo accountTokenPojo = new AccountTokenPojo();
        if (credentialsPojo.login.equalsIgnoreCase("admin")
                && credentialsPojo.pass.equalsIgnoreCase("setup")) {
            accountTokenPojo.id = UUID.randomUUID();
            accountTokenPojo.authorities = Collections.singleton(Authority.ADMIN);
            accountTokenPojo.token = new AuthenticationTokenService().issueToken(credentialsPojo.login, accountTokenPojo.authorities);
        } else {
            CustomerPojo customerPojo = MainFactory.getInstance().getCustomerManager().lookupCustomer(credentialsPojo.login);

            if (customerPojo == null) {
                throw new AuthenticationException(String.format("Customer with login '%s' is not exists.", credentialsPojo.login));
            }

            accountTokenPojo.id = UUID.randomUUID();
            accountTokenPojo.authorities = Collections.singleton(Authority.CUSTOMER);
            accountTokenPojo.token = new AuthenticationTokenService().issueToken(customerPojo.login, accountTokenPojo.authorities);
        }

        return dbService.createAccountToken(accountTokenPojo);
    }

    public AuthenticationTokenDetails lookupAuthenticationTokenDetails(String authenticationToken) {
        dbService.checkAccountToken(authenticationToken);

        return new AuthenticationTokenService().parseToken(authenticationToken);
    }

    public AuthenticatedUserDetails lookupAuthenticatedUserDetails(AuthenticationTokenDetails authenticationTokenDetails) {
        if (authenticationTokenDetails.getUserName().equalsIgnoreCase("admin")) {
            if (authenticationTokenDetails.getAuthorities().size() != 1 || !authenticationTokenDetails.getAuthorities().contains(Authority.ADMIN)) {
                throw new InvalidAuthenticationTokenException("Invalid token...");
            }
            return new AuthenticatedUserDetails(null, authenticationTokenDetails.getUserName(), authenticationTokenDetails.getAuthorities());
        }

        if (authenticationTokenDetails.getAuthorities().size() != 1 || !authenticationTokenDetails.getAuthorities().contains(Authority.CUSTOMER)) {
            throw new InvalidAuthenticationTokenException("Invalid token...");
        }

        CustomerPojo customerPojo = dbService.getCustomerByLogin(authenticationTokenDetails.getUserName());

        return new AuthenticatedUserDetails(customerPojo.id.toString(), authenticationTokenDetails.getUserName(), authenticationTokenDetails.getAuthorities());
    }
}

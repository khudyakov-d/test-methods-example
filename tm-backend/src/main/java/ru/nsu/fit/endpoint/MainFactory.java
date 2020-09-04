package ru.nsu.fit.endpoint;

import org.slf4j.LoggerFactory;
import ru.nsu.fit.endpoint.database.DBService;
import ru.nsu.fit.endpoint.manager.AuthenticationTokenManager;
import ru.nsu.fit.endpoint.manager.CustomerManager;
import ru.nsu.fit.endpoint.manager.PlanManager;
import ru.nsu.fit.endpoint.manager.SubscriptionManager;

public class MainFactory {
    private static MainFactory instance;

    private final AuthenticationTokenManager authenticationTokenManager;
    private final CustomerManager customerManager;
    private final PlanManager planManager;
    private final SubscriptionManager subscriptionManager;

    private MainFactory() {
        DBService dbService = new DBService(LoggerFactory.getLogger(DBService.class));

        authenticationTokenManager = new AuthenticationTokenManager(dbService, LoggerFactory.getLogger(AuthenticationTokenManager.class));
        customerManager = new CustomerManager(dbService, LoggerFactory.getLogger(CustomerManager.class));
        planManager = new PlanManager(dbService, LoggerFactory.getLogger(PlanManager.class));
        subscriptionManager = new SubscriptionManager(dbService, LoggerFactory.getLogger(SubscriptionManager.class));
    }

    public static MainFactory getInstance() {
        synchronized (MainFactory.class) {
            if (instance == null) {
                instance = new MainFactory();
            }
            return instance;
        }
    }

    public AuthenticationTokenManager getAuthenticationTokenManager() {
        return authenticationTokenManager;
    }

    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    public PlanManager getPlanManager() {
        return planManager;
    }

    public SubscriptionManager getSubscriptionManager() {
        return subscriptionManager;
    }
}

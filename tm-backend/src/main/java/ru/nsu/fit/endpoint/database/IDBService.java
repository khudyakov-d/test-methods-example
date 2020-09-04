package ru.nsu.fit.endpoint.database;

import ru.nsu.fit.endpoint.database.data.AccountTokenPojo;
import ru.nsu.fit.endpoint.database.data.CustomerPojo;
import ru.nsu.fit.endpoint.database.data.PlanPojo;
import ru.nsu.fit.endpoint.database.data.SubscriptionPojo;
import ru.nsu.fit.endpoint.security.api.AuthenticationTokenDetails;

import java.util.List;
import java.util.UUID;

public interface IDBService {
    CustomerPojo createCustomer(CustomerPojo customerPojo);

    void editCustomer(CustomerPojo customerPojo);

    void deleteCustomer(UUID id);

    List<CustomerPojo> getCustomers();

    CustomerPojo getCustomer(UUID id);

    CustomerPojo getCustomerByLogin(String customerLogin);

    AccountTokenPojo createAccountToken(AccountTokenPojo accountTokenPojo);

    void checkAccountToken(String authenticationToken);

    PlanPojo createPlan(PlanPojo plan);

    void deletePlan(UUID id);

    List<PlanPojo> getPlans();

    SubscriptionPojo createSubscription(SubscriptionPojo subscriptionPojo);

    void deleteSubscription(UUID id);

    List<SubscriptionPojo> getSubscriptions();

    List<SubscriptionPojo> getSubscriptions(UUID customerId);
}

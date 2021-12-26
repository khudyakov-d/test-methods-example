package org.nsu.fit.tm_backend.operations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.SubscriptionPojo;
import org.nsu.fit.tm_backend.manager.CustomerManager;
import org.nsu.fit.tm_backend.manager.SubscriptionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StatisticOperationTest {

    private CustomerManager customerManager;
    private SubscriptionManager subscriptionManager;

    @BeforeEach
    void init() {
        customerManager = mock(CustomerManager.class);
        subscriptionManager = mock(SubscriptionManager.class);
    }

    @Test
    void testStatisticOperation_nullCustomerManager_throwException() {
        List<UUID> ids = Collections.singletonList(UUID.randomUUID());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    new StatisticOperation(null,
                            subscriptionManager,
                            ids);
                });
        assertEquals("customerManager", exception.getMessage());
    }

    @Test
    void testStatisticOperation_nullSubscriptionManager_throwException() {
        List<UUID> ids = Collections.singletonList(UUID.randomUUID());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    new StatisticOperation(customerManager,
                            null,
                            ids);
                });
        assertEquals("subscriptionManager", exception.getMessage());
    }

    @Test
    void testStatisticOperation_nullCustomerIds_throwException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    new StatisticOperation(customerManager,
                            subscriptionManager,
                            null);
                });
        assertEquals("customerIds", exception.getMessage());
    }

    @Test
    void testExecute_notEmptyCustomerList_rightBalance() {
        Random random = new Random();
        List<UUID> ids = new ArrayList<>();

        int balanceSum = 0;
        int planFeeSum = 0;

        for (int i = 0; i < 3; i++) {
            UUID id = UUID.randomUUID();
            int balance = random.nextInt(1000);
            CustomerPojo customer = getParametrizedCustomer(id, balance);
            when(customerManager.getCustomer(id)).thenReturn(customer);

            balanceSum += balance;
            ids.add(id);

            List<SubscriptionPojo> subscriptions = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                int fee = random.nextInt(600);
                SubscriptionPojo subscription = new SubscriptionPojo();
                subscription.planFee = fee;

                subscriptions.add(subscription);
                planFeeSum += fee;
            }
            when(subscriptionManager.getSubscriptions(id)).thenReturn(subscriptions);
        }

        StatisticOperation statisticOperation = new StatisticOperation(customerManager, subscriptionManager, ids);
        StatisticOperation.StatisticOperationResult result = statisticOperation.Execute();

        for (UUID id : ids) {
            verify(customerManager, times(1)).getCustomer(id);
            verify(subscriptionManager, times(1)).getSubscriptions(id);
        }

        assertEquals(balanceSum, result.overallBalance);
        assertEquals(planFeeSum, result.overallFee);
    }

    private CustomerPojo getParametrizedCustomer(UUID id, int balance) {
        CustomerPojo customer = new CustomerPojo();
        customer.id = id;
        customer.balance = balance;
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john@email.com";
        customer.pass = "eFmRT7KC";

        return customer;
    }


}

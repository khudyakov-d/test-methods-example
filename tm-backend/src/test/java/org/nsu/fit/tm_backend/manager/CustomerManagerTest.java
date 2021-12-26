package org.nsu.fit.tm_backend.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nsu.fit.tm_backend.database.IDBService;
import org.nsu.fit.tm_backend.database.data.ContactPojo;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.TopUpBalancePojo;
import org.nsu.fit.tm_backend.manager.auth.data.AuthenticatedUserDetails;
import org.nsu.fit.tm_backend.shared.Authority;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Лабораторная 2: покрыть юнит тестами класс CustomerManager на 100%.
class CustomerManagerTest {
    private Logger logger;
    private IDBService dbService;
    private CustomerManager customerManager;

    @BeforeEach
    void init() {
        dbService = mock(IDBService.class);
        logger = mock(Logger.class);
        customerManager = new CustomerManager(dbService, logger);
    }

    @Test
    void testCreateCustomer_customerNull_throwsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.createCustomer(null));

        assertEquals("Argument 'customer' is null.", exception.getMessage());
    }

    @Test
    void testCreateCustomer_customerLoginNull_throwsException() {
        CustomerPojo customer = getValidCustomer();
        customer.login = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.createCustomer(customer));
        assertEquals("Field 'customer.login' is null.", exception.getMessage());
    }

    @Test
    void testCreateCustomer_customerPassNull_throwsException() {
        testBadCustomerPass(null, "Field 'customer.pass' is null.");
    }

    @Test
    void testCreateCustomer_customerPassLessMinLength_throwsException() {
        testBadCustomerPass("12345",
                "Password's length should be more or equal 6 symbols and less or equal 12 symbols.");
    }

    @Test
    void testCreateCustomer_customerPassGreaterMaxLength_throwsException() {
        testBadCustomerPass("123456789101112",
                "Password's length should be more or equal 6 symbols and less or equal 12 symbols.");
    }

    @Test
    void testCreateCustomer_customerPassOneOfEasy_throwsException() {
        testBadCustomerPass("123qwe",
                "Password is very easy.");
    }

    void testBadCustomerPass(String pass, String message) {
        CustomerPojo customer = getValidCustomer();
        customer.pass = pass;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.createCustomer(customer));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testCreateCustomer_customerBalanceIsNotNull_throwsException() {
        CustomerPojo customer = getValidCustomer();
        customer.balance = 100;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.createCustomer(customer));
        assertEquals("Balance must be zero.", exception.getMessage());
    }

    @Test
    void testCreateCustomer_customerLoginAlreadyReserved_throwsException() {
        CustomerPojo customer = getValidCustomer();

        CustomerPojo existedCustomer = new CustomerPojo();
        existedCustomer.login = customer.login;

        when(dbService.getCustomers()).thenReturn(Collections.singletonList(existedCustomer));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.createCustomer(customer));
        assertEquals("Customer with this login already exists.", exception.getMessage());

        verify(dbService, times(1)).getCustomers();
    }

    @Test
    void testCreateCustomer_customerLoginNotReserved_rightCustomer() {
        CustomerPojo customer = getValidCustomer();

        when(dbService.getCustomers()).thenReturn(Collections.emptyList());

        CustomerPojo outputCustomer = getValidCustomer();
        outputCustomer.id = UUID.randomUUID();
        when(dbService.createCustomer(customer)).thenReturn(outputCustomer);

        CustomerPojo result = customerManager.createCustomer(customer);

        verify(dbService, times(1)).getCustomers();
        verify(dbService, times(1)).createCustomer(customer);
        assertEquals(outputCustomer, result);
    }

    @Test
    void testGetCustomers_notResultEmptyList_equals() {
        CustomerPojo customer = getValidCustomer();

        List<CustomerPojo> customerList = Collections.singletonList(customer);
        when(dbService.getCustomers()).thenReturn(customerList);

        List<CustomerPojo> result = customerManager.getCustomers();

        verify(dbService, times(1)).getCustomers();
        assertEquals(customerList, result);
    }

    @Test
    void testGetCustomer_returnNotNullCustomer() {
        UUID id = UUID.randomUUID();

        CustomerPojo customer = getValidCustomer();
        customer.id = id;

        when(dbService.getCustomer(id)).thenReturn(customer);
        CustomerPojo result = customerManager.getCustomer(id);

        verify(dbService, times(1)).getCustomer(id);
        assertEquals(id, result.id);
    }

    @Test
    void testLookupCustomer_customerFound_returnCustomer() {
        CustomerPojo customer = getValidCustomer();

        when(dbService.getCustomers()).thenReturn(Collections.singletonList(customer));
        CustomerPojo result = customerManager.lookupCustomer(customer.login);

        verify(dbService, times(1)).getCustomers();
        assertEquals(customer, result);
    }

    @Test
    void testLookupCustomer_customerNotFound_returnNull() {
        when(dbService.getCustomers()).thenReturn(Collections.emptyList());
        CustomerPojo result = customerManager.lookupCustomer("login");

        verify(dbService, times(1)).getCustomers();
        assertNull(result);
    }


    @Test
    void testMe_userIsAdmin_returnAdmin() {
        String login = "admin";

        AuthenticatedUserDetails details = new AuthenticatedUserDetails(UUID.randomUUID().toString(),
                login,
                Collections.singleton(Authority.ADMIN_ROLE));

        ContactPojo contact = customerManager.me(details);
        assertEquals(login, contact.login);
    }

    @Test
    void testMe_userIsACustomer_returnCustomer() {
        CustomerPojo customer = getValidCustomer();

        AuthenticatedUserDetails details = new AuthenticatedUserDetails(UUID.randomUUID().toString(),
                customer.login,
                Collections.singleton(Authority.CUSTOMER_ROLE));

        when(dbService.getCustomerByLogin(customer.login)).thenReturn(customer);

        ContactPojo contact = customerManager.me(details);

        verify(dbService, times(1)).getCustomerByLogin(customer.login);
        assertEquals(customer.login, contact.login);
    }

   /* @Test
    void testMe_userIsACustomer_withoutPassword() {
        CustomerPojo customer = getValidCustomer();

        AuthenticatedUserDetails details = new AuthenticatedUserDetails(UUID.randomUUID().toString(),
                customer.login,
                Collections.singleton(Authority.CUSTOMER_ROLE));

        when(dbService.getCustomerByLogin(customer.login)).thenReturn(customer);

        ContactPojo contact = customerManager.me(details);

        verify(dbService, times(1)).getCustomerByLogin(customer.login);
        assertNull(contact.pass);
    }
*/

    @Test
    void testDeleteCustomer_deleteCustomer() {
        UUID id = UUID.randomUUID();
        customerManager.deleteCustomer(id);
        verify(dbService, times(1)).deleteCustomer(id);
    }

    @Test
    void testTopUpBalance_upBalance() {
        UUID id = UUID.randomUUID();
        int customerBalance=100;

        CustomerPojo customer = getValidCustomer();
        customer.id = id;
        customer.balance=customerBalance;

        int money = 1000;
        TopUpBalancePojo topBalance = new TopUpBalancePojo();
        topBalance.customerId = id;
        topBalance.money = money;

        when(dbService.getCustomer(id)).thenReturn(customer);

        customerManager.topUpBalance(topBalance);

        verify(dbService, times(1)).getCustomer(id);
        verify(dbService, times(1)).editCustomer(customer);
        assertEquals(customerBalance+money, customer.balance);
    }


    private CustomerPojo getValidCustomer() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = "eFmRT7KC";
        customer.balance = 0;
        return customer;
    }

}

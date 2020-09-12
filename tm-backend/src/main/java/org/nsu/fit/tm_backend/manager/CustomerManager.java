package org.nsu.fit.tm_backend.manager;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.nsu.fit.tm_backend.database.IDBService;
import org.nsu.fit.tm_backend.database.data.ContactPojo;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.TopUpBalancePojo;
import org.nsu.fit.tm_backend.manager.auth.data.AuthenticatedUserDetails;
import org.nsu.fit.tm_backend.shared.Globals;

import java.util.List;
import java.util.UUID;

public class CustomerManager extends ParentManager {
    public CustomerManager(IDBService dbService, Logger flowLog) {
        super(dbService, flowLog);
    }

    /**
     * Метод создает новый объект класса Customer. Ограничения:
     * Аргумент 'customerData' - не null;
     * firstName - нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов;
     * lastName - нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов;
     * login - указывается в виде email, проверить email на корректность, проверить что нет customer с таким же email;
     * pass - длина от 6 до 12 символов включительно, не должен быть простым (123qwe или 1q2w3e), не должен содержать части login, firstName, lastName
     * money - должно быть равно 0.
     */
    public CustomerPojo createCustomer(CustomerPojo customer) {
        Validate.notNull(customer, "Argument 'customerData' is null.");

        Validate.notNull(customer.pass);
        Validate.isTrue(customer.pass.length() >= 6 && customer.pass.length() < 13, "Password's length should be more or equal 6 symbols and less or equal 12 symbols.");
        Validate.isTrue(!customer.pass.equalsIgnoreCase("123qwe"), "Password is easy.");

        // TODO: необходимо дописать дополнительные проверки

        return dbService.createCustomer(customer);
    }

    /**
     * Метод возвращает список customer'ов.
     */
    public List<CustomerPojo> getCustomers() {
        return dbService.getCustomers();
    }

    public CustomerPojo lookupCustomer(String login) {
        return dbService.getCustomers().stream()
                .filter(x -> x.login.equals(login))
                .findFirst()
                .orElse(null);
    }

    public ContactPojo me(AuthenticatedUserDetails authenticatedUserDetails) {
        ContactPojo contactPojo = new ContactPojo();

        if (authenticatedUserDetails.isAdmin()) {
            contactPojo.login = Globals.ADMIN_LOGIN;

            return contactPojo;
        }

        // Лабораторная_2: обратите внимание что вернули данных больше чем надо...
        return dbService.getCustomerByLogin(authenticatedUserDetails.getName());
    }

    /**
     * Метод обновляет объект типа Customer.
     * Можно обновить только firstName и lastName.
     */
    public CustomerPojo updateCustomer(CustomerPojo customer) {
        throw new NotImplementedException("Please implement the method.");
    }

    public void deleteCustomer(UUID id) {
        dbService.deleteCustomer(id);
    }

    /**
     * Метод добавляет к текущему баласу money.
     * money - должен быть строго больше нуля.
     */
    public CustomerPojo topUpBalance(TopUpBalancePojo topUpBalancePojo) {
        CustomerPojo customerPojo = dbService.getCustomer(topUpBalancePojo.customerId);

        customerPojo.balance += topUpBalancePojo.money;

        dbService.editCustomer(customerPojo);

        return customerPojo;
    }
}

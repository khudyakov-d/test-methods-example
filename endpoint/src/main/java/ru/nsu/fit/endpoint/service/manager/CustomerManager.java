package ru.nsu.fit.endpoint.service.manager;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import ru.nsu.fit.endpoint.service.database.DBService;
import ru.nsu.fit.endpoint.service.database.data.Customer;

import java.util.List;

public class CustomerManager extends ParentManager {
    public CustomerManager(DBService dbService, Logger flowLog) {
        super(dbService, flowLog);
    }

    /**
     * Метод создает новый объект типа Customer. Ограничения:
     * Аргумент 'customerData' - не null;
     * firstName - нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов;
     * lastName - нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов;
     * login - указывается в виде email, проверить email на корректность, проверить что нет customer с таким же email;
     * pass - длина от 6 до 12 символов включительно, не должен быть простым (123qwe или 1q2w3e), не должен содержать части login, firstName, lastName
     * money - должно быть равно 0.
     */
    public Customer createCustomer(Customer customerData) {
        Validate.notNull(customerData, "Argument 'customerData' is null.");

        Validate.notNull(customerData.getPass());
        Validate.isTrue(customerData.getPass().length() >= 6 && customerData.getPass().length() < 13, "Password's length should be more or equal 6 symbols and less or equal 12 symbols.");
        Validate.isTrue(!customerData.getPass().equalsIgnoreCase("123qwe"), "Password is easy.");

        // TODO: необходимо дописать дополнительные проверки

        return dbService.createCustomer(customerData);
    }

    /**
     * Метод возвращает список объектов типа customer.
     */
    public List<Customer> getCustomers() {
        return dbService.getCustomers();
    }
}

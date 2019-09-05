package ru.nsu.fit.endpoint.database;

import ru.nsu.fit.endpoint.database.data.CustomerPojo;
import ru.nsu.fit.endpoint.database.data.Plan;

import java.util.List;
import java.util.UUID;

public interface IDBService {
    CustomerPojo createCustomer(CustomerPojo customerData);

    List<CustomerPojo> getCustomers();

    UUID getCustomerIdByLogin(String customerLogin);

    Plan createPlan(Plan plan);
}

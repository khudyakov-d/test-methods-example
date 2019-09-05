package ru.nsu.fit.endpoint.database;

import ru.nsu.fit.endpoint.database.data.CustomerPojo;
import ru.nsu.fit.endpoint.database.data.PlanPojo;

import java.util.List;
import java.util.UUID;

public interface IDBService {
    CustomerPojo createCustomer(CustomerPojo customerData);

    List<CustomerPojo> getCustomers();

    UUID getCustomerIdByLogin(String customerLogin);

    PlanPojo createPlan(PlanPojo plan);
}

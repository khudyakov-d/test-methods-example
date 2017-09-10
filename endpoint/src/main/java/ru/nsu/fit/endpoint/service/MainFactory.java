package ru.nsu.fit.endpoint.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.fit.endpoint.service.database.DBService;
import ru.nsu.fit.endpoint.service.manager.CustomerManager;

public class MainFactory {
    private static MainFactory instance;

    private Logger logger;
    private DBService dbService;
    private CustomerManager customerManager;

    public MainFactory() {
        logger = LoggerFactory.getLogger("FLOW_LOG");

        dbService = new DBService(logger);

        customerManager = new CustomerManager(dbService, logger);
    }

    public static MainFactory getInstance() {
        synchronized (MainFactory.class) {
            if (instance == null)
                instance = new MainFactory();
            return instance;
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public DBService getDbService() {
        return dbService;
    }

    public CustomerManager getCustomerManager() {
        return customerManager;
    }
}

package ru.nsu.fit.endpoint.manager;

import org.slf4j.Logger;
import ru.nsu.fit.endpoint.database.IDBService;

public class ParentManager {
    protected IDBService dbService;
    protected Logger log;

    public ParentManager(IDBService dbService, Logger log) {
        this.dbService = dbService;
        this.log = log;
    }
}

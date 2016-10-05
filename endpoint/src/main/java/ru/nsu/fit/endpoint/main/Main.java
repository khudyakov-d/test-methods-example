package ru.nsu.fit.endpoint.main;

import ru.nsu.fit.endpoint.service.database.DBService;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class Main {
    public static void main(String[] args) {
        DBService.createCustomer("John", "Wick", "john_wick@gmail.com", "strongpass");
    }
}

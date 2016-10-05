package ru.nsu.fit.endpoint.service.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.fit.endpoint.service.database.data.Customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class DBService {
    // Constants
    private static final String INSERT_CUSTOMER = "INSERT INTO CUSTOMER(id, first_name, last_name, login, pass, money) values ('%s', '%s', '%s', '%s', '%s', 0)";

    private static final Logger logger = LoggerFactory.getLogger("DB_LOG");
    private static final Object generalMutex = new Object();
    private static Connection connection;

    static {
        init();
    }

    public static void createCustomer(String firstName, String lastName, String login, String pass) {
        synchronized (generalMutex) {
            logger.info("Try to create customer");

            Customer customer = new Customer(firstName, lastName, login, pass, 0);
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(
                        String.format(
                                INSERT_CUSTOMER,
                                customer.getId(),
                                customer.getFirstName(),
                                customer.getLastName(),
                                customer.getLogin(),
                                customer.getPass()));
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    private static void init() {
        logger.debug("-------- MySQL JDBC Connection Testing ------------");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            logger.debug("Where is your MySQL JDBC Driver?", ex);
            throw new RuntimeException(ex);
        }

        logger.debug("MySQL JDBC Driver Registered!");

        try {
            connection = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7138900","sql7138900", "XJ4yPcVRYv");
        } catch (SQLException ex) {
            logger.debug("Connection Failed! Check output console", ex);
            throw new RuntimeException(ex);
        }

        if (connection != null) {
            logger.debug("You made it, take control your database now!");
        } else {
            logger.debug("Failed to make connection!");
        }
    }
}

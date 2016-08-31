package ru.nsu.fit.endpoint.service.database.data;

import org.apache.commons.lang.Validate;

import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class Customer {
    public UUID id;
    public String firstName;
    public String lastName;
    public String login;
    public String pass;

    public Customer(String firstName, String lastName, String login, String pass) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.pass = pass;
        validate(firstName, lastName, login, pass);
    }

    public static void validate(String firstName, String lastName, String login, String pass) {
        Validate.notNull(pass);
        Validate.isTrue(pass.length() >= 6 && pass.length() < 13, "Password's length should be more or equal 6 symbols and less or equal 12 symbols");
        Validate.isTrue(!pass.equalsIgnoreCase("123qwe"), "Password is easy");
    }
}

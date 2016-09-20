package ru.nsu.fit.endpoint.service.database.data;

import org.apache.commons.lang.Validate;

import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class Customer {
    private UUID id;
    /* нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов */
    private String firstName;
    /* нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов */
    private String lastName;
    /* указывается в виде email, проверить email на корректность */
    private String login;
    /* длина от 6 до 12 символов включительно, недолжен быть простым, не должен содержать части login, firstName, lastName */
    private String pass;
    /* счет не может быть отрицательным */
    private int money;

    public Customer(String firstName, String lastName, String login, String pass, int money) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.pass = pass;
        this.money = money;
        validate(firstName, lastName, login, pass, money);
    }

    public static void validate(String firstName, String lastName, String login, String pass, int money) {
        Validate.notNull(pass);
        Validate.isTrue(pass.length() >= 6 && pass.length() < 13, "Password's length should be more or equal 6 symbols and less or equal 12 symbols");
        Validate.isTrue(!pass.equalsIgnoreCase("123qwe"), "Password is easy");
        Validate.isTrue(money >= 0, "Money mist be positive value");
    }
}

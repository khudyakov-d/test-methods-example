package ru.nsu.fit.endpoint.service.database.data;

import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class User {
    private UUID customerId;
    private UUID[] subscriptionIds;
    private UUID id;
    /* нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов */
    private String firstName;
    /* нет пробелов, длина от 2 до 12 символов включительно, начинается с заглавной буквы, остальные символы строчные, нет цифр и других символов */
    private String lastName;
    /* указывается в виде email, проверить email на корректность */
    private String login;
    /* длина от 6 до 12 символов включительно, недолжен быть простым, не должен содержать части login, firstName, lastName */
    private String pass;
    private UserRole userRole;

    public static enum UserRole {
        COMPANY_ADMINISTRATOR("Company administrator"),
        TECHNICAL_ADMINISTRATOR("Technical administrator"),
        BILLING_ADMINISTRATOR("Billing administrator"),
        USER("User");

        private String roleName;

        UserRole(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleName() {
            return roleName;
        }
    }
}

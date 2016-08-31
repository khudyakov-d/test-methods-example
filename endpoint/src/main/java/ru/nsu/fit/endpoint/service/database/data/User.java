package ru.nsu.fit.endpoint.service.database.data;

import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class User {
    private UUID customerId;
    private UUID[] subscriptionIds;
    public UUID id;
    public String firstName;
    public String lastName;
    public String login;
    public String pass;
}

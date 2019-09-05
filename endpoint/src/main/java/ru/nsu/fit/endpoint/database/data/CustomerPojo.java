package ru.nsu.fit.endpoint.database.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerPojo {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("login")
    private String login;

    @JsonProperty("pass")
    private String pass;

    @JsonProperty("balance")
    private int balance;

    public UUID getId() {
        return id;
    }

    public CustomerPojo setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public int getBalance() {
        return balance;
    }

    public CustomerPojo setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerPojo setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerPojo setLogin(String login) {
        this.login = login;
        return this;
    }

    public CustomerPojo setPass(String pass) {
        this.pass = pass;
        return this;
    }

    public CustomerPojo setBalance(int balance) {
        this.balance = balance;
        return this;
    }
}

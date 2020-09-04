package ru.nsu.fit.endpoint.database.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CredentialsPojo {
    @JsonProperty("login")
    public String login;

    @JsonProperty("pass")
    public String pass;
}

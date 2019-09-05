package ru.nsu.fit.endpoint.database.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionPojo {
    @JsonProperty("id")
    public UUID id;

    @JsonProperty("customerId")
    public UUID customerId;

    @JsonProperty("planId")
    public UUID planId;
}

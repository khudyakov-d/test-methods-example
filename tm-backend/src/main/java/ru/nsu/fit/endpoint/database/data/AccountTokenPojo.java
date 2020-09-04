package ru.nsu.fit.endpoint.database.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.fit.endpoint.security.domain.Authority;

import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountTokenPojo {
    @JsonProperty("id")
    public UUID id;

    @JsonProperty("authorities")
    public Set<Authority> authorities;

    @JsonProperty("token")
    public String token;
}

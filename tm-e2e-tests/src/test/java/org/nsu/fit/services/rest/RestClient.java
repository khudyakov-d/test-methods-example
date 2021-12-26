package org.nsu.fit.services.rest;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.nsu.fit.services.log.Logger;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.nsu.fit.services.rest.data.CredentialsPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.shared.JsonMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RestClient {

    private final Faker faker = new Faker();

    private static final String REST_URI = "http://localhost:8089/tm-backend/rest";

    private final static Client client = ClientBuilder.newClient(new ClientConfig().register(RestClientLogFilter.class));

    public AccountTokenPojo authenticate(String login, String pass) {
        CredentialsPojo credentialsPojo = new CredentialsPojo();

        credentialsPojo.login = login;
        credentialsPojo.pass = pass;

        return post("authenticate", JsonMapper.toJson(credentialsPojo, true), AccountTokenPojo.class, null);
    }

    public CustomerPojo createAutoGeneratedCustomer(AccountTokenPojo accountToken) {
        ContactPojo contactPojo = new ContactPojo();

        // Лабораторная 3: Добавить обработку генерацию фейковых имен, фамилий и логинов.
        // * Исследовать этот вопрос более детально, возможно прикрутить специальную библиотеку для генерации фейковых данных.
        contactPojo.firstName = faker.name().firstName();
        contactPojo.lastName = faker.name().lastName();
        contactPojo.login = UUID.randomUUID() + "@gmail.com";
        contactPojo.pass = "strongpass";

        return post("customers", JsonMapper.toJson(contactPojo, true), CustomerPojo.class, accountToken);
    }

    public <R> R get(String path,
                     GenericType<R> responseType,
                     AccountTokenPojo accountToken,
                     Map<String, List<Object>> queryParams) {

        Invocation.Builder request = buildRequest(path, queryParams);

        if (accountToken != null) {
            request.header("Authorization", "Bearer " + accountToken.token);
        }

        try {
            Logger.logRequestDetails(path);
            return request.get(responseType);
        } catch (ResponseProcessingException ex) {
            Logger.logError(ex.getResponse().readEntity(String.class));
            throw ex;
        } catch (Exception ex) {
            Logger.logError(ex.getMessage());
            throw ex;
        }
    }

    public <R> R get(String path, Class<R> responseType,
                     AccountTokenPojo accountToken,
                     Map<String, List<Object>> queryParams) {

        Invocation.Builder request = buildRequest(path, queryParams);

        if (accountToken != null) {
            request.header("Authorization", "Bearer " + accountToken.token);
        }

        try {
            Logger.logRequestDetails(path);
            String response = request.get(String.class);
            Logger.logResponse(response);

            return JsonMapper.fromJson(response, responseType);
        } catch (ResponseProcessingException ex) {
            Logger.logError(ex.getResponse().readEntity(String.class));
            throw ex;
        } catch (Exception ex) {
            Logger.logError(ex.getMessage());
            throw ex;
        }
    }

    public void delete(String path, AccountTokenPojo accountToken) {
        Invocation.Builder request = buildRequest(path, null);

        if (accountToken != null) {
            request.header("Authorization", "Bearer " + accountToken.token);
        }

        try {
            Logger.logRequestDetails(path);
            String response = request.delete(String.class);
            Logger.logResponse(response);
        } catch (ResponseProcessingException ex) {
            Logger.logError(ex.getResponse().readEntity(String.class));
            throw ex;
        } catch (Exception ex) {
            Logger.logError(ex.getMessage());
            throw ex;
        }
    }

    public <R> R post(String path, String body, Class<R> responseType, AccountTokenPojo accountToken) {
        // Лабораторная 3: Добавить обработку Responses и Errors. Выводите их в лог.
        // Подумайте почему в filter нет Response чтобы можно было удобно его сохранить.
        Invocation.Builder request = buildRequest(path, null);

        if (accountToken != null) {
            request.header("Authorization", "Bearer " + accountToken.token);
        }

        try {
            Logger.logRequestDetails(path, body);
            String response = request.post(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
            Logger.logResponse(response);

            if (StringUtils.isBlank(response)) {
                return null;
            }

            return JsonMapper.fromJson(response, responseType);
        } catch (ResponseProcessingException ex) {
            Logger.logError(ex.getResponse().readEntity(String.class));
            throw ex;
        } catch (Exception ex) {
            Logger.logError(ex.getMessage());
            throw ex;
        }
    }

    private Invocation.Builder buildRequest(String path, Map<String, List<Object>> queryParams) {
        WebTarget target = client
                .target(REST_URI)
                .path(path);

        if (queryParams != null) {
            for (Map.Entry<String, List<Object>> entry : queryParams.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue().toArray());
            }
        }

        return target
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private static class RestClientLogFilter implements ClientRequestFilter {
        @Override
        public void filter(ClientRequestContext requestContext) {
            // Лабораторная 3: разобраться как работает данный фильтр
            // и добавить логирование METHOD и HEADERS.
            Logger.logContext(requestContext);
        }
    }
}

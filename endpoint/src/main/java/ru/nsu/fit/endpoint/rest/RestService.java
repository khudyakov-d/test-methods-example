package ru.nsu.fit.endpoint.rest;

import org.apache.commons.lang.exception.ExceptionUtils;
import ru.nsu.fit.endpoint.MainFactory;
import ru.nsu.fit.endpoint.database.data.CustomerPojo;
import ru.nsu.fit.endpoint.database.data.HealthCheckPojo;
import ru.nsu.fit.endpoint.database.data.RolePojo;
import ru.nsu.fit.endpoint.shared.Globals;
import ru.nsu.fit.endpoint.shared.JsonMapper;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("")
public class RestService {
    @RolesAllowed({ Globals.UNKNOWN_ROLE, Globals.ADMIN_ROLE })
    @GET
    @Path("/health_check")
    public Response healthCheck() {
        return Response.ok().entity(JsonMapper.toJson(new HealthCheckPojo(), true)).build();
    }

    @RolesAllowed({ Globals.UNKNOWN_ROLE , Globals.ADMIN_ROLE })
    @GET
    @Path("/role")
    public Response getRole(@Context ContainerRequestContext crc) {
        RolePojo rolePojo = new RolePojo();
        rolePojo.role = crc.getProperty("ROLE").toString();

        return Response.ok().entity(JsonMapper.toJson(rolePojo, true)).build();
    }

    // Example request: ../customers?login='john_wick@gmail.com'
    @RolesAllowed(Globals.ADMIN_ROLE)
    @GET
    @Path("/customers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomers(@DefaultValue("") @QueryParam("login") String customerLogin) {
        try {
            List<CustomerPojo> customers = MainFactory.getInstance()
                    .getCustomerManager()
                    .getCustomers().stream()
                    .filter(x -> customerLogin.isEmpty() || x.login.equals(customerLogin))
                    .collect(Collectors.toList());

            return Response.ok().entity(JsonMapper.toJson(customers, true)).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }

    @RolesAllowed(Globals.ADMIN_ROLE)
    @POST
    @Path("/customers")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(String customerDataJson) {
        try {
            // convert json to object
            CustomerPojo customerData = JsonMapper.fromJson(customerDataJson, CustomerPojo.class);

            // create new customer
            CustomerPojo customer = MainFactory.getInstance().getCustomerManager().createCustomer(customerData);

            // send the answer
            return Response.ok().entity(JsonMapper.toJson(customer, true)).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }
}
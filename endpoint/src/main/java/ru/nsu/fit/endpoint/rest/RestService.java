package ru.nsu.fit.endpoint.rest;

import org.apache.commons.lang.exception.ExceptionUtils;
import ru.nsu.fit.endpoint.service.database.DBService;
import ru.nsu.fit.endpoint.service.database.data.Customer;
import ru.nsu.fit.endpoint.shared.JsonMapper;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
@Path("")
public class RestService {
    @RolesAllowed(Roles.UNKNOWN)
    @GET
    @Path("/health_check")
    public Response healthCheck() {
        return Response.ok().entity("{\"status\": \"OK\"}").build();
    }

    @RolesAllowed({Roles.UNKNOWN , Roles.ADMIN})
    @GET
    @Path("/get_role")
    public Response getRole(@Context ContainerRequestContext crc) {
        return Response.ok().entity(String.format("{\"role\": \"%s\"}", crc.getProperty("ROLE"))).build();
    }

    @RolesAllowed(Roles.ADMIN)
    @GET
    @Path("/get_customers")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCustomers() {
        try {
            List<Customer.CustomerData> result = DBService.getCustomers();
            String resultData = JsonMapper.toJson(result, true);
            return Response.ok().entity(resultData).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }

    @RolesAllowed(Roles.ADMIN)
    @POST
    @Path("/create_customer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(String customerDataJson) {
        try {
            Customer.CustomerData customerData = JsonMapper.fromJson(customerDataJson, Customer.CustomerData.class);
            DBService.createCustomer(customerData);
            return Response.ok().entity(customerData.toString()).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }

    @RolesAllowed(Roles.ADMIN)
    @GET
    @Path("/get_customer_id/{customer_login}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerId(@PathParam("customer_login") String customerLogin) {
        try {
            UUID id = DBService.getCustomerIdByLogin(customerLogin);
            return Response.ok().entity("{\"id\":\"" + id.toString() + "\"}").build();
        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(ex.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(ex)).build();
        }
    }
}
package org.nsu.fit.services.rest.data;

import org.glassfish.jersey.internal.inject.Custom;

import javax.ws.rs.core.GenericType;
import java.util.List;

public class CustomerListType extends GenericType<List<CustomerPojo>> {
}

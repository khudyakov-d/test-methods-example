package ru.nsu.fit.endpoint.database;

import org.slf4j.Logger;
import ru.nsu.fit.endpoint.database.data.AccountTokenPojo;
import ru.nsu.fit.endpoint.database.data.CustomerPojo;
import ru.nsu.fit.endpoint.database.data.PlanPojo;
import ru.nsu.fit.endpoint.database.data.SubscriptionPojo;
import ru.nsu.fit.endpoint.security.exception.AccessDeniedException;
import ru.nsu.fit.endpoint.shared.JsonMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBService implements IDBService{
    // Constants
    private static final String INSERT_CUSTOMER = "INSERT INTO CUSTOMER(id, first_name, last_name, login, pass, balance) values ('%s', '%s', '%s', '%s', '%s', %s)";
    private static final String UPDATE_CUSTOMER = "UPDATE CUSTOMER SET balance=%s WHERE id='%s'";
    private static final String DELETE_CUSTOMER = "DELETE FROM CUSTOMER where id='%s'";

    private static final String INSERT_SUBSCRIPTION = "INSERT INTO SUBSCRIPTION(id, customer_id, plan_id) values ('%s', '%s', '%s')";
    private static final String DELETE_SUBSCRIPTION = "DELETE FROM SUBSCRIPTION where id='%s'";
    private static final String SELECT_ALL_SUBSCRIPTIONS = "SELECT * FROM SUBSCRIPTION";
    private static final String SELECT_SUBSCRIPTIONS = "SELECT * FROM SUBSCRIPTION WHERE customer_id='%s'";

    private static final String INSERT_PLAN = "INSERT INTO PLAN(id, name, details, fee) values ('%s', '%s', '%s', %s)";
    private static final String DELETE_PLAN = "DELETE FROM PLAN where id='%s'";

    private static final String SELECT_CUSTOMER_BY_LOGIN = "SELECT * FROM CUSTOMER WHERE login='%s'";
    private static final String SELECT_CUSTOMER = "SELECT * FROM CUSTOMER WHERE id='%s'";
    private static final String SELECT_CUSTOMERS = "SELECT * FROM CUSTOMER";

    private static final String SELECT_PLANS = "SELECT * FROM PLAN";

    private final Logger logger;
    private static final Object generalMutex = new Object();
    private Connection connection;

    private final List<AccountTokenPojo> accountTokens;

    public DBService(Logger logger) {
        this.logger = logger;
        this.accountTokens = new ArrayList<>();
        init();
    }

    @Override
    public AccountTokenPojo createAccountToken(AccountTokenPojo accountTokenPojo) {
        synchronized (generalMutex) {
            logger.debug(String.format("Method 'createAccountToken' was called with data: \n%s", JsonMapper.toJson(accountTokenPojo, true)));

            accountTokens.add(accountTokenPojo);

            return accountTokenPojo;
        }
    }

    @Override
    public void checkAccountToken(String authenticationToken) {
        synchronized (generalMutex) {
            logger.debug(String.format("Method 'checkAccountToken' was called with data: \n%s", authenticationToken));

            accountTokens.stream()
                    .filter(x -> x.token.equals(authenticationToken))
                    .findFirst()
                    .orElseThrow(() -> new AccessDeniedException(""));
        }
    }

    public CustomerPojo createCustomer(CustomerPojo customerData) {
        synchronized (generalMutex) {
            logger.debug(String.format("Method 'createCustomer' was called with data: \n%s", JsonMapper.toJson(customerData, true)));

            customerData.id = UUID.randomUUID();
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(
                        String.format(
                                INSERT_CUSTOMER,
                                customerData.id,
                                customerData.firstName,
                                customerData.lastName,
                                customerData.login,
                                customerData.pass,
                                customerData.balance));
                return customerData;
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public void editCustomer(CustomerPojo customerPojo) {
        synchronized (generalMutex) {
            logger.debug("Method 'editCustomer' was called with data: \n{}", JsonMapper.toJson(customerPojo, true));

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(String.format(UPDATE_CUSTOMER, customerPojo.balance, customerPojo.id));
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void deleteCustomer(UUID id) {
        synchronized (generalMutex) {
            logger.debug(String.format("Method 'removeCustomer' was called with data: \n%s", id));

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(String.format(DELETE_CUSTOMER, id));
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public List<CustomerPojo> getCustomers() {
        synchronized (generalMutex) {
            logger.debug("Method 'getCustomers' was called.");

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(SELECT_CUSTOMERS);
                List<CustomerPojo> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(fillCustomerPojo(rs));
                }
                return result;
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public CustomerPojo getCustomer(UUID id) {
        synchronized (generalMutex) {
            logger.debug(String.format("Method 'getCustomer' was called with data '%s'.", id));

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(String.format(SELECT_CUSTOMER, id));
                if (rs.next()) {
                    return fillCustomerPojo(rs);
                } else {
                    throw new IllegalArgumentException("Customer with id '" + id + " was not found.");
                }
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public CustomerPojo getCustomerByLogin(String customerLogin) {
        synchronized (generalMutex) {
            logger.debug(String.format("Method 'lookupCustomerByLogin' was called with data '%s'.", customerLogin));

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(String.format(SELECT_CUSTOMER_BY_LOGIN, customerLogin));
                if (rs.next()) {
                    return fillCustomerPojo(rs);
                } else {
                    throw new IllegalArgumentException("Customer with login '" + customerLogin + " was not found");
                }
            } catch (SQLException ex) {
                logger.debug(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public PlanPojo createPlan(PlanPojo plan) {
        synchronized (generalMutex) {
            logger.debug(String.format("Method 'createPlan' was called with data '%s'.", plan));

            plan.id = UUID.randomUUID();
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(
                        String.format(
                                INSERT_PLAN,
                                plan.id,
                                plan.name,
                                plan.details,
                                plan.fee));
                return plan;
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void deletePlan(UUID id) {
        synchronized (generalMutex) {
            logger.debug(String.format("Method 'deletePlan' was called with data: \n%s", id));

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(String.format(DELETE_PLAN, id));
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public List<PlanPojo> getPlans() {
        synchronized (generalMutex) {
            logger.debug("Method 'getPlans' was called.");

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(SELECT_PLANS);
                List<PlanPojo> result = new ArrayList<>();
                while (rs.next()) {
                    PlanPojo planData = new PlanPojo();

                    planData.id = UUID.fromString(rs.getString(1));
                    planData.name = rs.getString(2);
                    planData.details = rs.getString(3);
                    planData.fee = rs.getInt(4);

                    result.add(planData);
                }
                return result;
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public SubscriptionPojo createSubscription(SubscriptionPojo subscriptionPojo) {
        synchronized (generalMutex) {
            logger.debug("Method 'createSubscription' was called with data '{}'.", subscriptionPojo);

            subscriptionPojo.id = UUID.randomUUID();
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(
                        String.format(
                                INSERT_SUBSCRIPTION,
                                subscriptionPojo.id,
                                subscriptionPojo.customerId,
                                subscriptionPojo.planId));
                return subscriptionPojo;
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void deleteSubscription(UUID id) {
        synchronized (generalMutex) {
            logger.debug("Method 'deleteSubscription' was called with data: \n{}", id);

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(String.format(DELETE_SUBSCRIPTION, id));
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public List<SubscriptionPojo> getSubscriptions() {
        synchronized (generalMutex) {
            logger.debug("Method 'getSubscriptions' was called.");

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(SELECT_ALL_SUBSCRIPTIONS);
                List<SubscriptionPojo> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(fillSubscriptionPojo(rs));
                }
                return result;
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public List<SubscriptionPojo> getSubscriptions(UUID customerId) {
        synchronized (generalMutex) {
            logger.debug("Method 'getSubscriptions' was called.");

            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(String.format(SELECT_SUBSCRIPTIONS, customerId));
                List<SubscriptionPojo> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(fillSubscriptionPojo(rs));
                }
                return result;
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    private CustomerPojo fillCustomerPojo(ResultSet resultSet) throws SQLException {
        CustomerPojo customerPojo = new CustomerPojo();

        customerPojo.id = UUID.fromString(resultSet.getString(1));
        customerPojo.firstName = resultSet.getString(2);
        customerPojo.lastName = resultSet.getString(3);
        customerPojo.login = resultSet.getString(4);
        customerPojo.pass = resultSet.getString(5);
        customerPojo.balance = resultSet.getInt(6);

        return customerPojo;
    }

    private SubscriptionPojo fillSubscriptionPojo(ResultSet resultSet) throws SQLException {
        SubscriptionPojo subscriptionPojo = new SubscriptionPojo();

        subscriptionPojo.id = UUID.fromString(resultSet.getString(1));
        subscriptionPojo.customerId = UUID.fromString(resultSet.getString(2));
        subscriptionPojo.planId = UUID.fromString(resultSet.getString(3));

        return subscriptionPojo;
    }

    private void init() {
        logger.debug("-------- MySQL JDBC Connection Testing ------------");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            logger.debug("Where is your MySQL JDBC Driver?", ex);
            throw new RuntimeException(ex);
        }

        logger.debug("MySQL JDBC Driver Registered!");

        try {
            connection = DriverManager
                    .getConnection(
                            "jdbc:mysql://localhost:3306/testmethods?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false",
                            "user",
                            "user");
        } catch (SQLException ex) {
            logger.error("Connection Failed! Check output console", ex);
            throw new RuntimeException(ex);
        }

        if (connection != null) {
            logger.debug("You made it, take control your database now!");
        } else {
            logger.error("Failed to make connection!");
        }
    }
}

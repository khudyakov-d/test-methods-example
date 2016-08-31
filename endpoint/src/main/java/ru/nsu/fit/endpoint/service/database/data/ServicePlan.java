package ru.nsu.fit.endpoint.service.database.data;

import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class ServicePlan {
    private UUID id;
    private String name;
    private String details;
    private int min;
    private int max;
    private int fee;
    private int feePerUnit;
}

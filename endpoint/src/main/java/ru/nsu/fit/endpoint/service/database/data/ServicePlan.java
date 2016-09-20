package ru.nsu.fit.endpoint.service.database.data;

import java.util.UUID;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class ServicePlan {
    private UUID id;
    /* Длина не больше 128 символов и не меньше 2 включительно не содержит спец символов */
    private String name;
    /* Длина не больше 1024 символов и не меньше 1 включительно */
    private String details;
    /* Не больше 999999 и не меньше 1 включительно */
    private int maxSeats;
    /* Не больше 999999 и не меньше 1 включительно, minSeats >= maxSeats */
    private int minSeats;
    /* Больше ли равно 0 но меньше либо равно 999999 */
    private int feePerUnit;
}

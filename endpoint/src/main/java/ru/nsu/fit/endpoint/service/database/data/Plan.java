package ru.nsu.fit.endpoint.service.database.data;

import java.util.UUID;

public class Plan {
    private UUID id;
    /* Длина не больше 128 символов и не меньше 2 включительно не содержит спец символов */
    private String name;
    /* Длина не больше 1024 символов и не меньше 1 включительно */
    private String details;
    /* Больше ли равно 0 но меньше либо равно 999999 */
    private int fee;
}

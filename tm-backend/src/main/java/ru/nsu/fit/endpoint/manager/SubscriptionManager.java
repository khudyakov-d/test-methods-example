package ru.nsu.fit.endpoint.manager;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import ru.nsu.fit.endpoint.database.IDBService;
import ru.nsu.fit.endpoint.database.data.PlanPojo;
import ru.nsu.fit.endpoint.database.data.SubscriptionPojo;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubscriptionManager extends ParentManager {
    public SubscriptionManager(IDBService dbService, Logger flowLog) {
        super(dbService, flowLog);
    }

    /**
     * Метод создает подписку. Ограничения:
     * 1. Подписки с таким планом пользователь не имеет.
     * 2. Стоймость подписки не превышает текущего баланса кастомера и после покупки вычитается из его баласа.
     */
    public SubscriptionPojo createSubscription(SubscriptionPojo subscriptionPojo) {
        return dbService.createSubscription(subscriptionPojo);
    }

    public void deleteSubscription(UUID subscriptionId) {
        dbService.deleteSubscription(subscriptionId);
    }

    /**
     * Возвращает список подписок для указанного customer'а.
     *
     * Лабораторная_2: Покрыть данный метод юнит тестами.
     */
    public List<SubscriptionPojo> getSubscriptions(UUID customerId) {
        Map<UUID, PlanPojo> planIdToPlan = dbService.getPlans().stream()
                .collect(Collectors.toMap(plan -> plan.id, plan -> plan));

        List<SubscriptionPojo> subscriptions;
        if (customerId == null) {
            subscriptions = dbService.getSubscriptions();
        } else {
            subscriptions = dbService.getSubscriptions(customerId);
        }

        // Дозаполняем поля, типа planName, planDetails и planFee.
        for (SubscriptionPojo subscription : subscriptions) {
            PlanPojo plan = planIdToPlan.getOrDefault(subscription.planId, null);
            if (plan != null) {
                subscription.planName = plan.name;
                subscription.planDetails = plan.details;
                subscription.planFee = plan.fee;
            }
        }

        return subscriptions;
    }
}

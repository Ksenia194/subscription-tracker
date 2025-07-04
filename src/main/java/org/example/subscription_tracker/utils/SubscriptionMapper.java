package org.example.subscription_tracker.utils;

import org.example.subscription_tracker.dto.SubscriptionDTO;
import org.example.subscription_tracker.entity.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {
    public Subscription toEntity(SubscriptionDTO dto) {
        Subscription subscription = new Subscription();
        subscription.setServiceName(dto.getServiceName());
        subscription.setPrice(dto.getPrice());
        subscription.setStartDate(dto.getStartDate());
        subscription.setEndDate(dto.getEndDate());
        subscription.setAutoRenew(dto.isAutoRenew());
        return subscription;
    }

    public SubscriptionDTO toDto(Subscription entity) {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO(
                entity.getServiceName(),
                entity.getPrice(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isAutoRenew()
        );
        return subscriptionDTO;
    }
}

package org.example.subscription_tracker.service;

import org.example.subscription_tracker.entity.Subscription;
import org.example.subscription_tracker.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public Optional<Subscription> findById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Subscription update(Subscription subscription, Long id) {
        return subscriptionRepository.findById(id)
                .map(existing -> {
                    existing.setServiceName(subscription.getServiceName());
                    existing.setPrice(subscription.getPrice());
                    existing.setStartDate(subscription.getStartDate());
                    existing.setEndDate(subscription.getEndDate());
                    existing.setAutoRenew(subscription.isAutoRenew());
                    return subscriptionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Subscription with ID " + id + " not found"));
    }

    public void delete(Subscription subscription) {
        subscriptionRepository.delete(subscription);
    }
}

package org.example.subscription_tracker.controller;

import jakarta.validation.Valid;
import org.example.subscription_tracker.entity.Subscription;
import org.example.subscription_tracker.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/subscriptions")
    public List<Subscription> getAllSubscriptions() {
        return subscriptionService.findAll();
    }

    @GetMapping("/subscriptions/{id}")
    public Subscription getByIdSubscriptions(@PathVariable Long id) {
        return subscriptionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription with ID " + id + " not found"));
    }

    @PostMapping("/subscriptions")
    public Subscription saveSubscription(@RequestBody @Valid Subscription subscription) {
        return subscriptionService.save(subscription);
    }

    @PutMapping("/subscriptions/{id}")
    public Subscription updateSubscription(@RequestBody @Valid Subscription subscription, @PathVariable Long id) {
        return subscriptionService.update(subscription, id);
    }

    @DeleteMapping("/subscriptions/{id}")
    public void deleteSubscription(@PathVariable Long id) {
        Subscription subscription = subscriptionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription with ID " + id + " not found"));
        subscriptionService.delete(subscription);
    }
}

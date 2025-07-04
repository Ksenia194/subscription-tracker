package org.example.subscription_tracker.controller;

import jakarta.validation.Valid;
import org.example.subscription_tracker.dto.SubscriptionDTO;
import org.example.subscription_tracker.entity.Subscription;
import org.example.subscription_tracker.service.SubscriptionService;
import org.example.subscription_tracker.utils.SubscriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @GetMapping("/subscriptions")
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionService.findAll().stream()
                .map(subscriptionMapper::toDto)
                .toList();
    }

    @GetMapping("/subscriptions/{id}")
    public SubscriptionDTO getByIdSubscriptions(@PathVariable Long id) {
        Subscription subscription = subscriptionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription with ID " + id + " not found"));
        return subscriptionMapper.toDto(subscription);
    }

    @PostMapping("/subscriptions")
    public SubscriptionDTO saveSubscription(@RequestBody @Valid SubscriptionDTO subscriptionDTO) {
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO);
        Subscription saved = subscriptionService.save(subscription);
        return subscriptionMapper.toDto(saved);
    }

    @PutMapping("/subscriptions/{id}")
    public SubscriptionDTO updateSubscription(@RequestBody @Valid SubscriptionDTO subscriptionDTO, @PathVariable Long id) {
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO);
        Subscription updated = subscriptionService.update(subscription, id);
        return subscriptionMapper.toDto(updated);
    }

    @DeleteMapping("/subscriptions/{id}")
    public void deleteSubscription(@PathVariable Long id) {
        Subscription subscription = subscriptionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription with ID " + id + " not found"));
        subscriptionService.delete(subscription);
    }
}

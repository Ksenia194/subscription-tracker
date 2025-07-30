package org.example.subscription_tracker.controller;

import jakarta.validation.Valid;
import org.example.subscription_tracker.dto.SubscriptionDTO;
import org.example.subscription_tracker.service.SubscriptionService;
import org.example.subscription_tracker.utils.SubscriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return subscriptionService.findAll();
    }

    @GetMapping("/subscriptions/{id}")
    public SubscriptionDTO getByIdSubscriptions(@PathVariable Long id) {
        return subscriptionService.findById(id);
    }

    @GetMapping("/subscriptions/filter")
    public List<SubscriptionDTO> getFilteredSubscriptions(@RequestParam(required = false) String serviceName,
                                                          @RequestParam(required = false) Boolean active) {
        return subscriptionService.findFiltered(serviceName, active);
    }

    @PostMapping("/subscriptions")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDTO saveSubscription(@RequestBody @Valid SubscriptionDTO subscriptionDTO) {
        return subscriptionService.save(subscriptionDTO);
    }

    @PutMapping("/subscriptions/{id}")
    public SubscriptionDTO updateSubscription(@RequestBody @Valid SubscriptionDTO subscriptionDTO, @PathVariable Long id) {
        return subscriptionService.update(subscriptionDTO, id);
    }

    @DeleteMapping("/subscriptions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(@PathVariable Long id) {
        subscriptionService.delete(id);
    }
}

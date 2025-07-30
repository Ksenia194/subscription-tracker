package org.example.subscription_tracker.controller;

import org.example.subscription_tracker.service.SubscriptionService;
import org.example.subscription_tracker.utils.SubscriptionMapper;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public SubscriptionService subscriptionService() {
        return Mockito.mock(SubscriptionService.class);
    }

    @Bean
    public SubscriptionMapper subscriptionMapper() {
        return Mockito.mock(SubscriptionMapper.class);
    }
}
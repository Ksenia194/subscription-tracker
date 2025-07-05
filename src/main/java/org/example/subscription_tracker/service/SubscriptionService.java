package org.example.subscription_tracker.service;

import org.example.subscription_tracker.dto.SubscriptionDTO;
import org.example.subscription_tracker.entity.Subscription;
import org.example.subscription_tracker.repository.SubscriptionRepository;
import org.example.subscription_tracker.utils.SubscriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<SubscriptionDTO> findAll() {
        return subscriptionRepository.findAll()
                .stream()
                .map(subscriptionMapper::toDto)
                .toList();
    }

    public SubscriptionDTO findById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        return subscriptionMapper.toDto(subscription);
    }

    public SubscriptionDTO save(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO);
        Subscription saved = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDto(saved);
    }

    public SubscriptionDTO update(SubscriptionDTO subscriptionDTO, Long id) {

        Subscription updated = subscriptionRepository.findById(id)
                .map(existing -> {
                    existing.setServiceName(subscriptionDTO.getServiceName());
                    existing.setPrice(subscriptionDTO.getPrice());
                    existing.setStartDate(subscriptionDTO.getStartDate());
                    existing.setEndDate(subscriptionDTO.getEndDate());
                    existing.setAutoRenew(subscriptionDTO.isAutoRenew());
                    return subscriptionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Subscription with ID " + id + " not found"));
        return subscriptionMapper.toDto(updated);
    }

    public void delete(Long id) {
        Subscription deleted = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription with ID " + id + " not found"));
        subscriptionRepository.delete(deleted);
    }
}

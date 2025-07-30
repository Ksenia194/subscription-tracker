package org.example.subscription_tracker.service;

import org.example.subscription_tracker.dto.SubscriptionDTO;
import org.example.subscription_tracker.entity.Subscription;
import org.example.subscription_tracker.repository.SubscriptionRepository;
import org.example.subscription_tracker.utils.SubscriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, SubscriptionMapper subscriptionMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionMapper = subscriptionMapper;
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

    public List<SubscriptionDTO> findFiltered(String serviceName, Boolean active) {
        List<SubscriptionDTO> mappedList = subscriptionRepository.findAll()
                .stream()
                .map(subscriptionMapper::toDto)
                .filter(s -> serviceName == null || serviceName.isBlank() || s.getServiceName().equalsIgnoreCase(serviceName))
                .filter(s -> active == null ||
                        (active && s.getEndDate().isAfter(LocalDate.now())) ||
                        (!active && s.getEndDate().isBefore(LocalDate.now())))
                .toList();
        return mappedList;
    }
}

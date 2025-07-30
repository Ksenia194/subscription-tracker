package org.example.subscription_tracker.service;

import org.example.subscription_tracker.dto.SubscriptionDTO;
import org.example.subscription_tracker.entity.Subscription;
import org.example.subscription_tracker.repository.SubscriptionRepository;
import org.example.subscription_tracker.utils.SubscriptionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    private SubscriptionRepository subscriptionRepository;
    private SubscriptionMapper subscriptionMapper;
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        subscriptionRepository = mock(SubscriptionRepository.class);
        subscriptionMapper = mock(SubscriptionMapper.class);
        subscriptionService = new SubscriptionService(subscriptionRepository, subscriptionMapper);
    }

    @Test
    void testFindByIdReturnsCorrectSubscription() {
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setServiceName("Netflix");
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        SubscriptionDTO result = subscriptionService.findById(1L);

        assertNotNull(result);
        assertEquals("Netflix", result.getServiceName());
    }

    @Test
    void testFindByIdThrowsWhenNotFound() {
        when(subscriptionRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> subscriptionService.findById(99L));

        assertEquals("Subscription not found", ex.getMessage());
    }

    @Test
    void testFindAllReturnsMappedDTOs() {
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setServiceName("Netflix");

        Subscription subscription1 = new Subscription();
        subscription1.setId(2L);
        subscription1.setServiceName("YouTube");

        when(subscriptionRepository.findAll()).thenReturn(List.of(subscription, subscription1));

        List<SubscriptionDTO> result = subscriptionService.findAll();

        assertEquals(2, result.size());
        assertEquals("Netflix", result.get(0).getServiceName());
        assertEquals("YouTube", result.get(1).getServiceName());
    }

    @Test
    void testSaveSavedCorrectSubscription() {
        SubscriptionDTO dto = new SubscriptionDTO(1L, "Netflix", 99.00,
                LocalDate.now(), LocalDate.now().plusMonths(8), true);

        Subscription entity = new Subscription();
        entity.setId(1L);
        entity.setServiceName("Netflix");
        entity.setPrice(99.00);
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setAutoRenew(true);

        when(subscriptionMapper.toEntity(dto)).thenReturn(entity);
        when(subscriptionRepository.save(entity)).thenReturn(entity);
        when(subscriptionMapper.toDto(entity)).thenReturn(dto);

        SubscriptionDTO result = subscriptionService.save(dto);

        assertNotNull(result);
        assertEquals("Netflix", result.getServiceName());
        assertEquals(99.00, result.getPrice());
    }

    @Test
    void testUpdateShouldUpdateAndReturnDto() {
        Long id = 1L;

        SubscriptionDTO dto = new SubscriptionDTO(id, "Spotify", 50.0, LocalDate.now(),
                LocalDate.now().plusMonths(9), false);

        Subscription existing = new Subscription();
        existing.setId(id);
        existing.setServiceName("Spotifi");
        existing.setPrice(50.0);
        existing.setStartDate(LocalDate.now().minusDays(10));
        existing.setEndDate(LocalDate.now().plusDays(10));
        existing.setAutoRenew(true);

        Subscription updated = new Subscription();
        updated.setId(id);
        updated.setServiceName("Spotify");
        updated.setPrice(50.0);
        updated.setStartDate(dto.getStartDate());
        updated.setEndDate(dto.getEndDate());
        updated.setAutoRenew(false);

        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(existing));
        when(subscriptionRepository.save(existing)).thenReturn(updated);
        when(subscriptionMapper.toDto(updated)).thenReturn(dto);

        SubscriptionDTO result = subscriptionService.update(dto, id);

        assertNotNull(result);
        assertEquals("Spotify", result.getServiceName());
        assertEquals(50.0, result.getPrice());
        assertEquals(dto.getStartDate(), result.getStartDate());
        assertFalse(result.isAutoRenew());
    }

    @Test
    void testDeleteShouldDeleteSubscription() {
        Long id = 1L;
        Subscription subscription = new Subscription();
        subscription.setId(id);

        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(subscription));

        subscriptionService.delete(id);

        verify(subscriptionRepository).delete(subscription);
    }

    @Test
    void testDeleteShouldThrowWhenSubscriptionNotFound() {
        Long id = 1L;

        when(subscriptionRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            subscriptionService.delete(id);
        });

        assertEquals("Subscription with ID " + id + " not found", exception.getMessage());
    }

    @Test
    void testFindFilteredReturnsAllWhenNoFilters() {
        List<Subscription> subscriptions = List.of(
                new Subscription("Netflix", 100.0, LocalDate.now().minusDays(1), LocalDate.now().plusDays(10), true),
                new Subscription("Spotify", 99.0, LocalDate.now().minusDays(5), LocalDate.now().plusDays(2), true)
        );

        when(subscriptionRepository.findAll()).thenReturn(subscriptions);
        when(subscriptionMapper.toDto(any())).thenAnswer(i -> {
            Subscription s = i.getArgument(0);
            return new SubscriptionDTO(null, s.getServiceName(), s.getPrice(), s.getStartDate(), s.getEndDate(), s.isAutoRenew());
        });

        List<SubscriptionDTO> result = subscriptionService.findFiltered(null, null);

        assertEquals(2, result.size());
    }

    @Test
    void testFindFilteredByServiceName() {
        List<Subscription> subscriptions = List.of(
                new Subscription("Netflix", 199.0, LocalDate.now(), LocalDate.now().plusDays(5), true),
                new Subscription("Spotify", 99.0, LocalDate.now(), LocalDate.now().plusDays(5), true)
        );

        when(subscriptionRepository.findAll()).thenReturn(subscriptions);
        when(subscriptionMapper.toDto(any())).thenAnswer(i -> {
            Subscription s = i.getArgument(0);
            return new SubscriptionDTO(null, s.getServiceName(), s.getPrice(), s.getStartDate(), s.getEndDate(), s.isAutoRenew());
        });
        
        List<SubscriptionDTO> result = subscriptionService.findFiltered("Netflix", null);
        
        assertEquals(1, result.size());
        assertEquals("Netflix", result.get(0).getServiceName());
    }

    @Test
    void testFindFilteredByActiveTrue() {
        List<Subscription> subscriptions = List.of(
                new Subscription("Netflix", 199.0, LocalDate.now(), LocalDate.now().plusDays(5), true),  // активна
                new Subscription("Spotify", 99.0, LocalDate.now(), LocalDate.now().minusDays(1), true)  // неактивна
        );

        when(subscriptionRepository.findAll()).thenReturn(subscriptions);
        when(subscriptionMapper.toDto(any())).thenAnswer(i -> {
            Subscription s = i.getArgument(0);
            return new SubscriptionDTO(null, s.getServiceName(), s.getPrice(), s.getStartDate(), s.getEndDate(), s.isAutoRenew());
        });

        List<SubscriptionDTO> result = subscriptionService.findFiltered(null, true);

        assertEquals(1, result.size());
        assertEquals("Netflix", result.get(0).getServiceName());
    }

    @Test
    void testFindFilteredByActiveFalse() {
        List<Subscription> subscriptions = List.of(
                new Subscription("Netflix", 199.0, LocalDate.now(), LocalDate.now().plusDays(5), true),  // активна
                new Subscription("Spotify", 99.0, LocalDate.now(), LocalDate.now().minusDays(1), true)  // неактивна
        );

        when(subscriptionRepository.findAll()).thenReturn(subscriptions);
        when(subscriptionMapper.toDto(any())).thenAnswer(i -> {
            Subscription s = i.getArgument(0);
            return new SubscriptionDTO(null, s.getServiceName(), s.getPrice(), s.getStartDate(), s.getEndDate(), s.isAutoRenew());
        });

        List<SubscriptionDTO> result = subscriptionService.findFiltered(null, false);

        assertEquals(1, result.size());
        assertEquals("Spotify", result.get(0).getServiceName());
    }

    @Test
    void testFindFilteredActiveNetflix() {
        Subscription sub1 = new Subscription();
        sub1.setServiceName("Netflix");
        sub1.setEndDate(LocalDate.now().plusDays(10));

        Subscription sub2 = new Subscription();
        sub2.setServiceName("Netflix");
        sub2.setEndDate(LocalDate.now().minusDays(5));

        Subscription sub3 = new Subscription();
        sub3.setServiceName("Spotify");
        sub3.setEndDate(LocalDate.now().plusDays(10));

        when(subscriptionRepository.findAll()).thenReturn(List.of(sub1, sub2, sub3));

        when(subscriptionMapper.toDto(sub1)).thenReturn(new SubscriptionDTO(
                null, "Netflix", 100.0,
                LocalDate.now().minusDays(1),
                sub1.getEndDate(),
                true
        ));

        when(subscriptionMapper.toDto(sub2)).thenReturn(new SubscriptionDTO(
                null, "Netflix", 100.0,
                LocalDate.now().minusDays(1),
                sub2.getEndDate(),
                true
        ));

        when(subscriptionMapper.toDto(sub3)).thenReturn(new SubscriptionDTO(
                null, "Spotify", 100.0,
                LocalDate.now().minusDays(1),
                sub3.getEndDate(),
                true
        ));


        List<SubscriptionDTO> result = subscriptionService.findFiltered("Netflix", true);

        assertEquals(1, result.size());
        assertEquals("Netflix", result.get(0).getServiceName());
    }


}
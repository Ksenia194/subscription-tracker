package org.example.subscription_tracker.controller;

import org.example.subscription_tracker.dto.SubscriptionDTO;
import org.example.subscription_tracker.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
@Import(TestConfig.class)
class SubscriptionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private SubscriptionService subscriptionService;

    @Test
    void testGetByIdReturnsDto() throws Exception {
        SubscriptionDTO dto = new SubscriptionDTO(1L, "Netflix", 199.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                true);

        when(subscriptionService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/subscriptions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.serviceName").value("Netflix"))
                .andExpect(jsonPath("$.price").value(199.0));
    }

    @Test
    void testGetByIdReturnsNotFoundWhenMissing() throws Exception {
        when(subscriptionService.findById(100L))
                .thenThrow(new RuntimeException("Subscription not found"));

        mockMvc.perform(get("/subscriptions/100"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Subscription not found"));
    }

    @Test
    void testGetAllReturnsDto() throws Exception {
        SubscriptionDTO dto1 = new SubscriptionDTO(1L, "Netflix", 199.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                true);
        SubscriptionDTO dto2 = new SubscriptionDTO(2L, "Spotify", 159.0,
                LocalDate.of(2025, 2, 3),
                LocalDate.of(2025, 6, 30),
                false);

        when(subscriptionService.findAll()).thenReturn(List.of(dto1,dto2));

        mockMvc.perform(get("/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].serviceName").value("Netflix"))
                .andExpect(jsonPath("$[0].price").value(199.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].serviceName").value("Spotify"))
                .andExpect(jsonPath("$[1].price").value(159.0));
    }

    @Test
    void testCreateSubscription() throws Exception {
        SubscriptionDTO requestDto = new SubscriptionDTO(null, "YouTube", 149.0,
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 12, 31), true);
        SubscriptionDTO responseDto = new SubscriptionDTO(1L, "YouTube", 149.0,
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 12, 31), true);

        when(subscriptionService.save(any(SubscriptionDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/subscriptions")
                        .contentType("application/json")
                        .content("""
                            {
                              "serviceName": "YouTube",
                              "price": 149.0,
                              "startDate": "2025-03-01",
                              "endDate": "2025-12-31",
                              "autoRenew": true
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.serviceName").value("YouTube"))
                .andExpect(jsonPath("$.price").value(149.0))
                .andExpect(jsonPath("$.autoRenew").value(true));
    }

    @Test
    void testCreateInvalidReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/subscriptions")
                        .contentType("application/json")
                        .content("""
                        {
                          "serviceName": "",
                          "price": 149.0,
                          "startDate": "2025-03-01",
                          "endDate": "2025-12-31",
                          "autoRenew": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateSubscription() throws Exception {
        SubscriptionDTO requestDto = new SubscriptionDTO(null, "Netflix Premium", 299.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31), true);

        SubscriptionDTO updatedDto = new SubscriptionDTO(1L, "Netflix Premium", 299.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31), true);

        when(subscriptionService.update(any(SubscriptionDTO.class), eq(1L))).thenReturn(updatedDto);

        mockMvc.perform(put("/subscriptions/1")
                        .contentType("application/json")
                        .content("""
                        {
                          "serviceName": "Netflix Premium",
                          "price": 299.0,
                          "startDate": "2025-01-01",
                          "endDate": "2025-12-31",
                          "autoRenew": true
                        }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.serviceName").value("Netflix Premium"))
                .andExpect(jsonPath("$.price").value(299.0));
    }

    @Test
    void testDeleteSubscription() throws Exception {
        mockMvc.perform(delete("/subscriptions/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetFilteredSubscriptions() throws Exception {
        SubscriptionDTO dto = new SubscriptionDTO(1L, "Netflix", 199.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                true);

        when(subscriptionService.findFiltered("Netflix", true)).thenReturn(List.of(dto));

        mockMvc.perform(get("/subscriptions/filter")
                        .param("serviceName", "Netflix")
                        .param("active", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].serviceName").value("Netflix"))
                .andExpect(jsonPath("$[0].autoRenew").value(true));
    }

}
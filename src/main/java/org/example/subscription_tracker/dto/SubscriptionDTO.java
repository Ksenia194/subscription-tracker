package org.example.subscription_tracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class SubscriptionDTO {

    public SubscriptionDTO() {
    }

    private Long id;

    @NotBlank(message = "Name must not be blank")
    private String serviceName;

    @NotNull
    @Min(value = 1, message = "Price must be at least 1")
    private Double price;

    @NotNull(message = "Start date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private boolean autoRenew;

    public SubscriptionDTO(Long id, String serviceName, Double price, LocalDate startDate, LocalDate endDate, boolean autoRenew) {
        this.id = id;
        this.serviceName = serviceName;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.autoRenew = autoRenew;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(boolean autoRenew) {
        this.autoRenew = autoRenew;
    }
}


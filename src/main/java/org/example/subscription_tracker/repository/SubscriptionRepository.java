package org.example.subscription_tracker.repository;

import org.example.subscription_tracker.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository  extends JpaRepository<Subscription, Long> {

}

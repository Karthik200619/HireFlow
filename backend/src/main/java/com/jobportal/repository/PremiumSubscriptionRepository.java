package com.jobportal.repository;
import com.jobportal.entity.PremiumSubscription; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface PremiumSubscriptionRepository extends JpaRepository<PremiumSubscription,Long>{ Optional<PremiumSubscription> findByUserId(Long id); }

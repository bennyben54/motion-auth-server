package com.beo.motionauthserver.repository;

import com.beo.motionauthserver.entity.Subscription;
import com.beo.motionauthserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<User> findByUsername(String username);

}

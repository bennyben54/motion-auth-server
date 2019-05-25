package com.beo.motionauthserver.repository;

import com.beo.motionauthserver.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {

}

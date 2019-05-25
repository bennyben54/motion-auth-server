package com.beo.motionauthserver.repository;

import com.beo.motionauthserver.entity.Authority;
import com.beo.motionauthserver.entity.AuthorityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, AuthorityId> {

    Set<Authority> findByUsername(String username);

}

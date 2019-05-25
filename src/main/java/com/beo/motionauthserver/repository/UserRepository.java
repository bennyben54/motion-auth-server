package com.beo.motionauthserver.repository;

import com.beo.motionauthserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}

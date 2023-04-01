package com.digicert.assesment.usermanagementservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digicert.assesment.usermanagementservices.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}

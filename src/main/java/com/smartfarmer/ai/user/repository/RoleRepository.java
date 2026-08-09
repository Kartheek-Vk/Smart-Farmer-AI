package com.smartfarmer.ai.user.repository;

import com.smartfarmer.ai.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    java.util.Optional<Role> findByName(com.smartfarmer.ai.common.enums.UserRole name);
}

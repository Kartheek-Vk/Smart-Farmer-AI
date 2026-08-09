package com.smartfarmer.ai.user.entity;

import com.smartfarmer.ai.common.enums.UserStatus;
import com.smartfarmer.ai.common.enums.UserRole;
import com.smartfarmer.ai.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Helper to get roles as enum set
    public Set<UserRole> getRolesAsEnumSet() {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}

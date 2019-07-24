package com.tracker.repository;

import com.tracker.domain.Role;
import com.tracker.domain.enums.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByRole(UserRole authority);
}

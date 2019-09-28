package com.tracker.repository;

import com.tracker.domain.Role;
import com.tracker.domain.enums.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Exposes operations on the {@link Role} domain entity.
 *
 * @see Role
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {


    /**
     * Find by {@link UserRole}.
     *
     * @param authority the authority
     * @return the role or {@link Optional#empty()} if none found
     */
    Optional<Role> findByRole(UserRole authority);
}

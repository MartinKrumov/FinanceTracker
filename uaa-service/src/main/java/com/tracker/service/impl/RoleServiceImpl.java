package com.tracker.service.impl;

import com.tracker.domain.Role;
import com.tracker.domain.enums.UserRole;
import com.tracker.repository.RoleRepository;
import com.tracker.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "roles")
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Cacheable(key = "#userRole")
    public Role findByUserRole(UserRole userRole) {
        return roleRepository.findByRole(userRole)
                .orElseThrow(() -> new EntityNotFoundException(userRole + " not found"));
    }

}

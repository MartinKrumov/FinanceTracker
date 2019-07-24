package com.tracker.service.impl;

import com.tracker.domain.Role;
import com.tracker.domain.enums.UserRole;
import com.tracker.repository.RoleRepository;
import com.tracker.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getUserRole() {
        return roleRepository.findByRole(UserRole.USER)
                .orElseThrow();
    }
}

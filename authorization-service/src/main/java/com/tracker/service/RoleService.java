package com.tracker.service;

import com.tracker.domain.Role;
import com.tracker.domain.enums.UserRole;

public interface RoleService {

    Role findByUserRole(UserRole userRole);

}

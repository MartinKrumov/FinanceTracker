package com.financetracker.area.user.services;

import com.financetracker.area.user.domain.Authority;

public interface AuthorityService {
    Authority findByName(String name);
    Authority getUserRole();
}

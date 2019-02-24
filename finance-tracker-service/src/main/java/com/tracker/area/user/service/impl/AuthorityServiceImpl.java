package com.tracker.area.user.service.impl;

import com.tracker.area.user.domain.Authority;
import com.tracker.area.user.repositories.AuthorityRepository;
import com.tracker.area.user.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private static final String ROLE_USER = "ROLE_USER";
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority getUserRole() {
        return authorityRepository.findOneByAuthority(ROLE_USER)
                .orElseGet(() -> authorityRepository.save(new Authority(ROLE_USER)));
    }
}

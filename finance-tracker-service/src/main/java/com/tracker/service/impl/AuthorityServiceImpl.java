package com.tracker.service.impl;

import com.tracker.domain.Authority;
import com.tracker.repository.AuthorityRepository;
import com.tracker.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private static final String ROLE_USER = "USER";
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority getUserRole() {
        return authorityRepository.findByAuthority(ROLE_USER)
                .orElseThrow();
    }
}

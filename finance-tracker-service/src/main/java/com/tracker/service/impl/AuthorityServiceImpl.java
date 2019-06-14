package com.tracker.service.impl;

import com.tracker.domain.Authority;
import com.tracker.domain.enums.UserAuthority;
import com.tracker.repository.AuthorityRepository;
import com.tracker.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    public Authority getUserRole() {
        return authorityRepository.findByAuthority(UserAuthority.USER)
                .orElseThrow();
    }
}

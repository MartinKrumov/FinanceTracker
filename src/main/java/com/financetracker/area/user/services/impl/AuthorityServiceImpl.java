package com.financetracker.area.user.services.impl;

import com.financetracker.area.user.domain.Authority;
import com.financetracker.area.user.repositories.AuthorityRepository;
import com.financetracker.area.user.services.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    public Authority findByName(String name) {
        return this.authorityRepository.findOneByAuthority(name);
    }

    @Override
    public Authority getUserRole() {
        Authority authority = this.findByName("ROLE_USER");

        if(authority == null){
            authority = new Authority("ROLE_USER");
            this.authorityRepository.save(authority);
        }

        return authority;
    }
}

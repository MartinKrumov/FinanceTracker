package com.financetracker.area.user.repositories;

import com.financetracker.area.user.domain.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Authority findOneByAuthority(String authority);
}

package com.tracker.repository;

import com.tracker.domain.Authority;
import com.tracker.domain.enums.UserAuthority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Optional<Authority> findByAuthority(UserAuthority authority);
}

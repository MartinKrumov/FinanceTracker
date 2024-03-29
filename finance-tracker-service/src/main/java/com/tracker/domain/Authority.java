package com.tracker.domain;

import com.tracker.domain.enums.UserAuthority;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false)
    private UserAuthority authority;

    @Override
    public String getAuthority() {
        return authority.toString();
    }
}


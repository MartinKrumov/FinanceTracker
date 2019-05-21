package com.tracker.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

@Data
@Entity
@ToString(exclude = {"wallets"})
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, updatable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false)
    private Set<Wallet> wallets;

    @Column(nullable = false)
    private LocalDateTime date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false)
    private Set<Category> categories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities = new HashSet<>();

    public void addWallet(@NotNull Wallet wallet) {
        if (isNull(wallets)) {
            wallets = new HashSet<>();
        }
        wallets.add(wallet);
    }

    public void addCategory(@NotNull Category category) {
        if (isNull(categories)) {
            categories = new HashSet<>();
        }
        categories.add(category);
    }
}

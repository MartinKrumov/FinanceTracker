package com.tracker.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "previous_passwords")
public class PreviousPassword implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "previous_passwords_id_seq")
    @SequenceGenerator(name = "previous_passwords_id_seq", sequenceName = "previous_passwords_id_seq")
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private Instant createdAt;
}

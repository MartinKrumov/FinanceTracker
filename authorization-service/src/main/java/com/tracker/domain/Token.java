package com.tracker.domain;

import com.tracker.domain.enums.TokenType;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tokens_id_seq")
    @SequenceGenerator(name = "tokens_id_seq", sequenceName = "tokens_id_seq")
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    private TokenType tokenType;

}

package com.tracker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base abstract class for entities which will hold definitions for created date, created by, last modified by,
 * last modified date.
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract class AbstractAuditingEntity implements Serializable {

    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 64, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Instant createdDate;

    @LastModifiedBy
    @Column(name = "updated_by", length = 64)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant lastModifiedDate;

}

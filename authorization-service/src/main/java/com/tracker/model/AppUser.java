package com.tracker.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @Column
    private String userEmail;

    @Column
    private String userPass;

    @Column
    private String userRole;
}

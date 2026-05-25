package com.pm.auth_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(unique = true,nullable=false)
    private String email;

    @Column(unique = true,nullable=false)
    private String password;

    @Column(nullable=false)
    private String role;


}

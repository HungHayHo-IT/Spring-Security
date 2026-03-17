package com.example.spring_security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "customer")
@Getter @Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String pwd;
    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "customer" , fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Authority> authorities;

}

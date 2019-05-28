package com.beo.motionauthserver.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends Account {

    @Column(nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

}

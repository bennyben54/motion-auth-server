package com.beo.motionauthserver.entity;

import com.beo.motionauthserver.entity.enums.AuthorityType;
import lombok.Data;

import java.io.Serializable;

@Data
public class AuthorityId implements Serializable {
    private String username;
    private AuthorityType authority;
}

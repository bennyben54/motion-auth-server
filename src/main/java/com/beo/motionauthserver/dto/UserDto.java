package com.beo.motionauthserver.dto;

import com.beo.motionauthserver.entity.enums.AuthorityType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class UserDto {

    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;
    private List<AuthorityType> authorities = new ArrayList<>();

}

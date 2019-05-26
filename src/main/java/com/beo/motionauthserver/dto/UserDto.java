package com.beo.motionauthserver.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;

}

package com.beo.motionauthserver.configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthPasswordEncoder extends BCryptPasswordEncoder {

    public AuthPasswordEncoder() {
        super(15);
    }

}

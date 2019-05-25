package com.beo.motionauthserver.configuration;

import com.beo.motionauthserver.entity.Authority;
import com.beo.motionauthserver.entity.User;
import com.beo.motionauthserver.entity.enums.AuthorityType;
import com.beo.motionauthserver.repository.UserRepository;
import com.beo.motionauthserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        createAdminIfDoesntExist();
        auth.userDetailsService(userService) // use our custom userService for AuthenticationManagerBuilder
                .passwordEncoder(passwordEncoder);
    }

    private void createAdminIfDoesntExist() {
        userRepository.findByUsername("admin")
                .orElseGet(
                        () -> {
                            User admin = new User();
                            admin.setUsername("admin");
                            admin.setPassword(passwordEncoder.encode("admin"));
                            admin.setEnabled(true);
                            admin.getAuthorities().addAll(
                                    Arrays.stream(AuthorityType.values())
                                            .map(authorityType -> new Authority(null, admin, authorityType))
                                            .collect(Collectors.toList())
                            );
                            return userRepository.save(admin);
                        }
                );
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

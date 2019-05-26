package com.beo.motionauthserver.service;

import com.beo.motionauthserver.configuration.AuthPasswordEncoder;
import com.beo.motionauthserver.dto.UserDto;
import com.beo.motionauthserver.entity.Authority;
import com.beo.motionauthserver.entity.User;
import com.beo.motionauthserver.entity.enums.AuthorityType;
import com.beo.motionauthserver.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final AuthPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(AuthPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameAndEnabled(username, true).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
        builder.password(user.getPassword());
        builder.roles(user.getAuthorities().stream().map(authority -> authority.getAuthority().name()).toArray(String[]::new));

        return builder.build();
    }

    public User findUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public UserDto createUser(UserDto userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("User is null");
        }
        if (Strings.isEmpty(userDto.getUsername())) {
            throw new IllegalArgumentException("username is empty or null");
        }
        if (Strings.isEmpty(userDto.getPassword())) {
            throw new IllegalArgumentException("password is empty or null");
        }
        userRepository.findByUsername(userDto.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("User already exists");
        });
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(false);

        user.getAuthorities().add(new Authority(null, user, AuthorityType.USER));

        userDto.setId(userRepository.save(user).getId());
        userDto.setEnabled(false);
        userDto.setPassword(null);

        return userDto;
    }

    public Boolean toggleUserActivation(UUID id, Boolean enabled) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(Boolean.TRUE.equals(enabled));
        return enabled;
    }

    public void deleteUser(String username) {
        userRepository.findByUsername(username).ifPresent(userRepository::delete);
    }


    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(
                user -> {
                    UserDto userDto = new UserDto();
                    return userDto;
                }
        ).collect(Collectors.toList());
    }
}

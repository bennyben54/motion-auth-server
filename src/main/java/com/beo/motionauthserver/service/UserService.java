package com.beo.motionauthserver.service;

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

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
        builder.password(user.getPassword());
        builder.roles(user.getAuthorities().stream().map(authority -> authority.getAuthority().name()).toArray(String[]::new));

        return builder.build();
    }

    public User findUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
        if (Strings.isEmpty(user.getUsername())) {
            throw new IllegalArgumentException("username is empty or null");
        }
        if (Strings.isEmpty(user.getPassword())) {
            throw new IllegalArgumentException("password is empty or null");
        }
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("User already exists");
        });
        user.setEnabled(false);

        user.getAuthorities().add(new Authority(null, user, AuthorityType.USER));

        return userRepository.save(user);
    }

    public User toggleUserActivation(String username, boolean enabled) {
        final User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    public void deleteUser(String username) {
        userRepository.findByUsername(username).ifPresent(userRepository::delete);
    }


}

package com.beo.motionauthserver.service;

import com.beo.motionauthserver.entity.Authority;
import com.beo.motionauthserver.entity.User;
import com.beo.motionauthserver.entity.enums.AuthorityType;
import com.beo.motionauthserver.repository.AuthorityRepository;
import com.beo.motionauthserver.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    public User findUser(String username) {
        return userRepository.findById(username).orElse(null);
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
        userRepository.findById(user.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("User already exists");
        });
        user.setEnabled(false);

        User save = userRepository.save(user);

        authorityRepository.save(new Authority(user.getUsername(), AuthorityType.USER));

        return save;
    }

    public User toggleUserActivation(String username, boolean enabled) {
        final User user = userRepository.findById(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }
}

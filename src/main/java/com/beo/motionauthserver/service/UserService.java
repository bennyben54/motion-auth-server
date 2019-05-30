package com.beo.motionauthserver.service;

import com.beo.motionauthserver.configuration.AuthPasswordEncoder;
import com.beo.motionauthserver.dto.UserDto;
import com.beo.motionauthserver.entity.Authority;
import com.beo.motionauthserver.entity.Subscription;
import com.beo.motionauthserver.entity.User;
import com.beo.motionauthserver.entity.enums.AuthorityType;
import com.beo.motionauthserver.repository.SubscriptionRepository;
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
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public UserService(AuthPasswordEncoder passwordEncoder, UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameAndEnabled(username, true).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
        builder.password(user.getPassword());
        String[] rolesAuthorities = user.getAuthorities().stream().map(authority -> authority.getAuthority().name()).toArray(String[]::new);
        builder.roles(rolesAuthorities);
//        builder.authorities(rolesAuthorities);
        builder.disabled(!user.getEnabled());

        return builder.build();
    }

    public User findUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public UserDto subscribe(UserDto userDto) {
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
        subscriptionRepository.findByUsername(userDto.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("User already exists");
        });
        Subscription user = new Subscription();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userDto.setId(subscriptionRepository.save(user).getId());
        userDto.setEnabled(false);
        userDto.setPassword(null);

        return userDto;
    }

    public UserDto createUser(UUID subscriptionId) {
        if (subscriptionId == null) {
            throw new IllegalArgumentException("subscriptionId is null");
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(
                () -> new IllegalArgumentException("Subscription not found")
        );

        User user = new User();
        user.setUsername(subscription.getUsername());
        user.setPassword(subscription.getPassword());
        user.setEnabled(false);

        user.getAuthorities().add(new Authority(null, user, AuthorityType.USER));

        user = userRepository.save(user);
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEnabled(user.getEnabled());
        userDto.setPassword(null);
        userDto.getAuthorities().addAll(user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList()));

        subscriptionRepository.deleteById(subscriptionId);

        return userDto;
    }

    public Boolean toggleUserActivation(UUID id, Boolean enabled) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(Boolean.TRUE.equals(enabled));
        return userRepository.save(user).getEnabled();
    }

    public void deleteUser(String username) {
        userRepository.findByUsername(username).ifPresent(userRepository::delete);
    }


    public List<UserDto> findAll(UUID userId) {
        return userRepository.findByIdNot(userId).stream().map(
                user -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setUsername(user.getUsername());
                    userDto.setEnabled(user.getEnabled());
                    userDto.getAuthorities().addAll(user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList()));
                    return userDto;
                }
        ).collect(Collectors.toList());
    }

    public List<UserDto> findAllSubscription() {
        return subscriptionRepository.findAll().stream().map(
                subscription -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(subscription.getId());
                    userDto.setUsername(subscription.getUsername());
                    return userDto;
                }
        ).collect(Collectors.toList());
    }
}

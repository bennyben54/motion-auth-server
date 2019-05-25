package com.beo.motionauthserver.rest;

import com.beo.motionauthserver.entity.User;
import com.beo.motionauthserver.repository.AuthorityRepository;
import com.beo.motionauthserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    private final UserService userService;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public UserRestController(UserService userService, AuthorityRepository authorityRepository) {
        this.userService = userService;
        this.authorityRepository = authorityRepository;
    }


    @GetMapping(path = "/api/user/who/am/i")
    public String whoAmI(Authentication authentication) {
        User user = userService.findUser(authentication.getName());
        StringBuilder userInfo = new StringBuilder("[");
        userInfo.append(user.getUsername()).append("=");
        user.getAuthorities().stream().forEach(a -> userInfo.append(a.getAuthority().name()));
        userInfo.append("]");
        return userInfo.toString();
    }

}

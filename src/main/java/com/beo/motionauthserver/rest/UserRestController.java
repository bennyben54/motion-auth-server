package com.beo.motionauthserver.rest;

import com.beo.motionauthserver.dto.UserDto;
import com.beo.motionauthserver.entity.User;
import com.beo.motionauthserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/user")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "/list")
    public ResponseEntity<List<UserDto>> getUsers(Authentication authentication) {
        User user = userService.findUser(authentication.getName());
        return ResponseEntity.ok(userService.findAll(user.getId()));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "/list/subscribers")
    public ResponseEntity<List<UserDto>> getSubscriptions() {
        return ResponseEntity.ok(userService.findAllSubscription());
    }

    @PostMapping(value = "/subscribe")
    public ResponseEntity<UserDto> subscribe(@RequestBody UserDto toCreate) {
        return new ResponseEntity<>(userService.subscribe(toCreate), HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = "/{subscritionId}")
    public ResponseEntity<UserDto> create(
            @PathVariable UUID subscritionId
    ) {
        return new ResponseEntity<>(userService.createUser(subscritionId), HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/{userId}/enable")
    public ResponseEntity<Boolean> enable(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(userService.toggleUserActivation(userId, true));
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/{userId}/disable")
    public ResponseEntity<Boolean> disable(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(userService.toggleUserActivation(userId, false));
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable UUID userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/subscription/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable UUID subscriptionId
    ) {
        userService.deleteSubscription(subscriptionId);
        return ResponseEntity.ok().build();
    }


//    @Secured({"ROLE_ADMIN", "ROLE_USER"})
//    @GetMapping(path = "/api/user/who/am/i")
//    public String whoAmI(Authentication authentication) {
//        User user = userService.findUser(authentication.getName());
//        StringBuilder userInfo = new StringBuilder("[");
//        userInfo.append(user.getUsername()).append("=");
//        user.getAuthorities().stream().forEach(a -> userInfo.append(a.getAuthority().name()));
//        userInfo.append("]");
//        return userInfo.toString();
//    }
//
//    @Secured({"ROLE_USER"})
//    @GetMapping(path = "/api/user/test")
//    public String test() {
//        return "test";
//    }
//
//    @Secured({"ROLE_OTHER"})
//    @GetMapping(path = "/api/user/other")
//    public String other() {
//        return "other";
//    }

}
